package com.sc.jn.seoulclass.Util;

public class FavoriteItem {
    String classID;
    String classTitle;

    public FavoriteItem(String classID, String classTitle) {
        this.classID = classID;
        this.classTitle = classTitle;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

}
