package ao.co.r4c.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Viagem {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("id_motorista")
    private int id_motorista;


    @Expose
    @SerializedName("nome")
    private String nome;

    @Expose
    @SerializedName("id_passageiro")
    private int id_passageiro;

    @Expose
    @SerializedName("id_local_origem")
    private int id_local_origem;

    @Expose
    @SerializedName("id_local_destino")
    private int id_local_destino;


    @Expose
    @SerializedName("descricao")
    private String descricao;

    @Expose
    @SerializedName("preco")
    private Double preco;

    @Expose
    @SerializedName("id_avaliacao")
    private int id_avaliacao;

    @Expose
    @SerializedName("id_estado")
    private int id_estado;

    @Expose
    @SerializedName("tempo_inicio")
    private String tempo_inicio;

    @Expose
    @SerializedName("tempo_termino")
    private String tempo_termino;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_motorista() {
        return id_motorista;
    }

    public void setId_motorista(int id_motorista) {
        this.id_motorista = id_motorista;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId_passageiro() {
        return id_passageiro;
    }

    public void setId_passageiro(int id_passageiro) {
        this.id_passageiro = id_passageiro;
    }

    public int getId_local_origem() {
        return id_local_origem;
    }

    public void setId_local_origem(int id_local_origem) {
        this.id_local_origem = id_local_origem;
    }

    public int getId_local_destino() {
        return id_local_destino;
    }

    public void setId_local_destino(int id_local_destino) {
        this.id_local_destino = id_local_destino;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public int getId_avaliacao() {
        return id_avaliacao;
    }

    public void setId_avaliacao(int id_avaliacao) {
        this.id_avaliacao = id_avaliacao;
    }

    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }

    public String getTempo_inicio() {
        return tempo_inicio;
    }

    public void setTempo_inicio(String tempo_inicio) {
        this.tempo_inicio = tempo_inicio;
    }

    public String getTempo_termino() {
        return tempo_termino;
    }

    public void setTempo_termino(String tempo_termino) {
        this.tempo_termino = tempo_termino;
    }
}
