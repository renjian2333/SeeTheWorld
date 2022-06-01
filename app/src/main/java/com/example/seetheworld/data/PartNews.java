package com.example.seetheworld.data;

import java.util.Objects;

public class PartNews {
    private int id;
    private String title;
    private String pubTime;
    private String media;
    private String catagory;
    private String content;

    @Override
    public String toString() {
        return "PartNews{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", pubTime='" + pubTime + '\'' +
                ", media='" + media + '\'' +
                ", catagory='" + catagory + '\'' +
                ", content='" + content + '\'' +
                '}';
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

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartNews partNews = (PartNews) o;
        return id == partNews.id && Objects.equals(title, partNews.title) && Objects.equals(pubTime, partNews.pubTime) && Objects.equals(media, partNews.media) && Objects.equals(catagory, partNews.catagory) && Objects.equals(content, partNews.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, pubTime, media, catagory, content);
    }
}