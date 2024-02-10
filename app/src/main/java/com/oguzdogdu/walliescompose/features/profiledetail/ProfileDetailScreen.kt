package com.oguzdogdu.walliescompose.features.profiledetail

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.data.common.ImageLoadingState
import com.oguzdogdu.walliescompose.domain.model.userdetail.UserCollections
import com.oguzdogdu.walliescompose.domain.model.userdetail.UsersPhotos
import com.oguzdogdu.walliescompose.features.profiledetail.event.ProfileDetailEvent
import com.oguzdogdu.walliescompose.features.profiledetail.state.ProfileDetailState
import com.oguzdogdu.walliescompose.features.profiledetail.state.UserCollectionState
import com.oguzdogdu.walliescompose.features.profiledetail.state.UserPhotosState
import com.oguzdogdu.walliescompose.ui.theme.bold
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular
import kotlinx.coroutines.launch

@Composable
fun ProfileDetailScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: ProfileDetailViewModel = hiltViewModel(),
    onUserPhotoListClick: (String) -> Unit,
    onCollectionItemClick: (String, String) -> Unit,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current

    val stateOfProfileDetail by viewModel.getUserDetails.collectAsStateWithLifecycle()
    val stateOfProfilePhotoList by viewModel.getUserPhotoList.collectAsStateWithLifecycle()
    val stateOfProfileCollectionList by viewModel.getUserCollectionList.collectAsStateWithLifecycle()

    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        viewModel.handleUIEvent(ProfileDetailEvent.FetchUserDetailInfos)
        viewModel.handleUIEvent(ProfileDetailEvent.FetchUserPhotosList)
        viewModel.handleUIEvent(ProfileDetailEvent.FetchUserCollectionsList)
    }

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
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
    }) {
        ProfileDetailScreen(
            modifier = modifier,
            paddingValues = it,
            profileDetailState = stateOfProfileDetail,
            userPhotosState = stateOfProfilePhotoList,
            userCollectionState = stateOfProfileCollectionList,
            onUserPhotoListClick = { id ->
                onUserPhotoListClick.invoke(id)
            },
            onCollectionItemClick = { id, title ->
                onCollectionItemClick.invoke(id, title)
            },
            context = context,
        )
    }
}

@Composable
fun ProfileDetailScreen(
    modifier: Modifier,
    paddingValues: PaddingValues,
    profileDetailState: ProfileDetailState?,
    userPhotosState: UserPhotosState?,
    userCollectionState: UserCollectionState?,
    onUserPhotoListClick: (String) -> Unit,
    onCollectionItemClick: (String, String) -> Unit,
    context: Context
) {
    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {

        InteractionCountOfUser(
            modifier = modifier,
            profileDetailState = profileDetailState
        )
        PersonalInfoOfUser(
            modifier = modifier,
            profileDetailState = profileDetailState,
            context = context,
        )
        TabUI(
            modifier = modifier,
            profileDetailState = profileDetailState,
            userPhotosState = userPhotosState,
            userCollectionState = userCollectionState,
            onUserPhotoListClick = {id ->
                onUserPhotoListClick.invoke(id)
            },
            onCollectionItemClick = {id, title ->
                onCollectionItemClick.invoke(id,title)
            }
        )
    }
}

