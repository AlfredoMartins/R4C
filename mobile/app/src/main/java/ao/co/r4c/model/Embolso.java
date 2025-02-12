package ao.co.r4c.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Embolso {

    @Expose
    @SerializedName("ganho")
    private Double ganho;


    @Expose
    @SerializedName("tempo")
    private String tempo;


    @Expose
    @SerializedName("viagens_completas")
    private int viagens_completas;


    @Expose
    @SerializedName("viagens_incompletas")
    private int viagens_incompletas;

    public Double getGanho() {
        return ganho;
    }

    public void setGanho(Double ganho) {
        this.ganho = ganho;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public int getViagens_completas() {
        return viagens_completas;
    }

    public void setViagens_completas(int viagens_completas) {
        this.viagens_completas = viagens_completas;
    }

    public int getViagens_incompletas() {
        return viagens_incompletas;
    }

    public void setViagens_incompletas(int viagens_incompletas) {
        this.viagens_incompletas = viagens_incompletas;
    }
}
