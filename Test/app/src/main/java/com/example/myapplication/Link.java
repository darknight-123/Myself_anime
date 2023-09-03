package com.example.myapplication;

import java.io.Serializable;

public class Link implements Serializable {
    String ep;
    String url;

    public Link(String ep, String url) {
        this.ep = ep;
        this.url = url;
    }

    public String getEp() {
        return ep;
    }

    public void setEp(String ep) {
        this.ep = ep;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Link{" +
                "ep='" + ep + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
