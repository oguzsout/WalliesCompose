package com.oguzdogdu.walliescompose.features.detail.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages

@Composable
fun WalliesFavoriteButton(
    modifier: Modifier = Modifier,
    favoriteImages: FavoriteImages,
    addPhotoToFavorites: (FavoriteImages) -> Unit,
    removePhotoFromFavorites: (FavoriteImages) -> Unit
) {
    var isFavorite by rememberSaveable(favoriteImages) { mutableStateOf(favoriteImages.isChecked) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val sizeScale by animateFloatAsState(if (isPressed) 0.5f else 1f, label = "")

    IconButton(onClick = {
        isFavorite = !isFavorite
        if (isFavorite) {
            addPhotoToFavorites.invoke(favoriteImages)
        } else {
            removePhotoFromFavorites.invoke(favoriteImages)
        }
    }, modifier = modifier.graphicsLayer {
        scaleX = sizeScale
        scaleY = sizeScale
    },
        interactionSource = interactionSource
    ) {
        val tintColor = if (isFavorite) Red else MaterialTheme.colorScheme.onPrimaryContainer
        Icon(
            modifier = Modifier.size(32.dp),
            painter = rememberVectorPainter(Icons.TwoTone.Favorite),
            contentDescription = null,
            tint = tintColor
        )
    }
}