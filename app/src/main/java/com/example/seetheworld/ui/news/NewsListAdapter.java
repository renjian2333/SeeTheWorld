package com.example.seetheworld.ui.news;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.seetheworld.R;
import com.example.seetheworld.data.PartNews;
import com.example.seetheworld.databinding.NewsItemBinding;

public class NewsListAdapter extends ListAdapter<PartNews, NewsViewHolder> {

    OnNewsItemClickEvent listener;

    public interface OnNewsItemClickEvent{
        void onNewItemClick(int position);
    }

    public void setOnNewsItemClickListener(OnNewsItemClickEvent listener){
        this.listener = listener;
    }

    public NewsListAdapter(@NonNull DiffUtil.ItemCallback<PartNews> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewsItemBinding newsItemBinding = DataBindingUtil.inflate(LayoutInflater.from(
                parent.getContext()), R.layout.news_item,parent,false);
        return new NewsViewHolder(newsItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.bindData(getItem(position));

        holder.newsItemBinding.getRoot().setOnClickListener(v -> {
            if (listener != null) {
                listener.onNewItemClick(holder.getAdapterPosition());
            }
        });
    }
}
