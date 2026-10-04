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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdSettings
import com.example.data.model.Advertisement
import com.example.data.model.Movie
import com.example.data.model.SiteSettings
import com.example.ui.theme.MovieVerseCyan
import com.example.ui.theme.MovieVerseGold
import com.example.ui.viewmodel.MovieVerseViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: MovieVerseViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Analytics", "Movies", "Ads & Links", "Ad Settings", "Site Settings")

    // Stats
    val totalMovies by viewModel.totalMoviesCount.collectAsState()
    val totalViews by viewModel.totalMovieViews.collectAsState()
    val totalUsers by viewModel.totalUsersCount.collectAsState()
    val totalFavorites by viewModel.totalFavoritesCount.collectAsState()
    val totalAdImpressions by viewModel.totalAdImpressions.collectAsState()
    val totalAdClicks by viewModel.totalAdClicks.collectAsState()

    val allMoviesAdmin by viewModel.allMoviesAdmin.collectAsState()
    val allAdsAdmin by viewModel.allAdsAdmin.collectAsState()
    val adSettings by viewModel.adSettings.collectAsState()
    val siteSettings by viewModel.siteSettings.collectAsState()

    var showMovieDialog by remember { mutableStateOf(false) }
    var editingMovie by remember { mutableStateOf<Movie?>(null) }

    var showAdDialog by remember { mutableStateOf(false) }
    var editingAd by remember { mutableStateOf<Advertisement?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Admin Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateBack() },
                modifier = Modifier.testTag("admin_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = "Admin Studio",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Operations, Content & Monetization",
                    fontSize = 11.sp,
                    color = MovieVerseCyan
                )
            }
        }

        // Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MovieVerseCyan,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = MovieVerseCyan
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier.testTag("admin_tab_$index")
                )
            }
        }

        // Tab Content
        Box(modifier = Modifier.fillMaxSize()) {
            when (selectedTab) {
                0 -> {
                    // Analytics Tab
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Text(
                                text = "Platform Performance & Reach",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        // Metric Cards Grid
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                AdminMetricCard(
                                    title = "Total Movies",
                                    value = "$totalMovies",
                                    icon = Icons.Default.Movie,
                                    modifier = Modifier.weight(1f)
                                )
                                AdminMetricCard(
                                    title = "Total Views",
                                    value = "$totalViews",
                                    icon = Icons.Default.Visibility,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                AdminMetricCard(
                                    title = "Registered Users",
                                    value = "$totalUsers",
                                    icon = Icons.Default.People,
                                    modifier = Modifier.weight(1f)
                                )
                                AdminMetricCard(
                                    title = "Saved Favorites",
                                    value = "$totalFavorites",
                                    icon = Icons.Default.Star,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                AdminMetricCard(
                                    title = "Ad Impressions",
                                    value = "$totalAdImpressions",
                                    icon = Icons.Default.Campaign,
                                    modifier = Modifier.weight(1f)
                                )
                                AdminMetricCard(
                                    title = "Ad & Link Clicks",
                                    value = "$totalAdClicks",
                                    icon = Icons.Default.AdsClick,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Top Stream Ranking",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        items(allMoviesAdmin.sortedByDescending { it.views }.take(5)) { movie ->
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = movie.title,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "${movie.genres} • ${movie.year}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "${movie.views} views",
                                            fontWeight = FontWeight.Bold,
                                            color = MovieVerseCyan,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = "★ ${movie.rating}",
                                            fontSize = 11.sp,
                                            color = MovieVerseGold
                                        )
                                    }
                                }
                            }
                        }

                        item { Spacer(modifier = Modifier.height(40.dp)) }
                    }
                }

                1 -> {
                    // Movies Management Tab
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Catalog Inventory (${allMoviesAdmin.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Button(
                                    onClick = {
                                        editingMovie = null
                                        showMovieDialog = true
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MovieVerseCyan,
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("admin_add_movie_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Add Movie", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        items(allMoviesAdmin, key = { it.id }) { movie ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = movie.title,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "${movie.genres} • ${movie.year} • ${movie.views} views",
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        Row {
                                            IconButton(
                                                onClick = {
                                                    editingMovie = movie
                                                    showMovieDialog = true
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "Edit",
                                                    tint = MovieVerseCyan,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }

                                            IconButton(
                                                onClick = { viewModel.deleteMovie(movie) }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Delete",
                                                    tint = MaterialTheme.colorScheme.error,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            // Featured toggle
                                            Button(
                                                onClick = { viewModel.toggleMovieFeatured(movie) },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (movie.featured) MovieVerseCyan else MaterialTheme.colorScheme.surface
                                                ),
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = if (movie.featured) "Featured ★" else "Set Featured",
                                                    fontSize = 10.sp,
                                                    color = if (movie.featured) Color.Black else MaterialTheme.colorScheme.onSurface
                                                )
                                            }

                                            // Published toggle
                                            Button(
                                                onClick = { viewModel.toggleMoviePublished(movie) },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (movie.published) Color(0xFF10B981) else MaterialTheme.colorScheme.surface
                                                ),
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = if (movie.published) "Published" else "Draft",
                                                    fontSize = 10.sp,
                                                    color = if (movie.published) Color.White else MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }

                                        Text(
                                            text = movie.qualityBadge,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        item { Spacer(modifier = Modifier.height(40.dp)) }
                    }
                }

                2 -> {
                    // Ads & Smart Links Tab
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Campaigns & Smart Links (${allAdsAdmin.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Button(
                                    onClick = {
                                        editingAd = null
                                        showAdDialog = true
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MovieVerseCyan,
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("admin_add_ad_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("New Campaign", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        items(allAdsAdmin, key = { it.id }) { ad ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(if (ad.type == "BANNER") MovieVerseCyan else MovieVerseGold)
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = ad.type,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.Black
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = ad.placement,
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = ad.name,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "Target: ${ad.destinationUrl}",
                                                fontSize = 10.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        Row {
                                            IconButton(onClick = {
                                                editingAd = ad
                                                showAdDialog = true
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "Edit",
                                                    tint = MovieVerseCyan,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }

                                            IconButton(onClick = { viewModel.deleteAd(ad) }) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Delete",
                                                    tint = MaterialTheme.colorScheme.error,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Impressions: ${ad.impressions} • Clicks: ${ad.clicks}",
                                            fontSize = 11.sp,
                                            color = MovieVerseCyan,
                                            fontWeight = FontWeight.Medium
                                        )

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = if (ad.enabled) "Active" else "Paused",
                                                fontSize = 11.sp,
                                                color = if (ad.enabled) Color(0xFF10B981) else Color.Gray
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Switch(
                                                checked = ad.enabled,
                                                onCheckedChange = { viewModel.toggleAdEnabled(ad) },
                                                colors = SwitchDefaults.colors(
                                                    checkedThumbColor = Color.Black,
                                                    checkedTrackColor = MovieVerseCyan
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        item { Spacer(modifier = Modifier.height(40.dp)) }
                    }
                }

                3 -> {
                    // Ad Configuration & Frequency Settings Tab
                    val currentSettings = adSettings ?: AdSettings()
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Monetization & Placement Visibility",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Instantly toggle ad visibility across entire platform without code modifications.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        item {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    SettingSwitchRow(
                                        title = "Banner Advertisements Master",
                                        subtitle = "Globally enable or disable all display banners",
                                        checked = currentSettings.bannerAdsEnabled,
                                        onCheckedChange = {
                                            viewModel.saveAdSettings(currentSettings.copy(bannerAdsEnabled = it))
                                        }
                                    )

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                                    SettingSwitchRow(
                                        title = "Smart Links Master",
                                        subtitle = "Globally enable or disable promotional smart links",
                                        checked = currentSettings.smartLinksEnabled,
                                        onCheckedChange = {
                                            viewModel.saveAdSettings(currentSettings.copy(smartLinksEnabled = it))
                                        }
                                    )

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                                    SettingSwitchRow(
                                        title = "Home Screen Banners",
                                        subtitle = "Show sponsored banner in middle of homepage",
                                        checked = currentSettings.homeBannerEnabled,
                                        onCheckedChange = {
                                            viewModel.saveAdSettings(currentSettings.copy(homeBannerEnabled = it))
                                        }
                                    )

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                                    SettingSwitchRow(
                                        title = "Movie Details Banner",
                                        subtitle = "Show partner banner below movie story metadata",
                                        checked = currentSettings.detailsBannerEnabled,
                                        onCheckedChange = {
                                            viewModel.saveAdSettings(currentSettings.copy(detailsBannerEnabled = it))
                                        }
                                    )

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                                    SettingSwitchRow(
                                        title = "Watch Page Banner",
                                        subtitle = "Show banner underneath video stream player",
                                        checked = currentSettings.watchPageBannerEnabled,
                                        onCheckedChange = {
                                            viewModel.saveAdSettings(currentSettings.copy(watchPageBannerEnabled = it))
                                        }
                                    )

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                                    SettingSwitchRow(
                                        title = "Mobile Device Targeting",
                                        subtitle = "Render advertisements on smartphones & tablets",
                                        checked = currentSettings.mobileAdsEnabled,
                                        onCheckedChange = {
                                            viewModel.saveAdSettings(currentSettings.copy(mobileAdsEnabled = it))
                                        }
                                    )
                                }
                            }
                        }

                        item { Spacer(modifier = Modifier.height(40.dp)) }
                    }
                }

                4 -> {
                    // Site Settings Tab
                    val currentSite = siteSettings ?: SiteSettings()
                    var siteName by remember(currentSite) { mutableStateOf(currentSite.siteName) }
                    var contactEmail by remember(currentSite) { mutableStateOf(currentSite.contactEmail) }
                    var legalNotice by remember(currentSite) { mutableStateOf(currentSite.legalNotice) }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Text(
                                text = "Platform & Compliance Configuration",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        item {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    OutlinedTextField(
                                        value = siteName,
                                        onValueChange = { siteName = it },
                                        label = { Text("Streaming Platform Name") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    OutlinedTextField(
                                        value = contactEmail,
                                        onValueChange = { contactEmail = it },
                                        label = { Text("Legal / Support Contact Email") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    OutlinedTextField(
                                        value = legalNotice,
                                        onValueChange = { legalNotice = it },
                                        label = { Text("Legal Content Notice & Anti-Piracy Policy") },
                                        maxLines = 4,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Button(
                                        onClick = {
                                            viewModel.saveSiteSettings(
                                                currentSite.copy(
                                                    siteName = siteName,
                                                    contactEmail = contactEmail,
                                                    legalNotice = legalNotice
                                                )
                                            )
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MovieVerseCyan,
                                            contentColor = Color.Black
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Save Site Settings", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        item { Spacer(modifier = Modifier.height(40.dp)) }
                    }
                }
            }
        }
    }

    if (showMovieDialog) {
        AdminMovieFormDialog(
            initialMovie = editingMovie,
            onDismiss = {
                showMovieDialog = false
                editingMovie = null
            },
            onSave = { movie ->
                viewModel.saveMovie(movie)
                showMovieDialog = false
                editingMovie = null
            }
        )
    }

    if (showAdDialog) {
        AdminAdFormDialog(
            initialAd = editingAd,
            onDismiss = {
                showAdDialog = false
                editingAd = null
            },
            onSave = { ad ->
                viewModel.saveAd(ad)
                showAdDialog = false
                editingAd = null
            }
        )
    }
}

@Composable
private fun AdminMetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = MovieVerseCyan
            )
        )
    }
}
