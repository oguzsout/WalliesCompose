package com.oguzdogdu.walliescompose.features.popular.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.features.home.LoadingState
import com.oguzdogdu.walliescompose.features.popular.boundsTransform
import com.oguzdogdu.walliescompose.ui.theme.regular

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PopularEditDetails(
    popularImage: PopularImage?,
    modifier: Modifier = Modifier,
    onConfirmClick: () -> Unit
) {
    val context = LocalContext.current

    AnimatedContent(
        modifier = modifier.padding(8.dp),
        targetState = popularImage,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "PopularEditDetails"
    ) { popular ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (popular != null) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "${popular.id}-bounds"),
                            animatedVisibilityScope = this@AnimatedContent,
                            clipInOverlayDuringTransition = OverlayClip(
                                RoundedCornerShape(16.dp)
                            )
                        )
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "${popular.id}-bounds"),
                                animatedVisibilityScope = this@AnimatedContent,
                                clipInOverlayDuringTransition = OverlayClip(
                                    RoundedCornerShape(16.dp)
                                ),
                            )
                            .clickable {
                                onConfirmClick.invoke()
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            AsyncImage(
                                model = popular.profileImage,
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .height(48.dp)
                                    .width(48.dp)
                                    .clip(RoundedCornerShape(64.dp))
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = popular.name.orEmpty(),
                                fontSize = 16.sp,
                                fontFamily = regular,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(popularImage?.highResolution)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .memoryCachePolicy(CachePolicy.ENABLED)
                                .networkCachePolicy(CachePolicy.ENABLED)
                                .build(),
                            contentDescription = popular.imageDesc,
                            contentScale = ContentScale.Inside,
                            modifier = Modifier
                                .fillMaxWidth(),
                           loading = {
                               LoadingState()
                           }
                        )
                    }
                }
            }
        }
    }
}