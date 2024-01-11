package com.oguzdogdu.walliescompose.features.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.data.common.LoadingState
import com.oguzdogdu.walliescompose.domain.model.topics.Topics
import com.oguzdogdu.walliescompose.features.component.BaseCenteredToolbar
import com.oguzdogdu.walliescompose.features.home.event.HomeScreenEvent
import com.oguzdogdu.walliescompose.features.home.state.HomeScreenState
import com.oguzdogdu.walliescompose.navigation.utils.WalliesIcons
import com.oguzdogdu.walliescompose.ui.theme.medium

@Composable
fun HomeScreenRoute(modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel()) {

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.handleScreenEvents(HomeScreenEvent.FetchHomeScreenLists)
    }
    val homeUiState by viewModel.homeListState.collectAsStateWithLifecycle()

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        BaseCenteredToolbar(modifier = Modifier,
            title = stringResource(id = R.string.app_name),
            imageLeft = painterResource(WalliesIcons.DefaultAvatar),
            imageRight = painterResource(WalliesIcons.SearchIcon),
            imageLeftTint = MaterialTheme.colorScheme.secondary,
            imageRightTint = MaterialTheme.colorScheme.secondary,
            leftClick = {
                Toast.makeText(context, "Nav Icon Click", Toast.LENGTH_SHORT).show()
            },
            rightClick = {
                Toast.makeText(context, "Add Click", Toast.LENGTH_SHORT).show()
            })
    }) {
        LazyColumn(
            modifier = modifier
                .padding(it)
                .fillMaxHeight()
        ) {

            item {
                TopicLayoutContainer(modifier = modifier, homeUiState = homeUiState)
            }
            item {
                PopularLayoutContainer(
                    modifier = modifier, homeUiState = homeUiState
                )
            }

            item {
                LatestLayoutContainer(modifier = modifier, homeUiState = homeUiState)
            }
        }
    }
}


@Composable
private fun TopicLayoutContainer(modifier: Modifier, homeUiState: HomeScreenState) {
    var visible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = homeUiState.topics) {
        if (homeUiState.topics.isNotEmpty()) {
            visible = true
        }
    }

    AnimatedVisibility(
            visible = visible,
            enter = expandHorizontally { 50 },
            exit = shrinkHorizontally(
                animationSpec = tween(),
                shrinkTowards = Alignment.End,
            )
        ) {
            Column(
                modifier = modifier
                    .wrapContentSize()
                    .padding(horizontal = 8.dp),
            ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.topics_title),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = modifier.padding(start = 8.dp, top = 16.dp, bottom = 8.dp)
                )

                Text(
                    text = stringResource(id = R.string.show_all),
                    fontFamily = medium,
                    fontSize = 12.sp,
                    color = Color.Unspecified,
                    modifier = modifier.padding(end = 8.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(184.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(homeUiState.topics, key = { index: Int, item: Topics ->
                    item.id.orEmpty()
                }) { index, item ->
                    TopicTitleView(imageUrl = item.titleBackground, title = item.title)
                }
            }
        }
    }
}

@Composable
private fun PopularLayoutContainer(modifier: Modifier, homeUiState: HomeScreenState) {
    var visible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = homeUiState.popular) {
        if (homeUiState.popular.isNotEmpty()) {
            visible = true
        }
    }
    AnimatedVisibility(
        visible = visible,
        enter = expandHorizontally { 50 },
        exit = shrinkHorizontally(
            animationSpec = tween(),
            shrinkTowards = Alignment.End,
        )
    ) {
        Column(
            modifier = modifier
                .wrapContentSize()
                .padding(horizontal = 8.dp),
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
                    modifier = modifier.padding(start = 8.dp, top = 16.dp, bottom = 8.dp)
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
                items(homeUiState.popular, key = {
                    it.id.orEmpty()
                }) {
                    PopularImageView(imageUrl = it.url)

                }
            })
        }
    }
}

@Composable
private fun LatestLayoutContainer(modifier: Modifier, homeUiState: HomeScreenState) {
    var visible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = homeUiState.latest) {
        if (homeUiState.latest.isNotEmpty()) {
            visible = true
        }
    }
    AnimatedVisibility(
        visible = visible,
        enter = expandHorizontally { 50 },
        exit = shrinkHorizontally(
            animationSpec = tween(),
            shrinkTowards = Alignment.End,
        )
    ) {
        Column(
            modifier = modifier
                .wrapContentSize()
                .padding(horizontal = 8.dp),
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
                    modifier = modifier.padding(start = 8.dp, top = 16.dp, bottom = 8.dp)
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
                items(homeUiState.latest, key = {
                    it.id.orEmpty()
                }) {
                    LatestImageView(imageUrl = it.url)

                }
            })
        }
    }
}

@Composable
private fun TopicTitleView(imageUrl: String?, title: String?) {
    Box(
        modifier = Modifier.wrapContentSize()
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
            loading = { LoadingState() },
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
private fun PopularImageView(imageUrl: String?) {
    Box(
        modifier = Modifier.wrapContentSize()
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .width(150.dp)
                .clip(CircleShape.copy(all = CornerSize(16.dp))),
            loading = { LoadingState() },
        )
    }
}

@Composable
private fun LatestImageView(imageUrl: String?) {
    Box(
        modifier = Modifier.wrapContentSize()
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .width(150.dp)
                .clip(CircleShape.copy(all = CornerSize(16.dp))),
            loading = { LoadingState() },
        )
    }
}