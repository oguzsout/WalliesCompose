package com.oguzdogdu.walliescompose.features.search.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.domain.model.userpreferences.UserPreferences
import com.oguzdogdu.walliescompose.ui.theme.regular
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CancelableChipList(
    userPreferences: List<UserPreferences>,
    onDeleteClick: (String?) -> Unit,
    onSearchWithQuery: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var isLongPressed by remember { mutableStateOf(false) }
    val removedChip = remember { mutableStateOf<String?>(null) }

    LazyRow(
        modifier = modifier.padding(horizontal = 8.dp),
        state = rememberLazyListState(),
    ) {
        items(items = userPreferences, key = { it.hashCode() }) { list ->
            val visible = remember { mutableStateOf(true) }
            LaunchedEffect(removedChip.value) {
                if (removedChip.value == list.keyword) {
                    visible.value = false
                }
            }
            AnimatedVisibility(
                visible = visible.value,
                enter = scaleIn(tween(500)),
                exit = scaleOut(tween(500)),
            ) {
                KeywordChip(
                    userPreferences = list,
                    isLongPressed = isLongPressed,
                    onClick = {
                        onSearchWithQuery.invoke(it)
                    },
                    onDeleteClick = {
                        coroutineScope.launch {
                            removedChip.value = it
                            delay(300)
                            onDeleteClick.invoke(it)
                        }
                    },
                    onLongPress = {
                        isLongPressed = !isLongPressed
                    }
                )
            }
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun KeywordChip(
    userPreferences: UserPreferences,
    isLongPressed: Boolean,
    onClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val shakeOffset by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ), shape = RoundedCornerShape(32.dp), border = BorderStroke(1.dp, Color.LightGray),
        modifier = modifier
            .combinedClickable(
                onClick = { onClick(userPreferences.keyword.orEmpty()) },
                onLongClick = onLongPress
            )
            .graphicsLayer {
                this.rotationZ = if (isLongPressed) shakeOffset.dp.value else 0.dp.value
            }
    ) {
        Row(
            modifier = Modifier
                .animateContentSize()
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = userPreferences.keyword.orEmpty(),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = regular,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
                modifier = Modifier.padding(4.dp)
            )
            androidx.compose.animation.AnimatedVisibility(
                visible = isLongPressed,
                enter = expandHorizontally(tween(500)),
                exit = shrinkHorizontally(tween(200))
            ) {
                Icon(
                    modifier = Modifier
                        .clickable {
                            onDeleteClick.invoke(userPreferences.keyword.orEmpty())
                        }
                        .background(Color.LightGray, RoundedCornerShape(16.dp)),
                    painter = painterResource(id = R.drawable.round_clear_24),
                    tint = Color.White,
                    contentDescription = ""
                )
            }
        }
    }
}