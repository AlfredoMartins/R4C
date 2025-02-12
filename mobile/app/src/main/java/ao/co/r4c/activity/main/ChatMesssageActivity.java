package ao.co.r4c.activity.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import ao.co.r4c.R;
import ao.co.r4c.adapter.MessageAdapter;
import ao.co.r4c.helper.Constantes;
import ao.co.r4c.model.ChatMessage;
import ao.co.r4c.service.ApiClient;
import ao.co.r4c.service.ApiInterface;
import ao.co.r4c.service.SocketClient;
import ao.co.r4c.storage.SharedPreferenceManager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatMesssageActivity extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;
    CircleImageView circleImageView;
    TextView txt_username, txt_state;
    ImageButton imageButton_call, imageButton_more_option, imageButton_send_message;
    MessageAdapter messageAdapter;
    List<ChatMessage> chatMessageList;
    RecyclerView recyclerView_messages;
    ApiInterface apiInterface;
    Timer timer;
    Runnable runnable;
    Handler handler;
    Intent intent;
    int user_id;
    String telefone;
    private EditText editText_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messsage);

        try {
            //Instatiate the socket
            Constantes.socketClient = new SocketClient();
            Constantes.socketClient.connectUser(SharedPreferenceManager.getInstance(this).getUsuario().getId(), 2);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        intent = getIntent();
        user_id = Objects.requireNonNull(intent.getExtras()).getInt("id");
        String username = intent.getStringExtra("username");

        telefone = Objects.requireNonNull(intent.getExtras().getString("telefone"));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView_messages = findViewById(R.id.recyclerview_chat_message);
        recyclerView_messages.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView_messages.setLayoutManager(linearLayoutManager);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.ANIMATION_TYPE_SWIPE_SUCCESS) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();

                AlertDialog.Builder builder = new AlertDialog.Builder(ChatMesssageActivity.this);
                builder.setTitle("Deletar");
                builder.setMessage("Tem certeza que deseja eliminar esta mensagem?\nAtenção: Não poderá recuperar a mesma.");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletarMensagem(messageAdapter.getMessageAt(position).getId(), position);
                    }
                });

                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.show();
            }
        }).attachToRecyclerView(recyclerView_messages);


        imageButton_call = findViewById(R.id.img_btn_call);
        imageButton_more_option = findViewById(R.id.img_btn_option);
        imageButton_send_message = findViewById(R.id.btn_send_message);
        editText_message = findViewById(R.id.txt_message);

        imageButton_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone(telefone);
            }
        });

        imageButton_more_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Indisponível de momento.", Snackbar.LENGTH_LONG)
                        .setAction("Acção", null).show();
            }
        });

        imageButton_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText_message.getText().toString().isEmpty())
                    enviarMensagem(SharedPreferenceManager.getInstance(ChatMesssageActivity.this).getUsuario().getId(), user_id, editText_message.getText().toString());
            }
        });

        editText_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Indisponível de momento.", Snackbar.LENGTH_LONG)
                        .setAction("Acção", null).show();
            }
        });

        circleImageView = findViewById(R.id.img_username);
        txt_username = findViewById(R.id.txt_username);
        txt_state = findViewById(R.id.txt_estado);

        try {
            Glide.with(this).load(ApiClient.getBaseUrl() + "r4c/api/objects/usuarios/upload_images/" + user_id + ".jpg").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(circleImageView);
        } catch (Exception e) {
            circleImageView.setImageResource(R.drawable.img_user_default);
        }

        txt_username.setText(username);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                try {
                    listarMensagens(SharedPreferenceManager.getInstance(ChatMesssageActivity.this).getUsuario().getId(), user_id);
                } catch (Exception e) {
                    Toast.makeText(ChatMesssageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 5, 2000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_trips, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_refresh:
                break;
            case R.id.nav_clean_history:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void callPhone(String telefone) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + telefone));


        if (ActivityCompat.checkSelfPermission(ChatMesssageActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ChatMesssageActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else
            startActivity(callIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone(telefone);
            }
        }
    }

    private void enviarMensagem(final int id_emissor, final int id_receptor, String texto) {

        /*Instanciate the ApiInterface*/
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        /*Call the interface*/
        Call<ChatMessage> listCall = apiInterface.inserirMensagem(id_emissor, id_receptor, texto);

        /*Enqueue*/
        listCall.enqueue(new Callback<ChatMessage>() {
            @Override
            public void onResponse(Call<ChatMessage> call, Response<ChatMessage> response) {
                //Abrir outra tela caso a conta for activada com sucesso
                if (response.isSuccessful() && response.code() == 201) {
                    /*Get the response body json object*/

                    Toast.makeText(ChatMesssageActivity.this, "Mensagem enviada.", Toast.LENGTH_SHORT).show();
                    listarMensagens(id_emissor, id_receptor);
                    editText_message.setText("");

                } else {
                    Toast.makeText(ChatMesssageActivity.this, "Erro inesperado: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChatMessage> call, Throwable t) {
                Toast.makeText(ChatMesssageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void deletarMensagem(final int id, final int position) {

        /*Instanciate the ApiInterface*/
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        /*Call the interface*/
        Call<ChatMessage> listCall = apiInterface.deletarMensagem(id);

        /*Enqueue*/
        listCall.enqueue(new Callback<ChatMessage>() {
            @Override
            public void onResponse(Call<ChatMessage> call, Response<ChatMessage> response) {
                //Abrir outra tela caso a conta for activada com sucesso
                if (response.isSuccessful() && response.code() == 201) {
                    /*Get the response body json object*/


                } else {
                    Toast.makeText(ChatMesssageActivity.this, "Erro inesperado: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChatMessage> call, Throwable t) {
                Toast.makeText(ChatMesssageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadMensagens(int id, Integer user_id) {

        Toast.makeText(this, id + " " + user_id, Toast.LENGTH_SHORT).show();

        chatMessageList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            ChatMessage chatMessage = new ChatMessage(1, 2, "Estou programando.", "22h:01min 28/07/2019");
            chatMessageList.add(chatMessage);

            chatMessage = new ChatMessage(2, 1, "Afinal? Sempre achei que és programador.", "22h:01min 28/07/2019");
            chatMessageList.add(chatMessage);
        }

        try {
            messageAdapter = new MessageAdapter(getApplicationContext(), chatMessageList);
            recyclerView_messages.setAdapter(messageAdapter);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void listarMensagens(int id_emissor, int id_receptor) {

        /*Instanciate the ApiInterface*/
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        /*Call the interface*/
        Call<ArrayList<ChatMessage>> listCall = apiInterface.carregar_chat_mensagens(id_emissor, id_receptor);

        /*Enqueue*/
        listCall.enqueue(new Callback<ArrayList<ChatMessage>>() {
            @Override
            public void onResponse(Call<ArrayList<ChatMessage>> call, Response<ArrayList<ChatMessage>> response) {
                //Abrir outra tela caso a conta for activada com sucesso
                if (response.isSuccessful() && response.code() == 201) {
                    /*Get the response body json object*/

                    ArrayList<ChatMessage> arrayList = response.body();

                    messageAdapter = new MessageAdapter(getApplicationContext(), arrayList);
                    recyclerView_messages.setAdapter(messageAdapter);

                } else {
                    Toast.makeText(ChatMesssageActivity.this, "Erro inesperado: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ChatMessage>> call, Throwable t) {
                Toast.makeText(ChatMesssageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
