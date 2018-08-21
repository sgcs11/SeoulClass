package com.changhwa.logintest;

public class ReviewItem {

    float rating;
    String contents;
    String ID;

    public ReviewItem(float rating, String contents, String ID) {
        this.rating = rating;
        this.contents = contents;
        this.ID = ID;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "ReviewItem{" +
                "rating=" + rating +
                ", contents='" + contents + '\'' +
                ", ID='" + ID + '\'' +
                '}';
    }
}
