package com.oguzdogdu.walliescompose.features.signup

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.signup.component.BasicInputTextField
import com.oguzdogdu.walliescompose.features.signup.component.EmailTextFieldWithoutSubText
import com.oguzdogdu.walliescompose.features.signup.component.PasswordRuleSetBox
import com.oguzdogdu.walliescompose.features.signup.component.PasswordTextFieldWithoutSubText
import com.oguzdogdu.walliescompose.features.signup.state.SignUpSteps
import com.oguzdogdu.walliescompose.features.signup.state.SignUpStepsData
import com.oguzdogdu.walliescompose.features.signup.state.SignUpUIState
import com.oguzdogdu.walliescompose.ui.theme.bold
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    goToLoginScreen: () -> Unit,
    viewModel: SignUpScreenViewModel = hiltViewModel()
) {

    val signUpState by viewModel.signUpUiState.collectAsStateWithLifecycle()
    val signUpStepsState by viewModel.signUpStepsState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if(signUpStepsState.signUpSteps != SignUpSteps.SIGN_UP_STATUS) {
                TopAppBar(
                    colors = TopAppBarColors(containerColor = MaterialTheme.colorScheme.background,
                        Color.Transparent, Color.Transparent, Color.Transparent, Color.Transparent),
                    title = {},
                    actions = {
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            IconButton(
                                onClick = {
                                    if (signUpStepsState.stepIndex == 0) {
                                        onBackClick.invoke()
                                    } else {
                                        viewModel.onPreviousPressed()
                                    }
                                },
                                modifier = modifier
                                    .wrapContentSize()
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.back),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = modifier
                                        .wrapContentSize()
                                )
                            }

                            Text(
                                modifier = modifier,
                                text = stringResource(R.string.text_create_account),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize = 16.sp,
                                fontFamily = medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                )
            }
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)

        ) {
                SignUpScreenContent(
                    signUpUIState = signUpState,
                    signUpStepsData = signUpStepsState,
                    passwordCheck = { password ->
                        viewModel.handleUiEvents(SignUpScreenEvent.CheckPasswordRule(password))
                    },
                    continueButtonClick = {email, password ->
                        when (signUpStepsState.shouldShowDoneButton) {
                            true -> {
                                goToLoginScreen.invoke()
                            }
                            false -> {
                                viewModel.handleUiEvents(SignUpScreenEvent.ResumeToSignUp(email, password))
                                viewModel.handleUiEvents(SignUpScreenEvent.ExecuteFlowOfSignUp)
                                viewModel.onNextPressed()
                            }

                            null -> {}
                        }
                    },
                    sendEmail = {email ->
                        viewModel.handleUiEvents(SignUpScreenEvent.SendEmail(email))
                    },
                    sendPassword = {password ->
                        viewModel.handleUiEvents(SignUpScreenEvent.SendPassword(password))
                    },
                    sendName = { name ->
                        viewModel.handleUiEvents(SignUpScreenEvent.SendName(name))
                    },
                    sendSurname = { surname ->
                        viewModel.handleUiEvents(SignUpScreenEvent.SendSurname(surname))
                    },
                    sendUri = { uri ->
                        viewModel.handleUiEvents(SignUpScreenEvent.SendUri(uri))
                    },
                    goToLoginScreen = {
                        goToLoginScreen.invoke()
                    }
                )
            }
        }
    }

