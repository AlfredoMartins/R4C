package ao.co.r4c.model;

import android.graphics.drawable.Drawable;

public class UserCallItem {
    private int user_id;
    private String user_name;
    private String data;
    private Drawable user_image;
    private Drawable cancelado_image;
    private Drawable atentido_image;

    public UserCallItem(int user_id, String user_name, String data, Drawable user_image, Drawable cancelado_image, Drawable atentido_image) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.data = data;
        this.user_image = user_image;
        this.cancelado_image = cancelado_image;
        this.atentido_image = atentido_image;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Drawable getUser_image() {
        return user_image;
    }

    public void setUser_image(Drawable user_image) {
        this.user_image = user_image;
    }

    public Drawable getCancelado_image() {
        return cancelado_image;
    }

    public void setCancelado_image(Drawable cancelado_image) {
        this.cancelado_image = cancelado_image;
    }

    public Drawable getAtentido_image() {
        return atentido_image;
    }

    public void setAtentido_image(Drawable atentido_image) {
        this.atentido_image = atentido_image;
    }
}
