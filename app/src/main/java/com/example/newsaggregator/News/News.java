package com.example.newsaggregator.News;

import java.io.Serializable;

public class News implements Comparable<News>, Serializable {
    String headline;
    String date;
    String author;
    String image;
    String description;
    String url;
    Integer count;

    public News(String headline, String date, String author, String image, String description, String url, Integer count) {
        this.headline = headline;
        this.date = date;
        this.author = author;
        this.image = image;
        this.description = description;
        this.url = url;
        this.count = count;
    }

    public String getHeadline() {
        return headline;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public Integer getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "News{" +
                "headline='" + headline + '\'' +
                ", date='" + date + '\'' +
                ", author='" + author + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", count='" + count + '\'' +
                '}';
    }

    @Override
    public int compareTo(News news) {
        return 0;
    }
}
