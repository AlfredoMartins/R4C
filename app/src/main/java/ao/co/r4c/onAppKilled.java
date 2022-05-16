package ao.co.r4c;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import ao.co.r4c.helper.Constantes;
import ao.co.r4c.storage.SharedPreferenceManager;

public class onAppKilled extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Constantes.socketClient.disconnectUser(SharedPreferenceManager.getInstance(this).getUsuario().getId(), SharedPreferenceManager.getInstance(this).getUsuario().getId_categoria());
    }
}
