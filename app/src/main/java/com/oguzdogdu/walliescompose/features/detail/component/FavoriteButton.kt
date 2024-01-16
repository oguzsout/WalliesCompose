package com.oguzdogdu.walliescompose.features.detail.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages
import kotlinx.coroutines.delay

@Composable
fun WalliesFavoriteButton(
    modifier: Modifier,
    favoriteImages: FavoriteImages,
    addPhotoToFavorites: (FavoriteImages) -> Unit,
    removePhotoFromFavorites: (FavoriteImages) -> Unit
) {
    var isFavorite by rememberSaveable(favoriteImages) { mutableStateOf(favoriteImages.isChecked) }

    IconButton(onClick = {
        isFavorite = !isFavorite

        if (isFavorite) {
            addPhotoToFavorites.invoke(favoriteImages)
        } else {
            removePhotoFromFavorites.invoke(favoriteImages)
        }
    }) {
        val tintColor = if (isFavorite) Red else Gray

        Icon(
            modifier = modifier.size(32.dp),
            painter = rememberVectorPainter(Icons.Default.Favorite),
            contentDescription = null,
            tint = tintColor
        )
    }
}