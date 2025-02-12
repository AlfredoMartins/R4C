package ao.co.r4c.helper;

import static ao.co.r4c.helper.Constantes.getName;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import ao.co.r4c.R;
import ao.co.r4c.model.MotoristaInfo;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    View view;

    public CustomInfoWindow(Context context) {
        this.view = LayoutInflater.from(context)
                .inflate(R.layout.custom_driver_info_window,
                        null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        TextView txt_pick_up_info = view.findViewById(R.id.txt_pick_up_info);
        txt_pick_up_info.setText(getName(marker.getTitle()));

        TextView txt_pick_up_snippet = view.findViewById(R.id.txt_pick_up_snippet);
        txt_pick_up_snippet.setText(marker.getSnippet());

        MotoristaInfo.distancia = marker.getSnippet();

        return view;
    }


    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}