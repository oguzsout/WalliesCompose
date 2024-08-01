package com.oguzdogdu.walliescompose.features.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.data.common.ImageLoadingState
import com.oguzdogdu.walliescompose.domain.model.latest.LatestImage
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.domain.model.topics.Topics
import com.oguzdogdu.walliescompose.features.home.components.HomeRandomPage
import com.oguzdogdu.walliescompose.features.home.event.HomeScreenEvent
import com.oguzdogdu.walliescompose.features.home.state.HomeUIState
import com.oguzdogdu.walliescompose.navigation.utils.WalliesIcons
import com.oguzdogdu.walliescompose.ui.theme.medium
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreenRoute(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: HomeViewModel,
    onTopicSeeAllClick: () -> Unit,
    onPopularSeeAllClick: () -> Unit,
    onLatestSeeAllClick: () -> Unit,
    onTopicDetailListClick: (String?) -> Unit,
    onLatestClick: (String) -> Unit,
    onPopularClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onUserPhotoClick: () -> Unit,
    onRandomImageClick: (String?) -> Unit,
    navigateBack:() -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val homeUiState by viewModel.homeListState.collectAsStateWithLifecycle()
    val authUserProfileImage by viewModel.userProfileImage.collectAsStateWithLifecycle()
    val appName = stringResource(id = R.string.app_name)
    var visibleChars by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        appName.forEachIndexed { index, _ ->
            delay(300)
            visibleChars = index + 1
        }
    }
    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE, lifecycleOwner = lifecycleOwner){
        viewModel.handleScreenEvents(HomeScreenEvent.FetchMainScreenUserData)
        viewModel.handleScreenEvents(HomeScreenEvent.FetchHomeScreenLists)
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
                onClick = { onUserPhotoClick.invoke() },
                modifier = modifier
                    .wrapContentSize()
                    .weight(1f)
            ) {
                AsyncImage(
                    model = if (authUserProfileImage?.isNotEmpty() == true) authUserProfileImage else WalliesIcons.DefaultAvatar,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "Profile Image",
                    modifier = modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(64.dp))
                        .border(1.dp, Color.DarkGray, shape = RoundedCornerShape(64.dp))
                )
            }

            Row(
                modifier = Modifier.weight(6f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                appName.forEachIndexed { index, char ->
                    val animatedColor by animateColorAsState(
                        targetValue = if (index < visibleChars) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessVeryLow
                        ) , label = ""
                    )
                    Text(
                        text = char.toString(),
                        color = animatedColor,
                        fontSize = 18.sp,
                        fontFamily = medium,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            IconButton(
                onClick = { onSearchClick.invoke() },
                modifier = modifier
                    .wrapContentSize()
                    .weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.inverseSurface
                )
            }
        }
    }) {paddingValues ->
            Column(modifier = modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize()) {
                HomeScreenContent(
                    animatedVisibilityScope = animatedVisibilityScope,
                    homeUiState = homeUiState,
                    modifier = modifier,
                    onTopicSeeAllClick = {
                        onTopicSeeAllClick.invoke()
                    },
                    onTopicDetailListClick = { id ->
                        onTopicDetailListClick.invoke(id)
                    },
                    onPopularClick = { id ->
                        onPopularClick.invoke(id)
                    },
                    onLatestClick = { id -> onLatestClick.invoke(id) },
                    onPopularSeeAllClick = {
                        onPopularSeeAllClick.invoke()
                    },
                    onLatestSeeAllClick = {
                        onLatestSeeAllClick.invoke()
                    },
                    onRandomImageClick = onRandomImageClick
                )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreenContent(
    animatedVisibilityScope: AnimatedVisibilityScope,
    homeUiState: HomeUIState,
    modifier: Modifier = Modifier,
    onTopicSeeAllClick: () -> Unit,
    onPopularSeeAllClick: () -> Unit,
    onLatestSeeAllClick: () -> Unit,
    onTopicDetailListClick: (String?) -> Unit,
    onPopularClick: (String) -> Unit,
    onLatestClick: (String) -> Unit,
    onRandomImageClick: (String?) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (homeUiState) {
            is HomeUIState.Loading -> {}

            is HomeUIState.Error -> {}

            is HomeUIState.Success -> {
                LazyColumn(
                    state = rememberLazyListState(),
                    modifier = modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    item(key = "randomContainer") {
                        HomeRandomPage(randomImageList = homeUiState.random, onRandomImageClick = onRandomImageClick)
                    }
                    item(key = "topicContainer") {
                        TopicLayoutContainer(
                            modifier = modifier,
                            topicsList = homeUiState.topics,
                            onTopicDetailListClick = {
                                onTopicDetailListClick.invoke(it)
                            },
                            onTopicSeeAllClick = {
                                onTopicSeeAllClick.invoke()
                            })
                    }
                    item(key = "popularContainer") {
                        PopularLayoutContainer(animatedVisibilityScope = animatedVisibilityScope,
                            popularList = homeUiState.popular,
                            onPopularClick = { id ->
                                onPopularClick.invoke(id)
                            },
                            onPopularSeeAllClick = {
                                onPopularSeeAllClick.invoke()
                            })
                    }

                    item(key = "latestContainer") {
                        LatestLayoutContainer(animatedVisibilityScope = animatedVisibilityScope,
                            latestList = homeUiState.latest,
                            onLatestClick = { id -> onLatestClick.invoke(id) },
                            onLatestSeeAllClick = { onLatestSeeAllClick.invoke() })

                    }
                }
            }
        }
    }
}

@Composable
private fun TopicLayoutContainer(
    modifier: Modifier,
    topicsList: List<Topics>,
    onTopicDetailListClick: (String?) -> Unit,
    onTopicSeeAllClick: () -> Unit
) {
    Column(
        modifier = modifier
            .wrapContentSize()
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.topics_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.padding(start = 4.dp, top = 16.dp, bottom = 8.dp)
            )

            Text(
                text = stringResource(id = R.string.show_all),
                fontFamily = medium,
                fontSize = 12.sp,
                color = Color.Unspecified,
                modifier = modifier
                    .padding(end = 8.dp, top = 16.dp, bottom = 8.dp)
                    .clickable {
                        onTopicSeeAllClick.invoke()
                    }
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.requiredHeightIn(max = 280.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(topicsList, key = { index: Int, item: Topics ->
                item.id.hashCode()
            }) { index, item ->
                TopicTitleView(imageUrl = item.titleBackground, title = item.title, imageName = item.title, onTopicDetailListClick = {
                    onTopicDetailListClick.invoke(it)
                })
            }
        }
    }

}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.PopularLayoutContainer(
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    popularList: List<PopularImage>,
    onPopularClick: (String) -> Unit,
    onPopularSeeAllClick: () -> Unit
) {
    Column(
        modifier = modifier
            .wrapContentSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.popular_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.padding(start = 4.dp, top = 16.dp, bottom = 8.dp)
            )

            Text(
                text = stringResource(id = R.string.show_all),
                fontFamily = medium,
                fontSize = 12.sp,
                color = Color.Unspecified,
                modifier = modifier
                    .padding(end = 8.dp, top = 16.dp, bottom = 8.dp)
                    .clickable {
                        onPopularSeeAllClick.invoke()
                    }
            )
        }

        LazyRow(modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),horizontalArrangement = Arrangement.spacedBy(8.dp), content = {
            items(popularList, key = {
                it.url.hashCode()
            }) { popularImage ->
                PopularImageView(
                    animatedVisibilityScope = animatedVisibilityScope,
                    id = popularImage.id,
                    imageUrl = popularImage.url,
                    imageName = popularImage.imageDesc,
                    onPopularClick = {
                        onPopularClick.invoke(it)
                    })
            }
        })

    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.LatestLayoutContainer(
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    latestList: List<LatestImage>,
    onLatestClick: (String) -> Unit,
    onLatestSeeAllClick: () -> Unit
) {
    Column(
        modifier = modifier
            .wrapContentSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.latest_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.padding(start = 4.dp, top = 16.dp, bottom = 8.dp)
            )

            Text(
                text = stringResource(id = R.string.show_all),
                fontFamily = medium,
                fontSize = 12.sp,
                color = Color.Unspecified,
                modifier = modifier
                    .padding(end = 8.dp, top = 16.dp, bottom = 8.dp)
                    .clickable {
                        onLatestSeeAllClick.invoke()
                    }
            )
        }

        LazyRow(modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),horizontalArrangement = Arrangement.spacedBy(8.dp), content = {
            items(latestList, key = {
                it.url.hashCode()
            }) { latestImage ->
                LatestImageView(
                    animatedVisibilityScope = animatedVisibilityScope,
                    id = latestImage.id,
                    imageUrl = latestImage.url,
                    imageName = latestImage.imageDesc,
                    onLatestClick = {
                        onLatestClick.invoke(it)
                    })
            }
        })

    }
}

