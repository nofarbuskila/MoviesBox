package com.example.nb.androidfinalproject.DataBase;

import com.example.nb.androidfinalproject.DataModels.User;

public class UserData {
    private static final UserData ourInstance = new UserData();
    private User user;

    public static UserData getInstance() {
        return ourInstance;
    }

    private UserData() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
