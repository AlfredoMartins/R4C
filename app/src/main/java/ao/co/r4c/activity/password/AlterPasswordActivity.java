package ao.co.r4c.activity.password;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Objects;

import ao.co.r4c.R;
import ao.co.r4c.activity.main.customer.fragment.CustomerMapsActivity;
import ao.co.r4c.activity.main.driver.fragment.DriverMapsActivity;
import ao.co.r4c.model.Usuario;
import ao.co.r4c.service.ApiClient;
import ao.co.r4c.service.ApiInterface;
import ao.co.r4c.storage.SharedPreferenceManager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlterPasswordActivity extends AppCompatActivity {

    Button btn_salvar;
    EditText txt_password;
    EditText txt_confirm_password;
    ApiInterface apiInterface;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_password);


        //Referenciando os componentes
        btn_salvar = findViewById(R.id.btn_salvar);
        txt_password = findViewById(R.id.txt_new_password);
        txt_confirm_password = findViewById(R.id.txt_confirm_password);


        Usuario usuario = SharedPreferenceManager.getInstance(this).getUsuario();

        circleImageView = findViewById(R.id.img_user_foto);

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
                if (txt_confirm_password.getText().toString().equals(txt_password.getText().toString()))
                    alterarSenha();
                else
                    Toast.makeText(AlterPasswordActivity.this, "A senhas não combinam! ", Toast.LENGTH_SHORT).show();

            }
        });
    }

    //Permite alterar a senha
    private void alterarSenha() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        retrofit2.Call<Usuario> call = apiInterface.alterarSenha(
                Objects.requireNonNull(getIntent().getExtras()).getString("email"),
                Objects.requireNonNull(getIntent().getExtras()).getString("telefone"),
                txt_password.getText().toString()
        );

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {

                //Abre a janela principal que alterou as duas senhas
                if (response.isSuccessful() && response.code() == 201) {

                    if (getIntent().getExtras().getString("id_categoria").equals("1")) {
                        Intent intent;
                        intent = new Intent(AlterPasswordActivity.this, DriverMapsActivity.class);
                        intent.putExtra("email", Objects.requireNonNull(getIntent().getExtras()).getString("email"));
                        startActivity(intent);
                    } else {
                        Intent intent;
                        intent = new Intent(AlterPasswordActivity.this, CustomerMapsActivity.class);
                        intent.putExtra("email", Objects.requireNonNull(getIntent().getExtras()).getString("email"));
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(AlterPasswordActivity.this, "Erro inesperado! " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                Toast.makeText(AlterPasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
