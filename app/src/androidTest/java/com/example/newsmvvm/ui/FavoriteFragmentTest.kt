package com.example.newsmvvm.ui

import MainCoroutineRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.example.newsmvvm.R
import com.example.newsmvvm.launchFragmentInHiltContainer
import com.example.newsmvvm.network.api.NewsApi
import com.example.newsmvvm.network.repository.NewsRepository
import com.example.newsmvvm.ui.favorite.FavoriteFragment
import com.example.newsmvvm.ui.favorite.FavoriteFragmentDirections
import com.example.newsmvvm.utils.NetworkHelper
import com.nhaarman.mockitokotlin2.whenever
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import provideFakeCoroutinesDispatcherProvider
import runBlockingTest

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class FavoriteFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    val itemInTest = 1

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var newsRepository: NewsRepository

    private lateinit var viewModel: NewsViewModel

    private val testDispatcher = coroutineRule.testDispatcher

    @Before
    fun setUp() {
        hiltRule.inject()
        MockitoAnnotations.initMocks(this)
        viewModel = NewsViewModel(
            repository = newsRepository,
            networkHelper = networkHelper,
            coroutinesDispatcherProvider = provideFakeCoroutinesDispatcherProvider(testDispatcher)
        )
    }

    @Test
    fun test_isFavoriteItemsVisible() {
        val scenario = launchFragmentInHiltContainer<FavoriteFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.rvFavoriteNews))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_isFavoriteItemsVisibleOnFavouriteTab() {
        val scenario = launchFragmentInHiltContainer<FavoriteFragment>()
            coroutineRule.runBlockingTest {
                whenever(networkHelper.isNetworkConnected())
                    .thenReturn(false)
                whenever(newsRepository.getSavedNews())
                    .thenReturn(FakeDataUtil.getFakeNewsArticleLiveData())

                //When
                viewModel.getFavoriteNews()

                Espresso.onView(ViewMatchers.withId(R.id.rvFavoriteNews))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            }
        }

    @Test
    fun clickFavoriteItem_navigateToDetailFragment() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<FavoriteFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
        Espresso.onView(ViewMatchers.withId(R.id.rvFavoriteNews))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    itemInTest,
                    ViewActions.click()
                )
            )
        Mockito.verify(navController).navigate(
            FavoriteFragmentDirections.actionFavoriteFragmentToDetailsFragment(FakeDataUtil.getFakeArticle())
        )
    }
}