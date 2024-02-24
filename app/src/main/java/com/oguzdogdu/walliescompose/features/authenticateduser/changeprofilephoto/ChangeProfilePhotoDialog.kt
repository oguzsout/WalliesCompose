package com.oguzdogdu.walliescompose.features.authenticateduser.changeprofilephoto

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.ShapeDefaults
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.SecureFlagPolicy
import coil.compose.AsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.authenticateduser.AuthenticatedUserScreenState
import com.oguzdogdu.walliescompose.ui.theme.medium
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeProfilePhotoDialog(
    userInfos: AuthenticatedUserScreenState.UserInfos,
    modifier: Modifier,
    isOpen: Boolean,
    profilePhotoUri:Uri?,
    onDismiss: () -> Unit,
    onProfilePhotoClick: () -> Unit,
    onChangeProfilePhotoButtonClick: () -> Unit,
) {
    var openBottomSheet by remember { mutableStateOf(isOpen) }
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
                BottomSheetContent(userInfos = userInfos,
                    modifier = modifier.navigationBarsPadding(),
                    profilePhotoUri = profilePhotoUri,
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
    userInfos: AuthenticatedUserScreenState.UserInfos,
    modifier: Modifier,
    profilePhotoUri:Uri?,
    onProfilePhotoClick: () -> Unit,
    onChangeProfilePhotoButtonClick: () -> Unit,
) {
    Column(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = profilePhotoUri ?: userInfos.profileImage,
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
                    text = stringResource(id = R.string.send_info),
                    fontSize = 14.sp,
                    fontFamily = medium,
                    color = Color.Black
                )
            }
        }
    }