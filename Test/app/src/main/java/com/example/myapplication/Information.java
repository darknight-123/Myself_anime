package com.example.myapplication;

public class Information {
    private String id,title,link,ep,image,watch;

    public Information(String id, String title, String link, String ep, String image, String watch) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.ep = ep;
        this.image = image;
        this.watch = watch;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getEp() {
        return ep;
    }

    public void setEp(String ep) {
        this.ep = ep;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWatch() {
        return watch;
    }

    public void setWatch(String watch) {
        this.watch = watch;
    }

    @Override
    public String toString() {
        return "Information{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", ep='" + ep + '\'' +
                ", image='" + image + '\'' +
                ", watch='" + watch + '\'' +
                '}';
    }
}
