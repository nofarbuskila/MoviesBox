package com.example.nb.androidfinalproject.DataModels;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private String firstName;
    private String lastName;
    private String profileImage;
    private String gender;
    private List<String> wishList;
    private List<String> watchList;

    public User() {
    }

    public User(String firstName, String lastName, String profileImage, String gender, List<String> wishList, List<String> watchList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
        this.gender = gender;
        this.wishList = wishList;
        this.watchList = watchList;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getWishList() {
        return wishList;
    }

    public void setWishList(List<String> wishList) {
        this.wishList = wishList;
    }

    public List<String> getWatchList() {
        return watchList;
    }

    public void setWatchList(List<String> watchList) {
        this.watchList = watchList;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", gender='" + gender + '\'' +
                ", wishList=" + wishList +
                ", watchList=" + watchList +
                '}';
    }
}
