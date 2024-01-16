package com.oguzdogdu.walliescompose.features.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.vector.rememberVectorPainter
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
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular
import com.oguzdogdu.walliescompose.util.formatDate
import com.oguzdogdu.walliescompose.util.toFormattedString


@Composable
fun DetailScreenRoute(
    modifier: Modifier = Modifier,
    detailViewModel: DetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by detailViewModel.photo.collectAsStateWithLifecycle()
    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        detailViewModel.handleScreenEvents(DetailScreenEvent.GetPhotoDetails)
    }
    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 16.dp, bottom = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Icon(painter = painterResource(id = R.drawable.back),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = modifier
                    .wrapContentSize()
                    .clickable {
                        onBackClick.invoke()
                    })

            Text(
                text = state.detail?.desc.orEmpty(),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 12.sp,
                fontFamily = medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(painter = painterResource(id = R.drawable.info),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = modifier
                    .wrapContentSize()
                    .clickable {

                    })
        }
    }) {
        Column(
            modifier = modifier
                .padding(it)
                .wrapContentSize(),
        ) {
            SubcomposeAsyncImage(
                model = state.detail?.highQuality,
                contentDescription = "",
                modifier = modifier
                    .fillMaxWidth()
                    .weight(0.5f),
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = modifier.size(32.dp))
            PostView(modifier = modifier, state = state, onFavoriteClick = {})
        }
    }
}

@Composable
fun PostView(modifier: Modifier, state: DetailState, onFavoriteClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ), shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
        ), elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ), modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp)
            ) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        AsyncImage(
                            model = state.detail?.profileimage,
                            contentDescription = "Profile Image",
                            modifier = modifier
                                .height(48.dp)
                                .width(48.dp)
                                .clip(RoundedCornerShape(64.dp))

                        )
                        Column(
                            modifier = modifier
                                .wrapContentSize()
                                .padding(start = 8.dp)
                        ) {
                            Text(
                                text = state.detail?.username.orEmpty(),
                                fontFamily = medium,
                                fontSize = 16.sp
                            )
                            Text(
                                text = state.detail?.portfolio.orEmpty(),
                                fontFamily = regular,
                                fontSize = 12.sp
                            )
                        }
                    }

                    IconButton(onClick = {

                    }, modifier = modifier
                            .wrapContentSize()
                            .padding(end = 8.dp)
                            .clickable {
                                onFavoriteClick.invoke()
                            }) {
                        val tintColor = if (false) Red else Gray

                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = rememberVectorPainter(Icons.Default.Favorite),
                            contentDescription = null,
                            tint = tintColor
                        )
                    }
                }
            }
            Divider(
                modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 16.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                thickness = 0.5.dp
            )
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.views_text),
                        fontFamily = regular,
                        fontSize = 12.sp
                    )
                    Text(
                        text = state.detail?.views?.toFormattedString().orEmpty(),
                        fontFamily = regular,
                        fontSize = 12.sp
                    )
                }
                Column(
                    modifier = modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.download_text),
                        fontFamily = regular,
                        fontSize = 12.sp
                    )
                    Text(
                        text = state.detail?.downloads?.toFormattedString().orEmpty(),
                        fontFamily = regular,
                        fontSize = 12.sp
                    )
                }
                Column(
                    modifier = modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.created_at),
                        fontFamily = regular,
                        fontSize = 12.sp
                    )
                    Text(
                        text = state.detail?.createdAt?.formatDate().orEmpty(),
                        fontFamily = regular,
                        fontSize = 12.sp
                    )
                }
                Column(
                    modifier = modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.like_text),
                        fontFamily = regular,
                        fontSize = 12.sp
                    )
                    Text(
                        text = state.detail?.likes?.toFormattedString().orEmpty(),
                        fontFamily = regular,
                        fontSize = 12.sp
                    )
                }
            }
            Divider(
                modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 16.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                thickness = 0.5.dp
            )
            LazyRow(
                contentPadding = PaddingValues(8.dp),
            ) {
                items(state.detail?.tag.orEmpty(), key = { item ->
                    item.hashCode()
                }) { item ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        ),
                        shape = CircleShape.copy(CornerSize(16.dp)),
                        modifier = modifier
                            .wrapContentSize()
                            .padding(4.dp),
                    ) {
                        Text(
                            text = item.orEmpty(),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = modifier.padding(8.dp),
                            fontFamily = regular,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.wallpaper),
                        tint = Color.White,
                        contentDescription = ""
                    )
                    Spacer(modifier = modifier.padding(horizontal = 8.dp))
                    Text(
                        text = stringResource(id = R.string.set_wallpaper_text),
                        color = Color.White,
                        fontFamily = regular,
                        fontSize = 12.sp
                    )
                }
                Row {
                    Icon(painter = painterResource(id = R.drawable.share), contentDescription = "")
                    Spacer(modifier = modifier.padding(horizontal = 8.dp))
                    Icon(painter = painterResource(id = R.drawable.download), contentDescription = "")
            }
        }
    }
}