package com.oguzdogdu.walliescompose.features.favorites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.data.common.LoadingState
import com.oguzdogdu.walliescompose.features.favorites.event.FavoriteScreenEvent
import com.oguzdogdu.walliescompose.features.favorites.state.FavoriteScreenState
import com.oguzdogdu.walliescompose.ui.theme.regular


@Composable
fun FavoritesScreenRoute(
    modifier: Modifier = Modifier, viewModel: FavoritesViewModel = hiltViewModel()
) {
    val state by viewModel.favoritesState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
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
            if (state.loading) {
                LoadingState()
            }
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
                    items(state.favorites.orEmpty()) {favorites ->
                        FavoritesImageView(modifier,imageUrl = favorites.url)
                    }
                }
            }
            if (state.favorites.isNullOrEmpty()) {
                EmptyView(modifier = modifier, state = state)
            }
        }
    }
}


@Composable
fun EmptyView(modifier: Modifier, state: FavoriteScreenState) {
    AnimatedVisibility(
        visible = state.favorites.isNullOrEmpty(),
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
            Text(
                text = stringResource(id = R.string.no_picture_text),
                fontSize = 16.sp,
                fontFamily = regular
            )
        }
    }
}

@Composable
private fun FavoritesImageView(modifier: Modifier,imageUrl: String?) {
    Box(
        modifier = Modifier.wrapContentSize()
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(CircleShape.copy(all = CornerSize(16.dp))),
            loading = { LoadingState() },
        )
    }
}