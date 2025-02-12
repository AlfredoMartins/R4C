package ao.co.r4c.model;

public class UserEarnItem {
    private int user_id;
    private String user_name;
    private Double money;
    private String data;

    public UserEarnItem(int user_id, String user_name, Double money, String data) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.money = money;
        this.data = data;
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

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
