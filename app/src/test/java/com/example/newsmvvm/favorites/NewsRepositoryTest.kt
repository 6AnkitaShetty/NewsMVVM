package com.example.newsmvvm.favorites

import MainCoroutineRule
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.newsmvvm.data.local.NewsDao
import com.example.newsmvvm.data.local.NewsDatabase
import com.example.newsmvvm.data.model.NewsArticle
import com.example.newsmvvm.network.api.ApiHelper
import com.example.newsmvvm.network.api.ApiHelperImpl
import com.example.newsmvvm.network.api.NewsApi
import com.example.newsmvvm.network.repository.NewsRepository
import com.example.newsmvvm.state.NetworkState
import com.example.newsmvvm.util.MockWebServerBaseTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import runBlockingTest
import java.net.HttpURLConnection

@ExperimentalCoroutinesApi
@Config(sdk = [30])
@RunWith(
    RobolectricTestRunner::class
)
class NewsRepositoryTest : MockWebServerBaseTest() {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    private lateinit var newsRepository: NewsRepository
    private lateinit var newsDatabase: NewsDatabase
    private lateinit var newsDao: NewsDao
    private lateinit var apiHelper: ApiHelper
    private lateinit var newsApi: NewsApi
    private lateinit var apiHelperImpl: ApiHelperImpl
    private lateinit var responseObserver: Observer<List<NewsArticle>>
    private val language = ArgumentMatchers.anyString()
    private val page = ArgumentMatchers.anyInt()

    override fun isMockServerEnabled(): Boolean = true

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        newsDatabase = Room.inMemoryDatabaseBuilder(
            context, NewsDatabase::class.java
        ).allowMainThreadQueries().build()
        newsDao = newsDatabase.getNewsDao()
        newsApi = provideTestApiService()
        apiHelperImpl = ApiHelperImpl(newsApi)
        apiHelper = apiHelperImpl
        newsRepository = NewsRepository(apiHelper, newsDao)
        responseObserver = Observer { }
    }

    @Test
    fun testFavoriteNewsInsertionInDb() {
        coroutineRule.runBlockingTest {
            newsRepository.saveNews(FakeDataUtil.getFakeArticle())
            val favNews = newsRepository.getSavedNews()
            favNews.observeForever(responseObserver)
            assertThat(favNews.value?.isNotEmpty()).isTrue()
        }
    }

    @Test
    fun testRemoveFromDb() {
        coroutineRule.runBlockingTest {
            newsRepository.deleteAllNews()
            val favNews = newsRepository.getSavedNews()
            favNews.observeForever(responseObserver)
            assertThat(favNews.value?.isEmpty()).isTrue()
        }
    }

    @Test
    fun testFavoriteNews() {
        coroutineRule.runBlockingTest {
            val fakeArticle = FakeDataUtil.getFakeArticle()
            newsRepository.saveNews(fakeArticle)
            val favoriteArticle = newsRepository.getSavedNews()
            favoriteArticle.observeForever(responseObserver)
            assertThat(favoriteArticle.value?.get(0)?.id == fakeArticle.id).isTrue()
        }
    }

    @Test
    fun `given response ok when fetching results then return a list with elements`() {
        runBlocking {
            mockHttpResponse("news_response.json", HttpURLConnection.HTTP_OK)
            val apiResponse = newsRepository.getNews(language, page)

            assertThat(apiResponse).isNotNull()
            assertThat(apiResponse.data?.articles).hasSize(10)
        }
    }

    @Test
    fun `given response ok when fetching empty results then return an empty list`() {
        runBlocking {
            mockHttpResponse("news_response_empty_list.json", HttpURLConnection.HTTP_OK)
            val apiResponse = newsRepository.getNews(language, page)
            assertThat(apiResponse).isNotNull()
            assertThat(apiResponse.data?.articles).hasSize(0)
        }
    }

    @Test
    fun `given response failure when fetching results then return exception`() {
        runBlocking {
            mockHttpResponse(502)
            val apiResponse = newsRepository.getNews(language, page)

            Assert.assertNotNull(apiResponse)
            val expectedValue = NetworkState.Error("An error occurred", null)
            assertThat(expectedValue.message).isEqualTo(apiResponse.message)
        }
    }

    @After
    fun release() {
        newsDatabase.close()
        newsRepository.getSavedNews().removeObserver(responseObserver)
    }
}