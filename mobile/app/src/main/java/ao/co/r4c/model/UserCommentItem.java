package ao.co.r4c.model;

public class UserCommentItem {
    private int user_id;
    private String user_name;
    private String data;
    private String comment;
    private int num_stars;

    public UserCommentItem(int user_id, String user_name, String data, String comment, int num_stars) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.data = data;
        this.comment = comment;
        this.num_stars = num_stars;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getNum_stars() {
        return num_stars;
    }

    public void setNum_stars(int num_stars) {
        this.num_stars = num_stars;
    }

}
