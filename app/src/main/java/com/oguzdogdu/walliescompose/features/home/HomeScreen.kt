package com.oguzdogdu.walliescompose.features.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.EaseOutElastic
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.data.common.ImageLoadingState
import com.oguzdogdu.walliescompose.domain.model.latest.LatestImage
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.domain.model.topics.Topics
import com.oguzdogdu.walliescompose.features.home.components.AnimatedImageRotationCard
import com.oguzdogdu.walliescompose.features.home.components.HomeRandomPage
import com.oguzdogdu.walliescompose.features.home.components.PhotoByOrientationCard
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
    onTopicSeeAllClick: () -> Unit,
    onPopularSeeAllClick: () -> Unit,
    onTopicDetailListClick: (String?) -> Unit,
    onPopularClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onUserPhotoClick: () -> Unit,
    onRandomImageClick: (String?) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeUiState by viewModel.homeListState.collectAsStateWithLifecycle()
    val authUserProfileImage by viewModel.userProfileImage.collectAsStateWithLifecycle()
    val appName = stringResource(id = R.string.app_name)
    var visibleChars by remember { mutableIntStateOf(0) }
    var animatedImageRotateCardVisibility by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.handleScreenEvents(HomeScreenEvent.FetchMainScreenUserData)
        viewModel.handleScreenEvents(HomeScreenEvent.FetchHomeScreenLists)
        appName.forEachIndexed { index, _ ->
            delay(300)
            visibleChars = index + 1
        }
    }

    LaunchedEffect(homeUiState) {
        delay(2000)
        animatedImageRotateCardVisibility = when(homeUiState) {
            is HomeUIState.Error -> false
            is HomeUIState.Loading -> false
            is HomeUIState.Success -> true
        }
        delay(10000)
        animatedImageRotateCardVisibility = false
    }

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            IconButton(
                onClick = { onUserPhotoClick.invoke() },
                modifier = Modifier
                    .wrapContentSize()
                    .weight(1f)
            ) {
                AsyncImage(
                    model = if (authUserProfileImage?.isNotEmpty() == true) authUserProfileImage else WalliesIcons.DefaultAvatar,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "Profile Image",
                    modifier = Modifier
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
                modifier = Modifier
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
            Column(modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize()) {
                AnimatedImageRotationCard(visible = animatedImageRotateCardVisibility)
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
                    onPopularSeeAllClick = {
                        onPopularSeeAllClick.invoke()
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
    onTopicDetailListClick: (String?) -> Unit,
    onPopularClick: (String) -> Unit,
    onRandomImageClick: (String?) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (homeUiState) {
            is HomeUIState.Loading -> CircularProgressIndicator()
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

                    if (homeUiState.random.isNotEmpty()) {
                        item(key = "randomContainer") {
                            HomeRandomPage(
                                randomImageList = homeUiState.random,
                                onRandomImageClick = onRandomImageClick
                            )
                        }
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeightIn(max = 280.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val chunkedList = topicsList.chunked(2)

            chunkedList.forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach { item ->
                        TopicTitleView(
                            imageUrl = item.titleBackground,
                            title = item.title,
                            imageName = item.title,
                            onTopicDetailListClick = {
                                onTopicDetailListClick.invoke(it)
                            },
                            modifier = Modifier
                                .weight(1f),
                        )
                    }
                    if (rowItems.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
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

        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .horizontalScroll(state = rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            popularList.forEach { popularImage ->
                HomeListImage (
                    animatedVisibilityScope = animatedVisibilityScope,
                    id = popularImage.id,
                    imageUrl = popularImage.url,
                    imageName = popularImage.imageDesc,
                    onImageClick = {
                        onPopularClick.invoke(it)
                    })
            }
        }
    }
}

@Composable
private fun TopicTitleView(
    imageUrl: String?,
    title: String?,
    imageName: String?,
    onTopicDetailListClick: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(88.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onTopicDetailListClick.invoke(title) }
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = imageName,
            contentScale = ContentScale.FillBounds,
            loading = { ImageLoadingState() },
            error = {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text("Image not available", Modifier.align(Alignment.Center))
                }
            },
            modifier = Modifier.align(Alignment.Center)
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.5f))
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
private fun SharedTransitionScope.HomeListImage(
    id: String?,
    imageUrl: String?,
    imageName: String?,
    onImageClick: (String) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val sharedContentState = rememberSharedContentState(key = "$SharedTransitionKey$id")

    with(animatedVisibilityScope) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = imageName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .sharedBounds(
                    sharedContentState = sharedContentState,
                    animatedVisibilityScope = this
                )
                .height(240.dp)
                .aspectRatio(ratio = 2 / 3f)
                .clip(CircleShape.copy(all = CornerSize(16.dp)))
                .clickable { onImageClick.invoke(id.orEmpty()) },
            loading = { ImageLoadingState() },
        )
    }
}

const val SharedTransitionKey = "mainImage-"