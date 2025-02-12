package ao.co.r4c.activity.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import ao.co.r4c.R;

public class WaitingDialogFragment extends DialogFragment {

    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.waiting_dialog, container, false);

        //Instanciating the components
        progressBar = view.findViewById(R.id.waiting);

        return view;
    }
}