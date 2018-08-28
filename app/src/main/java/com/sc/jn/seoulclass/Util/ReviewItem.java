package com.sc.jn.seoulclass.Util;

public class ReviewItem {

    float rating;
    String contents;
    String ID;
    String ClassID;

    public ReviewItem(float rating, String contents, String ID, String ClassID) {
        this.rating = rating;
        this.contents = contents;
        this.ID = ID;
        this.ClassID = ClassID;
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

    public String getClassID() {
        return ClassID;
    }

    public void setClassID(String classID) {
        ClassID = classID;
    }

    @Override
    public String toString() {
        return "ReviewItem{" +
                "rating=" + rating +
                ", contents='" + contents + '\'' +
                ", ID='" + ID + '\'' +
                ", ClassID='" + ClassID + '\'' +
                '}';
    }
}
