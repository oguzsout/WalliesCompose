package com.oguzdogdu.walliescompose.features.collections

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.domain.model.collections.WallpaperCollections
import com.oguzdogdu.walliescompose.features.collections.components.CollectionItem
import com.oguzdogdu.walliescompose.features.collections.components.ShowFilterOfCollections
import com.oguzdogdu.walliescompose.ui.theme.medium

typealias onCollectionsScreenEvent = (CollectionScreenEvent) -> Unit

@Composable
fun CollectionsScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: CollectionsViewModel = hiltViewModel(),
    onCollectionClick: (String,String) -> Unit,
) {
    val collectionPaginationState: LazyPagingItems<WallpaperCollections> =
        viewModel.collectionPhotosState.collectAsLazyPagingItems()
    val collectionScreenState by viewModel.collectionScreenState.collectAsStateWithLifecycle()

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.handleUIEvent(CollectionScreenEvent.CheckListType)
        if (collectionPaginationState.itemCount == 0) {
            viewModel.handleUIEvent(CollectionScreenEvent.FetchLatestData)
        }
    }
    LifecycleEventEffect(event = Lifecycle.Event.ON_STOP) {
        viewModel.onListTypeChanged(collectionScreenState.collectionsListType.name)
    }

    Scaffold(modifier = modifier
        .fillMaxSize(), topBar = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 12.dp, top = 16.dp, bottom = 8.dp, end = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
                Row(
                    modifier = modifier
                        .wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.collections_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 24.sp,
                        fontFamily = medium,
                    )
                }
            Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(R.string.collection_desc),
                    fontSize = 16.sp,
                    fontFamily = medium,
                    maxLines = 3,
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
            CollectionScreen(
                collectionState = collectionScreenState,
                collectionLazyPagingItems = collectionPaginationState,
                onCollectionClick = { id, title ->
                    onCollectionClick.invoke(id, title)
                },
                onCollectionsScreenEvent = viewModel::handleUIEvent
            )
        }
    }
}

@Composable
private fun CollectionScreen(
    collectionState: CollectionState,
    collectionLazyPagingItems: LazyPagingItems<WallpaperCollections>,
    onCollectionClick: (String, String) -> Unit,
    onCollectionsScreenEvent: onCollectionsScreenEvent,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .wrapContentWidth()
                .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp))
                .align(Alignment.End)
                .padding(vertical = 4.dp)
                .animateContentSize(
                    spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ShowFilterOfCollections(modifier = Modifier,
                collectionState = collectionState,
                onCollectionsScreenEvent = onCollectionsScreenEvent)
            VerticalDivider(
                modifier = Modifier.padding(8.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            ChangeListType(
                collectionState = collectionState,
                onCollectionsScreenEvent = onCollectionsScreenEvent
            )
        }

        LazyVerticalGrid(
            columns = when(collectionState.collectionsListType) {
                ListType.VERTICAL -> GridCells.Fixed(1)
                ListType.GRID -> GridCells.Fixed(2)
            },
            modifier = Modifier
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
                        collectionState = collectionState,
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
                                modifier = Modifier.fillMaxSize()
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
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
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .fillMaxWidth()
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.BottomCenter)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChangeListType(
    collectionState: CollectionState,
    onCollectionsScreenEvent: onCollectionsScreenEvent,
) {
    var changeListPresentation by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed) {
        if (isPressed) {
            when (changeListPresentation) {
                true -> onCollectionsScreenEvent.invoke(
                    CollectionScreenEvent.ChangeListType(
                        ListType.VERTICAL
                    )
                )

                false -> onCollectionsScreenEvent.invoke(
                    CollectionScreenEvent.ChangeListType(
                        ListType.GRID
                    )
                )
            }
        }
    }
    Row(
        modifier = Modifier
            .wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = when(collectionState.collectionsListType) {
                ListType.VERTICAL -> stringResource(R.string.grid)
                ListType.GRID -> stringResource(R.string.vertical)
            },
            textAlign = TextAlign.Center,
            fontFamily = medium,
            fontSize = 16.sp,
            modifier = Modifier.padding(4.dp)
        )
        IconButton(
            onClick = {
                changeListPresentation = !changeListPresentation
            },
            interactionSource = interactionSource,
            modifier = Modifier
                .size(32.dp),
        ) {
            Icon(
                painter = when(collectionState.collectionsListType) {
                    ListType.VERTICAL -> painterResource(id = R.drawable.grid_4_svgrepo_com)
                    ListType.GRID ->  painterResource(
                        id = R.drawable.grid_2_horizontal_svgrepo_com
                    )
                }, contentDescription = ""
            )
        }
    }
}