package com.oguzdogdu.walliescompose.features.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.data.common.ImageLoadingState
import com.oguzdogdu.walliescompose.domain.model.topics.Topics
import com.oguzdogdu.walliescompose.features.component.BaseCenteredToolbar
import com.oguzdogdu.walliescompose.features.home.event.HomeScreenEvent
import com.oguzdogdu.walliescompose.features.home.state.HomeScreenState
import com.oguzdogdu.walliescompose.navigation.utils.WalliesIcons
import com.oguzdogdu.walliescompose.ui.theme.medium

@Composable
fun HomeScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onTopicSeeAllClick: () -> Unit,
    onTopicDetailListClick: (String?) -> Unit,
    onLatestClick: (String) -> Unit,
    onPopularClick: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    val context = LocalContext.current

    val homeUiState by viewModel.homeListState.collectAsStateWithLifecycle()

    var itemsLoad by remember {
        mutableStateOf(false)
    }
    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.handleScreenEvents(HomeScreenEvent.FetchHomeScreenLists)
    }

    LaunchedEffect(homeUiState) {
        if (homeUiState.topics.isNotEmpty() && homeUiState.popular.isNotEmpty() && homeUiState.latest.isNotEmpty()) {
            itemsLoad = !itemsLoad
        }
    }


    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        BaseCenteredToolbar(modifier = modifier,
            title = stringResource(id = R.string.app_name),
            imageLeft = painterResource(WalliesIcons.DefaultAvatar),
            imageRight = painterResource(WalliesIcons.SearchIcon),
            imageLeftTint = MaterialTheme.colorScheme.secondary,
            imageRightTint = MaterialTheme.colorScheme.secondary,
            leftClick = {
                Toast.makeText(context, "Nav Icon Click", Toast.LENGTH_SHORT).show()
            },
            rightClick = {
                onSearchClick.invoke()
            })
    }) { it ->
        Box(modifier = modifier
            .padding(it)
            .fillMaxSize(), contentAlignment = Alignment.Center) {

            if (homeUiState.loading) {
                LoadingState(modifier = modifier)
            }
            if (itemsLoad) {
                HomeScreenContent(
                    homeUiState = homeUiState,
                    modifier = modifier,
                    onTopicSeeAllClick = {
                        onTopicSeeAllClick.invoke()
                    },
                    onTopicDetailListClick = {id->
                        onTopicDetailListClick.invoke(id)
                    },
                    onPopularClick = { id ->
                        onPopularClick.invoke(id)
                    },
                    onLatestClick = { id -> onLatestClick.invoke(id) }
                )
            }
        }
    }
}

@Composable
fun HomeScreenContent(
    homeUiState: HomeScreenState,
    modifier: Modifier,
    onTopicSeeAllClick: () -> Unit,
    onTopicDetailListClick: (String?) -> Unit,
    onPopularClick: (String) -> Unit,
    onLatestClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        item(key = 0) {
            TopicLayoutContainer(
                modifier = modifier,
                homeScreenState = homeUiState,
                onTopicDetailListClick =  {
                                  onTopicDetailListClick.invoke(it)
                },
                onTopicSeeAllClick = {
                    onTopicSeeAllClick.invoke()
                })
        }
        item(key = 1) {
            PopularLayoutContainer(modifier = modifier,
                homeScreenState = homeUiState,
                onPopularClick = { id ->
                    onPopularClick.invoke(id)
                })
        }

        item(key = 2) {
            LatestLayoutContainer(modifier = modifier,
                homeScreenState = homeUiState,
                onLatestClick = { id -> onLatestClick.invoke(id) })

        }
    }
}


@Composable
private fun TopicLayoutContainer(modifier: Modifier, homeScreenState: HomeScreenState,onTopicDetailListClick: (String?) -> Unit,onTopicSeeAllClick: () -> Unit) {
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
            modifier = modifier.heightIn(min = 92.dp, max = 280.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(homeScreenState.topics, key = { index: Int, item: Topics ->
                item.id.hashCode()
            }) { index, item ->
                TopicTitleView(imageUrl = item.titleBackground, title = item.title, onTopicDetailListClick = {
                    onTopicDetailListClick.invoke(it)
                })
            }
        }
    }
}

@Composable
private fun PopularLayoutContainer(
    modifier: Modifier, homeScreenState: HomeScreenState, onPopularClick: (String) -> Unit
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
                modifier = modifier.padding(end = 8.dp, top = 16.dp, bottom = 8.dp)
            )
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), content = {
            items(homeScreenState.popular, key = {
                it.id.hashCode()
            }) { popularImage ->
                PopularImageView(id = popularImage.id,
                    imageUrl = popularImage.url,
                    onPopularClick = {
                        onPopularClick.invoke(it)
                    })
            }
        })
    }
}

@Composable
private fun LatestLayoutContainer(
    modifier: Modifier, homeScreenState: HomeScreenState, onLatestClick: (String) -> Unit
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
                modifier = modifier.padding(end = 8.dp, top = 16.dp, bottom = 8.dp)
            )
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), content = {
            items(homeScreenState.latest, key = {
                it.id.hashCode()
            }) { latestImage ->
                LatestImageView(id = latestImage.id,
                    imageUrl = latestImage.url,
                    onLatestClick = {
                        onLatestClick.invoke(it)
                    })
            }
        })
    }
}

@Composable
private fun TopicTitleView(imageUrl: String?, title: String?,onTopicDetailListClick: (String?) -> Unit) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .clickable {
                onTopicDetailListClick.invoke(title)
            }
    ) {

        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = null,
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

@Composable
private fun PopularImageView(id: String?, imageUrl: String?, onPopularClick: (String) -> Unit) {
    Box(modifier = Modifier
        .wrapContentSize()
        .clickable { onPopularClick.invoke(id.orEmpty()) }) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(CircleShape.copy(all = CornerSize(16.dp))),
            loading = { ImageLoadingState() },
        )
    }
}

@Composable
private fun LatestImageView(id: String?, imageUrl: String?, onLatestClick: (String) -> Unit) {
    Box(modifier = Modifier
        .wrapContentSize()
        .clickable { onLatestClick.invoke(id.orEmpty()) }) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(CircleShape.copy(all = CornerSize(16.dp))),
            loading = { ImageLoadingState() },
        )
    }
}