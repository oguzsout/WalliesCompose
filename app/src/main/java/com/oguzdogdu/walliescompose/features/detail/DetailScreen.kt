package com.oguzdogdu.walliescompose.features.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages
import com.oguzdogdu.walliescompose.features.detail.component.DetailPhotoAttributesRow
import com.oguzdogdu.walliescompose.features.detail.component.DetailTagsRow
import com.oguzdogdu.walliescompose.features.detail.component.DetailTripleActionButtons
import com.oguzdogdu.walliescompose.features.detail.component.WalliesFavoriteButton
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular


@Composable
fun DetailScreenRoute(
    modifier: Modifier = Modifier,
    detailViewModel: DetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onProfileDetailClick: (String) -> Unit
) {
    val state by detailViewModel.photo.collectAsStateWithLifecycle()
    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        detailViewModel.handleScreenEvents(DetailScreenEvent.GetPhotoDetails)
        detailViewModel.handleScreenEvents(DetailScreenEvent.GetFavoriteCheckStat)
    }
    LaunchedEffect(key1 = state.favorites) {
        detailViewModel.handleScreenEvents(DetailScreenEvent.GetFavoriteCheckStat)
    }

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            IconButton(
                onClick = { onBackClick.invoke() },
                modifier = modifier.wrapContentSize().weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = modifier
                        .wrapContentSize()
                )
            }

            Text(
                modifier = modifier.weight(6f),
                text = state.detail?.desc.orEmpty(),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 12.sp,
                fontFamily = medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            IconButton(
                onClick = { onProfileDetailClick.invoke(state.detail?.id.orEmpty()) },
                modifier = modifier.wrapContentSize().weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.info),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = modifier
                        .wrapContentSize()
                )
            }
        }
    }) {
        Column(
            modifier = modifier
                .padding(it)
                .wrapContentSize(),
        ) {
            SubcomposeAsyncImage(
                model = state.detail?.highQuality,
                contentDescription = "",
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.FillBounds
            )
            PostView(modifier = modifier, state = state,
                onSetWallpaperClick = {

                }, onShareClick = {

                }, onDownloadClick = {

                },
                onAddFavoriteClick = { photo ->
                detailViewModel.handleScreenEvents(
                    DetailScreenEvent.AddFavorites(
                        FavoriteImages(id = state.detail?.id,
                            url = state.detail?.urls,
                            profileImage = state.detail?.profileimage,
                            name = state.detail?.name,
                            portfolioUrl = state.detail?.portfolio,
                            isChecked = true)
                    )
                )
            }) { photo ->
                detailViewModel.handleScreenEvents(
                    DetailScreenEvent.DeleteFavorites(
                        FavoriteImages(
                            id = state.detail?.id,
                            url = state.detail?.urls,
                            profileImage = state.detail?.profileimage,
                            name = state.detail?.name,
                            portfolioUrl = state.detail?.portfolio,
                            isChecked = false
                        )
                    )
                )
            }
        }
    }
}

@Composable
fun PostView(
    modifier: Modifier,
    state: DetailState,
    onSetWallpaperClick: () -> Unit,
    onShareClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onAddFavoriteClick: (FavoriteImages) -> Unit,
    onRemoteFavoriteClick: (FavoriteImages) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ), shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
        ), modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 16.dp)
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = state.detail?.profileimage,
                        contentDescription = "Profile Image",
                        modifier = modifier
                            .height(48.dp)
                            .width(48.dp)
                            .clip(RoundedCornerShape(64.dp))

                    )
                    Column(
                        modifier = modifier
                            .wrapContentSize()
                            .padding(start = 8.dp)
                    ) {
                        Text(
                            text = state.detail?.username.orEmpty(),
                            fontFamily = medium,
                            fontSize = 16.sp
                        )
                        Text(
                            text = state.detail?.portfolio.orEmpty(),
                            fontFamily = regular,
                            fontSize = 12.sp
                        )
                    }
                }
                WalliesFavoriteButton(modifier = modifier, favoriteImages = FavoriteImages(
                    id = state.favorites?.id,
                    url = state.favorites?.url,
                    profileImage = state.favorites?.profileImage,
                    name = state.favorites?.name,
                    portfolioUrl = state.favorites?.portfolioUrl,
                    isChecked = state.favorites?.isChecked ?: false
                ), addPhotoToFavorites = { favorite ->
                    onAddFavoriteClick.invoke(favorite)
                }, removePhotoFromFavorites = { favorite ->
                    onRemoteFavoriteClick.invoke(favorite)
                })
            }
        }
        Divider(
            modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 16.dp),
            color = Color(0xFF30363D),
            thickness = 0.8.dp
        )
        DetailPhotoAttributesRow(modifier = modifier, detail = state.detail)
        Divider(
            modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 16.dp),
            color = Color(0xFF30363D),
            thickness = 0.8.dp
        )
        DetailTagsRow(modifier = modifier, detail = state.detail)
        DetailTripleActionButtons(modifier = modifier, setWallpaperButtonClick = {
            onSetWallpaperClick.invoke()
        }, shareButtonClick = {
            onShareClick.invoke()
        }, downloadButtonClick = {
            onDownloadClick.invoke()
        })
    }
}