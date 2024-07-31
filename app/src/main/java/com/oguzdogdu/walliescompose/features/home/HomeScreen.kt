package com.oguzdogdu.walliescompose.features.home

import android.annotation.SuppressLint
import android.graphics.RenderEffect
import android.graphics.Shader
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.EaseInOutExpo
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.oguzdogdu.walliescompose.domain.model.random.RandomImage
import com.oguzdogdu.walliescompose.domain.model.topics.Topics
import com.oguzdogdu.walliescompose.features.home.event.HomeScreenEvent
import com.oguzdogdu.walliescompose.features.home.state.HomeUIState
import com.oguzdogdu.walliescompose.navigation.utils.WalliesIcons
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular
import com.oguzdogdu.walliescompose.util.capitalizeFirstLetter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

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
                    }
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
    onLatestClick: (String) -> Unit
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
                        HomeRandomPage(randomImageList = homeUiState.random)
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
@Composable
fun HomeRandomPage(randomImageList: List<RandomImage>, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = {
        randomImageList.size
    })

    val isDraggedState = pagerState.interactionSource.collectIsDraggedAsState()
    LaunchedEffect(isDraggedState) {
        snapshotFlow { isDraggedState.value }
            .collectLatest { isDragged ->
                if (!isDragged) {
                    while (true) {
                        delay(2500)
                        runCatching {
                            pagerState.animateScrollToPage(page = pagerState.currentPage.inc() % pagerState.pageCount,
                                animationSpec = tween(1000, easing = LinearOutSlowInEasing)
                            )
                        }
                    }
                }
            }
    }

    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        pageSpacing = 8.dp,
        key = { randomImageList.getOrNull(it)?.id.hashCode() ?: Int.MAX_VALUE }
    ) { index ->
        HomeRandomPageItem(
            randomImage = randomImageList[index],
            page = index,
            pagerState = pagerState,
            isSelected = pagerState.currentPage == index
        )
        }
        Row(
            Modifier
                .wrapContentSize()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
                .background(color = Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.secondaryContainer else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                        .animateContentSize()
                )
            }
        }
    }
}

@Composable
fun HomeRandomPageItem(
    randomImage: RandomImage,
    page: Int,
    pagerState: PagerState,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val titleState = rememberUpdatedState(newValue = isSelected)
    var colorOfImageBackground by remember {
        mutableStateOf<Color?>(null)
    }
    LaunchedEffect(randomImage.color) {
        launch {
            colorOfImageBackground =
                getTextColorBasedOnBackground(Color(android.graphics.Color.parseColor(randomImage.color)))
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .graphicsLayer {
                val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
                translationX = pageOffset * size.width
                alpha = 1 - pageOffset.absoluteValue
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(), propagateMinConstraints = true
        ) {
            SubcomposeAsyncImage(model = randomImage.url,
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .align(Alignment.Center),
                loading = {
                    ImageLoadingState()
                })
            androidx.compose.animation.AnimatedVisibility(visible = titleState.value,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .background(
                        color = when (colorOfImageBackground) {
                            Color.Black -> Color.White.copy(0.2f)
                            Color.White -> Color.Black.copy(0.2f)
                            else -> Color.Unspecified
                        }, shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)
                    )
                    .padding(8.dp),
                enter = slideInVertically(spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) ,
                exit = slideOutVertically(spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
            ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        AsyncImage(
                            model = randomImage.userImage.orEmpty(),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Text(
                                text = randomImage.username.orEmpty().capitalizeFirstLetter(),
                                fontSize = 16.sp,
                                fontFamily = medium,
                                maxLines = 1,
                                color = colorOfImageBackground ?: Color.Unspecified,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = randomImage.imageDesc.orEmpty().capitalizeFirstLetter(),
                                fontSize = 16.sp,
                                fontFamily = regular,
                                maxLines = 1,
                                color = colorOfImageBackground ?: Color.Unspecified,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                }
            }
        }
    }
}

fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}
fun getTextColorBasedOnBackground(backgroundColor: Color): Color {
    val luminanceThreshold = 0.5f
    return if (backgroundColor.luminance() > luminanceThreshold) {
        Color.Black
    } else {
        Color.White
    }
}