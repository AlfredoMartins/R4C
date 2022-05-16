package ao.co.r4c.service;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

import ao.co.r4c.model.UsuariosActivo;

public class SocketClient {

    private final Socket socket;

    {
        try {
            socket = IO.socket(ApiClient.BASE_IP + ":3000");
        } catch (URISyntaxException e) {
        }
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void connectUser(int id, int categoria) {
        if (!socket.connected()) {
            socket.connect();
            socket.emit("user_connect", id, categoria);
        }
    }

    public void disconnectUser(int id, int categoria) {
        if (socket.connected()) {
            socket.emit("user_disconnected", id, categoria);
            socket.disconnect();
        }
    }

    public void updateDriverState(UsuariosActivo usuariosActivo) {
        //Collections

        if (socket.connected()) {
            socket.emit("update_driver_data",
                    usuariosActivo.getId(),
                    usuariosActivo.getNome(),
                    usuariosActivo.getLatitude(),
                    usuariosActivo.getLongitude());
        }
    }


    public void updateCustomerState(UsuariosActivo usuariosActivo) {
        //Collections

        if (socket.connected()) {
            socket.emit("update_customer_data",
                    usuariosActivo.getId(),
                    usuariosActivo.getNome(),
                    usuariosActivo.getLatitude(),
                    usuariosActivo.getLongitude());
        }
    }

}
