package com.oguzdogdu.walliescompose.features.collections

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.data.common.ImageLoadingState
import com.oguzdogdu.walliescompose.domain.model.collections.WallpaperCollections
import com.oguzdogdu.walliescompose.ui.theme.medium

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CollectionsScreenRoute(
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    viewModel: CollectionsViewModel = hiltViewModel(),
    onCollectionClick: (String,String) -> Unit
) {
    val collectionState: LazyPagingItems<WallpaperCollections> =
        viewModel.collectionPhotosState.collectAsLazyPagingItems()

    val stateOfFilterBottomSheet by viewModel.filterBottomSheetOpenStat.collectAsStateWithLifecycle()

    val stateOfChoisedFilter by viewModel.choisedFilter.collectAsStateWithLifecycle()

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        if (collectionState.itemCount == 0) {
            viewModel.handleUIEvent(CollectionScreenEvent.FetchLatestData)
        }
    }

    Scaffold(modifier = modifier
        .fillMaxSize(), topBar = {
        Row(
            modifier = modifier
                .wrapContentWidth()
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.collections_title),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
        ) {
           ShowFilterOfCollections(modifier = modifier
               .align(Alignment.End)
               .padding(top = 8.dp, end = 8.dp, bottom = 8.dp),
               sheetState = stateOfFilterBottomSheet,
              onItemClick = { id ->
                  viewModel.handleUIEvent(CollectionScreenEvent.ChoisedFilterOption(id))
                   when(id) {
                       0 -> viewModel.handleUIEvent(CollectionScreenEvent.FetchLatestData)

                       1 -> viewModel.handleUIEvent(CollectionScreenEvent.SortByTitles)

                       2 -> viewModel.handleUIEvent(CollectionScreenEvent.SortByLikes)

                       3 -> viewModel.handleUIEvent(CollectionScreenEvent.SortByUpdatedDate)
                   }
               }, dynamicSheetState = {
                   viewModel.handleUIEvent(CollectionScreenEvent.OpenFilterBottomSheet(it))
               }, choisedFilter = stateOfChoisedFilter)
            CollectionScreen(
                animatedVisibilityScope = animatedVisibilityScope,
                collectionLazyPagingItems = collectionState,
                onCollectionClick = { id, title ->
                    onCollectionClick.invoke(id,title)
                })
        }
    }
}

@Composable
fun ShowFilterOfCollections(
    modifier: Modifier,
    sheetState: Boolean,
    onItemClick: (Int) -> Unit,
    choisedFilter: Int,
    dynamicSheetState: (Boolean) -> Unit
) {
    val sortTypeList = listOf(
        stringResource(R.string.text_recommended_ranking),
        stringResource(id = R.string.text_alphabetic_sort),
        stringResource(id = R.string.text_likes_sort),
        stringResource(R.string.text_updated_date)
    )
    var isContextMenuVisible by remember {
        mutableStateOf(sheetState)
    }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }

    LaunchedEffect(key1 = sheetState) {
        isContextMenuVisible = sheetState
    }

    Card(
        modifier = modifier
            .wrapContentSize()
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .pointerInput(true) {
                    detectTapGestures(onPress = {
                        dynamicSheetState.invoke(true)
                        pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                    })
                }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_sort), contentDescription = "")
            Spacer(modifier = modifier.size(2.dp))
            Text(
                text = stringResource(id = R.string.text_sort),
                fontFamily = medium,
                color = Color.Unspecified
            )
        }
    }
    FilterDialog(
        modifier = modifier,
        typeOfFilters = sortTypeList,
        isOpen = isContextMenuVisible,
        onItemClick = {
            onItemClick.invoke(it)
            dynamicSheetState.invoke(false)
        }, onDismiss = {
            dynamicSheetState.invoke(false)
        }, choisedFilter = choisedFilter)
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.CollectionScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    collectionLazyPagingItems: LazyPagingItems<WallpaperCollections>,
    onCollectionClick: (String, String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
        state = rememberLazyGridState(),
        verticalArrangement = Arrangement.Center
    ) {
        items(
            count = collectionLazyPagingItems.itemCount,
            key = collectionLazyPagingItems.itemKey { item: WallpaperCollections -> item.id.hashCode()},
            contentType = collectionLazyPagingItems.itemContentType { "Collections" }) { index: Int ->
            val collections: WallpaperCollections? = collectionLazyPagingItems[index]
            if (collections != null) {
                CollectionItem(
                    animatedVisibilityScope = animatedVisibilityScope,
                    collections = collections,
                    onCollectionItemClick = { id, title ->
                        onCollectionClick.invoke(id, title)
                    }
                )
            }
        }
        collectionLazyPagingItems.apply {
            when {
                loadState.source.refresh is LoadState.Loading -> {
                    item(span = { GridItemSpan(2) }) {
                        Box(
                            modifier = modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator(
                                modifier = modifier.align(Alignment.Center)
                            )
                        }
                    }
                }

                loadState.refresh is LoadState.Error || loadState.append is LoadState.Error -> {
                    val errorMessage = (loadState.refresh as? LoadState.Error)?.error?.localizedMessage.orEmpty()
                    item(span = { GridItemSpan(2) }) {
                        Text(text = errorMessage)
                    }
                }

                loadState.source.append is LoadState.Loading-> {
                    item(span = { GridItemSpan(2) }) {
                        Box(
                            modifier = modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                        ) {
                            CircularProgressIndicator(
                                modifier = modifier.align(Alignment.BottomCenter)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CollectionItem(
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    collections: WallpaperCollections,
    onCollectionItemClick: (String, String) -> Unit
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .clickable {
                onCollectionItemClick.invoke(
                    collections.id.orEmpty(), collections.title.orEmpty()
                )
            }
            .padding(4.dp)
        , contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = collections.photo,
            contentDescription = collections.title,
            contentScale = ContentScale.FillBounds,
            modifier = modifier
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
                modifier = modifier.sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "collectionTitle-${collections.title}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    enter = scaleInSharedContentToBounds(),
                    exit = scaleOutSharedContentToBounds()
                )
            )
        }
    }
}