package com.example.newsmvvm.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsmvvm.data.model.NewsArticle
import com.example.newsmvvm.data.model.NewsResponse
import com.example.newsmvvm.network.repository.INewsRepository
import com.example.newsmvvm.state.NetworkState

class FakeRepository: INewsRepository {

    private val observableNewsArticle = MutableLiveData<List<NewsArticle>>()

    override suspend fun getNews(
        language: String,
        pageNumber: Int
    ): NetworkState<NewsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun searchNews(
        searchQuery: String,
        pageNumber: Int
    ): NetworkState<NewsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun saveNews(news: NewsArticle): Long {
        TODO("Not yet implemented")
    }

    override fun getSavedNews(): LiveData<List<NewsArticle>> {
        return FakeDataUtil.getFakeNewsArticleLiveData()
    }

    override suspend fun deleteNews(news: NewsArticle) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllNews() {
        TODO("Not yet implemented")
    }
}