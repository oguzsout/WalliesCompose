package com.oguzdogdu.walliescompose.features.popular

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.data.common.ImageLoadingState
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.ui.theme.medium
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PopularScreenRoute(
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    viewModel: PopularViewModel = hiltViewModel(),
    onPopularClick: (String?) -> Unit,
    onBackClick: () -> Unit
) {

    val popularListState: LazyPagingItems<PopularImage> =
        viewModel.getPopular.collectAsLazyPagingItems()
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE, lifecycleOwner = lifecycleOwner) {
        viewModel.handleUIEvent(PopularScreenEvent.FetchPopularData)

    }
    Scaffold(modifier = modifier
        .fillMaxSize()
        .background(Color.Magenta), topBar = {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onBackClick.invoke() },
                modifier = modifier
                    .wrapContentSize()
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
                modifier = modifier,
                text = stringResource(id = R.string.popular_title),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 16.sp,
                fontFamily = medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start
            )
        }
    }) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
        ) {
            PopularDetailListScreen(
                animatedVisibilityScope = animatedVisibilityScope,
                popularLazyPagingItems = popularListState,
                onPopularClick = { id ->
                    onPopularClick.invoke(id)
                })
        }
    }
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.PopularDetailListScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    popularLazyPagingItems: LazyPagingItems<PopularImage>,
    onPopularClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyGridState()
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }
    var isClick by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier
                    .fillMaxSize(),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    count = popularLazyPagingItems.itemCount,
                    key = popularLazyPagingItems.itemKey { item: PopularImage -> item.url.hashCode() },
                    contentType = popularLazyPagingItems.itemContentType { "Popular" }) { index: Int ->
                    val popular: PopularImage? = popularLazyPagingItems[index]
                    if (popular != null) {
                        PopularListItem(
                            animatedVisibilityScope = animatedVisibilityScope,
                            popularImage = popular,
                            onPopularClick = { id ->
                                isClick = isClick.not()
                                onPopularClick.invoke(id)
                            })
                    }
                }
            }

            if (showButton and isClick.not()) {
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(20.dp)
                        .renderInSharedTransitionScopeOverlay(
                            zIndexInOverlay = 1f,
                        ),
                    containerColor = colorResource(id = R.color.lush_green),
                    onClick = {
                        scope.launch {
                            listState.scrollToItem(0)
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_upward),
                        contentDescription = "Scroll the list"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PopularListItem(
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    popularImage: PopularImage,
    onPopularClick: (String) -> Unit
) {
    SubcomposeAsyncImage(
        modifier = modifier
            .sharedBounds(
                sharedContentState = rememberSharedContentState(key = "popularImage-${popularImage.id}"),
                animatedVisibilityScope = animatedVisibilityScope,
                enter = scaleInSharedContentToBounds(),
                exit = scaleOutSharedContentToBounds()
            )
            .fillMaxWidth()
            .height(240.dp)
            .clickable {
                popularImage.id?.let {
                    onPopularClick.invoke(
                        it
                    )
                }
            }
            .clip(RoundedCornerShape(16.dp)),
        model = popularImage.url,
        contentDescription = popularImage.imageDesc,
        loading = {
            ImageLoadingState()
        },
        contentScale = ContentScale.FillBounds
    )
}
