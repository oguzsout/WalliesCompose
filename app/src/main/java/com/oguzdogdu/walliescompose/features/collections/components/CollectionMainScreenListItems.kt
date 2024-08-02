package com.oguzdogdu.walliescompose.features.collections.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.data.common.ImageLoadingState
import com.oguzdogdu.walliescompose.domain.model.collections.WallpaperCollections
import com.oguzdogdu.walliescompose.features.collections.CollectionState
import com.oguzdogdu.walliescompose.features.collections.ListType
import com.oguzdogdu.walliescompose.ui.theme.medium

@Composable
fun CollectionItem(
    collectionState: CollectionState,
    collections: WallpaperCollections,
    onCollectionItemClick: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = collectionState.collectionsListType, transitionSpec = {
            when (targetState) {
                ListType.VERTICAL -> slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(500),
                ) togetherWith fadeOut(
                    animationSpec = tween(500)
                )

                ListType.GRID -> slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500),
                ) togetherWith fadeOut(
                    animationSpec = tween(500)
                )
            }
        }, label = ""
    ) { listType ->
        Box(modifier = modifier
            .wrapContentSize()
            .clickable {
                onCollectionItemClick.invoke(
                    collections.id.orEmpty(), collections.title.orEmpty()
                )
            }
            .padding(4.dp), contentAlignment = Alignment.Center) {
            when (listType) {
                ListType.VERTICAL -> VerticalCollectionItem(collections = collections)
                ListType.GRID -> GridCollectionItem(collections = collections)
            }
        }
    }
}

@Composable
fun GridCollectionItem(
    collections: WallpaperCollections,
    modifier: Modifier = Modifier
) {
    Box(
        modifier =
        modifier
            .wrapContentSize()
            .animateContentSize(),
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = collections.photo,
            contentDescription = collections.title,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(16.dp))
                .drawWithContent {
                    drawContent()
                    drawRect(
                        color = Color.Black.copy(alpha = 0.5f),
                        size = size,
                    )
                }, loading = {
                ImageLoadingState()
            }
        )
        if (collections.title != null) {
            Text(
                text = collections.title,
                textAlign = TextAlign.Center,
                fontFamily = medium,
                fontSize = 16.sp,
                color = Color.White,
            )
        }
    }
}

@Composable
fun VerticalCollectionItem(
    collections: WallpaperCollections,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            AsyncImage(
                model = collections.profileImage,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = collections.name.orEmpty(),
                fontSize = 16.sp,
                fontFamily = medium,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
        Box {
            SubcomposeAsyncImage(
                model = collections.photo,
                contentDescription = collections.title,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            color = Color.Black.copy(alpha = 0.3f),
                            size = size,
                        )
                    }, loading = {
                    ImageLoadingState()
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.BottomStart),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                if (collections.title != null) {
                    Text(
                        text = collections.title,
                        textAlign = TextAlign.Center,
                        fontFamily = medium,
                        fontSize = 16.sp,
                        color = Color.White,
                    )
                }
                if (collections.totalPhotos != null) {
                    Text(
                        text = "${collections.totalPhotos} Photos",
                        textAlign = TextAlign.Center,
                        fontFamily = medium,
                        fontSize = 16.sp,
                        color = Color.White,
                    )
                }
            }
        }
    }
}