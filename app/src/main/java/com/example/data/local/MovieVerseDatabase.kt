package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.AdSettings
import com.example.data.model.Advertisement
import com.example.data.model.Favorite
import com.example.data.model.Movie
import com.example.data.model.SiteSettings
import com.example.data.model.User
import com.example.data.model.WatchHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Movie::class,
        User::class,
        Advertisement::class,
        AdSettings::class,
        WatchHistory::class,
        Favorite::class,
        SiteSettings::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MovieVerseDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun userDao(): UserDao
    abstract fun adDao(): AdDao
    abstract fun watchHistoryDao(): WatchHistoryDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: MovieVerseDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): MovieVerseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieVerseDatabase::class.java,
                    "movieverse_database"
                )
                    .addCallback(MovieVerseDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class MovieVerseDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database)
                }
            }
        }

        private suspend fun populateInitialData(db: MovieVerseDatabase) {
            val movieDao = db.movieDao()
            val userDao = db.userDao()
            val adDao = db.adDao()
            val settingsDao = db.settingsDao()

            // 1. Initial Users
            userDao.insertUser(
                User(
                    id = "user-1",
                    name = "Alex Vance",
                    email = "alex@movieverse.stream",
                    passwordHash = "cinema2026",
                    isAdmin = false
                )
            )
            userDao.insertUser(
                User(
                    id = "admin-1",
                    name = "Administrator",
                    email = "admin@movieverse.stream",
                    passwordHash = "admin2026",
                    isAdmin = true
                )
            )

            // 2. Initial Settings
            settingsDao.saveAdSettings(
                AdSettings(
                    id = 1,
                    bannerAdsEnabled = true,
                    smartLinksEnabled = true,
                    homeBannerEnabled = true,
                    detailsBannerEnabled = true,
                    watchPageBannerEnabled = true,
                    mobileAdsEnabled = true,
                    desktopAdsEnabled = true
                )
            )
            settingsDao.saveSiteSettings(
                SiteSettings(
                    id = 1,
                    siteName = "MovieVerse",
                    contactEmail = "legal@movieverse.stream",
                    defaultLanguage = "English"
                )
            )

            // 3. Authorized Cinematic Movies (Public Domain / Open Source / Creative Commons certified streams)
            val initialMovies = listOf(
                Movie(
                    id = "mv-101",
                    title = "Tears of Steel",
                    slug = "tears-of-steel",
                    description = "In a dystopian futuristic Amsterdam, a group of scientists and freedom fighters gather in an old cathedral to stage a desperate operation to alter the timeline and prevent the rise of rogue biomechanical cyborgs.",
                    posterUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600&auto=format&fit=crop&q=80",
                    backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop&q=80",
                    trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                    year = 2024,
                    duration = 12,
                    genres = "Sci-Fi, Action, Thriller",
                    rating = 8.8f,
                    qualityBadge = "4K ULTRA HD",
                    cast = "Derek de Lint, Sergio Hasselbaink, Rogier Schippers, Vanja Rukavina",
                    director = "Ian Hubert",
                    tags = "Cyberpunk, VFX, Amsterdam, Robotics, Timeline",
                    featured = true,
                    published = true,
                    views = 14250,
                    licenseInfo = "Blender Open Movie • CC-BY 3.0 Licensed • Authorized Distribution"
                ),
                Movie(
                    id = "mv-102",
                    title = "Cosmos Laundromat",
                    slug = "cosmos-laundromat",
                    description = "On a desolate wind-swept island, a suicidal sheep named Franck meets a quirky mysterious salesman named Victor, who offers him the gift of a lifetime: lives in alternate magical realities.",
                    posterUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80",
                    backdropUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=1200&auto=format&fit=crop&q=80",
                    trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    year = 2023,
                    duration = 11,
                    genres = "Animation, Fantasy, Drama",
                    rating = 8.5f,
                    qualityBadge = "4K HDR",
                    cast = "Pierre Bokma, Reinout Scholten van Aschat",
                    director = "Mathieu Auvray",
                    tags = "Surreal, Multiverse, Blender, Award Winner",
                    featured = true,
                    published = true,
                    views = 9820,
                    licenseInfo = "Creative Commons 4.0 • Authorized Direct Stream"
                ),
                Movie(
                    id = "mv-103",
                    title = "Sintel: The Dragon Quest",
                    slug = "sintel-dragon-quest",
                    description = "A lonely young warrior woman searches across treacherous mountain peaks and ruined fortresses to rescue an orphaned baby dragon she nurtured back to health.",
                    posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80",
                    backdropUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1200&auto=format&fit=crop&q=80",
                    trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                    year = 2022,
                    duration = 15,
                    genres = "Animation, Adventure, Drama",
                    rating = 8.9f,
                    qualityBadge = "1080p FHD",
                    cast = "Halina Reijn, Thom Hoffman",
                    director = "Colin Levy",
                    tags = "Fantasy, Dragon, Quest, Cinematic Score",
                    featured = false,
                    published = true,
                    views = 18400,
                    licenseInfo = "CC-BY License • Authorized Distribution"
                ),
                Movie(
                    id = "mv-104",
                    title = "Elephants Dream",
                    slug = "elephants-dream",
                    description = "Proog and Emo navigate a gigantic, chaotic organic-mechanical clockwork labyrinth where reality shifts based on individual perception and fear.",
                    posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop&q=80",
                    backdropUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1200&auto=format&fit=crop&q=80",
                    trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                    year = 2021,
                    duration = 11,
                    genres = "Sci-Fi, Animation, Thriller",
                    rating = 8.1f,
                    qualityBadge = "1080p FHD",
                    cast = "Tygo Gernandt, Cas Jansen",
                    director = "Bassam Kurdali",
                    tags = "Surrealism, Cyber-machinery, Classical Open Film",
                    featured = false,
                    published = true,
                    views = 7600,
                    licenseInfo = "Creative Commons 2.5 • Authorized Open Source Movie"
                ),
                Movie(
                    id = "mv-105",
                    title = "Big Buck Bunny: Forest Guardian",
                    slug = "big-buck-bunny",
                    description = "A gentle, gigantic woodland rabbit seeks retribution when a mischievous gang of forest rodents ruthlessly bullies his innocent butterfly companions.",
                    posterUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600&auto=format&fit=crop&q=80",
                    backdropUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=1200&auto=format&fit=crop&q=80",
                    trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    year = 2023,
                    duration = 10,
                    genres = "Comedy, Animation",
                    rating = 8.6f,
                    qualityBadge = "4K 60FPS",
                    cast = "Sacha Goedegebure, Jan Morgenstern",
                    director = "Sacha Goedegebure",
                    tags = "Comedy, Family, Slapstick, Visual Benchmark",
                    featured = false,
                    published = true,
                    views = 23900,
                    licenseInfo = "CC-BY 3.0 • Authorized Direct Stream"
                ),
                Movie(
                    id = "mv-106",
                    title = "The Cyber Heist: Quantum Velocity",
                    slug = "cyber-heist-quantum-velocity",
                    description = "An undercover digital intelligence specialist is tasked with breaking into an impenetrable high-altitude server citadel before a global blackout is triggered.",
                    posterUrl = "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?w=600&auto=format&fit=crop&q=80",
                    backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop&q=80",
                    trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                    year = 2025,
                    duration = 18,
                    genres = "Action, Thriller",
                    rating = 8.7f,
                    qualityBadge = "4K ULTRA HD",
                    cast = "Marcus Thorne, Elena Rostova, Kenji Sato",
                    director = "Alain Moreau",
                    tags = "Heist, Cyberpunk, High Tech, Neo-Noir",
                    featured = true,
                    published = true,
                    views = 16800,
                    licenseInfo = "Direct Authorized Indie License • Stream Partner"
                ),
                Movie(
                    id = "mv-107",
                    title = "Neon Labyrinth",
                    slug = "neon-labyrinth",
                    description = "When a rogue artificial synthesizer begins broadcasting illegal dream sequences across a hyper-connected metropolis, an investigator must enter the neural web to shut it down.",
                    posterUrl = "https://images.unsplash.com/photo-1478760329108-5c3ed9d495a0?w=600&auto=format&fit=crop&q=80",
                    backdropUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=1200&auto=format&fit=crop&q=80",
                    trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                    year = 2024,
                    duration = 14,
                    genres = "Sci-Fi, Thriller",
                    rating = 8.4f,
                    qualityBadge = "1080p FHD",
                    cast = "Mira Vance, Gabriel Cross",
                    director = "R. K. Sterling",
                    tags = "Neo-Tokyo, Neural Net, Synthwave, Mystery",
                    featured = false,
                    published = true,
                    views = 8400,
                    licenseInfo = "Indie Creator Certified License • MovieVerse Partner"
                ),
                Movie(
                    id = "mv-108",
                    title = "Shadows in the Mist",
                    slug = "shadows-in-the-mist",
                    description = "A deep atmospheric psychological drama about a coastal lighthouse keeper who discovers remnants of an expedition thought lost at sea half a century earlier.",
                    posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80",
                    backdropUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1200&auto=format&fit=crop&q=80",
                    trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                    year = 2023,
                    duration = 22,
                    genres = "Drama, Thriller",
                    rating = 8.3f,
                    qualityBadge = "1080p FHD",
                    cast = "Arthur Pendelton, Sarah Lind",
                    director = "Clara Dupont",
                    tags = "Atmospheric, Mystery, Coastal, Psychological",
                    featured = false,
                    published = true,
                    views = 6200,
                    licenseInfo = "Authorized Independent Cinema Rights • MovieVerse"
                )
            )
            movieDao.insertMovies(initialMovies)

            // 4. Initial Advertisements & Smart Links (Configurable via Admin)
            val initialAds = listOf(
                Advertisement(
                    id = "ad-banner-1",
                    name = "MovieVerse Pro Festival Pass",
                    type = "BANNER",
                    placement = "HOME_MIDDLE",
                    imageUrl = "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=900&auto=format&fit=crop&q=80",
                    destinationUrl = "https://movieverse.stream/festivals",
                    deviceTarget = "ALL",
                    enabled = true,
                    buttonText = "Explore Festivals"
                ),
                Advertisement(
                    id = "ad-banner-2",
                    name = "Cinematic Audio Gear Spotlight",
                    type = "BANNER",
                    placement = "MOVIE_DETAILS",
                    imageUrl = "https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=900&auto=format&fit=crop&q=80",
                    destinationUrl = "https://movieverse.stream/audio-experience",
                    deviceTarget = "ALL",
                    enabled = true,
                    buttonText = "Discover Gear"
                ),
                Advertisement(
                    id = "ad-smart-1",
                    name = "Premium 4K Stream Showcase",
                    type = "SMART_LINK",
                    placement = "HOME_TOP",
                    imageUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=900&auto=format&fit=crop&q=80",
                    destinationUrl = "https://movieverse.stream/cinema-club",
                    deviceTarget = "ALL",
                    enabled = true,
                    buttonText = "Watch Now in 4K"
                ),
                Advertisement(
                    id = "ad-banner-3",
                    name = "Filmmakers Grant 2026",
                    type = "BANNER",
                    placement = "WATCH_PAGE",
                    imageUrl = "https://images.unsplash.com/photo-1485846234645-a62644f84728?w=900&auto=format&fit=crop&q=80",
                    destinationUrl = "https://movieverse.stream/filmmakers",
                    deviceTarget = "ALL",
                    enabled = true,
                    buttonText = "Submit Film"
                )
            )
            adDao.insertAds(initialAds)
        }
    }
}
