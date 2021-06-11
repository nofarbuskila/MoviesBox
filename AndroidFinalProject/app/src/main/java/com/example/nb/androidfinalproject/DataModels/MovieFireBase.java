package com.example.nb.androidfinalproject.DataModels;

import java.util.List;

public class MovieFireBase {
    private String id;
    private List<MovieReview> reviews;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MovieReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<MovieReview> reviews) {
        this.reviews = reviews;
    }
}
