package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.AdSettings
import com.example.data.model.Advertisement
import com.example.data.model.Favorite
import com.example.data.model.Movie
import com.example.data.model.SiteSettings
import com.example.data.model.User
import com.example.data.model.WatchHistory
import com.example.data.repository.MovieVerseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    object Explore : Screen()
    data class MovieDetails(val movieId: String) : Screen()
    data class Watch(val movieId: String) : Screen()
    object Search : Screen()
    object Favorites : Screen()
    object Profile : Screen()
    object Admin : Screen()
}

class MovieVerseViewModel(
    private val repository: MovieVerseRepository
) : ViewModel() {

    // Screen navigation stack
    private val _screenStack = MutableStateFlow<List<Screen>>(listOf(Screen.Home))
    val currentScreen: StateFlow<Screen> = _screenStack.combine(MutableStateFlow(Unit)) { stack, _ ->
        stack.lastOrNull() ?: Screen.Home
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Screen.Home)

    // Dark/Light Theme state
    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    // Navigation methods
    fun navigateTo(screen: Screen) {
        val currentList = _screenStack.value.toMutableList()
        // Prevent duplicate top
        if (currentList.lastOrNull() != screen) {
            currentList.add(screen)
            _screenStack.value = currentList
        }
    }

    fun navigateBack(): Boolean {
        val currentList = _screenStack.value.toMutableList()
        if (currentList.size > 1) {
            currentList.removeAt(currentList.lastIndex)
            _screenStack.value = currentList
            return true
        }
        return false
    }

    // User session
    private val defaultUser = User(
        id = "user-1",
        name = "Alex Vance",
        email = "alex@movieverse.stream",
        passwordHash = "cinema2026",
        isAdmin = false
    )
    private val adminUser = User(
        id = "admin-1",
        name = "Administrator",
        email = "admin@movieverse.stream",
        passwordHash = "admin2026",
        isAdmin = true
    )

    private val _currentUser = MutableStateFlow<User?>(defaultUser)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun loginAsUser() {
        _currentUser.value = defaultUser
    }

    fun loginAsAdmin() {
        _currentUser.value = adminUser
    }

    fun logout() {
        _currentUser.value = null
    }

    fun loginCustom(email: String, name: String, isAdmin: Boolean) {
        val user = User(
            id = "user-${System.currentTimeMillis() % 10000}",
            name = name.ifBlank { "Movie Enthusiast" },
            email = email.ifBlank { "viewer@movieverse.stream" },
            isAdmin = isAdmin
        )
        viewModelScope.launch {
            repository.registerUser(user)
            _currentUser.value = user
        }
    }

    // Movies streams
    val publishedMovies: StateFlow<List<Movie>> = repository.publishedMovies
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allMoviesAdmin: StateFlow<List<Movie>> = repository.allMovies
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredMovies: StateFlow<List<Movie>> = repository.featuredMovies
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trendingMovies: StateFlow<List<Movie>> = repository.trendingMovies
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val popularMovies: StateFlow<List<Movie>> = repository.popularMovies
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val latestMovies: StateFlow<List<Movie>> = repository.latestMovies
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search & Filter state
    val searchQuery = MutableStateFlow("")
    val selectedGenreFilter = MutableStateFlow("All")
    val selectedSortOrder = MutableStateFlow("Latest") // "Latest", "Rating", "Popular"

    val searchResults: StateFlow<List<Movie>> = combine(
        publishedMovies,
        searchQuery,
        selectedGenreFilter,
        selectedSortOrder
    ) { movies, query, genre, sort ->
        var list = movies
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                it.cast.lowercase().contains(q) ||
                it.director.lowercase().contains(q) ||
                it.tags.lowercase().contains(q) ||
                it.genres.lowercase().contains(q)
            }
        }
        if (genre != "All") {
            list = list.filter { it.genres.contains(genre, ignoreCase = true) }
        }
        when (sort) {
            "Rating" -> list.sortedByDescending { it.rating }
            "Popular" -> list.sortedByDescending { it.views }
            else -> list.sortedByDescending { it.year }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Ads and Settings
    val allAdsAdmin: StateFlow<List<Advertisement>> = repository.allAds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val adSettings: StateFlow<AdSettings?> = repository.adSettings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AdSettings())

    val siteSettings: StateFlow<SiteSettings?> = repository.siteSettings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SiteSettings())

    // Active Banner Ads by placement
    val homeMiddleBanner: StateFlow<Advertisement?> = repository.getActiveAdsForPlacement("HOME_MIDDLE")
        .combine(adSettings) { ads, settings ->
            if (settings?.bannerAdsEnabled == true && settings.homeBannerEnabled) {
                ads.firstOrNull { it.type == "BANNER" }
            } else null
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val homeTopSmartLink: StateFlow<Advertisement?> = repository.getActiveAdsForPlacement("HOME_TOP")
        .combine(adSettings) { ads, settings ->
            if (settings?.smartLinksEnabled == true) {
                ads.firstOrNull { it.type == "SMART_LINK" }
            } else null
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val detailsBanner: StateFlow<Advertisement?> = repository.getActiveAdsForPlacement("MOVIE_DETAILS")
        .combine(adSettings) { ads, settings ->
            if (settings?.bannerAdsEnabled == true && settings.detailsBannerEnabled) {
                ads.firstOrNull { it.type == "BANNER" }
            } else null
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val watchPageBanner: StateFlow<Advertisement?> = repository.getActiveAdsForPlacement("WATCH_PAGE")
        .combine(adSettings) { ads, settings ->
            if (settings?.bannerAdsEnabled == true && settings.watchPageBannerEnabled) {
                ads.firstOrNull { it.type == "BANNER" }
            } else null
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Favorites & History for current user
    @OptIn(ExperimentalCoroutinesApi::class)
    val userFavorites: StateFlow<List<Favorite>> = currentUser.flatMapLatest { user ->
        val uid = user?.id ?: "user-1"
        repository.getFavoritesForUser(uid)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val userFavoriteMovies: StateFlow<List<Movie>> = currentUser.flatMapLatest { user ->
        val uid = user?.id ?: "user-1"
        repository.getFavoritesForUser(uid).combine(publishedMovies) { favs, movies ->
            val favIds = favs.map { it.movieId }.toSet()
            movies.filter { it.id in favIds }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getFavoritesList(): StateFlow<List<Movie>> = userFavoriteMovies

    @OptIn(ExperimentalCoroutinesApi::class)
    val userWatchHistory: StateFlow<List<WatchHistory>> = currentUser.flatMapLatest { user ->
        val uid = user?.id ?: "user-1"
        repository.getWatchHistoryForUser(uid)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getWatchHistoryList(): StateFlow<List<WatchHistory>> = userWatchHistory

    fun isMovieFavorite(movieId: String): StateFlow<Boolean> {
        val uid = _currentUser.value?.id ?: "user-1"
        return repository.isFavorite(uid, movieId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    }

    fun toggleFavorite(movieId: String, isCurrentlyFav: Boolean) {
        val uid = _currentUser.value?.id ?: "user-1"
        viewModelScope.launch {
            repository.toggleFavorite(uid, movieId, isCurrentlyFav)
        }
    }

    fun recordMovieView(movieId: String) {
        viewModelScope.launch {
            repository.recordMovieView(movieId)
        }
    }

    fun saveWatchProgress(movieId: String, title: String, poster: String, progressMs: Long, durationMs: Long) {
        val uid = _currentUser.value?.id ?: "user-1"
        viewModelScope.launch {
            repository.saveWatchProgress(uid, movieId, title, poster, progressMs, durationMs)
        }
    }

    fun clearWatchHistory() {
        val uid = _currentUser.value?.id ?: "user-1"
        viewModelScope.launch {
            repository.clearWatchHistory(uid)
        }
    }

    // Analytics counters
    val totalMoviesCount: StateFlow<Int> = repository.totalMoviesCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalMovieViews: StateFlow<Int> = repository.totalMovieViews
        .combine(MutableStateFlow(0)) { v, _ -> v ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalUsersCount: StateFlow<Int> = repository.totalUsersCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalFavoritesCount: StateFlow<Int> = repository.totalFavoritesCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalAdImpressions: StateFlow<Int> = repository.totalAdImpressions
        .combine(MutableStateFlow(0)) { imp, _ -> imp ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalAdClicks: StateFlow<Int> = repository.totalAdClicks
        .combine(MutableStateFlow(0)) { clk, _ -> clk ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Ad interactions
    fun recordAdImpression(adId: String) {
        viewModelScope.launch {
            repository.recordAdImpression(adId)
        }
    }

    fun recordAdClick(adId: String) {
        viewModelScope.launch {
            repository.recordAdClick(adId)
        }
    }

    // Admin movie actions
    fun saveMovie(movie: Movie) {
        viewModelScope.launch {
            repository.saveMovie(movie)
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            repository.deleteMovie(movie)
        }
    }

    fun toggleMoviePublished(movie: Movie) {
        viewModelScope.launch {
            repository.updateMovie(movie.copy(published = !movie.published))
        }
    }

    fun toggleMovieFeatured(movie: Movie) {
        viewModelScope.launch {
            repository.updateMovie(movie.copy(featured = !movie.featured))
        }
    }

    // Admin ad actions
    fun saveAd(ad: Advertisement) {
        viewModelScope.launch {
            repository.saveAd(ad)
        }
    }

    fun deleteAd(ad: Advertisement) {
        viewModelScope.launch {
            repository.deleteAd(ad)
        }
    }

    fun toggleAdEnabled(ad: Advertisement) {
        viewModelScope.launch {
            repository.updateAd(ad.copy(enabled = !ad.enabled))
        }
    }

    // Settings actions
    fun saveAdSettings(settings: AdSettings) {
        viewModelScope.launch {
            repository.saveAdSettings(settings)
        }
    }

    fun saveSiteSettings(settings: SiteSettings) {
        viewModelScope.launch {
            repository.saveSiteSettings(settings)
        }
    }
}

class MovieVerseViewModelFactory(
    private val repository: MovieVerseRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieVerseViewModel::class.java)) {
            return MovieVerseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
