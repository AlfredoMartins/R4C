package ao.co.r4c.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comentario {

    @Expose
    @SerializedName("id")
    private int id;


    @Expose
    @SerializedName("nome")
    private String nome;


    @Expose
    @SerializedName("avaliacao")
    private int avaliacao;


    @Expose
    @SerializedName("comentario")
    private String comentario;


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

    public int getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(int avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
