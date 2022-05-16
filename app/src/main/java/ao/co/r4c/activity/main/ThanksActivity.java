package ao.co.r4c.activity.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ao.co.r4c.R;
import ao.co.r4c.model.Viagem;
import ao.co.r4c.service.ApiClient;
import ao.co.r4c.service.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThanksActivity extends AppCompatActivity {

    Button btn_enviar;
    EditText txt_comentario;
    int id_avaliacao = 0;

    List<ImageView> imageViewList;
    ImageView img_star_1, img_star_2, img_star_3, img_star_4, img_star_5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);

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

        img_star_1 = findViewById(R.id.star_1);
        img_star_2 = findViewById(R.id.star_2);
        img_star_3 = findViewById(R.id.star_3);
        img_star_4 = findViewById(R.id.star_4);
        img_star_5 = findViewById(R.id.star_5);


        imageViewList = new ArrayList<>();
        imageViewList.add(img_star_1);
        imageViewList.add(img_star_2);
        imageViewList.add(img_star_3);
        imageViewList.add(img_star_4);
        imageViewList.add(img_star_5);

        for (ImageView item : imageViewList
        ) {
            item.setImageResource(R.drawable.ic_star_border_yellow);
        }

        img_star_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int j = id_avaliacao = 1;
                int i = 0;

                for (; i < j; i++) {
                    imageViewList.get(i).setImageResource(R.drawable.ic_star_yellow);
                }

                for (; j < imageViewList.size(); j++) {
                    imageViewList.get(i).setImageResource(R.drawable.ic_star_border_yellow);
                }
            }
        });

        img_star_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (ImageView item : imageViewList
                ) {
                    item.setImageResource(R.drawable.ic_star_border_yellow);
                }

                int j = id_avaliacao = 2;
                int i = 0;

                for (; i < j; i++) {
                    imageViewList.get(i).setImageResource(R.drawable.ic_star_yellow);
                }
            }
        });

        img_star_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for (ImageView item : imageViewList
                ) {
                    item.setImageResource(R.drawable.ic_star_border_yellow);
                }

                int j = id_avaliacao = 3;
                int i = 0;

                for (; i < j; i++) {
                    imageViewList.get(i).setImageResource(R.drawable.ic_star_yellow);
                }
            }
        });

        img_star_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (ImageView item : imageViewList
                ) {
                    item.setImageResource(R.drawable.ic_star_border_yellow);
                }

                int j = id_avaliacao = 4;
                int i = 0;

                for (i = 0; i < j; i++) {
                    imageViewList.get(i).setImageResource(R.drawable.ic_star_yellow);
                }

            }
        });

        img_star_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for (ImageView item : imageViewList
                ) {
                    item.setImageResource(R.drawable.ic_star_border_yellow);
                }

                int j = id_avaliacao = 5;
                int i = 0;

                for (i = 0; i < j; i++) {
                    imageViewList.get(i).setImageResource(R.drawable.ic_star_yellow);
                }

            }
        });

        btn_enviar = findViewById(R.id.btn_enviar);
        txt_comentario = findViewById(R.id.txt_comentario);

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Instanciate the ApiInterface*/
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

                /*Call the interface*/
                int id_viagem = Objects.requireNonNull(getIntent().getExtras()).getInt("id_viagem");
                String comentario = txt_comentario.getText().toString();

                Call<Viagem> call = apiInterface.avaliarViagem(id_viagem, id_avaliacao, comentario);

                /*Enqueue*/
                call.enqueue(new Callback<Viagem>() {
                    @Override
                    public void onResponse(Call<Viagem> call, Response<Viagem> response) {
                        if (response.isSuccessful() && response.code() == 201) {

                            FinishTripDialogFragment finishTripDialogFragment = new FinishTripDialogFragment();
                            finishTripDialogFragment.show(getSupportFragmentManager(), "Viagem terminada");

                        } else {
                            Toast.makeText(ThanksActivity.this, "Erro inesperado: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Viagem> call, Throwable t) {
                        Toast.makeText(ThanksActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

}
