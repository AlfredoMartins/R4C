package ao.co.r4c.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Noticia {

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("titulo")
    private String titulo;

    @Expose
    @SerializedName("descricao")
    private String descricao;

    @Expose
    @SerializedName("local")
    private String local;

    @Expose
    @SerializedName("foto_url")
    private String foto_url;

    @Expose
    @SerializedName("autor")
    private String autor;

    @Expose
    @SerializedName("data")
    private String data;

    public Noticia(int id, String titulo, String descricao, String local, String foto_url, String autor, String data) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.local = local;
        this.foto_url = foto_url;
        this.autor = autor;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getFoto_url() {
        return foto_url;
    }

    public void setFoto_url(String foto_url) {
        this.foto_url = foto_url;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
