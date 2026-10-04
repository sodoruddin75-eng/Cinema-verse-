package com.example

import android.app.Application
import com.example.data.local.MovieVerseDatabase
import com.example.data.repository.MovieVerseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class MovieVerseApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val database by lazy { MovieVerseDatabase.getDatabase(this, applicationScope) }
    val repository by lazy {
        MovieVerseRepository(
            movieDao = database.movieDao(),
            userDao = database.userDao(),
            adDao = database.adDao(),
            watchHistoryDao = database.watchHistoryDao(),
            favoriteDao = database.favoriteDao(),
            settingsDao = database.settingsDao()
        )
    }
}
