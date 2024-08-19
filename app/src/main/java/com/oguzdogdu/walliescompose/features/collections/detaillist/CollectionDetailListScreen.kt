package com.oguzdogdu.walliescompose.features.collections.detaillist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.data.common.ImageLoadingState
import com.oguzdogdu.walliescompose.domain.model.collections.Collection
import com.oguzdogdu.walliescompose.domain.model.collections.CollectionList
import com.oguzdogdu.walliescompose.features.home.LoadingState
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CollectionDetailListScreenRoute(
    animatedVisibilityScope: AnimatedVisibilityScope,
    collectionDetailId: String?,
    collectionDetailTitle: String?,
    modifier: Modifier = Modifier,
    viewModel: CollectionDetailListViewModel = hiltViewModel(),
    onCollectionClick: (String) -> Unit,
    onUserDetailClick: (String) -> Unit,
    onBackClick: () -> Unit
) {

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.handleUiEvent(CollectionListEvent.FetchCollectionList(id = collectionDetailId))
    }

    val stateOfCollectionList by viewModel.getCollectionDetailList.collectAsStateWithLifecycle()
    val stateOfCollectionInfo by viewModel.getCollectionInformation.collectAsStateWithLifecycle()
    var listScrollState by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = { onBackClick.invoke() },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                androidx.compose.animation.AnimatedVisibility(
                    visible = listScrollState,
                    enter = fadeIn(tween(500, easing = LinearEasing)),
                    exit = fadeOut(tween(500, easing = LinearEasing))
                ) {
                    Text(
                        text = collectionDetailTitle.orEmpty(),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 16.sp,
                        fontFamily = medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    ) {
        CollectionDetailListScreen(
            paddingValues = it,
            stateOfCollectionInfo = stateOfCollectionInfo,
            stateOfCollectionList = stateOfCollectionList,
            onCollectionClick = { id ->
                onCollectionClick.invoke(id)
            }, onScrollList = { scroll ->
                listScrollState = scroll
            },
            onUserDetailClick = onUserDetailClick
        )
    }
}

@Composable
fun CollectionDetailListScreen(
    paddingValues: PaddingValues,
    stateOfCollectionInfo: CollectionConstantInfoState,
    stateOfCollectionList: CollectionListState,
    onCollectionClick: (String) -> Unit,
    onScrollList: (Boolean) -> Unit,
    onUserDetailClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    var visible by remember { mutableStateOf(true) }
    val lazyListState = rememberLazyGridState()
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { index ->
                if (index < 2) {
                    visible = true
                    onScrollList.invoke(false)
                } else {
                    visible = false
                    onScrollList.invoke(true)
                }
            }
    }

    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        CollectionDetailInfoCard(
            stateOfCollectionInfo = stateOfCollectionInfo,
            visibilityOfInfoCard = visible,
            onUserDetailClick = onUserDetailClick
        )
        CollectionDetailListScreen(
            stateOfCollectionList = stateOfCollectionList,
            onCollectionClick = onCollectionClick,
            lazyGridState = lazyListState
        )
    }
}

@Composable
fun CollectionDetailInfoCard(
    stateOfCollectionInfo: CollectionConstantInfoState,
    visibilityOfInfoCard: Boolean,
    onUserDetailClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    AnimatedVisibility(
        visible = visibilityOfInfoCard,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp),
        enter = slideInVertically() + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(
            initialAlpha = 0.4f
        ),
        exit = slideOutVertically() + shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(
            targetAlpha = 0.4f
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            if (stateOfCollectionInfo.collection?.title?.isNotEmpty() == true) {
                Text(
                    text = stateOfCollectionInfo.collection.title,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 24.sp,
                    fontFamily = medium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                )
            }
            if (stateOfCollectionInfo.collection?.desc?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.size(12.dp))
                Text(
                    text = stateOfCollectionInfo.collection.desc,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 16.sp,
                    fontFamily = medium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                )
            }
            if (stateOfCollectionInfo.collection?.user?.name?.isNotEmpty() == true
                &&
                (stateOfCollectionInfo.collection.user.profileImage?.medium?.isNotEmpty() == true)
            ) {
                Spacer(modifier = Modifier.size(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable {
                            onUserDetailClick(stateOfCollectionInfo.collection.user.username.orEmpty())
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    AsyncImage(
                        model = stateOfCollectionInfo.collection.user.profileImage.medium,
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "${stateOfCollectionInfo.collection.user.name}",
                        fontSize = 16.sp,
                        fontFamily = medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.size(12.dp))
            }
        }
    }
}

@Composable
fun CollectionDetailListScreen(
    stateOfCollectionList: CollectionListState,
    onCollectionClick: (String) -> Unit,
    lazyGridState: LazyGridState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            stateOfCollectionList.collectionsLists?.isNotEmpty() == true -> {
                LazyVerticalGrid(
                    state = lazyGridState,
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(stateOfCollectionList.collectionsLists) { collection ->
                        CollectionListImageView(
                            modifier,
                            collectionDetailListItems = collection,
                            onCollectionItemClick = {
                                onCollectionClick.invoke(it)
                            }
                        )
                    }
                }
            }

            stateOfCollectionList.loading -> LoadingState(modifier = modifier)
            stateOfCollectionList.collectionsLists?.isEmpty() == true -> EmptyView(
                modifier = modifier,
                state = true
            )
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


@Composable
private fun CollectionListImageView(
    modifier: Modifier,
    collectionDetailListItems: CollectionList,
    onCollectionItemClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .clickable {
                onCollectionItemClick.invoke(collectionDetailListItems.id.orEmpty())
            }
    ) {
        SubcomposeAsyncImage(
            model = collectionDetailListItems.url,
            contentDescription = collectionDetailListItems.desc,
            contentScale = ContentScale.FillBounds,
            modifier = modifier
                .height(240.dp)
                .clip(CircleShape.copy(all = CornerSize(16.dp))),
            loading = { ImageLoadingState() },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CollectionDetailListPreview() {
    CollectionDetailListScreen(
        paddingValues = PaddingValues(12.dp),
        stateOfCollectionInfo = CollectionConstantInfoState(
            collection = Collection(
                "123",
                "Title",
                desc = "lorem ipsum lorem dolor amet",
                user = null
            )
        ),
        stateOfCollectionList = CollectionListState(),
        onCollectionClick = {},
        onScrollList = {},
        onUserDetailClick = {}
    )
}