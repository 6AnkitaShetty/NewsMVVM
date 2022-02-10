package com.example.newsmvvm.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.newsmvvm.data.model.NewsArticle

@BindingAdapter("displayImage")
fun ImageView.setDisplayImage(item: NewsArticle) {
    Glide.with(this)
        .load(item.urlToImage)
        .into(this)
}