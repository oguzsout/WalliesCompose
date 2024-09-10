package com.oguzdogdu.walliescompose.features.detail.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.detail.DetailState
import com.oguzdogdu.walliescompose.ui.theme.regular

@Composable
fun DetailActionButtons(
    state: DetailState,
    setWallpaperButtonClick: (Boolean) -> Unit,
    shareButtonClick: () -> Unit,
    downloadButtonClick: (Boolean) -> Unit,
    onAddFavoriteClick: () -> Unit,
    onRemoveFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val openBottomSheetOfDownload by remember { mutableStateOf(false) }
    val openBottomSheetOfSetWallpaper by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier.border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(16.dp)
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            onClick = { setWallpaperButtonClick.invoke(!openBottomSheetOfSetWallpaper) }) {
            Icon(
                painter = painterResource(id = R.drawable.wallpaper),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                contentDescription = ""
            )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Text(
                text = stringResource(id = R.string.set_wallpaper_text),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontFamily = regular,
                fontSize = 14.sp
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            WalliesFavoriteButton(
                favoriteImages = state.favorites, addPhotoToFavorites = {
                    onAddFavoriteClick.invoke()
                }, removePhotoFromFavorites = {
                    onRemoveFavoriteClick.invoke()
                })
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(modifier = Modifier.size(32.dp), onClick = { shareButtonClick.invoke() }) {
                Icon(painter = rememberVectorPainter(Icons.Rounded.Share), contentDescription = "")
            }
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(modifier = Modifier.size(32.dp),
                onClick = { downloadButtonClick.invoke(!openBottomSheetOfDownload) }) {
                Icon(painter = painterResource(id = R.drawable.download), contentDescription = "")
            }
        }
    }
}