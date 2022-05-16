package ao.co.r4c.activity.main.customer.fragment;

import android.os.Bundle;
import android.os.Handler;
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
import java.util.Timer;
import java.util.TimerTask;

import ao.co.r4c.R;
import ao.co.r4c.activity.main.WaitingDialogFragment;
import ao.co.r4c.adapter.UsersChatListAdapter;
import ao.co.r4c.model.MessagesResponse;
import ao.co.r4c.model.UserItem;
import ao.co.r4c.service.ApiClient;
import ao.co.r4c.service.ApiInterface;
import ao.co.r4c.storage.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerChatFragment extends Fragment {


    Timer timer;
    Runnable runnable;
    Handler handler;
    WaitingDialogFragment waitingDialogFragment;
    private RecyclerView recyclerView;
    private UsersChatListAdapter usersChatListAdapter;
    private List<UserItem> userItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_customer, container, false);

        //Calling the dialog information proxy settings
        waitingDialogFragment = new WaitingDialogFragment();
        waitingDialogFragment.show(getFragmentManager(), "Informações");

        //Toast.makeText(getContext(), "Yes.", Toast.LENGTH_SHORT).show();

        recyclerView = view.findViewById(R.id.recyclerview_users_messages);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userItemList = new ArrayList<>();

        //readUsers();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                listarMensagens(SharedPreferenceManager.getInstance(getContext()).getUsuario().getId());
            }
        };


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 5, 60000);

        return view;
    }

    private void listarMensagens(int id_emissor) {

        /*Instanciate the ApiInterface*/
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        /*Call the interface*/
        Call<ArrayList<MessagesResponse>> listCall = apiInterface.listarMensagens(id_emissor);

        /*Enqueue*/
        listCall.enqueue(new Callback<ArrayList<MessagesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<MessagesResponse>> call, Response<ArrayList<MessagesResponse>> response) {
                //Abrir outra tela caso a conta for activada com sucesso
                if (response.isSuccessful() && response.code() == 201) {

                    try {

                        /*Get the response body json object*/
                        userItemList.clear();
                        ArrayList<MessagesResponse> arrayList = response.body();

                        for (MessagesResponse item : arrayList) {
                            userItemList.add(new UserItem(
                                    item.getUser_id(),
                                    item.getNome(),
                                    item.getTelefone(),
                                    item.getTexto(),
                                    item.getData(),
                                    item.getFoto_url()));
                        }

                        usersChatListAdapter = new UsersChatListAdapter(getContext(), userItemList);
                        recyclerView.setAdapter(usersChatListAdapter);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Ainda não possui dados suficientes para apresentar.", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getContext(), "Ainda não possui dados suficientes para apresentar.", Toast.LENGTH_LONG).show();
                }

                waitingDialogFragment.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<MessagesResponse>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                waitingDialogFragment.dismiss();
            }
        });
    }
}