@Composable
fun SignUpScreenContent(
    signUpUIState: SignUpUIState,
    signUpStepsData: SignUpStepsData,
    modifier: Modifier = Modifier,
    passwordCheck: (String) -> Unit,
    continueButtonClick: (String, String) -> Unit,
    sendEmail: (String) -> Unit,
    sendPassword: (String) -> Unit,
    sendName: (String) -> Unit,
    sendSurname: (String) -> Unit,
    sendUri: (Uri) -> Unit,
    goToLoginScreen: () -> Unit,
) {

    var emailField by remember {
        mutableStateOf("")
    }
    var passwordField by remember {
        mutableStateOf("")
    }

    var ruleSetVisible by remember {
        mutableStateOf(false)
    }

    AnimatedContent(
        targetState = signUpStepsData.signUpSteps,
        transitionSpec = {
            slideInHorizontally(
                animationSpec = tween(400),
                initialOffsetX = { fullWidth -> fullWidth }
            ) togetherWith
                    slideOutHorizontally(
                        animationSpec = tween(400),
                        targetOffsetX = { fullWidth -> -fullWidth }
                    )
        }, label = ""
    ) { targetState ->
        Box(modifier = modifier.fillMaxSize()) {
            when (targetState) {
                SignUpSteps.EMAIL_PASSWORD -> {
                    SignUpStepEmailAndPasswordBox(
                        signUpUIState = signUpUIState,
                        passwordCheck = { password ->
                            passwordCheck.invoke(password)
                        },
                        sendEmail = { email ->
                            sendEmail.invoke(email)
                            emailField = email
                        },
                        sendPassword = { password ->
                            sendPassword.invoke(password)
                            passwordField = password
                        },
                    )
                }

                SignUpSteps.PHOTO_NAME_SURNAME -> {
                    SignUpStepPhotoNameAndSurnameBox(
                        sendName = {
                            sendName.invoke(it)
                        },
                        sendSurname = {
                            sendSurname.invoke(it)
                        },
                        sendUri = {
                            sendUri.invoke(it)
                        }
                    )
                    ruleSetVisible = false
                }

                SignUpSteps.SIGN_UP_STATUS -> { ResultScreen(uiState = signUpUIState) }

                null -> {}
            }
            Button(
                onClick = {
                    if (signUpStepsData.shouldShowDoneButton == true) {
                        goToLoginScreen.invoke()
                    } else {
                        continueButtonClick.invoke(emailField, passwordField)
                    }
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                when (signUpUIState.loading) {
                    true -> {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    }

                    false -> {
                        Text(
                            text = when (signUpStepsData.stepIndex) {
                                0 -> stringResource(R.string.text_continue)
                                1 -> stringResource(id = R.string.text_create_account)
                                2 -> stringResource(R.string.go_to_login)
                                else -> ""
                            },
                            fontSize = 14.sp,
                            fontFamily = medium,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SignUpStepEmailAndPasswordBox(
    signUpUIState: SignUpUIState,
    passwordCheck: (String) -> Unit,
    sendEmail: (String) -> Unit,
    sendPassword: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var ruleSetVisible by remember {
        mutableStateOf(false)
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailAndPasswordFieldContainer(
            email = signUpUIState.email.orEmpty(),
            password = signUpUIState.password.orEmpty(),
            ruleSetVisibility = { ruleSetVisible = it },
            sendEmail = {
                sendEmail.invoke(it)
            },
            sendPassword = {
                sendPassword.invoke(it)
                passwordCheck.invoke(it)
            }
        )
        Spacer(modifier = modifier.size(8.dp))
        PasswordRuleSet(
            state = signUpUIState,
            ruleSetVisible = ruleSetVisible,
            modifier = modifier
        )
    }
}

@Composable
fun SignUpStepPhotoNameAndSurnameBox(
    sendName: (String) -> Unit,
    sendSurname: (String) -> Unit,
    sendUri: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var profileImage: Any by remember {
        mutableStateOf(0)
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                imageUri = it
            }
        }
    )
    LaunchedEffect(imageUri) {
        when {
            imageUri == null -> {
                profileImage = R.drawable.ic_large_default_avatar
            }
            imageUri != null -> {
                sendUri.invoke(imageUri!!)
                profileImage = imageUri as Uri
            }
        }
    }
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.image_name_surname_sub_title_text),
                fontSize = 20.sp,
                fontFamily = bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start,
                modifier = modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = modifier.size(16.dp))
            Box(
                modifier = modifier
                    .wrapContentSize()
            ) {
                AsyncImage(
                    model = profileImage,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "Profile Image",
                    modifier = modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(96.dp))
                        .border(1.dp, Color.LightGray, shape = RoundedCornerShape(96.dp)),
                )
                Image(
                    painter = if (imageUri == null) painterResource(id = R.drawable.ic_edit_photo) else painterResource(
                        id = R.drawable.ic_clear_image
                    ),
                    contentDescription = "Profile Image Edit",
                    modifier = modifier
                        .size(24.dp)
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .clickable {
                            coroutineScope.launch {
                                if (imageUri == null) {
                                    galleryLauncher.launch(
                                        PickVisualMediaRequest(
                                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                } else {
                                    imageUri = null
                                }
                            }
                        },
                )
            }
            Spacer(modifier = modifier.size(16.dp))
            BasicInputTextField(typeOfField = R.string.name, onTextChanged = {
                sendName.invoke(it)
            })
            Spacer(modifier = modifier.size(8.dp))
            BasicInputTextField(typeOfField = R.string.surname, onTextChanged = {
                sendSurname.invoke(it)
            })
        }
    }
}

@Composable
fun EmailAndPasswordFieldContainer(
    email:String,
    password:String,
    ruleSetVisibility: (Boolean) -> Unit,
    sendEmail: (String) -> Unit,
    sendPassword: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.email_password_sub_title_text),
            fontSize = 20.sp,
            fontFamily = medium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Start,
            modifier = modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = modifier.size(8.dp))
        EmailTextFieldWithoutSubText(email = email, onChangeEmail = {
            sendEmail.invoke(it)
        })
        Spacer(modifier = modifier.size(8.dp))
        PasswordTextFieldWithoutSubText(password = password, onChangePassword = {
            ruleSetVisibility.invoke(it != "")
            sendPassword.invoke(it)
        })
    }
}

