package ao.co.r4c.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ao.co.r4c.R;
import ao.co.r4c.activity.main.customer.fragment.CustomerMapsActivity;
import ao.co.r4c.helper.Constantes;

public class FinishTripDialogFragment extends DialogFragment {


    Button btn_thanks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_finish_trip_dialog, container, false);

        //Instanciating the components
        btn_thanks = view.findViewById(R.id.btn_thanks);

        btn_thanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CustomerMapsActivity.class);
                Constantes.Zerar();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                dismiss();

            }
        });

        return view;
    }

}