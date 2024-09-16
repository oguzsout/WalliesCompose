package com.oguzdogdu.walliescompose.features.detail

import TooltipPopup
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.detail.component.PhotoAttributesCard
import com.oguzdogdu.walliescompose.features.detail.component.PhotoDetailColorsCard
import com.oguzdogdu.walliescompose.features.detail.component.PhotoDetailUserInfoContainer
import com.oguzdogdu.walliescompose.features.detail.component.PhotoDetailedInformationCard
import com.oguzdogdu.walliescompose.features.detail.component.QuickFavoriteTransitionCard
import com.oguzdogdu.walliescompose.features.downloadimage.DownloadImageBottomSheet
import com.oguzdogdu.walliescompose.features.setwallpaper.SetWallpaperImageBottomSheet
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular
import com.oguzdogdu.walliescompose.util.adjustUrlForScreenConstraints
import com.oguzdogdu.walliescompose.util.downloadImageFromWeb
import com.oguzdogdu.walliescompose.util.setWallpaperFromUrl
import com.oguzdogdu.walliescompose.util.shareExternal
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.DetailScreenRoute(
    animatedVisibilityScope: AnimatedVisibilityScope,
    detailViewModel: DetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onProfileDetailClick: (String) -> Unit,
    onTagClick: (String) -> Unit,
    onNavigateToFavorite: () -> Unit
) {
    val state by detailViewModel.photo.collectAsStateWithLifecycle()
    val stateOfDownloadBottomSheet by detailViewModel.downloadBottomSheetOpenStat.collectAsStateWithLifecycle()
    val stateOfSetWallpaperBottomSheet by detailViewModel.setWallpaperBottomSheetOpenStat.collectAsStateWithLifecycle()
    val photoQuality by detailViewModel.photoQualityType.collectAsStateWithLifecycle()
    val wallpaperPlace by detailViewModel.setWallpaperPlace.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var shareEnabled by remember { mutableStateOf(false) }
    val launcherOfShare = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        shareEnabled = true
    }
    var colorFilter by remember {
        mutableStateOf<ColorFilter?>(null)
    }
    var isLoadingForSetWallpaper by remember {
        mutableStateOf(false)
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        detailViewModel.handleScreenEvents(DetailScreenEvent.GetPhotoDetails)
    }

    LaunchedEffect(key1 = state.favorites) {
        detailViewModel.handleScreenEvents(DetailScreenEvent.GetFavoriteCheckStat)
        detailViewModel.handleScreenEvents(DetailScreenEvent.GetFavoriteListForQuickInfo)
    }

    LaunchedEffect(key1 = photoQuality) {
        val imageUrl = when (photoQuality) {
            TypeOfPhotoQuality.RAW.name -> state.detail?.rawQuality.orEmpty()
            TypeOfPhotoQuality.HIGH.name -> state.detail?.highQuality.orEmpty()
            TypeOfPhotoQuality.MEDIUM.name -> state.detail?.mediumQuality.orEmpty()
            TypeOfPhotoQuality.LOW.name -> state.detail?.lowQuality.orEmpty()
            else -> return@LaunchedEffect
        }

        if (imageUrl.isNotEmpty()) {
            context.downloadImageFromWeb(
                imageTitle = state.detail?.desc.orEmpty(),
                url = imageUrl,
                success = {
                    if (it) {
                        detailViewModel.setNullValueOfImageUrl()
                    }
                },
                onDismiss = {
                    detailViewModel.handleScreenEvents(
                        DetailScreenEvent.OpenDownloadBottomSheet(
                            isOpen = false
                        )
                    )
                }
            )
        }
    }

    LaunchedEffect(key1 = wallpaperPlace) {
        if (wallpaperPlace.isNotEmpty()) {
            context.setWallpaperFromUrl(
                lifecycleOwner = lifecycleOwner,
                imageUrl = state.detail?.rawQuality?.adjustUrlForScreenConstraints(context),
                place = wallpaperPlace,
                colorFilter = colorFilter,
                onSuccess = {
                    detailViewModel.handleScreenEvents(
                        DetailScreenEvent.OpenSetWallpaperBottomSheet(
                            isOpen = false
                        )
                    )
                }
                , onError = {},
                isLoading = {
                    isLoadingForSetWallpaper = it
                }
            )
        }
    }

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onBackClick.invoke() },
                modifier = Modifier
                    .wrapContentSize()

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .wrapContentSize()
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = state.detail?.desc.orEmpty(),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 14.sp,
                fontFamily = medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start
            )
        }
    }) { paddingValues ->
        DetailScreenContent(
            animatedVisibilityScope = animatedVisibilityScope,
            paddingValues = paddingValues,
            state = state,
            wallpaperPlace = wallpaperPlace,
            stateOfDownloadBottomSheet = stateOfDownloadBottomSheet,
            stateOfSetWallpaperBottomSheet = stateOfSetWallpaperBottomSheet,
            onDownloadBottomSheetDismiss = { isOpen ->
                detailViewModel.handleScreenEvents(DetailScreenEvent.OpenDownloadBottomSheet(isOpen = isOpen))
            },
            onSetWallpaperBottomSheetDismiss = {isOpen ->
                detailViewModel.handleScreenEvents(
                    DetailScreenEvent.OpenSetWallpaperBottomSheet(
                        isOpen = isOpen
                    )
                )
            },
            onRawButtonClick = {
                detailViewModel.handleScreenEvents(DetailScreenEvent.PhotoQualityType(type = it))
            },
            onFullButtonClick = {
                detailViewModel.handleScreenEvents(DetailScreenEvent.PhotoQualityType(type = it))
            },
            onMediumButtonClick = {
                detailViewModel.handleScreenEvents(DetailScreenEvent.PhotoQualityType(type = it))
            },
            onLowButtonClick = {
                detailViewModel.handleScreenEvents(DetailScreenEvent.PhotoQualityType(type = it))
            },
            onSetHomeButtonClick = {
                detailViewModel.handleScreenEvents(DetailScreenEvent.SetWallpaperPlace(it))
            },
            onSetLockButtonClick = {
                detailViewModel.handleScreenEvents(DetailScreenEvent.SetWallpaperPlace(it))
            },
            onSetHomeAndLockButtonClick = {
                detailViewModel.handleScreenEvents(DetailScreenEvent.SetWallpaperPlace(it))
            },
            onSetWallpaperButtonClick = {isOpen ->
                detailViewModel.handleScreenEvents(DetailScreenEvent.OpenSetWallpaperBottomSheet(isOpen = isOpen))

            },
            onShareButtonClick = {url ->
                launcherOfShare.launch(url.shareExternal())
            },
            onDownloadButtonClick = {isOpen ->
                detailViewModel.handleScreenEvents(DetailScreenEvent.OpenDownloadBottomSheet(isOpen = isOpen))
            },
            onAddFavoriteButtonClick = {
                detailViewModel.handleScreenEvents(
                    DetailScreenEvent.AddFavorites
                )
            },
            onRemoveFavoriteButtonClick = {
                detailViewModel.handleScreenEvents(
                    DetailScreenEvent.DeleteFavorites
                )
            },
            onTagButtonClick = {tag ->
                onTagClick.invoke(tag)
            },
            newBitmap = {
                colorFilter = it
            },
            loadingForSetWallpaperButton = isLoadingForSetWallpaper,
            onProfileDetailClick = onProfileDetailClick,
            onNavigateToFavorite = onNavigateToFavorite
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.DetailScreenContent(
    animatedVisibilityScope: AnimatedVisibilityScope,
    paddingValues: PaddingValues,
    state: DetailState,
    wallpaperPlace: String?,
    stateOfDownloadBottomSheet: Boolean,
    stateOfSetWallpaperBottomSheet: Boolean,
    onDownloadBottomSheetDismiss: (Boolean) -> Unit,
    onSetWallpaperBottomSheetDismiss: (Boolean) -> Unit,
    onRawButtonClick: (TypeOfPhotoQuality) -> Unit,
    onFullButtonClick: (TypeOfPhotoQuality) -> Unit,
    onMediumButtonClick: (TypeOfPhotoQuality) -> Unit,
    onLowButtonClick: (TypeOfPhotoQuality) -> Unit,
    onSetHomeButtonClick: (TypeOfSetWallpaper) -> Unit,
    onSetLockButtonClick: (TypeOfSetWallpaper) -> Unit,
    onSetHomeAndLockButtonClick: (TypeOfSetWallpaper) -> Unit,
    onSetWallpaperButtonClick: (Boolean) -> Unit,
    onShareButtonClick: (String) -> Unit,
    onDownloadButtonClick: (Boolean) -> Unit,
    onAddFavoriteButtonClick: () -> Unit,
    onRemoveFavoriteButtonClick: () -> Unit,
    onTagButtonClick: (String) -> Unit,
    onNavigateToFavorite: () -> Unit,
    newBitmap: (ColorFilter) -> Unit,
    loadingForSetWallpaperButton: Boolean,
    onProfileDetailClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val photoAttributesPairList = remember { mutableStateListOf<Pair<Int, AnnotatedString>>() }
    val unknown = remember { mutableStateOf(context.getString(R.string.unknown)) }
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()
    var bitmapForDialog by remember { mutableStateOf<Bitmap?>(null) }
    val favoriteListSize = rememberUpdatedState(state.favoriteImagesList.size > 6)

    LaunchedEffect(state.detail) {
        photoAttributesPairList.apply {
            clear()
            add(
                R.string.camera to if (state.detail?.exif?.name != null) {
                    AnnotatedString(state.detail.exif.name)
                } else {
                    AnnotatedString(unknown.value)
                }
            )
            add(R.string.aperture to if (state.detail?.exif?.aperture != null) {
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                        append("f")
                    }
                    append("/${state.detail.exif.aperture}")
                }
            } else {
                AnnotatedString(unknown.value)
            })
            add(
                R.string.focal_length to if (state.detail?.exif?.focalLength != null) {
                    AnnotatedString("${state.detail.exif.focalLength}mm")
                } else {
                    AnnotatedString(unknown.value)
                }
            )
            add(
                R.string.shutter_speed to if (state.detail?.exif?.exposureTime != null) {
                    AnnotatedString("${state.detail.exif.exposureTime}s")
                } else {
                    AnnotatedString(unknown.value)
                }
            )
            add(
                R.string.iso to if (state.detail?.exif?.iso != null) {
                    AnnotatedString(state.detail.exif.iso.toString())
                } else {
                    AnnotatedString(unknown.value)
                }
            )
            add(
                R.string.dimensions to if (state.detail?.width != null && state.detail.height != null) {
                    AnnotatedString("${state.detail.width} × ${state.detail.height}")
                } else {
                    AnnotatedString(unknown.value)
                }
            )
        }
    }
    Box(modifier = modifier
        .fillMaxSize()
        .padding(paddingValues)) {
        val targetOffset = if (state.detail != null) 0.dp else 400.dp
        val animatedOffset by animateDpAsState(
            targetValue = targetOffset,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            ), label = ""
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .animateContentSize(),
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                DetailScreenMainPhoto(state = state,
                    animatedVisibilityScope = animatedVisibilityScope,
                    graphicsLayer = graphicsLayer
                )
            }
            item {
                PhotoDetailUserInfoContainer(state = state,
                    onProfileDetailClick = onProfileDetailClick,
                    modifier = Modifier.offset {
                        IntOffset(x = animatedOffset.roundToPx(), y = 0)
                    }
                )
            }
            item {
                PhotoAttributesCard(
                    pairsOfPhotoAttributes = photoAttributesPairList,
                    modifier = Modifier.offset {
                        IntOffset(x = animatedOffset.roundToPx(), y = 0)
                    }
                )
            }
            item {
                AnimatedVisibility(
                    visible = favoriteListSize.value,
                    enter = slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(600, easing = FastOutSlowInEasing)
                    ) + expandVertically(
                        expandFrom = Alignment.CenterVertically,
                        animationSpec = tween(600, easing = FastOutSlowInEasing)
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(600, easing = FastOutSlowInEasing)
                    ) + shrinkVertically(
                        shrinkTowards = Alignment.CenterVertically,
                        animationSpec = tween(600, easing = FastOutSlowInEasing)
                    )
                ) {
                    QuickFavoriteTransitionCard(
                        favoriteImages = state.favoriteImagesList,
                        onClickGoToFavorites = { onNavigateToFavorite.invoke() }
                    )
                }

            }

            item {
                PhotoDetailedInformationCard(state = state,
                    onSetWallpaperClick = { isOpen ->
                        onSetWallpaperButtonClick.invoke(isOpen)
                        coroutineScope.launch {
                            val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
                            bitmapForDialog = bitmap
                        }
                    },
                    onShareClick = { url -> onShareButtonClick.invoke(url) },
                    onDownloadClick = { isOpen -> onDownloadButtonClick.invoke(isOpen) },
                    onAddFavoriteClick = { onAddFavoriteButtonClick.invoke() },
                    onRemoveFavoriteClick = { onRemoveFavoriteButtonClick.invoke() },
                    onTagClick = { tag -> onTagButtonClick.invoke(tag) },
                    modifier = Modifier.offset {
                        IntOffset(x = animatedOffset.roundToPx(), y = 0)
                    })
            }
        }
        DownloadImageBottomSheet(isOpen = stateOfDownloadBottomSheet,
            onDismiss = { onDownloadBottomSheetDismiss.invoke(false) },
            onRawButtonClick = { onRawButtonClick.invoke(TypeOfPhotoQuality.RAW) },
            onFullButtonClick = { onFullButtonClick.invoke(TypeOfPhotoQuality.HIGH) },
            onMediumButtonClick = { onMediumButtonClick.invoke(TypeOfPhotoQuality.MEDIUM) },
            onLowButtonClick = { onLowButtonClick.invoke(TypeOfPhotoQuality.LOW) })
        SetWallpaperImageBottomSheet(imageForFilter = bitmapForDialog,
            wallpaperPlace = wallpaperPlace,
            isOpen = stateOfSetWallpaperBottomSheet,
            isLoading = loadingForSetWallpaperButton,
            onDismiss = { onSetWallpaperBottomSheetDismiss.invoke(false) },
            onSetLockButtonClick = {
                onSetLockButtonClick.invoke(TypeOfSetWallpaper.LOCK)
            },
            onSetHomeButtonClick = {
                onSetHomeButtonClick.invoke(TypeOfSetWallpaper.HOME)
            },
            onSetHomeAndLockButtonClick = {
                onSetHomeAndLockButtonClick.invoke(TypeOfSetWallpaper.HOME_AND_LOCK)
            },
            adjustColorFilter = {
                newBitmap.invoke(it)
            })
    }
}
@OptIn(ExperimentalSharedTransitionApi::class)
private val boundsTransform = BoundsTransform { _: Rect, _: Rect ->
    tween(durationMillis = boundsAnimationDurationMillis, easing = LinearEasing)
}
private const val boundsAnimationDurationMillis = 500

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.DetailScreenMainPhoto(
    state: DetailState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    graphicsLayer: GraphicsLayer
) {
    Box {
        SubcomposeAsyncImage(
            model = state.detail?.mediumQuality,
            contentDescription = state.detail?.desc,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .sharedElement(
                    state = rememberSharedContentState(key = "popularImage-${state.detail?.id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(16.dp)),
                    boundsTransform = boundsTransform
                )
                .fillMaxWidth()
                .height(400.dp)
                .clip(RoundedCornerShape(16.dp))
                .drawWithContent {
                    graphicsLayer.record {
                        this@drawWithContent.drawContent()
                    }
                    drawLayer(graphicsLayer)
                }
        )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {

                TooltipPopup(
                    triggeredView = { modifier ->
                        Icon(
                            modifier = modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.info),
                            contentDescription = "TooltipPopup",
                            tint = Color.White,
                        )
                    },
                    tooltipContent = {
                        PhotoDetailColorsCard(photoDetailState = state)
                    }
                )
            }

        if (state.detail?.location?.country?.isNotEmpty() == true) {
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
                    .align(Alignment.BottomStart)
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.onPrimary.copy(0.5f))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = "Location Icon",
                    modifier = Modifier.wrapContentSize(),
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = state.detail.location.name.orEmpty(),
                    modifier = Modifier.wrapContentSize(),
                    fontSize = 14.sp,
                    fontFamily = regular,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}