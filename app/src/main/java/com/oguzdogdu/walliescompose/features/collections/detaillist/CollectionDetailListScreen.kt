package com.oguzdogdu.walliescompose.features.collections.detaillist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.data.common.ImageLoadingState
import com.oguzdogdu.walliescompose.domain.model.collections.CollectionList
import com.oguzdogdu.walliescompose.features.home.LoadingState
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CollectionDetailListScreenRoute(
    animatedVisibilityScope: AnimatedVisibilityScope,
    collectionDetailId: String?,
    collectionDetailTitle: String?,
    modifier: Modifier = Modifier,
    viewModel: CollectionDetailListViewModel = hiltViewModel(),
    onCollectionClick: (String) -> Unit,
    onBackClick: () -> Unit
) {

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.handleUiEvent(CollectionListEvent.FetchCollectionList(id = collectionDetailId))

    }
    val state by viewModel.getCollectionDetail.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onBackClick.invoke() },
                    modifier = modifier.wrapContentSize()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = modifier.wrapContentSize()
                    )
                }

                Text(
                    modifier = modifier.sharedBounds(
                        sharedContentState = rememberSharedContentState(key = "collectionTitle-${collectionDetailTitle}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        enter = scaleInSharedContentToBounds(),
                        exit = scaleOutSharedContentToBounds()
                    ),
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
    ) {
        CollectionDetailListScreen(modifier = modifier, paddingValues = it, state = state, onCollectionClick = {id ->
            onCollectionClick.invoke(id)
        })
    }
}

@Composable
fun CollectionDetailListScreen(modifier: Modifier,paddingValues: PaddingValues,state:CollectionsListsState,onCollectionClick: (String) -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when {
            state.collectionsLists?.isNotEmpty() == true -> {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    state = rememberLazyStaggeredGridState(),
                    verticalItemSpacing = 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.collectionsLists) { collection ->
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
            state.loading -> LoadingState(modifier = modifier)
            state.collectionsLists?.isEmpty() == true -> EmptyView(modifier = modifier, state = true)
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
private fun CollectionListImageView(modifier: Modifier, collectionDetailListItems: CollectionList, onCollectionItemClick:(String) -> Unit) {
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
                .clip(CircleShape.copy(all = CornerSize(16.dp))),
            loading = { ImageLoadingState() },
        )
    }
}