package ao.co.r4c.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Estatistica {

    @Expose
    @SerializedName("soma")
    private String soma;


    @Expose
    @SerializedName("quantidade_avaliacoes")
    private int quantidade_avaliacoes;


    @Expose
    @SerializedName("quantidade_1")
    private int quantidade_1;


    @Expose
    @SerializedName("quantidade_2")
    private int quantidade_2;


    @Expose
    @SerializedName("quantidade_3")
    private int quantidade_3;


    @Expose
    @SerializedName("quantidade_4")
    private int quantidade_4;


    @Expose
    @SerializedName("quantidade_5")
    private int quantidade_5;

    public String getSoma() {
        return soma;
    }

    public void setSoma(String soma) {
        this.soma = soma;
    }

    public int getQuantidade_avaliacoes() {
        return quantidade_avaliacoes;
    }

    public void setQuantidade_avaliacoes(int quantidade_avaliacoes) {
        this.quantidade_avaliacoes = quantidade_avaliacoes;
    }

    public int getQuantidade_1() {
        return quantidade_1;
    }

    public void setQuantidade_1(int quantidade_1) {
        this.quantidade_1 = quantidade_1;
    }

    public int getQuantidade_2() {
        return quantidade_2;
    }

    public void setQuantidade_2(int quantidade_2) {
        this.quantidade_2 = quantidade_2;
    }

    public int getQuantidade_3() {
        return quantidade_3;
    }

    public void setQuantidade_3(int quantidade_3) {
        this.quantidade_3 = quantidade_3;
    }

    public int getQuantidade_4() {
        return quantidade_4;
    }

    public void setQuantidade_4(int quantidade_4) {
        this.quantidade_4 = quantidade_4;
    }

    public int getQuantidade_5() {
        return quantidade_5;
    }

    public void setQuantidade_5(int quantidade_5) {
        this.quantidade_5 = quantidade_5;
    }
}
