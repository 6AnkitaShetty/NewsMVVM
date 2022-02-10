package com.example.newsmvvm.network.repository

import androidx.lifecycle.LiveData
import com.example.newsmvvm.data.model.NewsArticle
import com.example.newsmvvm.data.model.NewsResponse
import com.example.newsmvvm.state.NetworkState

interface INewsRepository {
    suspend fun getNews(language: String, pageNumber: Int): NetworkState<NewsResponse>

    suspend fun searchNews(searchQuery: String, pageNumber: Int): NetworkState<NewsResponse>

    suspend fun saveNews(news: NewsArticle): Long

    fun getSavedNews(): LiveData<List<NewsArticle>>

    suspend fun deleteNews(news: NewsArticle)

    suspend fun deleteAllNews()
}