@Composable
private fun TopicTitleView(imageUrl: String?, title: String?, imageName:String?,onTopicDetailListClick: (String?) -> Unit) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .clickable {
                onTopicDetailListClick.invoke(title)
            }
    ) {

        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = imageName,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .clip(CircleShape.copy(all = CornerSize(16.dp)))
                .drawWithContent {
                    drawContent()
                    drawRect(
                        color = Color.Black.copy(alpha = 0.5f),
                        size = size,
                    )
                },
            loading = { ImageLoadingState() },
        )

        Text(
            text = title.orEmpty(),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.PopularImageView(
    animatedVisibilityScope: AnimatedVisibilityScope,
    id: String?,
    imageUrl: String?,
    imageName: String?,
    onPopularClick: (String) -> Unit
) {
    SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = imageName,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "popularImage-${id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
                )
                .width(160.dp)
                .height(240.dp)
                .clip(CircleShape.copy(all = CornerSize(16.dp)))
                .clickable { onPopularClick.invoke(id.orEmpty()) },
            loading = { ImageLoadingState() },
        )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.LatestImageView(
    animatedVisibilityScope: AnimatedVisibilityScope,
    id: String?,
    imageUrl: String?,
    imageName: String?,
    onLatestClick: (String) -> Unit
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = imageName,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .sharedBounds(
                sharedContentState = rememberSharedContentState(key = "popularImage-${id}"),
                animatedVisibilityScope = animatedVisibilityScope,
            )
            .width(160.dp)
            .height(240.dp)
            .clip(CircleShape.copy(all = CornerSize(16.dp)))
            .clickable { onLatestClick.invoke(id.orEmpty()) },
        loading = { ImageLoadingState() },
    )
}