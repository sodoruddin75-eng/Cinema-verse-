package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey val id: String,
    val title: String,
    val slug: String,
    val description: String,
    val posterUrl: String,
    val backdropUrl: String,
    val trailerUrl: String,
    val videoUrl: String,
    val year: Int,
    val duration: Int, // minutes
    val genres: String, // comma-separated e.g. "Sci-Fi, Action"
    val rating: Float,
    val qualityBadge: String = "4K HDR",
    val cast: String,
    val director: String,
    val tags: String,
    val featured: Boolean = false,
    val published: Boolean = true,
    val views: Int = 0,
    val licenseInfo: String = "Authorized Content • Creative Commons / Open Streaming License",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val passwordHash: String = "demo123",
    val isAdmin: Boolean = false,
    val memberSince: Long = System.currentTimeMillis()
)

@Entity(tableName = "advertisements")
data class Advertisement(
    @PrimaryKey val id: String,
    val name: String,
    val type: String, // "BANNER", "SMART_LINK"
    val placement: String, // "HOME_TOP", "HOME_MIDDLE", "HOME_BOTTOM", "MOVIE_DETAILS", "WATCH_PAGE"
    val imageUrl: String,
    val destinationUrl: String,
    val deviceTarget: String = "ALL", // "ALL", "MOBILE", "DESKTOP"
    val enabled: Boolean = true,
    val startDate: Long = 0L,
    val endDate: Long = Long.MAX_VALUE,
    val impressions: Int = 0,
    val clicks: Int = 0,
    val buttonText: String = "Explore More"
)

@Entity(tableName = "ad_settings")
data class AdSettings(
    @PrimaryKey val id: Int = 1,
    val bannerAdsEnabled: Boolean = true,
    val smartLinksEnabled: Boolean = true,
    val homeBannerEnabled: Boolean = true,
    val detailsBannerEnabled: Boolean = true,
    val watchPageBannerEnabled: Boolean = true,
    val mobileAdsEnabled: Boolean = true,
    val desktopAdsEnabled: Boolean = true
)

@Entity(tableName = "watch_history")
data class WatchHistory(
    @PrimaryKey val id: String, // "${userId}_${movieId}"
    val userId: String,
    val movieId: String,
    val movieTitle: String,
    val posterUrl: String,
    val progressMs: Long,
    val durationMs: Long,
    val lastWatchedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey val id: String, // "${userId}_${movieId}"
    val userId: String,
    val movieId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "site_settings")
data class SiteSettings(
    @PrimaryKey val id: Int = 1,
    val siteName: String = "MovieVerse",
    val contactEmail: String = "admin@movieverse.stream",
    val defaultLanguage: String = "English",
    val legalNotice: String = "All streams on MovieVerse are strictly authorized, direct producer distributions, or licensed Creative Commons films. Unauthorized or copyrighted torrent/piracy links are strictly prohibited.",
    val analyticsEnabled: Boolean = true
)
