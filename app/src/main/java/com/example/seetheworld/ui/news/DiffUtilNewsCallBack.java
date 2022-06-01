package com.example.seetheworld.ui.news;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.seetheworld.data.PartNews;

public class DiffUtilNewsCallBack extends DiffUtil.ItemCallback<PartNews> {

    @Override
    public boolean areItemsTheSame(@NonNull PartNews oldItem, @NonNull PartNews newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull PartNews oldItem, @NonNull PartNews newItem) {
        return oldItem.getId() == newItem.getId();
    }
}
