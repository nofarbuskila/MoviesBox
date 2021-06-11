package com.example.nb.androidfinalproject.DataModels;

import java.util.HashMap;

public class MovieIDMap {
    private String id;
    private HashMap<String,String> idsMap;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, String> getIdsMap() {
        return idsMap;
    }

    public void setIdsMap(HashMap<String, String> idsMap) {
        this.idsMap = idsMap;
    }
}
