package com.example.noka.a42translate;

/**
 * Created by noka on 5/3/17.
 */

public class UserModel {
    public UserModel(String user_id, String name, String usertype) {
        this.setUser_id(user_id);
        this.setName(name);
        this.setUsertype(usertype);
    }
    String user_id;
    String name;
    String usertype;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }


}
