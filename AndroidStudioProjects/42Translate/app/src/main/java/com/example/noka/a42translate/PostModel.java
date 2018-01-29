package com.example.noka.a42translate;

/**
 * Created by Beni on 4/3/2017.
 */

public class PostModel {

    public PostModel( String id_forum,String item,
                      String submitted_by,String date_submitted){
        this.setItem(item);
        this.setSubmitted_by(submitted_by);
        this.setDate_submitted(date_submitted);
        this.setId_forum(id_forum);
       // this.setComments(comment);



    }

    String item;
    String submitted_by;
    String comments;
    String id_forum;
    String date_submitted;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getSubmitted_by() {
        return submitted_by;
    }

    public void setSubmitted_by(String submitted_by) {
        this.submitted_by = submitted_by;
    }

    public String getId_forum() {
        return id_forum;
    }

    public void setId_forum(String id_forum) {
        this.id_forum = id_forum;
    }

    public String getDate_submitted() {
        return date_submitted;
    }

    public void setDate_submitted(String date_submitted) {
        this.date_submitted = date_submitted;
    }



}
