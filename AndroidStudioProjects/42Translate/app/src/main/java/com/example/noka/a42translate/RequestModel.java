package com.example.noka.a42translate;

/**
 * Created by noka on 5/5/17.
 */

public class RequestModel {
    public RequestModel(String id_request, String item_type, String item, String language, String date, String request_by) {
        this.setId_request(id_request);
        this.setItem_type(item_type);
        this.setItem(item);
        this.setLanguage(language);
        this.setDate(date);
        this.setRequest_by(request_by);

    }
    String id_request;
    String item_type;
    String item;
    String language;
    String date;
    String request_by;

    public String getRequest_by() {
        return request_by;
    }

    public void setRequest_by(String request_by) {
        this.request_by = request_by;
    }

    public String getId_request() {
        return id_request;
    }

    public void setId_request(String id_request) {
        this.id_request = id_request;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
