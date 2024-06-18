package com.oguzdogdu.walliescompose.features.detail

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages
import com.oguzdogdu.walliescompose.features.detail.component.PhotoDetailedInformationCard
import com.oguzdogdu.walliescompose.features.downloadimage.DownloadImageBottomSheet
import com.oguzdogdu.walliescompose.features.setwallpaper.SetWallpaperImageBottomSheet
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.util.adjustUrlForScreenConstraints
import com.oguzdogdu.walliescompose.util.downloadImageFromWeb
import com.oguzdogdu.walliescompose.util.setWallpaperFromUrl
import com.oguzdogdu.walliescompose.util.shareExternal
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.DetailScreenRoute(
    animatedVisibilityScope: AnimatedVisibilityScope,
    detailViewModel: DetailViewModel,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onProfileDetailClick: (String) -> Unit,
    onTagClick: (String) -> Unit
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
        detailViewModel.handleScreenEvents(DetailScreenEvent.GetFavoriteCheckStat)
    }

    LaunchedEffect(key1 = state.favorites) {
        detailViewModel.handleScreenEvents(DetailScreenEvent.GetFavoriteCheckStat)
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
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            IconButton(
                onClick = { onBackClick.invoke() },
                modifier = modifier
                    .wrapContentSize()
                    .weight(1f)
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
                modifier = modifier.weight(6f),
                text = state.detail?.desc.orEmpty(),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 12.sp,
                fontFamily = medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = { onProfileDetailClick.invoke(state.detail?.username.orEmpty()) },
                modifier = modifier
                    .wrapContentSize()
                    .weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.info),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = modifier
                        .wrapContentSize()
                )
            }
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
                    DetailScreenEvent.AddFavorites(
                        FavoriteImages(id = state.detail?.id,
                            url = state.detail?.urls,
                            profileImage = state.detail?.profileimage,
                            name = state.detail?.name,
                            portfolioUrl = state.detail?.portfolio,
                            isChecked = true
                        )
                    )
                )
            },
            onRemoveFavoriteButtonClick = {
                detailViewModel.handleScreenEvents(
                    DetailScreenEvent.DeleteFavorites(
                        FavoriteImages(
                            id = state.detail?.id,
                            url = state.detail?.urls,
                            profileImage = state.detail?.profileimage,
                            name = state.detail?.name,
                            portfolioUrl = state.detail?.portfolio,
                            isChecked = false
                        )
                    )
                )
            },
            onTagButtonClick = {tag ->
                onTagClick.invoke(tag)
            },
            newBitmap = {
                colorFilter = it
            },
            loadingForSetWallpaperButton = isLoadingForSetWallpaper
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalFoundationApi::class
)
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
    onAddFavoriteButtonClick: (FavoriteImages) -> Unit,
    onRemoveFavoriteButtonClick: (FavoriteImages) -> Unit,
    onTagButtonClick: (String) -> Unit,
    newBitmap: (ColorFilter) -> Unit,
    loadingForSetWallpaperButton: Boolean,
    modifier: Modifier = Modifier
) {
    var isExpand by remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()
    var bitmapForDialog by remember{
        mutableStateOf<Bitmap?>(null)
    }

    Box(modifier = modifier
        .fillMaxSize()
        .padding(paddingValues = paddingValues)
    ) {

    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "popularImage-${state.detail?.id}"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .fillMaxSize()
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .combinedClickable(onClick = {}, onLongClick = { isExpand = !isExpand })
                .drawWithContent {
                    graphicsLayer.record {
                        this@drawWithContent.drawContent()
                    }
                    drawLayer(graphicsLayer)
                },
            model = state.detail?.mediumQuality,
            contentDescription = state.detail?.desc,
            contentScale = ContentScale.FillBounds)

        Spacer(modifier = modifier.size(8.dp))

        PhotoDetailedInformationCard(
            modifier = Modifier.align(Alignment.End),
            state = state,
            onSetWallpaperClick = { isOpen -> onSetWallpaperButtonClick.invoke(isOpen)
                coroutineScope.launch {
                    val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
                    bitmapForDialog = bitmap
                } },
            onShareClick = { url -> onShareButtonClick.invoke(url) },
            onDownloadClick = { isOpen -> onDownloadButtonClick.invoke(isOpen) },
            onAddFavoriteClick = { photo -> onAddFavoriteButtonClick.invoke(photo) },
            onRemoveFavoriteClick = { photo -> onRemoveFavoriteButtonClick.invoke(photo) },
            onTagClick = { tag -> onTagButtonClick.invoke(tag) })
        DownloadImageBottomSheet(
            isOpen = stateOfDownloadBottomSheet,
            onDismiss = { onDownloadBottomSheetDismiss.invoke(false) },
            onRawButtonClick = { onRawButtonClick.invoke(TypeOfPhotoQuality.RAW) },
            onFullButtonClick = { onFullButtonClick.invoke(TypeOfPhotoQuality.HIGH) },
            onMediumButtonClick = { onMediumButtonClick.invoke(TypeOfPhotoQuality.MEDIUM) },
            onLowButtonClick = { onLowButtonClick.invoke(TypeOfPhotoQuality.LOW) })
        SetWallpaperImageBottomSheet(
            imageForFilter = bitmapForDialog,
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
}