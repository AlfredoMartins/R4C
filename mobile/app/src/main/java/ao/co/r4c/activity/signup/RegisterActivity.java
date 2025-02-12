package ao.co.r4c.activity.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import ao.co.r4c.R;
import ao.co.r4c.activity.login.LoginActivity;
import ao.co.r4c.activity.password.ConfirmationCodeActivity;
import ao.co.r4c.helper.Constantes;
import ao.co.r4c.model.Usuario;
import ao.co.r4c.service.ApiClient;
import ao.co.r4c.service.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    ApiInterface apiInterface;

    EditText txt_nome, txt_sobrenome, txt_email, txt_telefone, txt_senha, txt_confirmar_senha;
    Button btn_cadastro;
    RadioButton radioButtonPassenger, radioButtonDriver;
    TextInputLayout layout_confirm_password;
    TextView txt_conta;
    int id_categoria = 1;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Referenciando
        btn_cadastro = findViewById(R.id.btn_signup);
        txt_nome = findViewById(R.id.txt_firstname);
        txt_sobrenome = findViewById(R.id.txt_surname);
        txt_email = findViewById(R.id.txt_email);
        txt_telefone = findViewById(R.id.txt_phone);
        txt_senha = findViewById(R.id.txt_password);
        txt_confirmar_senha = findViewById(R.id.txt_confirm_password);
        layout_confirm_password = findViewById(R.id.layout_confirm_password);
        radioButtonDriver = findViewById(R.id.rbt_driver);
        radioButtonPassenger = findViewById(R.id.rbt_passenger);

        txt_conta = findViewById(R.id.txt_conta);

        //Evento Click do botão Esqueci senha
        txt_conta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


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


        txt_confirmar_senha.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (!txt_confirmar_senha.getText().toString().isEmpty())
                    layout_confirm_password.setPasswordVisibilityToggleEnabled(true);
                else
                    layout_confirm_password.setPasswordVisibilityToggleEnabled(false);

                return false;
            }
        });

        //Eventos
        btn_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = txt_nome.getText().toString().trim();
                String sobrenome = txt_sobrenome.getText().toString().trim();
                String email = txt_email.getText().toString().trim();
                String telefone = txt_telefone.getText().toString().trim();
                String senha = txt_senha.getText().toString().trim();
                String confirmar_senha = txt_confirmar_senha.getText().toString().trim();

                if (nome.isEmpty()) {
                    txt_nome.setError("Campo obrigatório");
                    txt_nome.requestFocus();
                    return;
                }


                if (sobrenome.isEmpty()) {
                    txt_sobrenome.setError("Campo obrigatório");
                    txt_sobrenome.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    txt_email.setError("Campo obrigatório");
                    txt_email.requestFocus();
                    return;
                }

                if (telefone.isEmpty()) {
                    txt_telefone.setError("Campo obrigatório");
                    txt_telefone.requestFocus();
                    return;
                }

                if (senha.isEmpty()) {
                    txt_senha.setError("Campo obrigatório");
                    txt_senha.requestFocus();
                    return;
                }


                if (senha.length() < 4) {
                    txt_senha.setError("A senha deve conter no mínimo 4 dígitos.");
                    txt_senha.requestFocus();
                    return;
                }


                if (!confirmar_senha.equals(senha)) {
                    layout_confirm_password.setPasswordVisibilityToggleEnabled(false);
                    txt_confirmar_senha.setError("As senhas não correspondem.");
                    txt_confirmar_senha.requestFocus();
                    return;
                }

                if (confirmar_senha.isEmpty()) {
                    layout_confirm_password.setPasswordVisibilityToggleEnabled(false);
                    txt_confirmar_senha.setError("Campo obrigatório");
                    txt_confirmar_senha.requestFocus();
                    return;
                }

                if (!radioButtonPassenger.isChecked() && !radioButtonDriver.isChecked()) {
                    radioButtonPassenger.setError("Campo obrigatório");
                    radioButtonDriver.setError("Campo obrigatório");
                }

                existencia(txt_telefone.getText().toString(), txt_telefone.getText().toString());

            }
        });
    }

    private void existencia(String telefone, String email) {

        if (radioButtonDriver.isChecked())
            id_categoria = 1;
        else id_categoria = 2;

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Usuario> usuarioCall = apiInterface.existencia(
                txt_email.getText().toString(),
                txt_telefone.getText().toString()
        );

        usuarioCall.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                if (response.isSuccessful() && response.code() == 201) {
                    //Começa nova actividade enviando o código de confirmação ao telefone

                    Constantes.usuario = new Usuario();

                    Constantes.usuario.setNome(txt_nome.getText().toString());
                    Constantes.usuario.setSobrenome(txt_sobrenome.getText().toString());
                    Constantes.usuario.setEmail(txt_email.getText().toString());
                    Constantes.usuario.setId_categoria(id_categoria);
                    Constantes.usuario.setTelefone(txt_telefone.getText().toString());
                    Constantes.usuario.setSenha(txt_senha.getText().toString());

                    Intent intent = new Intent(RegisterActivity.this, ConfirmationCodeActivity.class);
                    intent.putExtra("telefone", txt_telefone.getText().toString());
                    intent.putExtra("r", "sim");
                    startActivity(intent);

                } else
                    Toast.makeText(RegisterActivity.this,
                            "Usuário existente.",
                            Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

                Toast.makeText(RegisterActivity.this,
                        "Falha",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}

