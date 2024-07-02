package com.oguzdogdu.walliescompose.features.profiledetail

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.data.common.ImageLoadingState
import com.oguzdogdu.walliescompose.domain.model.userdetail.UserCollections
import com.oguzdogdu.walliescompose.domain.model.userdetail.UsersPhotos
import com.oguzdogdu.walliescompose.features.home.LoadingState
import com.oguzdogdu.walliescompose.features.profiledetail.components.CircleImageWithBorderAndText
import com.oguzdogdu.walliescompose.features.profiledetail.components.InteractionCountOfUser
import com.oguzdogdu.walliescompose.features.profiledetail.components.PersonalInfoOfUser
import com.oguzdogdu.walliescompose.features.profiledetail.event.ProfileDetailEvent
import com.oguzdogdu.walliescompose.features.profiledetail.state.ProfileDetailState
import com.oguzdogdu.walliescompose.features.profiledetail.state.ProfileDetailUIState
import com.oguzdogdu.walliescompose.features.profiledetail.state.UserCollectionState
import com.oguzdogdu.walliescompose.features.profiledetail.state.UserPhotosState
import com.oguzdogdu.walliescompose.features.profiledetail.tab.ProfileDetailTabWithPager
import com.oguzdogdu.walliescompose.ui.theme.medium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: ProfileDetailViewModel = hiltViewModel(),
    onUserPhotoListClick: (String) -> Unit,
    onCollectionItemClick: (String, String) -> Unit,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val stateOfUiState by viewModel.getProfileDetailState.collectAsStateWithLifecycle()
    val stateOfProfileDetail by viewModel.getUserDetails.collectAsStateWithLifecycle()
    val stateOfProfilePhotoList by viewModel.getUserPhotoList.collectAsStateWithLifecycle()
    val stateOfProfileCollectionList by viewModel.getUserCollectionList.collectAsStateWithLifecycle()

    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        viewModel.handleUIEvent(ProfileDetailEvent.FetchUserDetailInfos)
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val isCollapsed = remember { derivedStateOf { scrollBehavior.state.collapsedFraction < 0.5 } }

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
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
                        onClick = { onBackClick.invoke() },
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
                        text = stateOfProfileDetail.userDetails?.username.orEmpty(),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 14.sp,
                        fontFamily = medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )

    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when(stateOfUiState) {
                is ProfileDetailUIState.Loading -> {
                    LoadingState(modifier = modifier)
                }
                is ProfileDetailUIState.Error -> {

                }
                is ProfileDetailUIState.ReadyForShown -> ProfileDetailScreen(
                    profileDetailState = stateOfProfileDetail,
                    userPhotosState = stateOfProfilePhotoList,
                    userCollectionState = stateOfProfileCollectionList,
                    onUserPhotoListClick = { id ->
                        onUserPhotoListClick.invoke(id)
                    },
                    onCollectionItemClick = { id, title ->
                        onCollectionItemClick.invoke(id, title)
                    },
                    context = context,scrollBehavior,animatedVisibility = isCollapsed.value
                )

                else -> {}
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(
    profileDetailState: ProfileDetailState?,
    userPhotosState: UserPhotosState?,
    userCollectionState: UserCollectionState?,
    onUserPhotoListClick: (String) -> Unit,
    onCollectionItemClick: (String, String) -> Unit,
    context: Context,
    scrollBehavior: TopAppBarScrollBehavior,
    animatedVisibility:Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.wrapContentSize()) {
        var visible by remember { mutableStateOf(true) }

        LaunchedEffect(animatedVisibility) {
            visible = animatedVisibility
        }

        val density = LocalDensity.current
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically {
                with(density) { 60.dp.roundToPx() }
            } + expandVertically(
                expandFrom = Alignment.Top
            ) + fadeIn(
                initialAlpha = 0.2f
            ),
            exit = slideOutVertically() + shrinkVertically() + fadeOut()
        ) {
            FullInfoCardOfUser(
                profileDetailState = profileDetailState,
                context = context
            )
        }
        ProfileDetailTabWithPager(
            profileDetailState = profileDetailState,
            userPhotosState = userPhotosState,
            userCollectionState = userCollectionState,
            onUserPhotoListClick = { id ->
                onUserPhotoListClick.invoke(id)
            },
            onCollectionItemClick = { id, title ->
                onCollectionItemClick.invoke(id, title)
            }, scrollBehavior
        )
    }
}

@Composable
fun FullInfoCardOfUser(
    profileDetailState: ProfileDetailState?, context: Context,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircleImageWithBorderAndText(
            profileDetailState = profileDetailState,
            text = stringResource(R.string.available_for_hire),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.size(12.dp))
        PersonalInfoOfUser(profileDetailState = profileDetailState, context = context)
        Spacer(modifier = Modifier.size(12.dp))
        InteractionCountOfUser(profileDetailState = profileDetailState)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoListOfUser(
    userPhotosState: UserPhotosState?,
    onUserPhotoListClick: (String) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .wrapContentSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        state = rememberLazyGridState(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    )   {
        items(userPhotosState?.usersPhotos.orEmpty(), key = {
            it.id.hashCode()
        }) {
            PhotoListItem(modifier = modifier,userPhotosState = it, onUserPhotoListClick = {id ->
                onUserPhotoListClick.invoke(id)
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionListOfUser(
    userCollectionState: UserCollectionState?,
    onCollectionItemClick: (String, String) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        state = rememberLazyGridState(),
        columns = GridCells.Fixed(2),
        modifier = modifier
            .wrapContentSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    )   {
        items(userCollectionState?.usersCollection.orEmpty(), key = {
            it.id.hashCode()
        }) {
            CollectionListItem(modifier = modifier,userCollection = it, onCollectionItemClick = {id,title ->
                onCollectionItemClick.invoke(id,title)
            })
        }
    }
}

@Composable
fun PhotoListItem(modifier: Modifier,userPhotosState: UsersPhotos, onUserPhotoListClick: (String) -> Unit) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .clickable {
                userPhotosState.id?.let {
                    onUserPhotoListClick.invoke(
                        it
                    )
                }
            }
        , contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = userPhotosState.url,
            contentDescription = userPhotosState.imageDesc,
            contentScale = ContentScale.FillBounds,
            modifier = modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(CircleShape.copy(all = CornerSize(16.dp)))
            , loading = {
                ImageLoadingState()
            }
        )
    }
}

@Composable
fun CollectionListItem(
    modifier: Modifier,
    userCollection: UserCollections?,
    onCollectionItemClick: (String, String) -> Unit
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .clickable {
                onCollectionItemClick.invoke(
                    userCollection?.id.orEmpty(), userCollection?.title.orEmpty()
                )
            }
        , contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = userCollection?.photo,
            contentDescription = userCollection?.title,
            contentScale = ContentScale.FillBounds,
            modifier = modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(CircleShape.copy(all = CornerSize(16.dp)))
                .drawWithContent {
                    drawContent()
                    drawRect(
                        color = Color.Black.copy(alpha = 0.5f),
                        size = size,
                    )
                }, loading = {
                ImageLoadingState()
            }
        )
        if (userCollection?.title != null) {
            Text(
                text = userCollection.title,
                textAlign = TextAlign.Center,
                fontFamily = medium,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}