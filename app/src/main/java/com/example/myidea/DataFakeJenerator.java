package com.example.myidea;

import java.util.ArrayList;
import java.util.List;

public class DataFakeJenerator {
    private List<User> users = new ArrayList<>();
    public List<User> getUsers (){
        User user = new User();
        user.setId(1);
        user.setName("abolfazl kargari");
        user.setBio("shine bright like a diamond");
        user.setScore("865");
        users.add(user);
        return users;
    }

}
