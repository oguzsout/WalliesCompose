package com.oguzdogdu.walliescompose.features.authenticateduser

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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

@Composable
fun AuthenticatedUserScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: AuthenticatedUserViewModel = hiltViewModel(),
    navigateBack:() -> Unit,
    navigateToLogin: () -> Unit,
    navigateToChangePassword: () -> Unit
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
                userScreenState = userState,
                modifier = modifier,
                profilePhotoUri = imageUri,
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
                                   viewModel.handleUiEvents(AuthenticatedUserEvent.OpenChangeProfileBottomSheet(dialog))
                }, onChangePasswordClick = {
                    navigateToChangePassword.invoke()
                },showDialog = dialogState)
        }
    }
}

@Composable
fun AuthenticatedUserScreenContent(
    userScreenState: AuthenticatedUserScreenState?,
    modifier: Modifier,
    profilePhotoUri: Uri?,
    onSignOutClick: () -> Unit,
    onChangeProfilePhotoClick: (Boolean) -> Unit,
    onProfilePhotoClick: () -> Unit,
    onChangeProfilePhotoButtonClick: () -> Unit,
    dismissDialog: (Boolean) -> Unit,
    onChangePasswordClick: () -> Unit,
    showDialog: Boolean
) {
    var isGoogleSign by remember {
        mutableStateOf(false)
    }
    when(userScreenState) {
                is AuthenticatedUserScreenState.CheckUserAuthenticated -> {
                    if (!userScreenState.isAuthenticated) {
                        UserNotAuthenticatedInfo(modifier = modifier, authenticatedUserScreenState = userScreenState)
                    }
                }
                is AuthenticatedUserScreenState.CheckUserGoogleSignIn -> {
                    isGoogleSign = userScreenState.isAuthenticated
                }
                AuthenticatedUserScreenState.Loading -> {

                }
                is AuthenticatedUserScreenState.UserInfoError -> {

                }
                is AuthenticatedUserScreenState.UserInfos -> {
                    Box(modifier = modifier
                        .fillMaxSize()
                        .navigationBarsPadding()) {
                        Column(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) {
                            AuthenticatedUserWelcomeCard(
                                modifier = modifier,
                                userInfos = userScreenState,
                                isGoogleSignIn = isGoogleSign,
                                onChangeProfilePhotoClick = {
                                    onChangeProfilePhotoClick.invoke(it)
                                }
                            )
                            EditProfileInformationContent(modifier = modifier, onChangePasswordClick = {
                                onChangePasswordClick.invoke()
                            })
                        }
                        ChangeProfilePhotoDialog(userInfos = userScreenState, modifier = modifier, profilePhotoUri = profilePhotoUri ,isOpen = showDialog, onDismiss = {
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
                                .align(Alignment.BottomCenter)
                            ,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            ),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(12.dp)
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

                null -> {

                }
            }
    }

@Composable
fun UserNotAuthenticatedInfo(
    modifier: Modifier,
    authenticatedUserScreenState: AuthenticatedUserScreenState.CheckUserAuthenticated
) {
    if (!authenticatedUserScreenState.isAuthenticated) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.user_not_auth_caution),fontSize = 14.sp,
                fontFamily = medium,)
        }
    }
}

@Composable
fun AuthenticatedUserWelcomeCard(
    modifier: Modifier,
    userInfos: AuthenticatedUserScreenState.UserInfos,
    isGoogleSignIn: Boolean,
    onChangeProfilePhotoClick: (Boolean) -> Unit
) {
    val openBottomSheetOfProfilePhotoChange by remember { mutableStateOf(false) }

    var balloonWindow: BalloonWindow? by remember { mutableStateOf(null) }

    val builder = rememberBalloonBuilder {
        setArrowSize(10)
        setArrowPosition(0.9f)
        setWidth(BalloonSizeSpec.WRAP)
        setHeight(BalloonSizeSpec.WRAP)
        setPadding(12)
        setMarginHorizontal(12)
        setCornerRadius(8f)
        setBackgroundColor(Color.DarkGray)
        setBalloonAnimation(BalloonAnimation.NONE)
    }
    Card(modifier = modifier
        .clickable {
            balloonWindow?.dismiss()
        }
        .wrapContentHeight()
        .fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
        Box(modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(), contentAlignment = Alignment.Center) {
            Balloon(
                    onBalloonWindowInitialized = { balloonWindow = it },
                    modifier = modifier
                        .align(Alignment.CenterEnd)
                        .padding(top = 24.dp),
                    builder = builder,
                    balloonContent = {
                        Text(
                            text = stringResource(id = R.string.show_info_edit_infos),
                            fontFamily = medium,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                ) {
                }

            Row(
                modifier = modifier
                    .padding(16.dp)
                    .wrapContentWidth()
                    .align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Box(
                    modifier = modifier
                        .size(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = userInfos.profileImage,
                        contentScale = ContentScale.FillBounds,
                        contentDescription = "Profile Image",
                        modifier = modifier
                            .height(48.dp)
                            .width(48.dp)
                            .clip(RoundedCornerShape(64.dp)),
                    )
                    if (!isGoogleSignIn){
                        Image(
                            painter = painterResource(id = R.drawable.ic_edit_photo),
                            contentDescription = "Profile Image Edit",
                            modifier = modifier
                                .height(20.dp)
                                .width(20.dp)
                                .align(Alignment.TopEnd)
                                .clip(RoundedCornerShape(32.dp))
                                .background(Color.White)
                                .clickable {
                                    onChangeProfilePhotoClick.invoke(!openBottomSheetOfProfilePhotoChange)
                                },
                        )
                    }
                }

                Text(
                    buildAnnotatedString {
                        append(stringResource(id = R.string.welcome_profile))
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(", ${userInfos.name.orEmpty()} \uD83D\uDD90 ")
                        }
                    },
                    fontSize = 14.sp,
                    fontFamily = medium,
                    modifier = modifier
                )
            }
            IconButton(
                onClick = { balloonWindow?.showAlignBottom()},
                modifier = modifier
                    .wrapContentSize()
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.info),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = modifier.wrapContentSize()
                )
            }
        }
    }
}

@Composable
fun EditProfileInformationContent(modifier: Modifier,onChangePasswordClick: () -> Unit) {
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
                        modifier = modifier, menuRow = menu
                    )
                },
                onClick = {
                    handleMenuItemClick(
                        itemIndex = it,
                       coroutineScope = scope,
                        openPersonalInformation = {

                        },
                        openEditEmail = {

                        }, openChangePassword = {
                            onChangePasswordClick.invoke()
                        }
                    )
                })
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