# NewsMVVM

Android Application which uses [News API](https://newsapi.org/) to display Top Headlines and finds different Articles from all over the web. 
Also, gives users the ability to save those articles to refer to them later.

## Architecture
The app uses `MVVM(Model View View Model)` design pattern. 
MVVM provides better separation of concern, easier testing, lifecycle awareness, etc.
![](images/MVVM_Flow.png)

## Functionality
The app's functionality includes:
1. Fetch Current News data from https://newsapi.org/ & show them in `RecylerView` with smooth pagination.
2. When an item is selected from `RecyclerView` it will load the news article in a `Webview`.
3. From Details view , a news article can be added to Favorite news - which will store the News article in the Room database.
4. From Today's news section users can search for specific news topic & return the search results with pagination.
5. From favorite news section users can view all their saved news articles, they can also swipe left/right to delete the article from local database.

## Technologies Used
1.  [Android appcompat](https://developer.android.com/jetpack/androidx/releases/appcompat), [KTX](https://developer.android.com/kotlin/ktx), [Constraint layout](https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout), [Material Support](https://material.io/develop/android/docs/getting-started).
2.  [Android View Binding](https://developer.android.com/topic/libraries/view-binding)
3. [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency injection.
4. [Retrofit](https://square.github.io/retrofit/) for REST API communication
5. [Coroutine](https://developer.android.com/kotlin/coroutines) for Network call
6. [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle), [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
7. [StateFlow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/)
8. [Room](https://developer.android.com/jetpack/androidx/releases/room) for local database.
9. [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started) for supporting navigation through the app.
10. [Glide](https://github.com/bumptech/glide) for image loading.
11. [Swipe Refresh Layout](https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout) for pull-to-refresh `RecyclerView`.
12. [Mockito](https://developer.android.com/training/testing/local-tests) & [Junit](https://developer.android.com/training/testing/local-tests) for Unit testing.
13. [Robolectric](http://robolectric.org/) for Instrumentation testing.
14. [Truth](https://truth.dev/) for Assertion in testing.
15. [Espresso](https://developer.android.com/training/testing/espresso) for UI testing.

