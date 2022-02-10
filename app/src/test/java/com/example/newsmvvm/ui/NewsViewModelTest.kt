package com.example.newsmvvm.ui

import MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.newsmvvm.network.api.NewsApi
import com.example.newsmvvm.network.repository.NewsRepository
import com.example.newsmvvm.state.NetworkState
import com.example.newsmvvm.utils.Constants.language
import com.example.newsmvvm.utils.NetworkHelper
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import provideFakeCoroutinesDispatcherProvider
import runBlockingTest

@ExperimentalCoroutinesApi
class NewsViewModelTest {
    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var newsRepository: NewsRepository

    private val testDispatcher = coroutineRule.testDispatcher

    private lateinit var viewModel: NewsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = NewsViewModel(
            repository = newsRepository,
            networkHelper = networkHelper,
            coroutinesDispatcherProvider = provideFakeCoroutinesDispatcherProvider(testDispatcher)
        )
    }

    @Test
    fun `when calling for results then return loading state`() {
        coroutineRule.runBlockingTest {
            whenever(networkHelper.isNetworkConnected())
                .thenReturn(true)
            whenever(newsRepository.getNews(language, 1))
                .thenReturn(NetworkState.Loading())

            //When
            viewModel.fetchNews(language)

            //Then
            assertThat(viewModel.newsResponse.value).isNotNull()
            assertThat(viewModel.newsResponse.value.data).isNull()
            assertThat(viewModel.newsResponse.value.message).isNull()
        }
    }

    @Test
    fun `when calling for results then return news results`() {
        coroutineRule.runBlockingTest {
            whenever(networkHelper.isNetworkConnected())
                .thenReturn(true)

            // Stub repository with fake favorites
            whenever(newsRepository.getNews(language, 1))
                .thenAnswer { (FakeDataUtil.getFakeNewsArticleResponse()) }

            //When
            viewModel.fetchNews(language)

            //then
            assertThat(viewModel.newsResponse.value).isNotNull()
            val articles = viewModel.newsResponse.value.data?.articles
            assertThat(articles?.isNotEmpty())
            // compare the response with fake list
            assertThat(articles).hasSize(FakeDataUtil.getFakeArticles().size)
            // compare the data and also order
            assertThat(articles).containsExactlyElementsIn(
                FakeDataUtil.getFakeArticles()
            ).inOrder()
        }
    }

    @Test
    fun `when calling for results then return error`() {
        coroutineRule.runBlockingTest {
            whenever(networkHelper.isNetworkConnected())
                .thenReturn(true)
            // Stub repository with fake favorites
            whenever(newsRepository.getNews(language, 1))
                .thenAnswer { NetworkState.Error("Error occurred", null) }

            //When
            viewModel.fetchNews(language)

            //then
            val response = viewModel.newsResponse.value
            assertThat(response.message).isNotNull()
            assertThat(response.message).isEqualTo("Error occurred")
        }
    }

    @Test
    fun `when calling for search then return search result`() {
        coroutineRule.runBlockingTest {
            whenever(networkHelper.isNetworkConnected())
                .thenReturn(true)

            // Stub repository with fake favorites
            whenever(newsRepository.searchNews(language, 1))
                .thenAnswer { (FakeDataUtil.getFakeNewsArticleResponse()) }

            //When
            viewModel.searchNews(language)

            //then
            assertThat(viewModel.searchNewsResponse.value).isNotNull()
            val articles = viewModel.searchNewsResponse.value.data?.articles
            assertThat(articles?.isNotEmpty())
            // compare the response with fake list
            assertThat(articles).hasSize(FakeDataUtil.getFakeArticles().size)
            // compare the data and also order
            assertThat(articles).containsExactlyElementsIn(
                FakeDataUtil.getFakeArticles()
            ).inOrder()
        }
    }

    @After
    fun release() {
        Mockito.framework().clearInlineMocks()
    }
}