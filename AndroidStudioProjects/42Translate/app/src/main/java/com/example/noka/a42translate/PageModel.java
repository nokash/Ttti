package com.example.noka.a42translate;

/**
 * Created by noka on 4/29/17.
 */

public class PageModel {

    public PageModel(String comment, String submitted_by, String submitted_date, String forum_id) {
        this.setComment(comment);
        this.setSubmitted_by(submitted_by);
        this.setSubmitted_date(submitted_date);
        this.setForum_id(forum_id);


    }
    String comment;
    String submitted_by;
    String submitted_date;

    public String getForum_id() {
        return forum_id;
    }

    public void setForum_id(String forum_id) {
        this.forum_id = forum_id;
    }

    String forum_id;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getSubmitted_by() {
        return submitted_by;
    }

    public void setSubmitted_by(String submitted_by) {
        this.submitted_by = submitted_by;
    }

    public String getSubmitted_date() {
        return submitted_date;
    }

    public void setSubmitted_date(String submitted_date) {
        this.submitted_date = submitted_date;
    }


}
