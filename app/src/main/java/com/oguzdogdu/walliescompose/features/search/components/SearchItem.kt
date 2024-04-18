package com.oguzdogdu.walliescompose.features.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.data.common.ImageLoadingState
import com.oguzdogdu.walliescompose.domain.model.search.SearchPhoto

@Composable
fun SearchItem(searchPhoto: SearchPhoto, onSearchPhotoClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .clickable {
                searchPhoto.id?.let {
                    onSearchPhotoClick.invoke(
                        it
                    )
                }
            }
        , contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = searchPhoto.url,
            contentDescription = searchPhoto.imageDesc,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(CircleShape.copy(all = CornerSize(16.dp)))
            , loading = {
                ImageLoadingState()
            }
        )
    }
}