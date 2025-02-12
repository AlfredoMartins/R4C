package ao.co.r4c.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TelcoSMS {

    @Expose
    @SerializedName("api_key_app")
    private final String api_key_app = "qasd742db5bc01cef8d540d0c1393";

    @Expose
    @SerializedName("phone_number")
    private final String phone_number;

    @Expose
    @SerializedName("message_body")
    private final String message_body;

    public TelcoSMS(String phone_number, String message_body) {
        this.phone_number = phone_number;
        this.message_body = message_body;
    }
}