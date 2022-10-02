package com.example.sondenemeproje;

public class MovieModelClass {

    String title;
    String year;
    String img;
    String type;

    public MovieModelClass(String title, String year, String img,String type) {
        this.title = title;
        this.year = year;
        this.img = img;
        this.type=type;
    }

    public MovieModelClass() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
