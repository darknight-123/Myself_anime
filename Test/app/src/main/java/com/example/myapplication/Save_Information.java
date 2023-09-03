package com.example.myapplication;

public class Save_Information {
    private  String title;
    private String ep;
    private String path;
    private String status;

    public Save_Information(String title, String ep, String path, String status) {
        this.title = title;
        this.ep = ep;
        this.path = path;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEp() {
        return ep;
    }

    public void setEp(String ep) {
        this.ep = ep;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Save_Information{" +
                "title='" + title + '\'' +
                ", ep='" + ep + '\'' +
                ", path='" + path + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
