package ao.co.r4c.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Local {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("nome")
    private String nome;

    @Expose
    @SerializedName("latitude")
    private Double latitude;

    @Expose
    @SerializedName("longitude")
    private Double longitude;

    /*
    @Expose
    @SerializedName("descricao")
    private String descricao;*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
