package com.oguzdogdu.walliescompose.features.authenticateduser

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.authenticateduser.changeprofilephoto.ChangeProfilePhotoDialog
import com.oguzdogdu.walliescompose.features.settings.components.MenuRowItems
import com.oguzdogdu.walliescompose.ui.theme.Background
import com.oguzdogdu.walliescompose.ui.theme.BackgroundDark
import com.oguzdogdu.walliescompose.ui.theme.bold
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.util.MenuRow
import com.oguzdogdu.walliescompose.util.ReusableMenuRow
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.BalloonWindow
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.compose.setBackgroundColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.AuthenticatedUserScreenRoute(
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    viewModel: AuthenticatedUserViewModel = hiltViewModel(),
    navigateBack:() -> Unit,
    navigateToLogin: () -> Unit,
    navigateToChangeNameAndSurname: () -> Unit,
    navigateToChangePassword: () -> Unit,
    navigateToChangeEmail: () -> Unit,
    ) {

    val userState by viewModel.userState.collectAsStateWithLifecycle()
    val dialogState by viewModel.changeProfilePhotoBottomSheetOpenStat.collectAsStateWithLifecycle()

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                imageUri = it
            }
        }
    )

    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        viewModel.handleUiEvents(AuthenticatedUserEvent.CheckUserAuth)
        viewModel.handleUiEvents(AuthenticatedUserEvent.FetchUserInfos)
    }

    BackHandler(enabled = true) {
        navigateBack.invoke()
    }

    LaunchedEffect(imageUri) {
        if (imageUri != null) {
            viewModel.setInstantlyProfileImageToDialog(imageUri)
        }
    }

    Scaffold(modifier = modifier
        .fillMaxSize(), topBar = {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navigateBack.invoke() },
                modifier = modifier.wrapContentSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = modifier.wrapContentSize()
                )
            }

            Text(
                modifier = modifier,
                text = stringResource(id = R.string.profile_title),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 16.sp,
                fontFamily = medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start
            )
        }
    }) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
        ) {
            AuthenticatedUserScreenContent(
                animatedVisibilityScope = animatedVisibilityScope,
                userInfoState = userState,
                modifier = modifier,
                onSignOutClick = {
                    viewModel.handleUiEvents(AuthenticatedUserEvent.SignOut)
                    navigateToLogin.invoke()
                },
                onChangeProfilePhotoClick = { dialog ->
                    viewModel.handleUiEvents(
                        AuthenticatedUserEvent.OpenChangeProfileBottomSheet(
                            isOpen = dialog
                        )
                    )
                }, onProfilePhotoClick = {
                    galleryLauncher.launch("image/*")
                }, onChangeProfilePhotoButtonClick = {
                    viewModel.handleUiEvents(AuthenticatedUserEvent.ChangeProfileImage(photoUri = imageUri))
                } ,dismissDialog = {dialog ->
                    viewModel.handleUiEvents(
                        AuthenticatedUserEvent.OpenChangeProfileBottomSheet(
                            dialog
                        )
                    )
                }, onChangeNameAndSurnameClick = {
                    navigateToChangeNameAndSurname.invoke()
                }, onChangePasswordClick = {
                    navigateToChangePassword.invoke()
                }, onChangeEmailClick = {
                    navigateToChangeEmail.invoke()
                }, showDialog = dialogState
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.AuthenticatedUserScreenContent(
    animatedVisibilityScope: AnimatedVisibilityScope,
    userInfoState: UserInfoState,
    modifier: Modifier = Modifier,
    onSignOutClick: () -> Unit,
    onChangeProfilePhotoClick: (Boolean) -> Unit,
    onProfilePhotoClick: () -> Unit,
    onChangeProfilePhotoButtonClick: () -> Unit,
    dismissDialog: (Boolean) -> Unit,
    onChangeNameAndSurnameClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onChangeEmailClick: () -> Unit,
    showDialog: Boolean
) {
    val isAuthenticated =
        rememberUpdatedState(newValue = userInfoState.isAuthenticatedWithFirebase or userInfoState.isAuthenticatedWithGoogle)
    if (!isAuthenticated.value) {
        UserNotAuthenticatedInfo()
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            AuthenticatedUserWelcomeCard(
                animatedVisibilityScope = animatedVisibilityScope,
                userInfoState = userInfoState,
                onChangeProfilePhotoClick = {
                    onChangeProfilePhotoClick.invoke(it)
                }
            )
            EditProfileInformationContent(
                modifier = modifier,
                onChangeNameAndSurnameClick = {
                    onChangeNameAndSurnameClick.invoke()
                },
                onChangePasswordClick = {
                    onChangePasswordClick.invoke()
                },
                onChangeEmailClick = {
                    onChangeEmailClick.invoke()
                }
            )
        }
        ChangeProfilePhotoDialog(
            userInfoState = userInfoState,
            modifier = modifier,
            isOpen = showDialog,
            onDismiss = {
                dismissDialog.invoke(false)
            }, onProfilePhotoClick = {
                onProfilePhotoClick.invoke()
            }, onChangeProfilePhotoButtonClick = {
                onChangeProfilePhotoButtonClick.invoke()
            })
        Button(
            onClick = {
                onSignOutClick.invoke()
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(
                text = stringResource(R.string.sign_out),
                fontSize = 14.sp,
                fontFamily = medium,
                color = Color.White
            )
        }
    }
}

@Composable
fun UserNotAuthenticatedInfo(
    modifier: Modifier = Modifier,
) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.user_not_auth_caution),fontSize = 14.sp,
                fontFamily = medium)
        }
    }

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationApi::class)
@Composable
fun SharedTransitionScope.AuthenticatedUserWelcomeCard(
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    userInfoState: UserInfoState,
    onChangeProfilePhotoClick: (Boolean) -> Unit
) {
    Card(modifier = modifier
        .wrapContentHeight()
        .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = Color.Transparent,
            disabledContentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ).copy(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {

        Column(
            modifier = modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = userInfoState.profileImage, transitionSpec = {
                    (expandIn(tween(1000)))
                        .togetherWith(shrinkOut(tween(1000)))
                },
                label = ""
            ) { image ->
                AsyncImage(
                    model = image,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "Profile Image",
                    modifier = modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(64.dp))
                        .clickable {
                            onChangeProfilePhotoClick.invoke(true)
                        },
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                buildAnnotatedString {
                    append(stringResource(id = R.string.welcome_profile))
                    withStyle(style = SpanStyle(fontFamily = bold)) {
                        append(", ${userInfoState.name.orEmpty()} ${userInfoState.surname.orEmpty()} \uD83D\uDD90")
                    }
                },
                fontSize = 16.sp,
                fontFamily = medium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun EditProfileInformationContent(
    modifier: Modifier,
    onChangeNameAndSurnameClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onChangeEmailClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val profileOptionsList = immutableListOf(
        MenuRow(
            titleRes = R.string.edit_user_info_title,
            icon = R.drawable.ic_person
        ),
        MenuRow(
            titleRes = R.string.edit_email_title,
            icon = R.drawable.ic_email
        ),
        MenuRow(
            titleRes = R.string.forgot_password_title,
            icon = R.drawable.password
        )
    )
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp)
    ) {
        items(count = 1) { index: Int ->
            ReusableMenuRow(data = profileOptionsList,
                index = index,
                modifier = modifier.fillMaxWidth(),
                itemContent = { menu ->
                    MenuRowItems(
                        modifier = modifier, menuRow = menu,arrow = true
                    )
                }
            ) {
                handleMenuItemClick(
                    itemIndex = it,
                    coroutineScope = scope,
                    openPersonalInformation = {
                        onChangeNameAndSurnameClick.invoke()
                    },
                    openEditEmail = {
                        onChangeEmailClick.invoke()
                    }, openChangePassword = {
                        onChangePasswordClick.invoke()
                    }
                )
            }
        }
    }
}

fun handleMenuItemClick(
    itemIndex: Int,
    coroutineScope: CoroutineScope,
    openPersonalInformation: () -> Unit,
    openEditEmail: () -> Unit,
    openChangePassword: () -> Unit,
) {
    coroutineScope.launch {
        when (itemIndex) {
            0 -> {
                openPersonalInformation.invoke()
            }

            1 -> {
                openEditEmail.invoke()
            }

            2 -> {
                openChangePassword.invoke()

            }
        }
    }
}