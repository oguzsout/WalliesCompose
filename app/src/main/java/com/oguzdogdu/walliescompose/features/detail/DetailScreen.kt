package com.oguzdogdu.walliescompose.features.detail

import android.app.WallpaperManager
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.Coil
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.data.common.Constants.AUTO
import com.oguzdogdu.walliescompose.data.common.Constants.FILE_NAME_SUFFIX
import com.oguzdogdu.walliescompose.data.common.Constants.FIT
import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages
import com.oguzdogdu.walliescompose.features.detail.component.DetailPhotoAttributesRow
import com.oguzdogdu.walliescompose.features.detail.component.DetailTagsRow
import com.oguzdogdu.walliescompose.features.detail.component.DetailTripleActionButtons
import com.oguzdogdu.walliescompose.features.detail.component.WalliesFavoriteButton
import com.oguzdogdu.walliescompose.features.downloadimage.DownloadImageScreenRoute
import com.oguzdogdu.walliescompose.features.home.LoadingState
import com.oguzdogdu.walliescompose.features.setwallpaper.SetWallpaperImageScreenRoute
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular
import com.oguzdogdu.walliescompose.util.adjustUrlForScreenConstraints
import com.oguzdogdu.walliescompose.util.downloadImage
import com.oguzdogdu.walliescompose.util.shareExternal
import kotlinx.coroutines.Dispatchers


