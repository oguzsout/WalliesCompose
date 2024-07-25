package com.oguzdogdu.walliescompose.features.collections

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.data.common.ImageLoadingState
import com.oguzdogdu.walliescompose.domain.model.collections.WallpaperCollections
import com.oguzdogdu.walliescompose.ui.theme.medium

@Composable
fun CollectionsScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: CollectionsViewModel = hiltViewModel(),
    onCollectionClick: (String,String) -> Unit,
) {
    val collectionPaginationState: LazyPagingItems<WallpaperCollections> =
        viewModel.collectionPhotosState.collectAsLazyPagingItems()
    val collectionScreenState by viewModel.collectionScreenState.collectAsStateWithLifecycle()
    var collectionListTypeState by remember { mutableStateOf("") }
   // val stateOfFilterBottomSheet by viewModel.filterBottomSheetOpenStat.collectAsStateWithLifecycle()
    // val stateOfChoisedFilter by viewModel.choisedFilter.collectAsStateWithLifecycle()

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.handleUIEvent(CollectionScreenEvent.CheckListType)
        if (collectionPaginationState.itemCount == 0) {
            viewModel.handleUIEvent(CollectionScreenEvent.FetchLatestData)
        }
    }
    LifecycleEventEffect(event = Lifecycle.Event.ON_STOP) {
        viewModel.onListTypeChanged(collectionListTypeState)
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
//           ShowFilterOfCollections(modifier = Modifier
//               .align(Alignment.End),
//               sheetState = stateOfFilterBottomSheet,
//              onItemClick = { id ->
//                  viewModel.handleUIEvent(CollectionScreenEvent.ChoisedFilterOption(id))
//                   when(id) {
//                       0 -> viewModel.handleUIEvent(CollectionScreenEvent.FetchLatestData)
//
//                       1 -> viewModel.handleUIEvent(CollectionScreenEvent.SortByTitles)
//
//                       2 -> viewModel.handleUIEvent(CollectionScreenEvent.SortByLikes)
//
//                       3 -> viewModel.handleUIEvent(CollectionScreenEvent.SortByUpdatedDate)
//                   }
//               }, dynamicSheetState = {
//                   viewModel.handleUIEvent(CollectionScreenEvent.OpenFilterBottomSheet(it))
//               }, choisedFilter = stateOfChoisedFilter)
            CollectionScreen(
                collectionState = collectionScreenState,
                collectionLazyPagingItems = collectionPaginationState,
                onCollectionClick = { id, title ->
                    onCollectionClick.invoke(id,title)
                },
                onChangeListType = { listType ->
                    when(listType) {
                        true -> {
                            viewModel.handleUIEvent(CollectionScreenEvent.ChangeListType(ListType.VERTICAL))
                            collectionListTypeState = ListType.VERTICAL.name
                        }
                        false -> {
                            viewModel.handleUIEvent(CollectionScreenEvent.ChangeListType(ListType.GRID))
                            collectionListTypeState = ListType.GRID.name
                        }
                    }
                }
            )
        }
    }
}

//@Composable
//fun ShowFilterOfCollections(
//    sheetState: Boolean,
//    onItemClick: (Int) -> Unit,
//    choisedFilter: Int,
//    dynamicSheetState: (Boolean) -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    val sortTypeList = listOf(
//        stringResource(R.string.text_recommended_ranking),
//        stringResource(id = R.string.text_alphabetic_sort),
//        stringResource(id = R.string.text_likes_sort),
//        stringResource(R.string.text_updated_date)
//    )
//    var isContextMenuVisible by remember {
//        mutableStateOf(sheetState)
//    }
//    var pressOffset by remember {
//        mutableStateOf(DpOffset.Zero)
//    }
//
//    LaunchedEffect(key1 = sheetState) {
//        isContextMenuVisible = sheetState
//    }
//
//    Card(
//        modifier = modifier
//            .wrapContentSize()
//            .padding(horizontal = 8.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .wrapContentSize()
//                .pointerInput(true) {
//                    detectTapGestures(onPress = {
//                        dynamicSheetState.invoke(true)
//                        pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
//                    })
//                }
//                .padding(8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(painter = painterResource(id = R.drawable.ic_sort), contentDescription = "")
//            Spacer(modifier = modifier.size(2.dp))
//            Text(
//                text = stringResource(id = R.string.text_sort),
//                fontFamily = medium,
//                color = Color.Unspecified
//            )
//        }
//    }
//    FilterDialog(
//        modifier = modifier,
//        typeOfFilters = sortTypeList,
//        isOpen = isContextMenuVisible,
//        onItemClick = {
//            onItemClick.invoke(it)
//            dynamicSheetState.invoke(false)
//        }, onDismiss = {
//            dynamicSheetState.invoke(false)
//        }, choisedFilter = choisedFilter)
//}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
private fun CollectionScreen(
    collectionState: CollectionState,
    collectionLazyPagingItems: LazyPagingItems<WallpaperCollections>,
    onCollectionClick: (String, String) -> Unit,
    onChangeListType: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var changeListPresentation by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed) {
        if (isPressed) {
            onChangeListType.invoke(changeListPresentation)
        }
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 8.dp)
                .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp))
                .align(Alignment.End)
                .padding(8.dp)
                .animateContentSize(
                    spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Center
        ) {
                Text(
                    text = when(collectionState.collectionsListType) {
                        ListType.VERTICAL -> stringResource(R.string.grid)
                        ListType.GRID -> stringResource(R.string.vertical)
                    },
                    textAlign = TextAlign.Center,
                    fontFamily = medium,
                    fontSize = 16.sp,
                    color = Color.White,
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
    ) {
        Box(modifier = modifier
            .wrapContentSize()
            .clickable {
                onCollectionItemClick.invoke(
                    collections.id.orEmpty(), collections.title.orEmpty()
                )
            }
            .padding(4.dp), contentAlignment = Alignment.Center) {
            when (it) {
                ListType.VERTICAL -> VerticalCollectionItem(collections = collections)
                ListType.GRID -> GridCollectionItem(collections = collections)
            }
        }

    }
}

@Composable
fun GridCollectionItem(
    collections: WallpaperCollections,
    modifier: Modifier = Modifier) {
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