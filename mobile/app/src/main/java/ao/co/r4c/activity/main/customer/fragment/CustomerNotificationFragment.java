package ao.co.r4c.activity.main.customer.fragment;

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
import ao.co.r4c.adapter.NotificationAdapter;
import ao.co.r4c.model.NotificationItem;

public class CustomerNotificationFragment extends Fragment {

    NotificationAdapter notificationAdapter;
    private RecyclerView recyclerView;
    private List<NotificationItem> notificationItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_customer, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_notification);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        notificationItemList = new ArrayList<>();

        readNotifications();

        return view;
    }

    private void readNotifications() {

        notificationItemList.clear();

        try {

            for (int i = 0; i < 20; i++)
                notificationItemList.add(new NotificationItem(
                        i,
                        "Viagem",
                        "Motorista chegou. Há, também, o forte apelo em relação ao contato com outras pessoas. O participante melhora significativamente a relação social, conhecendo novas ideias e fazendo novos amigos, o que é fundamental para o desenvolvimento dos jovens.",
                        "10h:30min 27/07/2019"));

            notificationAdapter = new NotificationAdapter(getContext(), notificationItemList);
            recyclerView.setAdapter(notificationAdapter);

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}