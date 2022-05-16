package ao.co.r4c.model;

public class NotificationItem {
    private int notification_id;
    private String title;
    private String text;
    private String date;


    public NotificationItem(int notification_id, String title, String text, String date) {
        this.notification_id = notification_id;
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
