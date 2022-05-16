package ao.co.r4c.activity.main;

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
import ao.co.r4c.adapter.TripHistoryAdapter;
import ao.co.r4c.model.TripHistoryItem;

public class TripsHistoryFragment extends Fragment {

    TripHistoryAdapter tripHistoryAdapter;
    List<TripHistoryItem> tripHistoryItemList;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_history, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_history_trips);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        tripHistoryItemList = new ArrayList<>();

        readHistory();

        return view;
    }


    private void readHistory() {
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

            tripHistoryAdapter = new TripHistoryAdapter(getContext(), tripHistoryItemList);
            recyclerView.setAdapter(tripHistoryAdapter);

        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
