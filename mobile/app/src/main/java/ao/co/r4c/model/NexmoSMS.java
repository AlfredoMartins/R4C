package ao.co.r4c.model;

import com.google.gson.annotations.SerializedName;


public class NexmoSMS {

    @SerializedName("api_key")
    private final String API_KEY = "92bfc121_";

    @SerializedName("api_secret")
    private final String API_SECRET = "IV5eqJ9DB0g4vc6R";

    @SerializedName("to")
    private final String to;

    @SerializedName("from")
    private final String FROM = "R4C-Serviço de Táxi";

    @SerializedName("text")
    private final String text;

    public NexmoSMS(String to, String text) {
        this.to = to;
        this.text = text;
    }

    public String getAPI_KEY() {
        return API_KEY;
    }

    public String getAPI_SECRET() {
        return API_SECRET;
    }

    public String getTo() {
        return to;
    }

    public String getFROM() {
        return FROM;
    }

    public String getText() {
        return text;
    }
}