@Composable
fun DetailScreenRoute(
    modifier: Modifier = Modifier,
    detailViewModel: DetailViewModel = hiltViewModel(),
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
    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        detailViewModel.handleScreenEvents(DetailScreenEvent.GetPhotoDetails)
        detailViewModel.handleScreenEvents(DetailScreenEvent.GetFavoriteCheckStat)
    }
    LaunchedEffect(key1 = state.favorites) {
        detailViewModel.handleScreenEvents(DetailScreenEvent.GetFavoriteCheckStat)
    }

    LaunchedEffect(key1 = photoQuality) {
        when (photoQuality) {
            "raw" -> {
                downloadImageFromWeb(
                    context = context,
                    imageTitle = state.detail?.desc.orEmpty(),
                    url = state.detail?.rawQuality.orEmpty(),
                    onDismiss = {
                        detailViewModel.handleScreenEvents(
                            DetailScreenEvent.OpenDownloadBottomSheet(
                                isOpen = false
                            )
                        )
                    })
            }

            "high" -> {
                downloadImageFromWeb(
                    context = context,
                    imageTitle = state.detail?.desc.orEmpty(),
                    url = state.detail?.highQuality.orEmpty(),
                    onDismiss = {
                        detailViewModel.handleScreenEvents(
                            DetailScreenEvent.OpenDownloadBottomSheet(
                                isOpen = false
                            )
                        )
                    })
            }

            "medium" -> {
                downloadImageFromWeb(
                    context = context,
                    imageTitle = state.detail?.desc.orEmpty(),
                    url = state.detail?.mediumQuality.orEmpty(),
                    onDismiss = {
                        detailViewModel.handleScreenEvents(
                            DetailScreenEvent.OpenDownloadBottomSheet(
                                isOpen = false
                            )
                        )
                    })

            }

            "low" -> {
                downloadImageFromWeb(
                    context = context,
                    imageTitle = state.detail?.desc.orEmpty(),
                    url = state.detail?.lowQuality.orEmpty(),
                    onDismiss = {
                        detailViewModel.handleScreenEvents(
                            DetailScreenEvent.OpenDownloadBottomSheet(
                                isOpen = false
                            )
                        )
                    })
            }
        }
    }

    LaunchedEffect(key1 = wallpaperPlace) {
        if (wallpaperPlace.isNotEmpty()) {
            setWallpaperFromUrl(
                context = context,
                lifecycleOwner = lifecycleOwner,
                imageUrl = state.detail?.rawQuality?.adjustUrlForScreenConstraints(context),
                place = wallpaperPlace
            )
        }
    }

    var shareEnabled by remember { mutableStateOf(false) }
    val launcherOfShare = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        shareEnabled = true
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
    }) {
        Box(modifier = modifier.padding(it), contentAlignment = Alignment.Center){
            DownloadImageScreenRoute(modifier = modifier, isOpen = stateOfDownloadBottomSheet, onDismiss = {
                detailViewModel.handleScreenEvents(DetailScreenEvent.OpenDownloadBottomSheet(isOpen = false))
            }, onRawButtonClick = {
                detailViewModel.handleScreenEvents(DetailScreenEvent.PhotoQualityType(type = "raw"))
            },
                onFullButtonClick = {
                    detailViewModel.handleScreenEvents(DetailScreenEvent.PhotoQualityType(type = "high"))
                }, onMediumButtonClick = {
                    detailViewModel.handleScreenEvents(DetailScreenEvent.PhotoQualityType(type = "medium"))
                }, onLowButtonClick = {
                    detailViewModel.handleScreenEvents(DetailScreenEvent.PhotoQualityType(type = "low"))
                })
            SetWallpaperImageScreenRoute(
                modifier = modifier,
                isOpen = stateOfSetWallpaperBottomSheet,
                onDismiss = {
                    detailViewModel.handleScreenEvents(
                        DetailScreenEvent.OpenSetWallpaperBottomSheet(
                            isOpen = false
                        )
                    )
                },
                onSetLockButtonClick = {
                    detailViewModel.handleScreenEvents(
                        DetailScreenEvent.SetWallpaperPlace(
                            place = "Lock"
                        )
                    )
                },
                onSetHomeButtonClick = {
                    detailViewModel.handleScreenEvents(
                        DetailScreenEvent.SetWallpaperPlace(
                            place = "Home"
                        )
                    )
                },
                onSetHomeAndLockButtonClick = {
                    detailViewModel.handleScreenEvents(
                        DetailScreenEvent.SetWallpaperPlace(
                            place = "Home and Lock"
                        )
                    )
                })
            Column(
                modifier = modifier
                    .wrapContentSize(),
            ) {
                SubcomposeAsyncImage(
                    model = state.detail?.highQuality,
                    contentDescription = state.detail?.desc,
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentScale = ContentScale.FillBounds,
                    loading = { LoadingState(modifier = modifier) }
                )
                PostView(modifier = modifier, state = state,
                    onSetWallpaperClick = {isOpen ->
                        detailViewModel.handleScreenEvents(DetailScreenEvent.OpenSetWallpaperBottomSheet(isOpen = isOpen))
                    }, onShareClick = { url ->
                        launcherOfShare.launch(url.shareExternal())

                    }, onDownloadClick = {isOpen ->
                        detailViewModel.handleScreenEvents(DetailScreenEvent.OpenDownloadBottomSheet(isOpen = isOpen))
                    },
                    onAddFavoriteClick = { photo ->
                        detailViewModel.handleScreenEvents(
                            DetailScreenEvent.AddFavorites(
                                FavoriteImages(id = state.detail?.id,
                                    url = state.detail?.urls,
                                    profileImage = state.detail?.profileimage,
                                    name = state.detail?.name,
                                    portfolioUrl = state.detail?.portfolio,
                                    isChecked = true)
                            )
                        )
                    }, onRemoveFavoriteClick = { photo ->
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
                    },onTagClick = {tag ->
                        onTagClick.invoke(tag)
                    })
            }
        }
    }
}

