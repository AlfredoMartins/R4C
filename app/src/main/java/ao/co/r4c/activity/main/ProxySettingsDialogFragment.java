package ao.co.r4c.activity.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ao.co.r4c.R;

public class ProxySettingsDialogFragment extends DialogFragment {

    TextView txt_url_web_service, txt_port_web_socket;
    Button btn_update_proxy;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_proxy_settings_dialog, container, false);

        //Instanciating the components
        txt_port_web_socket = view.findViewById(R.id.txt_websocket_port);
        txt_url_web_service = view.findViewById(R.id.txt_url_webservice);
        btn_update_proxy = view.findViewById(R.id.btn_update_proxy);

        return view;
    }
}