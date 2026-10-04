package com.example.data.repository

import com.example.data.local.AdDao
import com.example.data.local.FavoriteDao
import com.example.data.local.MovieDao
import com.example.data.local.SettingsDao
import com.example.data.local.UserDao
import com.example.data.local.WatchHistoryDao
import com.example.data.model.AdSettings
import com.example.data.model.Advertisement
import com.example.data.model.Favorite
import com.example.data.model.Movie
import com.example.data.model.SiteSettings
import com.example.data.model.User
import com.example.data.model.WatchHistory
import kotlinx.coroutines.flow.Flow

class MovieVerseRepository(
    private val movieDao: MovieDao,
    private val userDao: UserDao,
    private val adDao: AdDao,
    private val watchHistoryDao: WatchHistoryDao,
    private val favoriteDao: FavoriteDao,
    private val settingsDao: SettingsDao
) {
    // Movies
    val allMovies: Flow<List<Movie>> = movieDao.getAllMovies()
    val publishedMovies: Flow<List<Movie>> = movieDao.getPublishedMovies()
    val featuredMovies: Flow<List<Movie>> = movieDao.getFeaturedMovies()
    val trendingMovies: Flow<List<Movie>> = movieDao.getTrendingMovies()
    val popularMovies: Flow<List<Movie>> = movieDao.getPopularMovies()
    val latestMovies: Flow<List<Movie>> = movieDao.getLatestMovies()

    fun getMovieById(id: String): Flow<Movie?> = movieDao.getMovieById(id)
    suspend fun getMovieByIdDirect(id: String): Movie? = movieDao.getMovieByIdDirect(id)

    fun getMoviesByGenre(genre: String): Flow<List<Movie>> = movieDao.getMoviesByGenre(genre)
    fun searchMovies(query: String): Flow<List<Movie>> = movieDao.searchMovies(query)

    suspend fun saveMovie(movie: Movie) = movieDao.insertMovie(movie)
    suspend fun updateMovie(movie: Movie) = movieDao.updateMovie(movie)
    suspend fun deleteMovie(movie: Movie) = movieDao.deleteMovie(movie)
    suspend fun deleteMovieById(id: String) = movieDao.deleteMovieById(id)
    suspend fun recordMovieView(id: String) = movieDao.incrementMovieViews(id)

    // Users & Auth
    val allUsers: Flow<List<User>> = userDao.getAllUsers()
    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    fun getUserById(id: String): Flow<User?> = userDao.getUserById(id)
    suspend fun registerUser(user: User) = userDao.insertUser(user)

    // Ads
    val allAds: Flow<List<Advertisement>> = adDao.getAllAds()
    fun getActiveAdsForPlacement(placement: String): Flow<List<Advertisement>> =
        adDao.getActiveAdsForPlacement(placement)

    suspend fun saveAd(ad: Advertisement) = adDao.insertAd(ad)
    suspend fun updateAd(ad: Advertisement) = adDao.updateAd(ad)
    suspend fun deleteAd(ad: Advertisement) = adDao.deleteAd(ad)
    suspend fun deleteAdById(id: String) = adDao.deleteAdById(id)
    suspend fun recordAdImpression(id: String) = adDao.incrementImpressions(id)
    suspend fun recordAdClick(id: String) = adDao.incrementClicks(id)

    // Settings
    val adSettings: Flow<AdSettings?> = settingsDao.getAdSettings()
    suspend fun saveAdSettings(settings: AdSettings) = settingsDao.saveAdSettings(settings)

    val siteSettings: Flow<SiteSettings?> = settingsDao.getSiteSettings()
    suspend fun saveSiteSettings(settings: SiteSettings) = settingsDao.saveSiteSettings(settings)

    // Watch History
    fun getWatchHistoryForUser(userId: String): Flow<List<WatchHistory>> =
        watchHistoryDao.getWatchHistoryForUser(userId)

    suspend fun saveWatchProgress(
        userId: String,
        movieId: String,
        movieTitle: String,
        posterUrl: String,
        progressMs: Long,
        durationMs: Long
    ) {
        val history = WatchHistory(
            id = "${userId}_${movieId}",
            userId = userId,
            movieId = movieId,
            movieTitle = movieTitle,
            posterUrl = posterUrl,
            progressMs = progressMs,
            durationMs = durationMs,
            lastWatchedAt = System.currentTimeMillis()
        )
        watchHistoryDao.insertOrUpdateHistory(history)
    }

    suspend fun clearWatchHistory(userId: String) = watchHistoryDao.clearHistory(userId)
    suspend fun deleteWatchHistoryItem(id: String) = watchHistoryDao.deleteHistory(id)

    // Favorites
    fun getFavoritesForUser(userId: String): Flow<List<Favorite>> =
        favoriteDao.getFavoritesForUser(userId)

    fun isFavorite(userId: String, movieId: String): Flow<Boolean> =
        favoriteDao.isFavorite(userId, movieId)

    suspend fun toggleFavorite(userId: String, movieId: String, isFav: Boolean) {
        if (isFav) {
            favoriteDao.removeFavorite(userId, movieId)
        } else {
            favoriteDao.addFavorite(
                Favorite(
                    id = "${userId}_${movieId}",
                    userId = userId,
                    movieId = movieId
                )
            )
        }
    }

    // Analytics queries
    val totalMoviesCount: Flow<Int> = movieDao.getMovieCount()
    val totalMovieViews: Flow<Int?> = movieDao.getTotalViews()
    val totalUsersCount: Flow<Int> = userDao.getUserCount()
    val totalFavoritesCount: Flow<Int> = favoriteDao.getTotalFavoritesCount()
    val totalAdImpressions: Flow<Int?> = adDao.getTotalImpressions()
    val totalAdClicks: Flow<Int?> = adDao.getTotalClicks()
}
