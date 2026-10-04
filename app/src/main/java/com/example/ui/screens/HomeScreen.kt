package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BannerAdView
import com.example.ui.components.HeroSection
import com.example.ui.components.MovieCarousel
import com.example.ui.components.SmartLinkButton
import com.example.ui.theme.MovieVerseCyan
import com.example.ui.theme.MovieVerseGold
import com.example.ui.viewmodel.MovieVerseViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun HomeScreen(
    viewModel: MovieVerseViewModel,
    modifier: Modifier = Modifier
) {
    val featuredMovies by viewModel.featuredMovies.collectAsState()
    val trendingMovies by viewModel.trendingMovies.collectAsState()
    val latestMovies by viewModel.latestMovies.collectAsState()
    val popularMovies by viewModel.popularMovies.collectAsState()
    val allMovies by viewModel.publishedMovies.collectAsState()
    val userFavorites by viewModel.getFavoritesList().collectAsState()
    val favoritesSet = remember(userFavorites) { userFavorites.map { it.id }.toSet() }

    val homeMiddleBanner by viewModel.homeMiddleBanner.collectAsState()
    val homeTopSmartLink by viewModel.homeTopSmartLink.collectAsState()

    val actionMovies = remember(allMovies) {
        allMovies.filter { it.genres.contains("Action", ignoreCase = true) }
    }
    val sciFiMovies = remember(allMovies) {
        allMovies.filter { it.genres.contains("Sci-Fi", ignoreCase = true) }
    }
    val animationMovies = remember(allMovies) {
        allMovies.filter { it.genres.contains("Animation", ignoreCase = true) }
    }
    val dramaMovies = remember(allMovies) {
        allMovies.filter { it.genres.contains("Drama", ignoreCase = true) }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_lazy_column")
    ) {
        // Hero Section
        item {
            HeroSection(
                movie = featuredMovies.firstOrNull() ?: allMovies.firstOrNull(),
                onWatchClick = { movieId ->
                    viewModel.navigateTo(Screen.Watch(movieId))
                },
                onDetailsClick = { movieId ->
                    viewModel.navigateTo(Screen.MovieDetails(movieId))
                }
            )
        }

        // Configurable Smart Link Promotional CTA
        if (homeTopSmartLink != null) {
            item {
                SmartLinkButton(
                    ad = homeTopSmartLink,
                    onAdImpression = { viewModel.recordAdImpression(it) },
                    onAdClick = { viewModel.recordAdClick(it) }
                )
            }
        }

        // Trending Movies Carousel
        item {
            MovieCarousel(
                title = "Trending Now",
                icon = Icons.Default.LocalFireDepartment,
                movies = trendingMovies,
                favoritesSet = favoritesSet,
                onMovieClick = { movieId -> viewModel.navigateTo(Screen.MovieDetails(movieId)) },
                onToggleFavorite = { movieId, isFav -> viewModel.toggleFavorite(movieId, isFav) },
                onSeeAllClick = {
                    viewModel.selectedSortOrder.value = "Popular"
                    viewModel.navigateTo(Screen.Explore)
                }
            )
        }

        // Latest Releases
        item {
            MovieCarousel(
                title = "Latest Releases",
                icon = Icons.Default.Bolt,
                movies = latestMovies,
                favoritesSet = favoritesSet,
                onMovieClick = { movieId -> viewModel.navigateTo(Screen.MovieDetails(movieId)) },
                onToggleFavorite = { movieId, isFav -> viewModel.toggleFavorite(movieId, isFav) },
                onSeeAllClick = {
                    viewModel.selectedSortOrder.value = "Latest"
                    viewModel.navigateTo(Screen.Explore)
                }
            )
        }

        // Configurable Middle Banner Ad
        if (homeMiddleBanner != null) {
            item {
                BannerAdView(
                    ad = homeMiddleBanner,
                    onAdImpression = { viewModel.recordAdImpression(it) },
                    onAdClick = { viewModel.recordAdClick(it) }
                )
            }
        }

        // Popular Across MovieVerse
        item {
            MovieCarousel(
                title = "Top Rated Masterpieces",
                icon = Icons.Default.Star,
                movies = popularMovies,
                favoritesSet = favoritesSet,
                onMovieClick = { movieId -> viewModel.navigateTo(Screen.MovieDetails(movieId)) },
                onToggleFavorite = { movieId, isFav -> viewModel.toggleFavorite(movieId, isFav) },
                onSeeAllClick = {
                    viewModel.selectedSortOrder.value = "Rating"
                    viewModel.navigateTo(Screen.Explore)
                }
            )
        }

        // Sci-Fi & Cyberpunk
        item {
            MovieCarousel(
                title = "Sci-Fi & Cyberpunk Odysseys",
                icon = Icons.Default.AutoAwesome,
                movies = sciFiMovies,
                favoritesSet = favoritesSet,
                onMovieClick = { movieId -> viewModel.navigateTo(Screen.MovieDetails(movieId)) },
                onToggleFavorite = { movieId, isFav -> viewModel.toggleFavorite(movieId, isFav) },
                onSeeAllClick = {
                    viewModel.selectedGenreFilter.value = "Sci-Fi"
                    viewModel.navigateTo(Screen.Explore)
                }
            )
        }

        // Action & High Octane
        item {
            MovieCarousel(
                title = "Action & High Octane",
                icon = Icons.Default.Bolt,
                movies = actionMovies,
                favoritesSet = favoritesSet,
                onMovieClick = { movieId -> viewModel.navigateTo(Screen.MovieDetails(movieId)) },
                onToggleFavorite = { movieId, isFav -> viewModel.toggleFavorite(movieId, isFav) },
                onSeeAllClick = {
                    viewModel.selectedGenreFilter.value = "Action"
                    viewModel.navigateTo(Screen.Explore)
                }
            )
        }

        // Animation & Visual Wonders
        item {
            MovieCarousel(
                title = "Animation & Visual Wonders",
                icon = Icons.Default.Movie,
                movies = animationMovies,
                favoritesSet = favoritesSet,
                onMovieClick = { movieId -> viewModel.navigateTo(Screen.MovieDetails(movieId)) },
                onToggleFavorite = { movieId, isFav -> viewModel.toggleFavorite(movieId, isFav) },
                onSeeAllClick = {
                    viewModel.selectedGenreFilter.value = "Animation"
                    viewModel.navigateTo(Screen.Explore)
                }
            )
        }

        // Drama & Stories
        item {
            MovieCarousel(
                title = "Atmospheric Drama",
                icon = Icons.Default.Movie,
                movies = dramaMovies,
                favoritesSet = favoritesSet,
                onMovieClick = { movieId -> viewModel.navigateTo(Screen.MovieDetails(movieId)) },
                onToggleFavorite = { movieId, isFav -> viewModel.toggleFavorite(movieId, isFav) },
                onSeeAllClick = {
                    viewModel.selectedGenreFilter.value = "Drama"
                    viewModel.navigateTo(Screen.Explore)
                }
            )
        }

        // Legal Content Notice Card & Footer
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0F2942)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = MovieVerseCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "100% Authorized & Certified Content",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Every stream on MovieVerse is legally distributed under certified Open Film licenses, Creative Commons, or authorized producer agreements.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Bottom space for bottom navigation
        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
