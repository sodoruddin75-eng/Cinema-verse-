package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Advertisement
import com.example.ui.theme.MovieVerseCyan

@Composable
fun AdminAdFormDialog(
    initialAd: Advertisement?,
    onDismiss: () -> Unit,
    onSave: (Advertisement) -> Unit
) {
    var name by remember { mutableStateOf(initialAd?.name ?: "") }
    var type by remember { mutableStateOf(initialAd?.type ?: "BANNER") }
    var placement by remember { mutableStateOf(initialAd?.placement ?: "HOME_MIDDLE") }
    var imageUrl by remember { mutableStateOf(initialAd?.imageUrl ?: "") }
    var destinationUrl by remember { mutableStateOf(initialAd?.destinationUrl ?: "https://") }
    var deviceTarget by remember { mutableStateOf(initialAd?.deviceTarget ?: "ALL") }
    var buttonText by remember { mutableStateOf(initialAd?.buttonText ?: "Explore More") }
    var enabled by remember { mutableStateOf(initialAd?.enabled ?: true) }

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
                    text = if (initialAd == null) "Create Advertisement" else "Edit Advertisement",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Configure transparent, user-friendly promotional campaigns.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Campaign / Ad Name *") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_ad_name")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Type (BANNER or SMART_LINK)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = placement,
                    onValueChange = { placement = it },
                    label = { Text("Placement (HOME_TOP, HOME_MIDDLE, MOVIE_DETAILS, WATCH_PAGE)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("Banner Image URL") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = destinationUrl,
                    onValueChange = { destinationUrl = it },
                    label = { Text("Destination URL *") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_ad_destination")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = buttonText,
                    onValueChange = { buttonText = it },
                    label = { Text("Call to Action Button Label") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Enable Campaign", style = MaterialTheme.typography.bodyMedium)
                    Switch(
                        checked = enabled,
                        onCheckedChange = { enabled = it },
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
                            val ad = Advertisement(
                                id = initialAd?.id ?: "ad-${System.currentTimeMillis() % 100000}",
                                name = name.ifBlank { "Promotional Campaign" },
                                type = type.uppercase().trim(),
                                placement = placement.uppercase().trim(),
                                imageUrl = imageUrl,
                                destinationUrl = destinationUrl,
                                deviceTarget = deviceTarget,
                                enabled = enabled,
                                buttonText = buttonText.ifBlank { "Learn More" }
                            )
                            onSave(ad)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MovieVerseCyan,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("admin_save_ad_button")
                    ) {
                        Text("Save Ad", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
