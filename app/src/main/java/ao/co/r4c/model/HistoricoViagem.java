package ao.co.r4c.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoricoViagem {

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("id_user")
    private int id_user;

    @Expose
    @SerializedName("nome")
    private String nome;

    @Expose
    @SerializedName("origem")
    private String origem;

    @Expose
    @SerializedName("destino")
    private String destino;

    @Expose
    @SerializedName("preco")
    private String preco;

    @Expose
    @SerializedName("duracao")
    private String duracao;


    @Expose
    @SerializedName("avaliacao")
    private String avaliacao;


    @Expose
    @SerializedName("data")
    private String data;


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

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
}
