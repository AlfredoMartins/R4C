package ao.co.r4c.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ao.co.r4c.R;
import ao.co.r4c.activity.main.customer.fragment.CustomerMapsActivity;
import ao.co.r4c.activity.main.driver.fragment.DriverMapsActivity;
import ao.co.r4c.activity.password.SendCodeActivity;
import ao.co.r4c.activity.signup.RegisterActivity;
import ao.co.r4c.model.Settings;
import ao.co.r4c.model.Usuario;
import ao.co.r4c.service.ApiClient;
import ao.co.r4c.service.ApiInterface;
import ao.co.r4c.storage.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    TextView txt_username, txt_password, txt_esqueci, txt_conta;
    ImageButton btn_google;
    TextInputLayout layout_input_password;
    ApiInterface apiInterface;
    ProgressBar progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Referenciando os componentes
        btn_login = findViewById(R.id.btn_login);
        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);
        txt_esqueci = findViewById(R.id.txt_esqueci);
        txt_conta = findViewById(R.id.txt_conta);
        btn_google = findViewById(R.id.img_google);
        layout_input_password = findViewById(R.id.layout_password);

        progressDialog = findViewById(R.id.loading);

        txt_password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (!txt_password.getText().toString().isEmpty())
                    layout_input_password.setPasswordVisibilityToggleEnabled(true);
                else
                    layout_input_password.setPasswordVisibilityToggleEnabled(false);

                return false;
            }
        });

        txt_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                layout_input_password.setPasswordVisibilityToggleEnabled(false);
            }
        });

        txt_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                layout_input_password.setPasswordVisibilityToggleEnabled(true);
            }
        });


        //Evento Click do botão Login
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txt_username.getText().toString().trim().toLowerCase();
                String password = txt_password.getText().toString();

                if (username.isEmpty()) {
                    txt_username.setError("Campo obrigatório");
                    txt_username.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    layout_input_password.setPasswordVisibilityToggleEnabled(false);
                    txt_password.setError("Campo obrigatório");
                    txt_password.requestFocus();
                    return;
                }


                iniciarSessao(username, password);
            }
        });

        //Evento Click do botão Google
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this,
                        "Ainda não foram implementadas as funcionalidades, Ok? Tente outro dia, Obrigado!",
                        Toast.LENGTH_LONG).show();
            }
        });

        //Evento Click do botão Esqueci senha
        txt_esqueci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SendCodeActivity.class);
                startActivity(intent);
            }
        });

        txt_conta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (SharedPreferenceManager.getInstance(this).sessaoIniciada()) {

            if (SharedPreferenceManager.getInstance(this).getUsuario().getId_categoria() == 1) {
                Intent intent = new Intent(LoginActivity.this, DriverMapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("email", txt_username.getText());
                startActivity(intent);
            } else {
                Intent intent = new Intent(LoginActivity.this, CustomerMapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("email", txt_username.getText());
                startActivity(intent);
            }
        }
    }

    //Método para início de sessão
    private void iniciarSessao(String email, String senha) {

        progressDialog.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Usuario> call = apiInterface.iniciarSessao(email, senha);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                if (response.isSuccessful() && response.code() == 201) {

                    Usuario usuario = response.body();

                    SharedPreferenceManager.getInstance(LoginActivity.this).
                            salvarDadosUsuario(usuario);


                    Settings settings = new Settings();
                    settings.setVibrar(true);
                    settings.setReceber_notificacoes_actualizacoes(true);
                    settings.setMotorista_mais_proximo(true);
                    settings.setReceber_notificacoes(true);
                    SharedPreferenceManager.getInstance(LoginActivity.this).atualizarDefinicoes(settings);


                    Intent intent;
                    if (usuario.getId_categoria() == 1) {
                        intent = new Intent(LoginActivity.this, DriverMapsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        intent = new Intent(LoginActivity.this, CustomerMapsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }


                } else if (response.code() == 503) {

                    Toast.makeText(LoginActivity.this,
                            "E-mail ou senha inválida! ",
                            Toast.LENGTH_LONG).show();

                    progressDialog.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this,
                        "Erro! " + t.getMessage(),
                        Toast.LENGTH_LONG).show();

                progressDialog.setVisibility(View.GONE);
                btn_login.setVisibility(View.VISIBLE);
            }
        });
    }
}