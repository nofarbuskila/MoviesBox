package com.example.nb.androidfinalproject.DataModels;

import java.util.List;

public class MovieDetails {

    private Movie movie;
    private List<MovieReview> reviews;

    public MovieDetails() {
    }

    public MovieDetails(Movie movie, List<MovieReview> reviews) {
        this.movie = movie;
        this.reviews = reviews;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public List<MovieReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<MovieReview> reviews) {
        this.reviews = reviews;
    }
}
