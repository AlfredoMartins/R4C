package ao.co.r4c.model;

public class Settings {

    private boolean motorista_mais_proximo;
    private boolean receber_notificacoes;
    private boolean vibrar;
    private boolean receber_notificacoes_actualizacoes;
    private String ur_web_service;
    private String porta_web_service;

    public boolean isMotorista_mais_proximo() {
        return motorista_mais_proximo;
    }

    public void setMotorista_mais_proximo(boolean motorista_mais_proximo) {
        this.motorista_mais_proximo = motorista_mais_proximo;
    }

    public boolean isReceber_notificacoes() {
        return receber_notificacoes;
    }

    public void setReceber_notificacoes(boolean receber_notificacoes) {
        this.receber_notificacoes = receber_notificacoes;
    }

    public boolean isVibrar() {
        return vibrar;
    }

    public void setVibrar(boolean vibrar) {
        this.vibrar = vibrar;
    }

    public boolean isReceber_notificacoes_actualizacoes() {
        return receber_notificacoes_actualizacoes;
    }

    public void setReceber_notificacoes_actualizacoes(boolean receber_notificacoes_actualizacoes) {
        this.receber_notificacoes_actualizacoes = receber_notificacoes_actualizacoes;
    }

    public String getUr_web_service() {
        return ur_web_service;
    }

    public void setUr_web_service(String ur_web_service) {
        this.ur_web_service = ur_web_service;
    }

    public String getPorta_web_service() {
        return porta_web_service;
    }

    public void setPorta_web_service(String porta_web_service) {
        this.porta_web_service = porta_web_service;
    }
}
