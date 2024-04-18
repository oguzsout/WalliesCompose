package com.oguzdogdu.walliescompose.features.favorites

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutCirc
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.EaseOutElastic
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.EaseOutQuint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.data.common.ImageLoadingState
import com.oguzdogdu.walliescompose.features.favorites.event.FavoriteScreenEvent
import com.oguzdogdu.walliescompose.features.favorites.state.FavoriteScreenState
import com.oguzdogdu.walliescompose.features.home.LoadingState
import com.oguzdogdu.walliescompose.ui.theme.regular
import com.oguzdogdu.walliescompose.util.shareExternal
import kotlinx.coroutines.launch


@Composable
fun FavoritesScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel(),
    onFavoriteClick: (String?) -> Unit
) {
    val state by viewModel.favoritesState.collectAsStateWithLifecycle()
    val cardFlippableState by viewModel.flipImageCard.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    var shareEnabled by remember { mutableStateOf(false) }
    val launcherOfShare = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        shareEnabled = true
    }
    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.handleUIEvent(FavoriteScreenEvent.GetFavorites)
    }
    Scaffold(modifier = modifier
        .fillMaxSize()
        .background(Color.Magenta), topBar = {
        Row(
            modifier = modifier
                .wrapContentWidth()
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.favorites_title),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }) {
        Column(
            modifier = modifier.padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (state.favorites?.isNotEmpty() == true) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    state = rememberLazyGridState(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(state.favorites.orEmpty(),key = { _, item -> item.url.hashCode() }) {_, item ->
                        FavoritesImageView(modifier,imageUrl = item.url, imageId = item.id, onFavoriteClick = {id ->
                            onFavoriteClick.invoke(id)
                            coroutineScope.launch {
                                viewModel.resetToFlipCardState(false)
                            }
                        }, imageName = item.name, onFavoriteLongClick = {cardState ->
                            viewModel.handleUIEvent(FavoriteScreenEvent.FlipToImage(cardState))
                        }, onDeleteFavoriteClick = {id ->
                            viewModel.handleUIEvent(FavoriteScreenEvent.DeleteFromFavorites(id))
                        }, onShareFavoriteClick = { url ->
                            launcherOfShare.launch(url.shareExternal())
                        }, flipCard = cardFlippableState)
                    }
                    items(state.favorites.orEmpty()) {favorites ->

                    }
                }
            }
            if (state.favorites.isNullOrEmpty()) {
                EmptyView(modifier = modifier, state = true)
            }
        }
    }
}


@Composable
fun EmptyView(modifier: Modifier, state: Boolean) {
    AnimatedVisibility(
        visible = state,
        enter = expandHorizontally { 20 },
        exit = shrinkHorizontally(
            animationSpec = tween(),
            shrinkTowards = Alignment.End,
        )
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                painter = painterResource(id = R.drawable.no_picture), contentDescription = ""
            )
            Spacer(modifier = modifier.size(8.dp))
            Text(
                text = stringResource(id = R.string.no_picture_text),
                fontSize = 16.sp,
                fontFamily = regular
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FavoritesImageView(
    modifier: Modifier,
    imageUrl: String?,
    imageId: String?,
    imageName: String?,
    onFavoriteClick: (String?) -> Unit,
    onFavoriteLongClick: (Boolean) -> Unit,
    onDeleteFavoriteClick: (String) -> Unit,
    onShareFavoriteClick: (String) -> Unit,
    flipCard: Boolean
) {
    var flippableCard by remember {
        mutableStateOf(flipCard)
    }
    val favoriteId by remember {
        mutableStateOf(imageId)
    }
    val favoriteUrl by remember {
        mutableStateOf(imageUrl)
    }

    val rotation by animateFloatAsState(
        targetValue = if (flippableCard) 180f else 0f,
        animationSpec = tween(2000, easing = EaseOutElastic), label = ""
    )

    val animateFront by animateFloatAsState(
        targetValue = if (!flippableCard) 1f else 0f,
        animationSpec = tween(1500), label = ""
    )

    val animateBack by animateFloatAsState(
        targetValue = if (flippableCard) 1f else 0f,
        animationSpec = tween(1500), label = ""
    )

    Card(shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            }
            .combinedClickable(onClick = {
                if (!flippableCard) {
                    onFavoriteClick.invoke(favoriteId)
                }
            }, onLongClick = {
                flippableCard = !flipCard
                onFavoriteLongClick.invoke(flippableCard)
            })
    ) {
        when (flippableCard) {
            true -> {
                Box(
                    modifier = modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(modifier = modifier.wrapContentSize()) {
                        IconButton(
                            onClick = {
                                onDeleteFavoriteClick.invoke(favoriteId.orEmpty())
                            },
                            modifier = modifier
                                .wrapContentSize()
                                .graphicsLayer {
                                    alpha = animateBack
                                }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.delete),
                                contentDescription = "",
                                tint = Color.Red,
                                modifier = modifier
                                    .wrapContentSize()
                                    .graphicsLayer {
                                        alpha = animateBack
                                    }
                            )
                        }
                        IconButton(
                            onClick = { onShareFavoriteClick.invoke(favoriteUrl.orEmpty()) },
                            modifier = modifier
                                .wrapContentSize()
                                .graphicsLayer {
                                    alpha = animateBack
                                }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.share),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = modifier
                                    .wrapContentSize()
                                    .graphicsLayer {
                                        alpha = animateBack
                                    }
                            )
                        }
                    }
                }
            }

            false -> {
                Box(
                    modifier = modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SubcomposeAsyncImage(
                        model = imageUrl,
                        contentDescription = imageName,
                        contentScale = ContentScale.FillBounds,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(CircleShape.copy(all = CornerSize(16.dp)))
                            .graphicsLayer {
                                alpha = animateFront
                            },
                        loading = { ImageLoadingState() },
                    )
                }
            }
        }
    }
}