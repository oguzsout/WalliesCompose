package com.oguzdogdu.walliescompose.features.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.components.BaseCenteredToolbar
import com.oguzdogdu.walliescompose.features.home.state.HomeScreenState

@Composable
fun HomeScreenRoute(modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel()) {
    val homeUiState by viewModel.homeListState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    Scaffold(modifier = modifier, topBar = {
        BaseCenteredToolbar(
                    modifier = Modifier,
                    title = stringResource(id = R.string.app_name),
                    imageLeft = painterResource(id = R.drawable.ic_default_avatar),
                    imageRight = painterResource(id = R.drawable.ic_completed),
                    imageLeftTint = MaterialTheme.colorScheme.secondary,
                    imageRightTint = MaterialTheme.colorScheme.secondary,
                    leftClick = {
                        Toast.makeText(context, "Nav Icon Click", Toast.LENGTH_SHORT).show()
                    },
                    rightClick = {
                        Toast.makeText(context, "Add Click", Toast.LENGTH_SHORT).show()
                    }
                )
            }) {
        Column(modifier = modifier.padding(it)) {
            Text(
                text = stringResource(id = R.string.topics_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = modifier.padding(start = 8.dp, top = 8.dp)
            )
            MainLayout(
                    modifier = modifier.padding(top = 16.dp),
                    homeUiState = homeUiState,
                )

        }

    }
}

@Composable
private fun MainLayout(modifier: Modifier, homeUiState: HomeScreenState?) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (homeUiState) {
                is HomeScreenState.TopicsTitleList -> {
                    if (homeUiState.loading) {
                        CircularProgressIndicator()
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 160.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        content = {
                            items(homeUiState.topics) {
                                TopicTitleView(imageUrl = it.titleBackground, title = it.title)

                            }
                        }
                    )
                }

                null -> {

                }
            }
        }
    }
}

@Composable
private fun TopicTitleView(imageUrl:String?, title:String?) {
    Box(
        modifier = Modifier
            .wrapContentSize()
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .clip(CircleShape.copy(all = CornerSize(16.dp))),
        )

        Text(
            text = title.orEmpty(),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            color = MaterialTheme.colorScheme.background
        )
    }
}