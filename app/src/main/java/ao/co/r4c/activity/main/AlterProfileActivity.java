package ao.co.r4c.activity.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.util.Objects;

import ao.co.r4c.R;
import ao.co.r4c.helper.Constantes;
import ao.co.r4c.model.Image;
import ao.co.r4c.model.Usuario;
import ao.co.r4c.service.ApiClient;
import ao.co.r4c.service.ApiInterface;
import ao.co.r4c.storage.SharedPreferenceManager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlterProfileActivity extends AppCompatActivity {

    private static final int IMG_REQUEST = 777;
    Button btn_salvar;
    EditText txt_password;
    EditText txt_confirm_password;
    EditText txt_old_password;
    TextView txt_nome, txt_email;
    ApiInterface apiInterface;
    CircleImageView circleImageView;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        txt_password = findViewById(R.id.txt_new_password);
        txt_old_password = findViewById(R.id.txt_old_password);
        txt_confirm_password = findViewById(R.id.txt_confirm_password);
        btn_salvar = findViewById(R.id.btn_salvar);

        txt_nome = findViewById(R.id.txt_nome);
        txt_email = findViewById(R.id.txt_email);

        Usuario usuario = SharedPreferenceManager.getInstance(this).getUsuario();
        txt_nome.setText(String.format("%s %s", usuario.getNome(), usuario.getSobrenome()));
        txt_email.setText(Objects.requireNonNull(usuario.getEmail()));

        circleImageView = findViewById(R.id.img_user_foto);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        try {
            if (usuario.getFoto_url().isEmpty())
                circleImageView.setImageResource(R.drawable.img_user_default);
            else
                Glide.with(this).load(ApiClient.getBaseUrl() + "r4c/api/objects/usuarios/upload_images/" + usuario.getId() + ".jpg").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(circleImageView);
        } catch (Exception e) {
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        //Se for clicado, verifique se as senhas combinam
        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String senha_antiga = txt_old_password.getText().toString().trim();
                String senha = txt_password.getText().toString().trim();
                String confirmar_senha = txt_confirm_password.getText().toString().trim();


                if (senha.equals(senha_antiga)) {
                    txt_old_password.setError("A senha antiga é igual à nova.");
                    txt_password.requestFocus();
                    return;
                }

                if (senha.isEmpty()) {
                    txt_password.setError("Campo obrigatório.");
                    txt_password.requestFocus();
                    return;
                }

                if (confirmar_senha.isEmpty()) {
                    txt_confirm_password.setError("Campo obrigatório.");
                    txt_confirm_password.requestFocus();
                    return;
                }


                if (!senha_antiga.equals(SharedPreferenceManager.getInstance(getApplicationContext()).getUsuario().getSenha())) {
                    txt_old_password.setError("Senha actual incorrecta.");
                    txt_old_password.requestFocus();
                    return;
                }

                if (!confirmar_senha.equals(senha)) {
                    txt_confirm_password.setError("As senhas não correspondem.");
                    txt_confirm_password.requestFocus();
                    return;
                }

                atualizarPerfil(senha);

            }
        });
    }


    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }


    //Permite alterar a senha
    private void atualizarPerfil(String senha) {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        int user_id = SharedPreferenceManager.getInstance(this).getUsuario().getId();

        Call<Usuario> call = apiInterface.actualizarPerfil(
                user_id,
                senha
        );

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {

                //Abre a janela principal que alterou as duas senhas
                if (response.isSuccessful() && response.code() == 201) {

                    Toast.makeText(AlterProfileActivity.this, "Dados alterados com sucesso.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(AlterProfileActivity.this, "Erro inesperado: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                Toast.makeText(AlterProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {

            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                circleImageView.setImageBitmap(bitmap);

                uploadImage(SharedPreferenceManager.getInstance(this).getUsuario().getId(), bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void uploadImage(int id_user, Bitmap bitmap) {
        String image = Constantes.imageToString(bitmap);

        /*Instanciate the ApiInterface*/
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        /*Call the interface*/
        Call<Image> listCall = apiInterface.uploadImage(id_user, image);

        /*Enqueue*/
        listCall.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {
                //Abrir outra tela caso a conta for activada com sucesso
                if (response.isSuccessful() && response.code() == 201) {
                    /*Get the response body json object*/

                    Image imageClass = response.body();
                    Toast.makeText(AlterProfileActivity.this, "Foto actualizada com sucesso!", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(AlterProfileActivity.this, "Erro inesperado: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) {
                Toast.makeText(AlterProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}