@Composable
fun InteractionCountOfUser(modifier: Modifier,profileDetailState: ProfileDetailState?) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 24.dp, end = 24.dp, top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = profileDetailState?.userDetails?.profileImage,
            contentDescription = "Profile Image",
            modifier = modifier
                .height(64.dp)
                .width(64.dp)
                .clip(RoundedCornerShape(64.dp))

        )
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = modifier.wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.posts_text),
                    fontFamily = regular,
                    fontSize = 14.sp
                )
                Text(
                    text = profileDetailState?.userDetails?.postCount.toString(),
                    fontFamily = regular,
                    fontSize = 14.sp
                )
            }
            Column(
                modifier = modifier.wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.followers_text),
                    fontFamily = regular,
                    fontSize = 14.sp
                )
                Text(
                    text = profileDetailState?.userDetails?.followersCount.toString(),
                    fontFamily = regular,
                    fontSize = 14.sp
                )
            }
            Column(
                modifier = modifier.wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.following_text),
                    fontFamily = regular,
                    fontSize = 14.sp
                )
                Text(
                    text = profileDetailState?.userDetails?.followingCount.toString(),
                    fontFamily = regular,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun PersonalInfoOfUser(
    modifier: Modifier,
    profileDetailState: ProfileDetailState?,
    context: Context
) {

    val fullText = context.getString(R.string.connect_with_user, profileDetailState?.userDetails?.username.orEmpty())

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        if (profileDetailState?.userDetails?.name?.isNotEmpty() == true) {
            Text(
                modifier = modifier.padding(horizontal = 16.dp),
                text = profileDetailState.userDetails.name,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 14.sp,
                fontFamily = bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = modifier.size(8.dp))
        }
        if (profileDetailState?.userDetails?.bio?.isNotEmpty() == true) {
            Text(
                modifier = modifier.padding(horizontal = 16.dp),
                text = profileDetailState.userDetails.bio,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 14.sp,
                fontFamily = regular,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                lineHeight = TextUnit(16f, TextUnitType.Sp)
            )
            Spacer(modifier = modifier.size(8.dp))
        }

        if (profileDetailState?.userDetails?.location?.isNotEmpty() == true) {
            Row(
                modifier = modifier
                    .wrapContentSize()
                    .padding(horizontal = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = "Location Icon"
                )
                Spacer(modifier = modifier.size(4.dp))
                Text(
                    modifier = modifier,
                    text = profileDetailState.userDetails.location,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 14.sp,
                    fontFamily = regular,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = modifier.size(8.dp))
            }
            Spacer(modifier = modifier.size(8.dp))
                val coloredText = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append(fullText)
                    }
                }
                Text(
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .clickable {},
                    text = coloredText,
                    fontSize = 14.sp,
                    fontFamily = medium,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                )

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabUI(
    modifier: Modifier,
    profileDetailState: ProfileDetailState?,
    userPhotosState: UserPhotosState?,
    userCollectionState: UserCollectionState?,
    onUserPhotoListClick: (String) -> Unit,
    onCollectionItemClick: (String, String) -> Unit
) {

    val tabItems = mutableListOf(
        "${stringResource(R.string.photos)} (${(profileDetailState?.userDetails?.totalPhotos)})",
        "${stringResource(R.string.collections_title)} (${(profileDetailState?.userDetails?.totalCollections)})"
    )
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    val pagerState = rememberPagerState {
        tabItems.size
    }

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = colorResource(id = R.color.orange),
            modifier = Modifier
                .padding(8.dp)
                .wrapContentHeight()
                .clip(RoundedCornerShape(64.dp)),
            divider = {
            },
            indicator = {

            }
        ) {

            tabItems.forEachIndexed { index, item ->
                val backgroundColor = if (index == selectedTabIndex) {
                    colorResource(id = R.color.white)
                } else {
                    colorResource(id = R.color.orange)
                }
                val selected = selectedTabIndex == index
                Tab(
                    modifier = if (selected) Modifier
                        .clip(RoundedCornerShape(64))
                        .background(colorResource(id = R.color.orange))
                    else Modifier
                        .clip(RoundedCornerShape(64))
                        .background(MaterialTheme.colorScheme.background),
                    selected = (index == selectedTabIndex),
                    onClick = {
                        selectedTabIndex = index
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(selectedTabIndex)
                        }
                    },
                    text = {
                        Text(
                            text = item, color = backgroundColor, fontSize = 14.sp,
                            fontFamily = medium,
                        )
                    },
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { index ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when(index) {
                   0 -> {
                       PhotoListOfUser(modifier = modifier, userPhotosState = userPhotosState, onUserPhotoListClick = {id ->
                           onUserPhotoListClick.invoke(id)
                       })
                   }
                    1 -> {
                        CollectionListOfUser(
                            modifier = modifier,
                            userCollectionState = userCollectionState,
                            onCollectionItemClick = { id, title ->
                                onCollectionItemClick.invoke(id, title)
                            }
                        )
                    }
                }
            }
        }
    }


    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
    }
}

@Composable
fun PhotoListOfUser(modifier: Modifier, userPhotosState: UserPhotosState?, onUserPhotoListClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxSize(),
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

@Composable
fun CollectionListOfUser(
    modifier: Modifier,
    userCollectionState: UserCollectionState?,
    onCollectionItemClick: (String, String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize(),
        state = rememberLazyGridState(),
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
            contentDescription = null,
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
fun CollectionListItem(modifier: Modifier, userCollection: UserCollections?, onCollectionItemClick: (String, String) -> Unit) {
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
            contentDescription = null,
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


// TODO: Text sizes for DropDown menu should be calculated, this feature will be developed later.         
/*
@Composable
fun DropdownMenuOfUserAccounts(
    modifier: Modifier, profileDetailState: ProfileDetailState?, onItemClick: (Int) -> Unit,
    menuVisible:Boolean
) {

    val items = remember { mutableListOf<UserSocialAccountsMenu>() }
    profileDetailState?.userDetails?.instagram?.let {
        items.add(
            0, UserSocialAccountsMenu(
                title = "Instagram",
                titleIcon = painterResource(id = R.drawable.icons8_instagram),
                UserSocialAccountsMenu.MenuItemType.INSTAGRAM
            )
        )
    }
    profileDetailState?.userDetails?.twitter?.let {
        items.add(
            1, UserSocialAccountsMenu(
                title = "Twitter",
                titleIcon = painterResource(id = R.drawable.icons8_twitterx),
                UserSocialAccountsMenu.MenuItemType.TWITTER
            )
        )
    }
    profileDetailState?.userDetails?.portfolio?.let {
        items.add(
            2, UserSocialAccountsMenu(
                title = "Portfolio",
                titleIcon = painterResource(id = R.drawable.portfolio_svgrepo_com),
                UserSocialAccountsMenu.MenuItemType.PORTFOLIO
            )
        )
    }

    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(menuVisible)
    }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }

        DropdownMenu(
            expanded = isContextMenuVisible, onDismissRequest = {
                isContextMenuVisible = false
            }, offset = pressOffset.copy(
                y = pressOffset.y - itemHeight
            )
        ) {
            items.forEach { title ->
                DropdownMenuItem(onClick = {
                    onItemClick(items.indexOf(title))
                    isContextMenuVisible = false
                }, text = {
                    Row(
                        modifier = modifier
                            .wrapContentSize()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = title.titleIcon, contentDescription = "UserAccounts"
                        )
                        Spacer(modifier = modifier.size(4.dp))
                        Text(
                            modifier = modifier,
                            text = title.title,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 14.sp,
                            fontFamily = regular,
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Start,
                        )
                        Spacer(modifier = modifier.size(8.dp))
                    }
                })
            }
        }
    }*/
