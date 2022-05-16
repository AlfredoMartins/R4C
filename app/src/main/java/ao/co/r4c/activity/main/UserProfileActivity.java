package ao.co.r4c.activity.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;

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

public class UserProfileActivity extends AppCompatActivity {

    private static final int IMG_REQUEST = 777;
    CircleImageView circleImageView;
    TextView txt_nome, txt_telefone, txt_email;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        circleImageView = findViewById(R.id.img_profile);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        txt_nome = findViewById(R.id.txt_nome);
        txt_email = findViewById(R.id.txt_email);
        txt_telefone = findViewById(R.id.txt_telefone);

        carregarDados();

        Usuario usuario = SharedPreferenceManager.getInstance(this).getUsuario();

        try {
            if (usuario.getFoto_url().isEmpty())
                circleImageView.setImageResource(R.drawable.img_user_default);
            else
                Glide.with(this).load(ApiClient.getBaseUrl() + "r4c/api/objects/usuarios/upload_images/" + SharedPreferenceManager.getInstance(this).getUsuario().getId() + ".jpg").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(circleImageView);
        } catch (Exception e) {
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

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
                    Toast.makeText(UserProfileActivity.this, "Foto actualizada com sucesso!", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(UserProfileActivity.this, "Erro inesperado: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) {
                Toast.makeText(UserProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void carregarDados() {
        txt_nome.setText(SharedPreferenceManager.getInstance(this).getUsuario().getNome() + " " + SharedPreferenceManager.getInstance(getApplicationContext()).getUsuario().getSobrenome());
        txt_telefone.setText(SharedPreferenceManager.getInstance(this).getUsuario().getTelefone());
        txt_email.setText(SharedPreferenceManager.getInstance(this).getUsuario().getEmail());
    }


    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu_user_profile, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_edit_user_profile:

                Intent intent = new Intent(UserProfileActivity.this, AlterProfileActivity.class);
                startActivity(intent);

                break;
            case R.id.nav_share_user_profile:

                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
