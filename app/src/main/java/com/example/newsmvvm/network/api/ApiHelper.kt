package com.example.newsmvvm.network.api

import com.example.newsmvvm.data.model.NewsResponse
import retrofit2.Response

interface ApiHelper {
    suspend fun searchNews(query: String, pageNumber: Int): Response<NewsResponse>
    suspend fun getNews(language: String, pageNumber: Int): Response<NewsResponse>
}