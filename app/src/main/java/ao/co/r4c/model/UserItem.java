package ao.co.r4c.model;

public class UserItem {
    private int user_id;
    private String user_name;
    private String telefone;
    private String last_message;
    private String time_sent_last_message;
    private String foto_url;

    public UserItem(int user_id, String user_name, String telefone, String last_message, String time_sent_last_message, String foto_url) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.telefone = telefone;
        this.last_message = last_message;
        this.time_sent_last_message = time_sent_last_message;
        this.foto_url = foto_url;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getTime_sent_last_message() {
        return time_sent_last_message;
    }

    public void setTime_sent_last_message(String time_sent_last_message) {
        this.time_sent_last_message = time_sent_last_message;
    }

    public String getFoto_url() {
        return foto_url;
    }

    public void setFoto_url(String foto_url) {
        this.foto_url = foto_url;
    }
}
