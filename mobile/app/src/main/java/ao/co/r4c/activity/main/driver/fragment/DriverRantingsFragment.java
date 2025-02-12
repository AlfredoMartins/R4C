package ao.co.r4c.activity.main.driver.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ao.co.r4c.R;
import ao.co.r4c.activity.main.WaitingDialogFragment;
import ao.co.r4c.adapter.UserCommentsAdapter;
import ao.co.r4c.helper.Constantes;
import ao.co.r4c.model.Comentario;
import ao.co.r4c.model.Estatistica;
import ao.co.r4c.model.UserCommentItem;
import ao.co.r4c.service.ApiClient;
import ao.co.r4c.service.ApiInterface;
import ao.co.r4c.storage.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverRantingsFragment extends Fragment {

    WaitingDialogFragment waitingDialogFragment;
    TextView txt_total_avalicao, txt_avaliacao;
    TextView txt_star_1, txt_star_2, txt_star_3, txt_star_4, txt_star_5;
    ProgressBar progressBar_star_1, progressBar_star_2, progressBar_star_3, progressBar_star_4, progressBar_star_5;
    ImageView img_star_1, img_star_2, img_star_3, img_star_4, img_star_5;
    List<ImageView> imageViewList;
    private RecyclerView recyclerView;
    private UserCommentsAdapter userCommentsAdapter;
    private List<UserCommentItem> userItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rantings_driver, container, false);

        try {

            waitingDialogFragment = new WaitingDialogFragment();
            waitingDialogFragment.show(getFragmentManager(), "Informações");


            txt_star_1 = view.findViewById(R.id.txt_star_1);
            txt_star_2 = view.findViewById(R.id.txt_star_2);
            txt_star_3 = view.findViewById(R.id.txt_star_3);
            txt_star_4 = view.findViewById(R.id.txt_star_4);
            txt_star_5 = view.findViewById(R.id.txt_star_5);

            progressBar_star_1 = view.findViewById(R.id.progress_star_1);
            progressBar_star_2 = view.findViewById(R.id.progress_star_2);
            progressBar_star_3 = view.findViewById(R.id.progress_star_3);
            progressBar_star_4 = view.findViewById(R.id.progress_star_4);
            progressBar_star_5 = view.findViewById(R.id.progress_star_5);

            txt_avaliacao = view.findViewById(R.id.txt_avaliacao);
            txt_total_avalicao = view.findViewById(R.id.txt_total_avaliacao);

            img_star_1 = view.findViewById(R.id.star_1);
            img_star_2 = view.findViewById(R.id.star_2);
            img_star_3 = view.findViewById(R.id.star_3);
            img_star_4 = view.findViewById(R.id.star_4);
            img_star_5 = view.findViewById(R.id.star_5);

            imageViewList = new ArrayList<>();
            imageViewList.add(img_star_1);
            imageViewList.add(img_star_2);
            imageViewList.add(img_star_3);
            imageViewList.add(img_star_4);
            imageViewList.add(img_star_5);

            recyclerView = view.findViewById(R.id.recyclerview_comments);
            recyclerView.hasFixedSize();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


            try {
                readRantingData();
            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            userItemList = new ArrayList<>();

            readComments();

        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return view;
    }


    private void readRantingData() {
        /*Instanciate the ApiInterface*/
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        int user_id = SharedPreferenceManager.getInstance(getContext()).getUsuario().getId();

        /*Call the interface*/
        Call<Estatistica> embolsoCall = apiInterface.dadosEstatisticos(user_id);

        /*Enqueue*/
        embolsoCall.enqueue(new Callback<Estatistica>() {
            @Override
            public void onResponse(Call<Estatistica> call, Response<Estatistica> response) {
                if (response.isSuccessful() && response.code() == 201) {

                    try {

                        /*Get the response body json object*/
                        Estatistica estatistica = response.body();

                        Double media = (double) (Integer.valueOf(Objects.requireNonNull(estatistica).getSoma()) / Objects.requireNonNull(estatistica).getQuantidade_avaliacoes());

                        txt_avaliacao.setText(media.toString());
                        txt_total_avalicao.setText(estatistica.getQuantidade_avaliacoes() + " Total");

                        Double percentagem = (media * 100) / Constantes.TOTAL_ESTRELAS;
                        Constantes.carregarEstrelasDeAvaliacao(percentagem, imageViewList);

                        progressBar_star_1.setMax(estatistica.getQuantidade_avaliacoes());
                        progressBar_star_2.setMax(estatistica.getQuantidade_avaliacoes());
                        progressBar_star_3.setMax(estatistica.getQuantidade_avaliacoes());
                        progressBar_star_4.setMax(estatistica.getQuantidade_avaliacoes());
                        progressBar_star_5.setMax(estatistica.getQuantidade_avaliacoes());

                        progressBar_star_1.setProgress(estatistica.getQuantidade_1());
                        progressBar_star_2.setProgress(estatistica.getQuantidade_2());
                        progressBar_star_3.setProgress(estatistica.getQuantidade_3());
                        progressBar_star_4.setProgress(estatistica.getQuantidade_4());
                        progressBar_star_5.setProgress(estatistica.getQuantidade_5());

                        txt_star_1.setText(String.valueOf(estatistica.getQuantidade_1()));
                        txt_star_2.setText(String.valueOf(estatistica.getQuantidade_2()));
                        txt_star_3.setText(String.valueOf(estatistica.getQuantidade_3()));
                        txt_star_4.setText(String.valueOf(estatistica.getQuantidade_4()));
                        txt_star_5.setText(String.valueOf(estatistica.getQuantidade_5()));
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Ainda não possui dados suficientes para apresentar.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "Erro inesperado: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Estatistica> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void readComments() {

        /*Instanciate the ApiInterface*/
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        int user_id = SharedPreferenceManager.getInstance(getContext()).getUsuario().getId();

        /*Call the interface*/
        Call<ArrayList<Comentario>> listCall = apiInterface.carregar_comentarios(user_id);

        /*Enqueue*/
        listCall.enqueue(new Callback<ArrayList<Comentario>>() {
            @Override
            public void onResponse(Call<ArrayList<Comentario>> call, Response<ArrayList<Comentario>> response) {
                if (response.isSuccessful() && response.code() == 201) {

                    userItemList.clear();

                    /*Get the response body json object*/
                    ArrayList<Comentario> comentarioArrayList = response.body();

                    userItemList = new ArrayList<>();

                    for (Comentario item : Objects.requireNonNull(comentarioArrayList)
                    ) {
                        userItemList.add(new UserCommentItem(item.getId(), item.getNome(), item.getData(), item.getComentario(), item.getAvaliacao()));
                        //Toast.makeText(context, item.getNome(), Toast.LENGTH_SHORT).show();
                    }

                    userCommentsAdapter = new UserCommentsAdapter(getContext(), userItemList);
                    recyclerView.setAdapter(userCommentsAdapter);
                    waitingDialogFragment.dismiss();

                } else {
                    Toast.makeText(getContext(), "Erro inesperado: " + response.message(), Toast.LENGTH_SHORT).show();
                    waitingDialogFragment.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Comentario>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                waitingDialogFragment.dismiss();
            }
        });
    }
}