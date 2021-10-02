package com.example.myapplication.model;

public class WebModel {
    private String title,url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public WebModel() {
    }

    public WebModel(String title, String url) {
        this.title = title;
        this.url = url;
    }
}
