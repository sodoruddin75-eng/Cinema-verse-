package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BannerAdView
import com.example.ui.components.MovieCarousel
import com.example.ui.components.QualityBadge
import com.example.ui.components.RatingBadge
import com.example.ui.components.VideoPlayerView
import com.example.ui.theme.MovieVerseCyan
import com.example.ui.viewmodel.MovieVerseViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun WatchScreen(
    movieId: String,
    viewModel: MovieVerseViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val allMovies by viewModel.publishedMovies.collectAsState()
    val movie = remember(allMovies, movieId) { allMovies.firstOrNull { it.id == movieId } }
    val isFavorite by viewModel.isMovieFavorite(movieId).collectAsState()
    val watchHistory by viewModel.getWatchHistoryList().collectAsState()
    val historyItem = remember(watchHistory, movieId) { watchHistory.firstOrNull { it.movieId == movieId } }
    val initialProgressMs = remember(historyItem) { historyItem?.progressMs ?: 0L }

    val watchPageBanner by viewModel.watchPageBanner.collectAsState()
    val userFavorites by viewModel.getFavoritesList().collectAsState()
    val favoritesSet = remember(userFavorites) { userFavorites.map { it.id }.toSet() }

    var isFullscreen by remember { mutableStateOf(false) }

    LaunchedEffect(movieId) {
        viewModel.recordMovieView(movieId)
    }

    if (movie == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Movie stream unavailable", color = MaterialTheme.colorScheme.onBackground)
        }
        return
    }

    val relatedMovies = remember(allMovies, movie) {
        val primaryGenre = movie.genres.split(",").firstOrNull()?.trim() ?: ""
        allMovies.filter { it.id != movie.id && it.genres.contains(primaryGenre, ignoreCase = true) }
    }

    if (isFullscreen) {
        // Fullscreen Mode
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            VideoPlayerView(
                videoUrl = movie.videoUrl,
                title = movie.title,
                initialProgressMs = initialProgressMs,
                isFullscreen = true,
                onToggleFullscreen = { isFullscreen = false },
                onProgressUpdate = { progressMs, durationMs ->
                    viewModel.saveWatchProgress(movie.id, movie.title, movie.posterUrl, progressMs, durationMs)
                }
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("watch_screen")
    ) {
        // Top Back & Title Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier.testTag("watch_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1
                    )
                    Text(
                        text = "Authorized Stream • ${movie.qualityBadge}",
                        fontSize = 11.sp,
                        color = MovieVerseCyan
                    )
                }

                IconButton(
                    onClick = { viewModel.toggleFavorite(movie.id, isFavorite) },
                    modifier = Modifier.testTag("watch_favorite_toggle")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) MovieVerseCyan else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Responsive Video Player
        item {
            VideoPlayerView(
                videoUrl = movie.videoUrl,
                title = movie.title,
                initialProgressMs = initialProgressMs,
                isFullscreen = false,
                onToggleFullscreen = { isFullscreen = true },
                onProgressUpdate = { progressMs, durationMs ->
                    viewModel.saveWatchProgress(movie.id, movie.title, movie.posterUrl, progressMs, durationMs)
                }
            )
        }

        // Continue Watching Resume Notice (if resumed)
        if (initialProgressMs > 5000) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF0C243B))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MovieVerseCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Resumed from your previous saved watch point",
                        fontSize = 11.sp,
                        color = Color(0xFFA5F3FC)
                    )
                }
            }
        }

        // Movie Info Below Player
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    RatingBadge(rating = movie.rating)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QualityBadge(text = movie.qualityBadge)
                    Text(
                        text = "${movie.year} • ${movie.duration}m • ${movie.genres}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = movie.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Legal Streaming Notice Card
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = MovieVerseCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = movie.licenseInfo,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Watch Page Banner Ad (if enabled)
        if (watchPageBanner != null) {
            item {
                BannerAdView(
                    ad = watchPageBanner,
                    onAdImpression = { viewModel.recordAdImpression(it) },
                    onAdClick = { viewModel.recordAdClick(it) }
                )
            }
        }

        // Related Content
        if (relatedMovies.isNotEmpty()) {
            item {
                MovieCarousel(
                    title = "Up Next",
                    icon = Icons.Default.Movie,
                    movies = relatedMovies,
                    favoritesSet = favoritesSet,
                    onMovieClick = { id -> viewModel.navigateTo(Screen.Watch(id)) },
                    onToggleFavorite = { id, isFav -> viewModel.toggleFavorite(id, isFav) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
