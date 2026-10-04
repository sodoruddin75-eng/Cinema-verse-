package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AdSettings
import com.example.data.model.Advertisement
import com.example.data.model.Favorite
import com.example.data.model.Movie
import com.example.data.model.SiteSettings
import com.example.data.model.User
import com.example.data.model.WatchHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies ORDER BY createdAt DESC")
    fun getAllMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM movies WHERE published = 1 ORDER BY createdAt DESC")
    fun getPublishedMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    fun getMovieById(id: String): Flow<Movie?>

    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    suspend fun getMovieByIdDirect(id: String): Movie?

    @Query("SELECT * FROM movies WHERE published = 1 AND featured = 1 ORDER BY createdAt DESC")
    fun getFeaturedMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM movies WHERE published = 1 ORDER BY views DESC LIMIT 10")
    fun getTrendingMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM movies WHERE published = 1 ORDER BY rating DESC LIMIT 10")
    fun getPopularMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM movies WHERE published = 1 ORDER BY year DESC, createdAt DESC LIMIT 10")
    fun getLatestMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM movies WHERE published = 1 AND genres LIKE '%' || :genre || '%'")
    fun getMoviesByGenre(genre: String): Flow<List<Movie>>

    @Query("SELECT * FROM movies WHERE published = 1 AND (title LIKE '%' || :query || '%' OR `cast` LIKE '%' || :query || '%' OR director LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%')")
    fun searchMovies(query: String): Flow<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Update
    suspend fun updateMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)

    @Query("DELETE FROM movies WHERE id = :id")
    suspend fun deleteMovieById(id: String)

    @Query("UPDATE movies SET views = views + 1 WHERE id = :id")
    suspend fun incrementMovieViews(id: String)

    @Query("SELECT COUNT(*) FROM movies")
    fun getMovieCount(): Flow<Int>

    @Query("SELECT SUM(views) FROM movies")
    fun getTotalViews(): Flow<Int?>
}

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserById(id: String): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT COUNT(*) FROM users")
    fun getUserCount(): Flow<Int>
}

@Dao
interface AdDao {
    @Query("SELECT * FROM advertisements ORDER BY startDate DESC")
    fun getAllAds(): Flow<List<Advertisement>>

    @Query("SELECT * FROM advertisements WHERE enabled = 1 AND placement = :placement")
    fun getActiveAdsForPlacement(placement: String): Flow<List<Advertisement>>

    @Query("SELECT * FROM advertisements WHERE id = :id LIMIT 1")
    fun getAdById(id: String): Flow<Advertisement?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAd(ad: Advertisement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAds(ads: List<Advertisement>)

    @Update
    suspend fun updateAd(ad: Advertisement)

    @Delete
    suspend fun deleteAd(ad: Advertisement)

    @Query("DELETE FROM advertisements WHERE id = :id")
    suspend fun deleteAdById(id: String)

    @Query("UPDATE advertisements SET impressions = impressions + 1 WHERE id = :id")
    suspend fun incrementImpressions(id: String)

    @Query("UPDATE advertisements SET clicks = clicks + 1 WHERE id = :id")
    suspend fun incrementClicks(id: String)

    @Query("SELECT SUM(impressions) FROM advertisements")
    fun getTotalImpressions(): Flow<Int?>

    @Query("SELECT SUM(clicks) FROM advertisements")
    fun getTotalClicks(): Flow<Int?>
}

@Dao
interface WatchHistoryDao {
    @Query("SELECT * FROM watch_history WHERE userId = :userId ORDER BY lastWatchedAt DESC")
    fun getWatchHistoryForUser(userId: String): Flow<List<WatchHistory>>

    @Query("SELECT * FROM watch_history WHERE userId = :userId AND movieId = :movieId LIMIT 1")
    suspend fun getHistoryItem(userId: String, movieId: String): WatchHistory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateHistory(history: WatchHistory)

    @Query("DELETE FROM watch_history WHERE id = :id")
    suspend fun deleteHistory(id: String)

    @Query("DELETE FROM watch_history WHERE userId = :userId")
    suspend fun clearHistory(userId: String)
}

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites WHERE userId = :userId ORDER BY addedAt DESC")
    fun getFavoritesForUser(userId: String): Flow<List<Favorite>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userId = :userId AND movieId = :movieId)")
    fun isFavorite(userId: String, movieId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: Favorite)

    @Query("DELETE FROM favorites WHERE userId = :userId AND movieId = :movieId")
    suspend fun removeFavorite(userId: String, movieId: String)

    @Query("SELECT COUNT(*) FROM favorites")
    fun getTotalFavoritesCount(): Flow<Int>
}

@Dao
interface SettingsDao {
    @Query("SELECT * FROM ad_settings WHERE id = 1 LIMIT 1")
    fun getAdSettings(): Flow<AdSettings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAdSettings(settings: AdSettings)

    @Query("SELECT * FROM site_settings WHERE id = 1 LIMIT 1")
    fun getSiteSettings(): Flow<SiteSettings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSiteSettings(settings: SiteSettings)
}
