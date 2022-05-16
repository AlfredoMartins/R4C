package ao.co.r4c.activity.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ao.co.r4c.R;
import ao.co.r4c.adapter.NotificationAdapter;
import ao.co.r4c.model.NotificationItem;

public class NotificationsActivity extends AppCompatActivity {

    NotificationAdapter notificationAdapter;
    List<NotificationItem> notificationItemList;
    RecyclerView recyclerView_notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

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


        recyclerView_notifications = findViewById(R.id.recyclerview_notifications);
        recyclerView_notifications.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(false);
        recyclerView_notifications.setLayoutManager(linearLayoutManager);


        notificationItemList = new ArrayList<>();

        readNotifications();


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

            notificationAdapter = new NotificationAdapter(getApplicationContext(), notificationItemList);
            recyclerView_notifications.setAdapter(notificationAdapter);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
