package ao.co.r4c.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatMessage {

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("id_emissor")
    private int id_sender;

    @Expose
    @SerializedName("id_receptor")
    private int id_receiver;

    @Expose
    @SerializedName("texto")
    private String message;

    @Expose
    @SerializedName("visualizado")
    private int visualizado;

    @Expose
    @SerializedName("data_mensagem")
    private String data;

    public ChatMessage(int id_sender, int id_receiver, String message, String data) {
        this.id_sender = id_sender;
        this.id_receiver = id_receiver;
        this.message = message;
        this.data = data;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_sender() {
        return id_sender;
    }

    public void setId_sender(int id_sender) {
        this.id_sender = id_sender;
    }

    public int getId_receiver() {
        return id_receiver;
    }

    public void setId_receiver(int id_receiver) {
        this.id_receiver = id_receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int isVisualizado() {
        return visualizado;
    }

    public void setVisualizado(int visualizado) {
        this.visualizado = visualizado;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
