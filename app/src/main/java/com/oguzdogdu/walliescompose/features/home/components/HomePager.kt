package com.oguzdogdu.walliescompose.features.home.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.data.common.ImageLoadingState
import com.oguzdogdu.walliescompose.domain.model.random.RandomImage
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular
import com.oguzdogdu.walliescompose.util.capitalizeFirstLetter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun HomeRandomPage(
    randomImageList: List<RandomImage>,
    onRandomImageClick: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
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
                isSelected = pagerState.currentPage == index,
                onRandomImageClick = onRandomImageClick
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
    onRandomImageClick: (String?) -> Unit,
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
            }
            .clickable {
                onRandomImageClick.invoke(randomImage.id.orEmpty())
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
                    .padding(8.dp), enter = slideInVertically(
                    spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ), exit = slideOutVertically(
                    spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
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