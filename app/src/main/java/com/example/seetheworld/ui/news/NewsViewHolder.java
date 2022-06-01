package com.example.seetheworld.ui.news;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seetheworld.data.PartNews;
import com.example.seetheworld.databinding.NewsItemBinding;

public class NewsViewHolder extends RecyclerView.ViewHolder{
    NewsItemBinding newsItemBinding;

    public NewsViewHolder(@NonNull NewsItemBinding itemView) {
        super(itemView.getRoot());
        this.newsItemBinding = itemView;
    }

    public void bindData(PartNews item) {
        newsItemBinding.setNewsItem(item);
    }
}

