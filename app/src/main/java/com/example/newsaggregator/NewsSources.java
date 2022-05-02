package com.example.newsaggregator;

import android.text.SpannableString;

import java.io.Serializable;

public class NewsSources implements Serializable {

    String id;
    String name;
    String category;
    transient SpannableString cn;

    public NewsSources(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public SpannableString getCn() {
        return cn;
    }

    public void setCn(SpannableString cn) {
        this.cn = cn;
    }

    @Override
    public String toString() {
        return "NewsSources{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
