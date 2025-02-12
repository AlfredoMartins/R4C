package ao.co.r4c.model;

public class TripHistoryItem {

    private int id;
    private int user_id;
    private String user_name;
    private String origem;
    private String destino;
    private String valor_pago;
    private String avaliacao;
    private String data;
    private String duracao;

    public TripHistoryItem(int id, int user_id, String user_name, String origem, String destino, String valor_pago, String avaliacao, String data, String duracao) {
        this.id = id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.origem = origem;
        this.destino = destino;
        this.valor_pago = valor_pago;
        this.avaliacao = avaliacao;
        this.data = data;
        this.duracao = duracao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public String getValor_pago() {
        return valor_pago;
    }

    public void setValor_pago(String valor_pago) {
        this.valor_pago = valor_pago;
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

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }
}
