package com.example.newsaggregator.News;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsaggregator.MainActivity;
import com.example.newsaggregator.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder>{

    private ArrayList<News> newslist;
    private final MainActivity mainAct;
    private Picasso picasso;

    public NewsAdapter(ArrayList<News> newslist, MainActivity mainActivity) {
        this.newslist = newslist;
        this.mainAct = mainActivity;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_entry, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

        News news = newslist.get(position);
        picasso = Picasso.get();

        String he = news.getHeadline();     //Head line
        if (he != null) {
            holder.headline.setText(he);
            holder.headline.setOnClickListener(v -> click(news.getUrl()));
        } else {
            holder.headline.setVisibility(TextView.GONE);
        }

        String dt = news.getDate();     //date
        if (dt != "null") {
            holder.date.setText(dt);
        } else {
            holder.date.setVisibility(TextView.GONE);
        }

        String au = news.getAuthor();       //author
        if (au != "null") {
            holder.author.setText(au);
        } else {
            holder.author.setVisibility(TextView.GONE);
        }

        String de = news.getDescription();      //description
        if (de != "null") {
            holder.description.setText(de);
            holder.description.setOnClickListener(v -> click(news.getUrl()));
        } else {
            holder.description.setVisibility(TextView.GONE);
        }

        if (news.getImage() == null) {                //image
            holder.image.setImageResource(R.drawable.noimage);
        } else {
            picasso.load(news.getImage())
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.loading)
                    .into(holder.image);
            holder.image.setOnClickListener(v -> click(news.getUrl()));
        }

        holder.count.setText(news.getCount()+" of 10");     //page number
    }

    @Override
    public int getItemCount() {
        return newslist.size();
    }

    public void click(String url) {             //onclick method to open news url
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mainAct.startActivity(intent);
    }
}
