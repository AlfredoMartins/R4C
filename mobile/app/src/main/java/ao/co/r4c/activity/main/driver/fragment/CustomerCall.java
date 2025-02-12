package ao.co.r4c.activity.main.driver.fragment;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.maps.GeoApiContext;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import ao.co.r4c.R;
import ao.co.r4c.helper.Constantes;
import ao.co.r4c.model.google.Directions;
import ao.co.r4c.service.ApiClient;
import ao.co.r4c.service.ApiGoogle;
import ao.co.r4c.service.ApiInterface;
import ao.co.r4c.storage.SharedPreferenceManager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerCall extends AppCompatActivity {

    TextView txt_timer, txt_distance, txt_address, txt_name, txt_phone, txt_descricao;
    ImageButton imageButton;
    MediaPlayer mediaPlayer;

    Vibrator vibrator;
    GoogleApiClient googleApiClient;
    ApiInterface googleAPI;
    boolean ligado = true;
    Timer timer;
    Runnable runnable;
    Handler handler;
    int cont = 0;
    NotificationManagerCompat notificationManagerCompat;
    Button btnAccept, btnReject;
    GeoApiContext geoApiContext;
    CircleImageView img_driver;

    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_customer_call);

        Constantes.ESTADO_CHAMADA = 1;

        txt_name = findViewById(R.id.txt_name);
        txt_phone = findViewById(R.id.txt_phone);
        txt_distance = findViewById(R.id.txt_distance);
        txt_address = findViewById(R.id.txt_address);

        imageButton = findViewById(R.id.img_volume);
        btnAccept = findViewById(R.id.btn_accept);
        btnReject = findViewById(R.id.btn_reject);

        img_driver = findViewById(R.id.img_driver);

        try {

            txt_name.setText(Objects.requireNonNull(getIntent().getExtras()).getString("nome"));
            txt_phone.setText(Objects.requireNonNull(getIntent().getExtras().getString("telefone")));
            txt_distance.setText(Objects.requireNonNull(getIntent().getExtras().getString("distancia")));
            txt_address.setText(Objects.requireNonNull(String.format("%s-%s", getIntent().getExtras().getString("origem"), getIntent().getExtras().getString("destino"))));

            try {
                Glide.with(CustomerCall.this).load(ApiClient.getBaseUrl() + "r4c/api/objects/usuarios/upload_images/" + Objects.requireNonNull(getIntent().getExtras()).getString("id_passageiro") + ".jpg").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(img_driver);
            } catch (Exception e) {
                img_driver.setImageResource(R.drawable.img_user_default);
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = SharedPreferenceManager.getInstance(getApplicationContext()).getUsuario().getNome();
                String telefone = SharedPreferenceManager.getInstance(getApplicationContext()).getUsuario().getTelefone();

                String id_passageiro = Objects.requireNonNull(getIntent().getExtras()).getString("id_passageiro");

                Constantes.socketClient.getSocket().emit("driver_answer",
                        Integer.parseInt(Objects.requireNonNull(id_passageiro)),
                        "Yes",
                        nome,
                        telefone);


                String nome_passageiro = Objects.requireNonNull(getIntent().getExtras()).getString("nome");
                String telefone_passageiro = Objects.requireNonNull(getIntent().getExtras().getString("telefone"));
                String origem_passageiro = Objects.requireNonNull(getIntent().getExtras().getString("origem"));
                String destino_passageiro = Objects.requireNonNull(getIntent().getExtras().getString("destino"));


                DriverHomeFragment.telefone_passageiro = telefone_passageiro;
                DriverHomeFragment.txt_nome.setText(nome_passageiro);
                DriverHomeFragment.txt_origem.setText(origem_passageiro);
                DriverHomeFragment.txt_destino.setText(destino_passageiro);
                DriverHomeFragment.txt_telefone.setText(telefone_passageiro);

                DriverHomeFragment.cardView_bottom_sheet_rider_customer.setVisibility(View.VISIBLE);

                DriverHomeFragment.btn_search_customer.setText("Iniciar viagem");
                Constantes.NIVEL_VIAGEM = 1;
                Constantes.ESTADO_CHAMADA = 0;

                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();

                if (vibrator != null)
                    vibrator.cancel();


                finish();
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerCall.this, "Rejeitar", Toast.LENGTH_SHORT).show();

                Constantes.socketClient.getSocket().emit("driver_answer", 0, "No", "R4C", "999 999 999");

                DriverHomeFragment.cardView_bottom_sheet_rider_customer.setVisibility(View.INVISIBLE);


                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();


                if (vibrator != null)
                    vibrator.cancel();

                Constantes.ESTADO_CHAMADA = 0;

                finish();
            }
        });


        notificationManagerCompat = NotificationManagerCompat.from(this);


        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ligado) {
                    ligado = false;
                    imageButton.setImageResource(R.drawable.ic_volume_off);
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);

                } else {
                    ligado = true;
                    imageButton.setImageResource(R.drawable.ic_volume_up);
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                }
            }
        });

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        mediaPlayer = MediaPlayer.create(this, R.raw.hello_remix);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                if (cont < 15) {

                    if (SharedPreferenceManager.getInstance(getApplicationContext()).getSettings().isVibrar())
                        vibrator.vibrate(900);

                    cont++;
                } else {
                    mediaPlayer.stop();
                    timer.cancel();

                    notificationManagerCompat.cancelAll();
                    RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification_app);

                    remoteViews.setTextViewText(R.id.txt_mensagem, "Solicitação perdida.");

                    PendingIntent clickPending = PendingIntent.getActivity(getApplicationContext(), 0, getIntent(), PendingIntent.FLAG_UPDATE_CURRENT);


                    remoteViews.setOnClickPendingIntent(R.id.txt_mensagem, clickPending);

                    Notification notification = new NotificationCompat.Builder(getApplicationContext(), "R4C")
                            .setSmallIcon(R.drawable.r4c_logo_img)
                            .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                            .setContentTitle("Notificação")
                            .setCategory(NotificationCompat.CATEGORY_CALL)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setCustomContentView(remoteViews)
                            .setAutoCancel(true)
                            .build();

                    notificationManagerCompat.notify(1, notification);
                    notificationManagerCompat.getImportance();

                }
            }
        };


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 10, 2500);
    }


    private void showNotification() {

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification_app);

        remoteViews.setTextViewText(R.id.txt_mensagem, "Algum passageiro está solicitando o seu serviço.");

        PendingIntent clickPending = PendingIntent.getActivity(getApplicationContext(), 0, getIntent(), PendingIntent.FLAG_UPDATE_CURRENT);


        remoteViews.setOnClickPendingIntent(R.id.txt_mensagem, clickPending);

        Notification notification = new NotificationCompat.Builder(this, "R4C")
                .setSmallIcon(R.drawable.r4c_logo_img)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentTitle("Notificação")
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCustomContentView(remoteViews)
                .setAutoCancel(true)
                .build();

        notificationManagerCompat.notify(1, notification);
        notificationManagerCompat.getImportance();

    }

    @Override
    protected void onPause() {
        super.onPause();
        showNotification();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void getDuration() {

        googleAPI = ApiGoogle.getApiClient().create(ApiInterface.class);

        Call<Directions> call = googleAPI.informacoesCaminhos(
                "driving",
                "less_driving",
                "-8.824326, 13.246662",
                " -8.823693, 13.245404",
                //"k"
                getResources().getString(R.string.google_api_key)
        );

        call.enqueue(new Callback<Directions>() {
            @Override
            public void onResponse(@NonNull Call<Directions> call, @NonNull Response<Directions> response) {
                try {
                    String error = Objects.requireNonNull(response.body()).toString();
                    Toast.makeText(CustomerCall.this, "Duração: " + error, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(CustomerCall.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Directions> call, @NonNull Throwable t) {
                Toast.makeText(CustomerCall.this, "Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notificationManagerCompat.cancel(1);
        notificationManagerCompat.cancelAll();

        Constantes.ESTADO_CHAMADA = 0;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Constantes.ESTADO_CHAMADA = 0;
    }
}
