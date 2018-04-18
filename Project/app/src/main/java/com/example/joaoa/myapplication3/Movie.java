package com.example.joaoa.myapplication3;

import java.util.List;

public class Movie {

    private int id;
    private String title;
    private List<Integer> category;
    private String description;
    private String poster;

    public Movie(int id, String title, List<Integer> category, String description, String poster) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.poster = poster;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getCategory() {
        return category;
    }

    public void setCategory(List<Integer> category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
