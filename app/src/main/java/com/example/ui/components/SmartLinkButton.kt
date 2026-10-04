package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Advertisement
import com.example.ui.theme.MovieVerseBorder
import com.example.ui.theme.MovieVerseGold

@Composable
fun SmartLinkButton(
    ad: Advertisement?,
    onAdImpression: (String) -> Unit,
    onAdClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (ad == null || !ad.enabled) return

    val context = LocalContext.current

    LaunchedEffect(ad.id) {
        onAdImpression(ad.id)
    }

    val openAd = {
        onAdClick(ad.id)
        if (ad.destinationUrl.isNotBlank() && (ad.destinationUrl.startsWith("http://") || ad.destinationUrl.startsWith("https://"))) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ad.destinationUrl))
                context.startActivity(intent)
            } catch (_: Exception) {
                // Ignore missing browser activity safely
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, MovieVerseBorder, RoundedCornerShape(10.dp))
            .background(Color(0xFF161F30))
            .clickable { openAd() }
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag("smart_link_${ad.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MovieVerseGold,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${ad.name} (Sponsored)",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ad.buttonText.ifBlank { "Explore More" },
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MovieVerseGold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                    contentDescription = null,
                    tint = MovieVerseGold,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
