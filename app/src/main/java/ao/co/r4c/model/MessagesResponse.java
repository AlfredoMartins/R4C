package ao.co.r4c.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessagesResponse {

    @Expose
    @SerializedName("user_id")
    private int user_id;

    @Expose
    @SerializedName("nome")
    private String nome;


    @Expose
    @SerializedName("telefone")
    private String telefone;

    @Expose
    @SerializedName("texto")
    private String texto;

    @Expose
    @SerializedName("foto_url")
    private String foto_url;

    @Expose
    @SerializedName("data")
    private String data;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFoto_url() {
        return foto_url;
    }

    public void setFoto_url(String foto_url) {
        this.foto_url = foto_url;
    }
}
