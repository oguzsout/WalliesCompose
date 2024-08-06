package com.oguzdogdu.walliescompose.features.authenticateduser.changeprofilephoto

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.authenticateduser.UserInfoState
import com.oguzdogdu.walliescompose.ui.theme.medium
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeProfilePhotoDialog(
    userInfoState: UserInfoState,
    modifier: Modifier,
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onProfilePhotoClick: () -> Unit,
    onChangeProfilePhotoButtonClick: () -> Unit,
) {
    var openBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = isOpen) {
        openBottomSheet = isOpen
    }

    if (openBottomSheet) {

        ModalBottomSheet(
            modifier = modifier,
            sheetState = bottomSheetState,
            onDismissRequest = {
                scope.launch { bottomSheetState.hide() }
                    .invokeOnCompletion { openBottomSheet = false }
                onDismiss.invoke()
            },
            dragHandle = {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetDefaults.DragHandle()
                    Icon(
                        painter = painterResource(id = R.drawable.ic_default_avatar),
                        contentDescription = ""
                    )
                    Spacer(modifier = modifier.size(8.dp))
                    Text(
                        text = stringResource(R.string.change_profile_photo_title),
                        fontSize = 14.sp,
                        fontFamily = medium,
                        color = Color.Unspecified,
                        maxLines = 3,
                        textAlign = TextAlign.Center,
                        lineHeight = TextUnit(24f, TextUnitType.Sp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                }
            }
        ) {
            Column(
                modifier
                    .wrapContentHeight(),
            ) {
                BottomSheetContent(
                    userInfoState = userInfoState,
                    modifier = modifier.navigationBarsPadding(),
                    onProfilePhotoClick = {
                        onProfilePhotoClick.invoke()
                    },
                    onChangeProfilePhotoButtonClick = {
                        onChangeProfilePhotoButtonClick.invoke()
                    })
            }
        }
    }
}

@Composable
fun BottomSheetContent(
    userInfoState: UserInfoState,
    onProfilePhotoClick: () -> Unit,
    onChangeProfilePhotoButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AnimatedContent(
            targetState = userInfoState.photoUri,
            transitionSpec = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessVeryLow
                    ),
                ) {
                    it
                } togetherWith slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(1000, easing = EaseOutQuad)
                )
            },
            label = ""
        ) { uri ->
            AsyncImage(
                model = uri ?: userInfoState.profileImage,
                contentScale = ContentScale.FillBounds,
                contentDescription = "Profile Image",
                modifier = modifier
                    .height(120.dp)
                    .width(120.dp)
                    .clip(RoundedCornerShape(64.dp))
                    .clickable {
                        onProfilePhotoClick.invoke()
                    }
            )
        }

        Button(
            onClick = {
                onChangeProfilePhotoButtonClick.invoke()
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(
                text = stringResource(R.string.change_button_text),
                fontSize = 14.sp,
                fontFamily = medium,
                color = Color.Black
            )
        }
    }
}