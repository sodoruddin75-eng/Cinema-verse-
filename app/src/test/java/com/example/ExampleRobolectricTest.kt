package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AdSettings
import com.example.data.model.Advertisement
import com.example.data.model.Movie
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context verifies MovieVerse app name`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("MovieVerse", appName)
    }

    @Test
    fun `verify movie entity properties and authorized licensing`() {
        val movie = Movie(
            id = "mv-test",
            title = "Tears of Steel",
            slug = "tears-of-steel",
            description = "Futuristic Amsterdam sci-fi film",
            posterUrl = "https://example.com/poster.jpg",
            backdropUrl = "https://example.com/backdrop.jpg",
            trailerUrl = "https://example.com/trailer.mp4",
            videoUrl = "https://example.com/video.mp4",
            year = 2024,
            duration = 12,
            genres = "Sci-Fi, Action",
            rating = 8.8f,
            cast = "Derek de Lint",
            director = "Ian Hubert",
            tags = "VFX, Sci-Fi",
            featured = true,
            published = true,
            licenseInfo = "Blender Open Movie • Authorized CC-BY 3.0"
        )

        assertEquals("Tears of Steel", movie.title)
        assertTrue(movie.featured)
        assertTrue(movie.published)
        assertTrue(movie.licenseInfo.contains("Authorized"))
    }

    @Test
    fun `verify advertisement settings defaults`() {
        val settings = AdSettings(
            id = 1,
            bannerAdsEnabled = true,
            smartLinksEnabled = true,
            homeBannerEnabled = true,
            detailsBannerEnabled = true,
            watchPageBannerEnabled = true,
            mobileAdsEnabled = true,
            desktopAdsEnabled = true
        )

        assertTrue(settings.bannerAdsEnabled)
        assertTrue(settings.smartLinksEnabled)

        val disabledSettings = settings.copy(bannerAdsEnabled = false)
        assertFalse(disabledSettings.bannerAdsEnabled)
    }

    @Test
    fun `verify advertisement model validation`() {
        val ad = Advertisement(
            id = "ad-1",
            name = "Cinema Pass",
            type = "BANNER",
            placement = "HOME_MIDDLE",
            imageUrl = "https://example.com/banner.jpg",
            destinationUrl = "https://movieverse.stream/pass",
            deviceTarget = "ALL",
            enabled = true,
            buttonText = "Explore"
        )

        assertEquals("BANNER", ad.type)
        assertEquals("HOME_MIDDLE", ad.placement)
        assertTrue(ad.destinationUrl.startsWith("https://"))
    }
}
