package ao.co.r4c.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Usuario {

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("nome")
    private String nome;

    @Expose
    @SerializedName("sobrenome")
    private String sobrenome;

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("senha")
    private String senha;

    @Expose
    @SerializedName("id_categoria")
    private int id_categoria;

    @Expose
    @SerializedName("telefone")
    private String telefone;

    @Expose
    @SerializedName("foto_url")
    private String foto_url;


    public String getFoto_url() {
        return foto_url;
    }

    public void setFoto_url(String foto_url) {
        this.foto_url = foto_url;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
