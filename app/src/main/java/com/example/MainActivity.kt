package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.components.MobileBottomNavigation
import com.example.ui.components.MovieVerseHeader
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MovieDetailsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.WatchScreen
import com.example.ui.theme.MovieVerseTheme
import com.example.ui.viewmodel.MovieVerseViewModel
import com.example.ui.viewmodel.MovieVerseViewModelFactory
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {

    private val viewModel: MovieVerseViewModel by viewModels {
        val app = application as MovieVerseApplication
        MovieVerseViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()
            val currentScreen by viewModel.currentScreen.collectAsState()
            val currentUser by viewModel.currentUser.collectAsState()

            MovieVerseTheme(darkTheme = isDarkTheme) {
                val showHeader = currentScreen is Screen.Home ||
                        currentScreen is Screen.Explore ||
                        currentScreen is Screen.Favorites ||
                        currentScreen is Screen.Profile

                val showBottomNav = currentScreen !is Screen.Watch &&
                        currentScreen !is Screen.Admin &&
                        currentScreen !is Screen.MovieDetails

                Scaffold(
                    topBar = {
                        if (showHeader) {
                            MovieVerseHeader(
                                currentUser = currentUser,
                                isDarkTheme = isDarkTheme,
                                onLogoClick = { viewModel.navigateTo(Screen.Home) },
                                onSearchClick = { viewModel.navigateTo(Screen.Search) },
                                onToggleTheme = { viewModel.toggleTheme() },
                                onProfileClick = { viewModel.navigateTo(Screen.Profile) },
                                onAdminClick = { viewModel.navigateTo(Screen.Admin) }
                            )
                        }
                    },
                    bottomBar = {
                        if (showBottomNav) {
                            MobileBottomNavigation(
                                currentScreen = currentScreen,
                                onNavigate = { screen -> viewModel.navigateTo(screen) }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(innerPadding)
                    ) {
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "ScreenTransition"
                        ) { screen ->
                            when (screen) {
                                is Screen.Home -> HomeScreen(viewModel = viewModel)
                                is Screen.Explore -> ExploreScreen(viewModel = viewModel)
                                is Screen.MovieDetails -> MovieDetailsScreen(
                                    movieId = screen.movieId,
                                    viewModel = viewModel
                                )
                                is Screen.Watch -> WatchScreen(
                                    movieId = screen.movieId,
                                    viewModel = viewModel
                                )
                                is Screen.Search -> SearchScreen(viewModel = viewModel)
                                is Screen.Favorites -> FavoritesScreen(viewModel = viewModel)
                                is Screen.Profile -> ProfileScreen(viewModel = viewModel)
                                is Screen.Admin -> AdminDashboardScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
