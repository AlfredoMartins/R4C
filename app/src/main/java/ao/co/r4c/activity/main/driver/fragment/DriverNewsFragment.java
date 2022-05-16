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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ao.co.r4c.R;
import ao.co.r4c.activity.main.WaitingDialogFragment;
import ao.co.r4c.adapter.NewsAdapter;
import ao.co.r4c.model.Noticia;
import ao.co.r4c.service.ApiClient;
import ao.co.r4c.service.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverNewsFragment extends Fragment {


    NewsAdapter newsAdapter;
    WaitingDialogFragment waitingDialogFragment;
    private RecyclerView recyclerView;
    private List<Noticia> noticiaList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_driver, container, false);


        //Calling the dialog information proxy settings
        waitingDialogFragment = new WaitingDialogFragment();
        waitingDialogFragment.show(getFragmentManager(), "Informações");


        recyclerView = view.findViewById(R.id.recyclerview_news);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setNestedScrollingEnabled(false);

        noticiaList = new ArrayList<>();

        try {

            readNews();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }


    /*List all avalible places*/
    private void readNews() {

        /*Instanciate the ApiInterface*/
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        /*Call the interface*/
        Call<ArrayList<Noticia>> listCall = apiInterface.carregar_noticias();

        /*Enqueue*/
        listCall.enqueue(new Callback<ArrayList<Noticia>>() {
            @Override
            public void onResponse(Call<ArrayList<Noticia>> call, Response<ArrayList<Noticia>> response) {
                if (response.isSuccessful() && response.code() == 201) {

                    try {

                        noticiaList.clear();

                        /*Get the response body json object*/
                        ArrayList<Noticia> noticias = response.body();

                        //noticiaList.addAll(Objects.requireNonNull(noticias));

                        newsAdapter = new NewsAdapter(getContext(), noticias);
                        recyclerView.setAdapter(newsAdapter);

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Ainda não possui dados suficientes para apresentar.", Toast.LENGTH_LONG).show();
                    }
                    waitingDialogFragment.dismiss();


                } else {
                    waitingDialogFragment.dismiss();
                    Toast.makeText(getActivity(), "Erro inesperado: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Noticia>> call, Throwable t) {
                waitingDialogFragment.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
