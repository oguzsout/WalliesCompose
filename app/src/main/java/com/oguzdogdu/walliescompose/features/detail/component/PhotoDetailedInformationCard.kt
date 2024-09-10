package com.oguzdogdu.walliescompose.features.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oguzdogdu.walliescompose.features.detail.DetailState

@Composable
fun PhotoDetailedInformationCard(
    state: DetailState,
    onSetWallpaperClick: (Boolean) -> Unit,
    onShareClick: (String) -> Unit,
    onDownloadClick: (Boolean) -> Unit,
    onAddFavoriteClick: () -> Unit,
    onRemoveFavoriteClick: () -> Unit,
    onTagClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ), shape = RoundedCornerShape(
            16.dp
        ), modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp, max = 360.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DetailActionButtons(
                state = state,
                setWallpaperButtonClick = { isOpen ->
                    onSetWallpaperClick.invoke(isOpen)
                },
                shareButtonClick = {
                    onShareClick.invoke(state.detail?.urls.orEmpty())
                },
                downloadButtonClick = {
                    onDownloadClick.invoke(it)
                },
                onAddFavoriteClick = onAddFavoriteClick,
                onRemoveFavoriteClick = onRemoveFavoriteClick
            )
            HorizontalDivider(
                modifier = Modifier.padding(16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            DetailPhotoAttributesRow(modifier = Modifier, detail = state.detail)
            HorizontalDivider(
                modifier = Modifier.padding(16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            DetailTagsRow(detail = state.detail, onTagClick = {
                onTagClick.invoke(it)
            })
        }
    }
}