package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Movie
import com.example.ui.theme.MovieVerseCyan

@Composable
fun AdminMovieFormDialog(
    initialMovie: Movie?,
    onDismiss: () -> Unit,
    onSave: (Movie) -> Unit
) {
    var title by remember { mutableStateOf(initialMovie?.title ?: "") }
    var slug by remember { mutableStateOf(initialMovie?.slug ?: "") }
    var description by remember { mutableStateOf(initialMovie?.description ?: "") }
    var posterUrl by remember { mutableStateOf(initialMovie?.posterUrl ?: "") }
    var backdropUrl by remember { mutableStateOf(initialMovie?.backdropUrl ?: "") }
    var trailerUrl by remember { mutableStateOf(initialMovie?.trailerUrl ?: "") }
    var videoUrl by remember { mutableStateOf(initialMovie?.videoUrl ?: "") }
    var yearText by remember { mutableStateOf(initialMovie?.year?.toString() ?: "2026") }
    var durationText by remember { mutableStateOf(initialMovie?.duration?.toString() ?: "15") }
    var genres by remember { mutableStateOf(initialMovie?.genres ?: "Sci-Fi, Action") }
    var ratingText by remember { mutableStateOf(initialMovie?.rating?.toString() ?: "8.5") }
    var qualityBadge by remember { mutableStateOf(initialMovie?.qualityBadge ?: "4K HDR") }
    var cast by remember { mutableStateOf(initialMovie?.cast ?: "") }
    var director by remember { mutableStateOf(initialMovie?.director ?: "") }
    var tags by remember { mutableStateOf(initialMovie?.tags ?: "") }
    var featured by remember { mutableStateOf(initialMovie?.featured ?: false) }
    var published by remember { mutableStateOf(initialMovie?.published ?: true) }
    var licenseInfo by remember {
        mutableStateOf(initialMovie?.licenseInfo ?: "Authorized Distribution • Creative Commons License")
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (initialMovie == null) "Add Certified Movie" else "Edit Movie Metadata",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Ensure all video streams are authorized or licensed.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (slug.isBlank() || slug == it.lowercase().replace(" ", "-")) {
                            slug = it.lowercase().replace(" ", "-")
                        }
                    },
                    label = { Text("Movie Title *") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_input_title")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = slug,
                    onValueChange = { slug = it },
                    label = { Text("URL Slug") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Synopsis / Description *") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = videoUrl,
                    onValueChange = { videoUrl = it },
                    label = { Text("Authorized Stream / Video URL (MP4/HLS) *") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_input_video_url")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = posterUrl,
                    onValueChange = { posterUrl = it },
                    label = { Text("Poster Image URL") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = backdropUrl,
                    onValueChange = { backdropUrl = it },
                    label = { Text("Backdrop Hero Image URL") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = yearText,
                        onValueChange = { yearText = it },
                        label = { Text("Year") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = durationText,
                        onValueChange = { durationText = it },
                        label = { Text("Duration (mins)") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = ratingText,
                        onValueChange = { ratingText = it },
                        label = { Text("Rating (1-10)") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = genres,
                    onValueChange = { genres = it },
                    label = { Text("Genres (comma separated)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = cast,
                    onValueChange = { cast = it },
                    label = { Text("Cast / Starring") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = director,
                    onValueChange = { director = it },
                    label = { Text("Director") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = licenseInfo,
                    onValueChange = { licenseInfo = it },
                    label = { Text("License / Rights Verification Notice") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Switches
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Feature on Premiere Hero", style = MaterialTheme.typography.bodyMedium)
                    Switch(
                        checked = featured,
                        onCheckedChange = { featured = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.Black, checkedTrackColor = MovieVerseCyan)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Publish to Catalog", style = MaterialTheme.typography.bodyMedium)
                    Switch(
                        checked = published,
                        onCheckedChange = { published = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.Black, checkedTrackColor = MovieVerseCyan)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val parsedYear = yearText.toIntOrNull() ?: 2026
                            val parsedDur = durationText.toIntOrNull() ?: 15
                            val parsedRating = ratingText.toFloatOrNull() ?: 8.0f
                            val movie = Movie(
                                id = initialMovie?.id ?: "mv-${System.currentTimeMillis() % 100000}",
                                title = title.ifBlank { "Untitled Movie" },
                                slug = slug.ifBlank { "untitled" },
                                description = description.ifBlank { "Authorized streaming content on MovieVerse." },
                                posterUrl = posterUrl.ifBlank { "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600&auto=format&fit=crop&q=80" },
                                backdropUrl = backdropUrl.ifBlank { "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop&q=80" },
                                trailerUrl = trailerUrl.ifBlank { videoUrl },
                                videoUrl = videoUrl.ifBlank { "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4" },
                                year = parsedYear,
                                duration = parsedDur,
                                genres = genres.ifBlank { "Cinema" },
                                rating = parsedRating,
                                qualityBadge = qualityBadge,
                                cast = cast.ifBlank { "Indie Ensemble" },
                                director = director.ifBlank { "Independent Director" },
                                tags = tags,
                                featured = featured,
                                published = published,
                                licenseInfo = licenseInfo
                            )
                            onSave(movie)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MovieVerseCyan,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("admin_save_movie_button")
                    ) {
                        Text("Save Movie", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
