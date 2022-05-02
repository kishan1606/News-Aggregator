package com.example.newsaggregator.News;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsaggregator.R;

public class NewsViewHolder extends RecyclerView.ViewHolder{

    TextView headline;
    TextView date;
    TextView author;
    ImageView image;
    TextView description;
    TextView count;


    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);

        headline = itemView.findViewById(R.id.news_headline);
        date = itemView.findViewById(R.id.news_date);
        author = itemView.findViewById(R.id.news_author);
        description = itemView.findViewById(R.id.news_description);
        description.setMovementMethod(new ScrollingMovementMethod());
        image = itemView.findViewById(R.id.news_image);
        count = itemView.findViewById(R.id.news_count);
    }
}
