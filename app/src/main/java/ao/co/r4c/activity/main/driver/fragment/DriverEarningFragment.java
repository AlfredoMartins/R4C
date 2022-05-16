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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ao.co.r4c.R;
import ao.co.r4c.activity.main.WaitingDialogFragment;
import ao.co.r4c.adapter.UserEarnAdapter;
import ao.co.r4c.model.Embolso;
import ao.co.r4c.model.UserEarnItem;
import ao.co.r4c.model.Viagem;
import ao.co.r4c.service.ApiClient;
import ao.co.r4c.service.ApiInterface;
import ao.co.r4c.storage.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverEarningFragment extends Fragment {


    WaitingDialogFragment waitingDialogFragment;
    private RecyclerView recyclerView;
    private UserEarnAdapter usersEarntListAdapter;
    private List<UserEarnItem> userItemList;
    private TextView txt_tempo, txt_ganho, txt_viagens_completas, txt_viagens_incompletas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_earning_driver, container, false);


        //Calling the dialog information proxy settings
        waitingDialogFragment = new WaitingDialogFragment();
        waitingDialogFragment.show(getFragmentManager(), "Informações");


        txt_ganho = view.findViewById(R.id.txt_ganho);
        txt_tempo = view.findViewById(R.id.txt_tempo);
        txt_viagens_completas = view.findViewById(R.id.txt_viagens_completas);
        txt_viagens_incompletas = view.findViewById(R.id.txt_viagens_incompletas);

        try {
            readEarnData();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        recyclerView = view.findViewById(R.id.recyclerview_history);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userItemList = new ArrayList<>();

        readEarnUsers();

        return view;
    }


    private void readEarnData() {
        /*Instanciate the ApiInterface*/
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        int user_id = SharedPreferenceManager.getInstance(getContext()).getUsuario().getId();

        /*Call the interface*/
        Call<Embolso> embolsoCall = apiInterface.carregarEmbolso(user_id);

        /*Enqueue*/
        embolsoCall.enqueue(new Callback<Embolso>() {
            @Override
            public void onResponse(Call<Embolso> call, Response<Embolso> response) {
                if (response.isSuccessful() && response.code() == 201) {

                    try {
                        /*Get the response body json object*/
                        Embolso embolso = response.body();

                        txt_ganho.setText(Objects.requireNonNull(embolso).getGanho().toString() + "Kzs");
                        txt_tempo.setText(embolso.getTempo() + "min");
                        txt_viagens_completas.setText(String.valueOf(embolso.getViagens_completas()));
                        txt_viagens_incompletas.setText(String.valueOf(embolso.getViagens_incompletas()));

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Ainda não possui dados suficientes para apresentar.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "Erro inesperado: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Embolso> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*List all avalible places*/
    private void readEarnUsers() {

        /*Instanciate the ApiInterface*/
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        int user_id = SharedPreferenceManager.getInstance(getContext()).getUsuario().getId();

        /*Call the interface*/
        Call<ArrayList<Viagem>> listCall = apiInterface.listarHistoricoViagens(user_id);

        /*Enqueue*/
        listCall.enqueue(new Callback<ArrayList<Viagem>>() {
            @Override
            public void onResponse(Call<ArrayList<Viagem>> call, Response<ArrayList<Viagem>> response) {
                if (response.isSuccessful() && response.code() == 201) {

                    try {

                        userItemList.clear();

                        /*Get the response body json object*/
                        ArrayList<Viagem> viagemArrayList = response.body();

                        userItemList = new ArrayList<>();


                        for (Viagem item : Objects.requireNonNull(viagemArrayList)
                        ) {
                            userItemList.add(new UserEarnItem(item.getId_passageiro(), item.getNome(), item.getPreco(), item.getTempo_inicio()));
                            //Toast.makeText(context, item.getNome(), Toast.LENGTH_SHORT).show();
                        }

                        usersEarntListAdapter = new UserEarnAdapter(getContext(), userItemList);
                        recyclerView.setAdapter(usersEarntListAdapter);

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
            public void onFailure(Call<ArrayList<Viagem>> call, Throwable t) {
                waitingDialogFragment.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}