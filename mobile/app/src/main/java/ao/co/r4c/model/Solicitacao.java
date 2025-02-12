package ao.co.r4c.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Solicitacao {

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("id_motorista")
    private int id_motorista;


    @Expose
    @SerializedName("nome_motorista")
    private String nome_motorista;

    @Expose
    @SerializedName("id_passageiro")
    private int id_passageiro;

    @Expose
    @SerializedName("nome_passageiro")
    private String nome_passageiro;


    @Expose
    @SerializedName("minutos")
    private String minutos;

    @Expose
    @SerializedName("endereco")
    private String endereco;


    @Expose
    @SerializedName("distancia")
    private float distancia;

    @Expose
    @SerializedName("lat_origem")
    private float lat_origem;

    @Expose
    @SerializedName("lon_origem")
    private float lon_origem;

    @Expose
    @SerializedName("lat_destino")
    private float lat_destino;

    @Expose
    @SerializedName("lon_destino")
    private float lon_destino;

    @Expose
    @SerializedName("descricao")
    private String descricao;

    @Expose
    @SerializedName("estado_solicitacao")
    private Boolean estado_solicitacao;


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

    public String getNome_motorista() {
        return nome_motorista;
    }

    public void setNome_motorista(String nome_motorista) {
        this.nome_motorista = nome_motorista;
    }

    public int getId_passageiro() {
        return id_passageiro;
    }

    public void setId_passageiro(int id_passageiro) {
        this.id_passageiro = id_passageiro;
    }

    public String getNome_passageiro() {
        return nome_passageiro;
    }

    public void setNome_passageiro(String nome_passageiro) {
        this.nome_passageiro = nome_passageiro;
    }

    public String getMinutos() {
        return minutos;
    }

    public void setMinutos(String minutos) {
        this.minutos = minutos;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    public float getLat_origem() {
        return lat_origem;
    }

    public void setLat_origem(float lat_origem) {
        this.lat_origem = lat_origem;
    }

    public float getLon_origem() {
        return lon_origem;
    }

    public void setLon_origem(float lon_origem) {
        this.lon_origem = lon_origem;
    }

    public float getLat_destino() {
        return lat_destino;
    }

    public void setLat_destino(float lat_destino) {
        this.lat_destino = lat_destino;
    }

    public float getLon_destino() {
        return lon_destino;
    }

    public void setLon_destino(float lon_destino) {
        this.lon_destino = lon_destino;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getEstado_solicitacao() {
        return estado_solicitacao;
    }

    public void setEstado_solicitacao(Boolean estado_solicitacao) {
        this.estado_solicitacao = estado_solicitacao;
    }
}
