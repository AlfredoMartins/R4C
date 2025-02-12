package ao.co.r4c.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ao.co.r4c.R;
import ao.co.r4c.adapter.TripHistoryAdapter;
import ao.co.r4c.helper.Constantes;
import ao.co.r4c.model.HistoricoViagem;
import ao.co.r4c.model.TripHistoryItem;
import ao.co.r4c.service.ApiClient;
import ao.co.r4c.service.ApiInterface;
import ao.co.r4c.service.SocketClient;
import ao.co.r4c.storage.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripsActivity extends AppCompatActivity {

    ViewPagerTripAdapter viewPagerTripAdapter;
    TripHistoryAdapter tripHistoryAdapter;
    List<TripHistoryItem> tripHistoryItemList;
    RecyclerView recyclerView;
    WaitingDialogFragment waitingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);


        try {
            //Instatiate the socket
            Constantes.socketClient = new SocketClient();
            Constantes.socketClient.connectUser(SharedPreferenceManager.getInstance(this).getUsuario().getId(), 2);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        //Calling the dialog information proxy settings
        waitingDialogFragment = new WaitingDialogFragment();
        waitingDialogFragment.show(getSupportFragmentManager(), "Informações");


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

        recyclerView = findViewById(R.id.recyclerview_history_trips);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        tripHistoryItemList = new ArrayList<>();

        readHistory();

    }


    private void readHistoryTest() {
        tripHistoryItemList.clear();

        try {

            for (int i = 0; i < 20; i++)
                tripHistoryItemList.add(new TripHistoryItem(
                        i,
                        i,
                        "Alfredo Martins",
                        "Origem: Rocha-Pinto",
                        "Destino: Benfica",
                        "Valor pago: 5 000 Kzs",
                        "Avaliação: 5.0",
                        "02h:42min 04-08-2019",
                        "Duração: 5 minutos"));

            tripHistoryAdapter = new TripHistoryAdapter(getApplicationContext(), tripHistoryItemList);
            recyclerView.setAdapter(tripHistoryAdapter);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    /*List all avalible places*/
    private void readHistory() {

        /*Instanciate the ApiInterface*/
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        int user_id = SharedPreferenceManager.getInstance(this).getUsuario().getId();

        /*Call the interface*/
        Call<ArrayList<HistoricoViagem>> listCall;

        int id_categoria = SharedPreferenceManager.getInstance(this).getUsuario().getId_categoria();

        if (id_categoria == 1)
            listCall = apiInterface.listarHistoricoTodasViagens(user_id);
        else
            listCall = apiInterface.listarHistoricoTodasViagensPassageiro(user_id);

        /*Enqueue*/
        listCall.enqueue(new Callback<ArrayList<HistoricoViagem>>() {
            @Override
            public void onResponse(Call<ArrayList<HistoricoViagem>> call, Response<ArrayList<HistoricoViagem>> response) {
                if (response.isSuccessful() && response.code() == 201) {

                    try {
                        tripHistoryItemList.clear();

                        /*Get the response body json object*/

                        for (HistoricoViagem item : Objects.requireNonNull(response.body())
                        ) {
                            tripHistoryItemList.add(new TripHistoryItem(item.getId(), item.getId_user(), item.getNome(), item.getOrigem(), item.getDestino(), item.getPreco(), item.getAvaliacao(), item.getData(), item.getDuracao()));
                            //Toast.makeText(context, item.getNome(), Toast.LENGTH_SHORT).show();
                        }

                        tripHistoryAdapter = new TripHistoryAdapter(getApplicationContext(), tripHistoryItemList);
                        recyclerView.setAdapter(tripHistoryAdapter);
                    } catch (Exception e) {
                        Toast.makeText(TripsActivity.this, "Ainda não possui dados suficientes para apresentar.", Toast.LENGTH_SHORT).show();
                    }
                    waitingDialogFragment.dismiss();

                } else {
                    waitingDialogFragment.dismiss();
                    Toast.makeText(getApplicationContext(), "Erro inesperado: " + response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<HistoricoViagem>> call, Throwable t) {
                waitingDialogFragment.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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


    class ViewPagerTripAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragmentArrayList;
        ArrayList<String> titles;

        ViewPagerTripAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

            fragmentArrayList = new ArrayList<>();
            titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentArrayList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentArrayList.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }


    }
}