@Composable
fun PasswordRuleSet(
    state: SignUpUIState,
    ruleSetVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val ruleSetVisibility = rememberUpdatedState(newValue = ruleSetVisible)

    AnimatedVisibility(
        visible = ruleSetVisibility.value,
        enter = slideInHorizontally(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) + fadeIn(
            animationSpec = tween(durationMillis = 300)
        ),
        exit = slideOutHorizontally(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) + fadeOut(
            animationSpec = tween(durationMillis = 300)
        )
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                Color.Transparent, Color.Transparent, Color.Transparent
            ),
            modifier = modifier
                .padding(horizontal = 24.dp)
                .wrapContentHeight()
                .border(1.dp, Color.LightGray, shape = RoundedCornerShape(16.dp)),
        ) {
            LazyColumn(
                state = rememberLazyListState(),
                modifier = modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                itemsIndexed(
                    state.ruleSet,
                    key = { index: Int, item: PasswordRuleSetContainer -> item.title.hashCode() }) { index, item ->
                    PasswordRuleSetBox(passwordRuleSetContainer = item)
                }
            }
        }
    }
}

@Composable
fun ResultScreen(uiState: SignUpUIState,modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var resultMessage by remember {
        mutableStateOf("")
    }
    var resultAnimation by remember {
        mutableStateOf(0)
    }
    LaunchedEffect(uiState.isSignUp) {
        when (uiState.isSignUp and uiState.errorMessage.isNullOrEmpty()) {
            true -> {
                resultMessage = context.getString(R.string.successfull_registration_message_text)
                resultAnimation = R.raw.success_animation
            }
            false -> {
                resultMessage = uiState.errorMessage.orEmpty()
                resultAnimation = R.raw.error_animation
            }
        }
    }
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            ResultAnimationScreen(animationResource = resultAnimation,
                modifier = modifier.size(180.dp)
            )
            Spacer(modifier = modifier.size(8.dp))
            Text(
                text = resultMessage,
                fontSize = 16.sp,
                fontFamily = regular,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ResultAnimationScreen(animationResource: Int, modifier: Modifier) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            animationResource
        )
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )


    LottieAnimation(
        composition = preloaderLottieComposition,
        progress = preloaderProgress,
        modifier = modifier
    )
}