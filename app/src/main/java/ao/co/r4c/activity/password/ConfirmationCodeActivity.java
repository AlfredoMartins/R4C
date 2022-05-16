package ao.co.r4c.activity.password;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.raycoarana.codeinputview.CodeInputView;
import com.raycoarana.codeinputview.OnCodeCompleteListener;

import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import ao.co.r4c.R;
import ao.co.r4c.activity.main.customer.fragment.CustomerMapsActivity;
import ao.co.r4c.activity.main.driver.fragment.DriverMapsActivity;
import ao.co.r4c.helper.Constantes;
import ao.co.r4c.model.NexmoSMS;
import ao.co.r4c.model.TelcoSMS;
import ao.co.r4c.model.Usuario;
import ao.co.r4c.service.ApiClient;
import ao.co.r4c.service.ApiInterface;
import ao.co.r4c.storage.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfirmationCodeActivity extends AppCompatActivity {

    Button btn_confirmar;
    String codigo_confirmacao = "123456";
    Retrofit retrofit = null;
    ApiInterface apiInterface;
    FirebaseAuth firebaseAuth;
    LinearLayout rootLayout;
    ProgressBar progressBar;
    Timer timer;
    Runnable runnable;
    Handler handler;
    int count = 0;
    TextView txt_timer;
    TextView txt_reenviar;
    ProgressBar progressDialog;
    CodeInputView txt_codigo;

    boolean errou = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_code);

        rootLayout = findViewById(R.id.rootLayout);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        progressBar.setMax(60);


        progressDialog = findViewById(R.id.loading);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                if (count < 60) {
                    progressBar.setProgress(count++);
                    progressBar.setSecondaryProgress(count + 3);

                    int segundos = (60 - count);

                    if (segundos != 0)
                        txt_timer.setText("Consulte seu telefone dentro de " + segundos + " segundos ... ");
                    else
                        txt_timer.setText("Consulte seu telefone ... ");

                } else {
                    progressBar.setVisibility(View.GONE);
                    timer.cancel();
                }
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 1000, 1000);


        //Recebe o telefone vindo de outra actividade e enviar o código ao mesmo
        //enviarCodigoConfirmacaoTelco(this.telefone = getIntent().getExtras().getString("email_telefone"));

        //enviarCodigoConfirmacaoNexmo(Objects.requireNonNull(getIntent().getExtras()).getString("telefone"));


        //Referenciando os objectos
        btn_confirmar = findViewById(R.id.btn_confirmar);
        txt_codigo = findViewById(R.id.txt_code);


        txt_codigo.addOnCompleteListener(new OnCodeCompleteListener() {
            @Override
            public void onCompleted(String code) {

                if (!errou) {
                    String codigo = "123456";

                    if (code.equals(codigo) || code.equals(codigo_confirmacao)) {
                        Toast.makeText(ConfirmationCodeActivity.this, "Senha correcta.", Toast.LENGTH_SHORT).show();
                        txt_codigo.clearError();
                        txt_codigo.setEditable(false);

                    } else {
                        txt_codigo.setError("Ups! Código de confirmação incorrecto ...");
                        txt_codigo.setCode("");
                        txt_codigo.setEditable(true);

                        errou = true;
                    }
                }
            }
        });

        //Evento Click do botão Confirmar
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setVisibility(View.GONE);
                btn_confirmar.setVisibility(View.VISIBLE);

                confirmarCodigoConfirmacao();
            }
        });

        enviarCodigoConfirmacaoFirebase(Objects.requireNonNull(getIntent().getExtras()).getString("telefone"));

        txt_timer = findViewById(R.id.txt_timer);

        txt_reenviar = findViewById(R.id.txt_reenviar);
        txt_reenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enviarCodigoConfirmacaoFirebase(Objects.requireNonNull(getIntent().getExtras()).getString("telefone"));
                count = 0;
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);


            }
        });
    }

    //Gerar código de confirmação
    private void gerarCodigoConfirmacao() {
        Random random = new Random();
        codigo_confirmacao = (String.valueOf(1000 + random.nextInt(9000)));
    }

    private void enviarCodigoConfirmacaoNexmo(String telefone) {
        //gerarCodigoConfirmacao();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://rest.nexmo.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        //Cria instância da classe NexmoSMS que possui a estrutura do Json
        NexmoSMS nexmoSMS = new NexmoSMS("244" + telefone, "Código de confirmação: " + codigo_confirmacao + "\n");

        Call<NexmoSMS> call = apiInterface.enviarMensagemNexmo(nexmoSMS.getAPI_KEY(), nexmoSMS.getAPI_SECRET(), nexmoSMS.getFROM(), nexmoSMS.getTo(), nexmoSMS.getText());

        call.enqueue(new Callback<NexmoSMS>() {
            @Override
            public void onResponse(@NonNull Call<NexmoSMS> call, @NonNull Response<NexmoSMS> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ConfirmationCodeActivity.this,
                            "Código enviado com sucesso! " + response.message(),
                            Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(ConfirmationCodeActivity.this,
                            "Erro inesperado! " + response.message() + " " + response.code(),
                            Toast.LENGTH_LONG).show();


                    progressDialog.setVisibility(View.GONE);
                    btn_confirmar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<NexmoSMS> call, @NonNull Throwable t) {
                Toast.makeText(ConfirmationCodeActivity.this,
                        "Erro! " + t.getMessage(),
                        Toast.LENGTH_LONG).show();


                progressDialog.setVisibility(View.GONE);
                btn_confirmar.setVisibility(View.VISIBLE);
            }
        });
    }


    private void enviarCodigoConfirmacaoFirebase(String phoneNumber) {

        phoneNumber = "+244" + phoneNumber;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.setLanguageCode("pt");

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                1,
                TimeUnit.MINUTES,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        codigo_confirmacao = phoneAuthCredential.getSmsCode();

                        Snackbar.make(rootLayout, "Código de confirmação enviado. Por favor, consulte o seu telefone ... ", Snackbar.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Snackbar.make(rootLayout, "Erro inesperado: " + e.getMessage(), Snackbar.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        progressBar.setProgress(count = 60);
                        txt_timer.setText("Consulte seu Telefone dentro de " + (60 - count) + " segundo ... ");
                        Toast.makeText(ConfirmationCodeActivity.this, "Código enviado ... ", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    //Confirmar código
    private void enviarCodigoConfirmacaoTelco(String telefone) {
        //gerarCodigoConfirmacao();

        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://telcosms.co.ao/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<TelcoSMS> call = apiInterface.enviarMensagemTelco(new TelcoSMS(telefone, codigo_confirmacao));

        call.enqueue(new Callback<TelcoSMS>() {
            @Override
            public void onResponse(Call<TelcoSMS> call, Response<TelcoSMS> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ConfirmationCodeActivity.this,
                            "Telefone activado com sucesso! " + response.message(),
                            Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(ConfirmationCodeActivity.this,
                            "Erro inesperado! " + response.message() + " " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TelcoSMS> call, Throwable t) {

                Toast.makeText(ConfirmationCodeActivity.this,
                        "Erro! " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    //Verifica se se o código digitado combina com o enviado ao telefone
    private void confirmarCodigoConfirmacao() {

        if (txt_codigo.getCode().equals(codigo_confirmacao)) {
                        /*Se existir um Extra Registrar, e for igual a Sim,
                            implica que acabou de criar conta e deve ser direcionado para a janela princial
                         */

            String valor =
                    Objects.requireNonNull(getIntent().getExtras()).getString("r");

            if (Objects.equals(valor, "sim")) {
                cadastrarUsuarios();
            } else { //Caso contrário, deve ir para a janela de alterar o formulário

                Intent intent = new Intent(ConfirmationCodeActivity.this, AlterPasswordActivity.class);
                intent.putExtra("email", getIntent().getExtras().getString("email"));
                intent.putExtra("telefone", getIntent().getExtras().getString("telefone"));
                intent.putExtra("id_categoria", getIntent().getExtras().getString("id_categoria"));
                startActivity(intent);
            }
        } else {
            txt_codigo.setError("Ups! Código de confirmação incorrecto ...");
            progressDialog.setVisibility(View.GONE);
            btn_confirmar.setVisibility(View.VISIBLE);
        }
    }


    //Método para início de sessão
    private void iniciarSessao(String email, String senha) {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Usuario> call = apiInterface.iniciarSessao(email, senha);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                if (response.isSuccessful() && response.code() == 201) {

                    Usuario usuario = response.body();

                    SharedPreferenceManager.getInstance(ConfirmationCodeActivity.this).
                            salvarDadosUsuario(usuario);

                    if (Objects.requireNonNull(usuario).getId_categoria() == 1) {
                        Intent intent = new Intent(ConfirmationCodeActivity.this, DriverMapsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(ConfirmationCodeActivity.this, CustomerMapsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                } else if (response.code() == 503) {

                    Toast.makeText(ConfirmationCodeActivity.this,
                            "E-mail ou senha inválida! ",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                Toast.makeText(ConfirmationCodeActivity.this,
                        "Erro! " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cadastrarUsuarios() {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);


        Call<Usuario> usuarioCall = apiInterface.cadastrarUsuario(
                Constantes.usuario.getNome(),
                Constantes.usuario.getSobrenome(),
                Constantes.usuario.getEmail(),
                Constantes.usuario.getSenha(),
                Constantes.usuario.getTelefone(),
                Constantes.usuario.getId_categoria()
        );

        usuarioCall.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                if (response.isSuccessful() && response.code() == 201) {
                    //Começa nova actividade enviando o código de confirmação ao telefone

                    iniciarSessao(Constantes.usuario.getEmail(),
                            Constantes.usuario.getSenha());

                } else
                    Toast.makeText(ConfirmationCodeActivity.this,
                            "Usuário existente.",
                            Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

                Toast.makeText(ConfirmationCodeActivity.this,
                        "Falha",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}