@Composable
fun PostView(
    modifier: Modifier,
    state: DetailState,
    onSetWallpaperClick: (Boolean) -> Unit,
    onShareClick: (String) -> Unit,
    onDownloadClick: (Boolean) -> Unit,
    onAddFavoriteClick: (FavoriteImages) -> Unit,
    onRemoveFavoriteClick: (FavoriteImages) -> Unit,
    onTagClick:(String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ), shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
        ), modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 16.dp)
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = state.detail?.profileimage,
                        contentDescription = "Profile Image",
                        modifier = modifier
                            .height(48.dp)
                            .width(48.dp)
                            .clip(RoundedCornerShape(64.dp))

                    )
                    Column(
                        modifier = modifier
                            .wrapContentSize()
                            .padding(start = 8.dp)
                    ) {
                        Text(
                            text = state.detail?.username.orEmpty(),
                            fontFamily = medium,
                            fontSize = 16.sp
                        )
                        Text(
                            text = state.detail?.portfolio.orEmpty(),
                            fontFamily = regular,
                            fontSize = 12.sp
                        )
                    }
                }
                WalliesFavoriteButton(modifier = modifier, favoriteImages = FavoriteImages(
                    id = state.favorites?.id,
                    url = state.favorites?.url,
                    profileImage = state.favorites?.profileImage,
                    name = state.favorites?.name,
                    portfolioUrl = state.favorites?.portfolioUrl,
                    isChecked = state.favorites?.isChecked ?: false
                ), addPhotoToFavorites = { favorite ->
                    onAddFavoriteClick.invoke(favorite)
                }, removePhotoFromFavorites = { favorite ->
                    onRemoveFavoriteClick.invoke(favorite)
                })
            }
        }
        Divider(
            modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 16.dp),
            color = Color(0xFF30363D),
            thickness = 0.8.dp
        )
        DetailPhotoAttributesRow(modifier = modifier, detail = state.detail)
        Divider(
            modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 16.dp),
            color = Color(0xFF30363D),
            thickness = 0.8.dp
        )
        DetailTagsRow(modifier = modifier, detail = state.detail, onTagClick = {
            onTagClick.invoke(it)
        })
        DetailTripleActionButtons(modifier = modifier, setWallpaperButtonClick = {isOpen ->
            onSetWallpaperClick.invoke(isOpen)
        }, shareButtonClick = {
            onShareClick.invoke(state.detail?.urls.orEmpty())
        }, downloadButtonClick = {
            onDownloadClick.invoke(it)
        })
    }
}
private fun downloadImageFromWeb(context: Context,imageTitle:String,url: String?,onDismiss:() -> Unit) {
    val directory: String = context.getString(R.string.app_name)
    val fileName = imageTitle + FILE_NAME_SUFFIX
    Toast.makeText(context, R.string.downloading_text, Toast.LENGTH_LONG).show()
    val downloadableImage = url?.let { context.downloadImage(it, directory, fileName) }
    if (downloadableImage == true) {
        Toast.makeText(context, R.string.download_photo_success, Toast.LENGTH_LONG).show()
        onDismiss.invoke()
    }
}
private fun setWallpaperFromUrl(context: Context,lifecycleOwner: LifecycleOwner,imageUrl: String?, place: String?) {
    val imageLoader = Coil.imageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .transformationDispatcher(Dispatchers.Main.immediate)
        .lifecycle(lifecycleOwner)
        .allowConversionToBitmap(true)
        .memoryCachePolicy(CachePolicy.READ_ONLY)
        .target(
            onSuccess = { result ->
                val wallpaperManager = WallpaperManager.getInstance(context)
                try {
                    when (place.orEmpty()) {
                        "Lock" -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            wallpaperManager
                                .setBitmap(
                                    result.toBitmapOrNull(),
                                    null,
                                    true,
                                    WallpaperManager.FLAG_LOCK
                                )
                        }

                        "Home and Lock" ->
                            wallpaperManager.setBitmap(result.toBitmapOrNull())

                        "Home" -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            wallpaperManager.setBitmap(
                                result.toBitmapOrNull(),
                                null,
                                true,
                                WallpaperManager.FLAG_SYSTEM
                            )
                        }
                    }
                    Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
                }
            },
            onError = { error ->
                Toast.makeText(context,"Something Went Wrong",Toast.LENGTH_SHORT).show()
            }
        )
        .build()
    imageLoader.enqueue(request)
}