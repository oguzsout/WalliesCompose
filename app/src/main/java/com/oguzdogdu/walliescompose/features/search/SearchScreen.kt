package com.oguzdogdu.walliescompose.features.search

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.domain.model.search.SearchPhoto
import com.oguzdogdu.walliescompose.domain.model.search.searchuser.SearchUser
import com.oguzdogdu.walliescompose.features.search.components.EmptySearchUserListView
import com.oguzdogdu.walliescompose.features.search.components.EmptyView
import com.oguzdogdu.walliescompose.features.search.components.ErrorItem
import com.oguzdogdu.walliescompose.features.search.components.SearchItem
import com.oguzdogdu.walliescompose.features.search.components.SpeechToTextDialog
import com.oguzdogdu.walliescompose.navigation.utils.WalliesIcons
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular
import com.oguzdogdu.walliescompose.util.mediumBackground
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

@Composable
fun SearchScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    searchPhotoClick: (String) -> Unit,
    searchUserClick: (String) -> Unit,
    queryFromDetail: String?
) {
    val searchPhotoListState: LazyPagingItems<SearchPhoto> =
        viewModel.searchListState.collectAsLazyPagingItems()
    val searchUserListState: LazyPagingItems<SearchUser> =
        viewModel.searchUserListState.collectAsLazyPagingItems()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    val stateOfSearchScreen by viewModel.searchScreenState.collectAsStateWithLifecycle()
    val currentLanguage = LocalContext.current.resources.configuration.locales[0].language

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.handleUIEvent(SearchEvent.GetAppLanguageValue)
    }

    LaunchedEffect(key1 = Unit) {
        if (queryFromDetail.isNullOrBlank().not()) {
            viewModel.handleUIEvent(
                SearchEvent.EnteredSearchQuery(
                    queryFromDetail.orEmpty(),
                    currentLanguage
                )
            )
        }
    }

    Scaffold(modifier = modifier.fillMaxSize()) {
        SearchScreen(
            query = query,
            searchState = stateOfSearchScreen,
            paddingValues = it,
            onQuerySearch = { query ->
                viewModel::handleUIEvent.invoke(
                    SearchEvent.EnteredSearchQuery(
                        query = query, language = appLanguage
                    )
                )
            },
            searchLazyPagingItems = searchPhotoListState,
            searchUserLazyPagingItems = searchUserListState,
            onBackClick = {
                onBackClick.invoke()
            },
            openSpeechDialog = { isOpen ->
                viewModel.handleUIEvent(SearchEvent.OpenSpeechDialog(isOpen))
            },
            searchPhotoClick = { photoId ->
                searchPhotoClick.invoke(photoId)
            },
            searchUserClick = { userId ->
                searchUserClick.invoke(userId)
            },
            queryFromDetail = queryFromDetail.orEmpty()
        )
    }
}

@Composable
fun SearchScreen(
    query: String?,
    searchState: SearchState,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    onQuerySearch: (String) -> Unit,
    searchLazyPagingItems: LazyPagingItems<SearchPhoto>,
    searchUserLazyPagingItems: LazyPagingItems<SearchUser>,
    onBackClick: () -> Unit,
    openSpeechDialog: (Boolean) -> Unit,
    searchPhotoClick: (String) -> Unit,
    searchUserClick: (String) -> Unit,
    queryFromDetail: String?
) {
    var photoListPosition by remember {
        mutableIntStateOf(0)
    }

    var userListPosition by remember {
        mutableIntStateOf(0)
    }

    var tabIndex by remember {
        mutableIntStateOf(0)
    }

    var listPositionForVisibility by remember {
        mutableStateOf(true)
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var permission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val recordAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permission = isGranted
            if (isGranted) {
                openSpeechDialog.invoke(true)
            }
        }
    )

    LaunchedEffect(photoListPosition,userListPosition,tabIndex) {
        listPositionForVisibility = when (tabIndex) {
                0 -> photoListPosition < 2
                1 -> userListPosition < 2
                else -> false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (searchState.speechDialogState) {
            SpeechToTextDialog(
                onDismiss = {
                    openSpeechDialog.invoke(false)
                },
                searchState = searchState,
                spokenText = {
                    onQuerySearch.invoke(it)
                }
            )
        }

        val rotation by animateFloatAsState(
            targetValue = if (listPositionForVisibility) 0f else 90f,
            animationSpec = tween(1000),
            label = ""
        )
        AnimatedVisibility(
            visible = listPositionForVisibility,
            enter = expandVertically(
                tween(1000)
            ),
            exit = shrinkVertically(
                animationSpec = tween(1000)
            )
            )
         {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        rotationX = rotation
                        cameraDistance = 12 * density
                    }
            ) {
                SearchTextField(
                    query = query,
                    onBackClick = { onBackClick.invoke() },
                    onQuerySearch = { onQuerySearch.invoke(it) },
                    queryFromDetail = queryFromDetail,
                    openSpeechDialog = {
                        coroutineScope.launch {
                            when {
                                !permission -> {
                                    coroutineScope.launch {
                                        recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                    }
                                }

                                else -> {
                                    openSpeechDialog.invoke(it)
                                }
                            }
                        }
                    }
                )
            }
        }

        TabPagerSearchScreen(searchLazyPagingItems = searchLazyPagingItems,
            searchUserLazyPagingItems = searchUserLazyPagingItems,
            searchPhotoClick = { searchPhotoClick.invoke(it) },
            searchUserClick = { searchUserClick.invoke(it) },
            photoListPosition = {
                photoListPosition = it
            },
            userListPosition = {
                userListPosition = it
            },
            tabIndex = {
                tabIndex = it
            }
        )

    }
}

@Composable
fun TabPagerSearchScreen(
    modifier: Modifier = Modifier,
    searchLazyPagingItems: LazyPagingItems<SearchPhoto>,
    searchUserLazyPagingItems: LazyPagingItems<SearchUser>,
    searchPhotoClick: (String) -> Unit,
    searchUserClick: (String) -> Unit,
    photoListPosition: (Int) -> Unit,
    userListPosition: (Int) -> Unit,
    tabIndex: (Int) -> Unit
) {
    val tabItems = listOf(
        stringResource(R.string.photos),
        stringResource(R.string.users)
    )
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    val pagerState = rememberPagerState {
        tabItems.size
    }
    val coroutineScope = rememberCoroutineScope()

    val isPhotoListEmptyState by remember {
        derivedStateOf {
            searchLazyPagingItems.itemCount < 1
        }
    }

    val isUserListEmptyState by remember {
        derivedStateOf {
            searchUserLazyPagingItems.itemCount < 1
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        snapshotFlow {
            pagerState.currentPage
        }.collectLatest {
            selectedTabIndex = it
            tabIndex.invoke(it)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .padding(8.dp)
                .wrapContentHeight()
                .border(
                    1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(64.dp)
                )
                .clip(RoundedCornerShape(64.dp)),
            divider = {},
            indicator = {}
        ) {

            tabItems.forEachIndexed { index, item ->
                val tabTextColor = if (index == selectedTabIndex) {
                    colorResource(id = R.color.white)
                } else {
                    colorResource(id = R.color.orange)
                }
                val selected = selectedTabIndex == index
                Tab(
                    modifier = if (selected) modifier
                        .clip(RoundedCornerShape(64))
                        .background(colorResource(id = R.color.orange))
                    else modifier
                        .clip(RoundedCornerShape(64))
                        .background(MaterialTheme.colorScheme.background),
                    selected = (index == selectedTabIndex),
                    onClick = {
                        selectedTabIndex = index
                        coroutineScope.launch {
                            pagerState.scrollToPage(selectedTabIndex)
                        }
                    },
                    text = {
                        Text(
                            text = item, color = tabTextColor, fontSize = 14.sp,
                            fontFamily = medium,
                        )
                    },
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = modifier.fillMaxWidth()
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
                        if (isPhotoListEmptyState) {
                            EmptySearchScreen()
                        } else {
                            SearchQueryList(
                                searchLazyPagingItems = searchLazyPagingItems,
                                searchPhotoClick = searchPhotoClick,
                                listPosition = {listPositionState ->
                                    photoListPosition.invoke(listPositionState)
                                }
                            )
                        }

                    }
                    1 -> {
                        if (isUserListEmptyState) {
                            EmptySearchUserScreen()
                        } else {
                            SearchUserQueryList(
                                searchUserLazyPagingItems = searchUserLazyPagingItems,
                                searchUserClick = searchUserClick,
                                listPosition = {
                                    userListPosition.invoke(it)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(FlowPreview::class)
@Composable
fun SearchQueryList(
    modifier: Modifier = Modifier,
    searchLazyPagingItems: LazyPagingItems<SearchPhoto>,
    searchPhotoClick: (String) -> Unit,
    listPosition: (Int) -> Unit
) {
    val listState = rememberLazyGridState()

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }.debounce(100).collectLatest {
            listPosition.invoke(it)
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        state = listState,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(count = searchLazyPagingItems.itemCount,
            key = searchLazyPagingItems.itemKey { item: SearchPhoto -> item.url.hashCode() },
            contentType = searchLazyPagingItems.itemContentType { "Search Items" }) { index: Int ->
            val searchPhoto: SearchPhoto? = searchLazyPagingItems[index]
            if (searchPhoto != null) {
                SearchItem(searchPhoto = searchPhoto,
                    onSearchPhotoClick = { searchPhotoClick.invoke(it) })
            }
        }

        searchLazyPagingItems.apply {
            when {
                searchLazyPagingItems.loadState.source.refresh is LoadState.Loading -> {
                    item(span = { GridItemSpan(2) }) {
                        Box(
                            modifier = modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator(
                                modifier = modifier.align(Alignment.Center)
                            )

                        }
                    }
                }

                loadState.refresh is LoadState.Error || loadState.append is LoadState.Error -> {
                    val errorMessage =
                        (loadState.refresh as? LoadState.Error)?.error?.localizedMessage.orEmpty()
                    item(span = { GridItemSpan(2) }) {
                        ErrorItem(message = errorMessage)
                    }
                }

                loadState.source.append is LoadState.Loading -> {
                    item(span = { GridItemSpan(2) }) {
                        Box(
                            modifier = modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                        ) {
                            CircularProgressIndicator(
                                modifier = modifier.align(Alignment.BottomCenter)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchUserQueryList(
    modifier: Modifier = Modifier,
    searchUserLazyPagingItems: LazyPagingItems<SearchUser>,
    searchUserClick: (String) -> Unit,
    listPosition: (Int) -> Unit
) {

    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }.debounce(100).collectLatest {
            listPosition.invoke(it)
        }
    }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(count = searchUserLazyPagingItems.itemCount,
            key = searchUserLazyPagingItems.itemKey { item: SearchUser -> item.username.hashCode() },
            contentType = searchUserLazyPagingItems.itemContentType { "Search Items" }) { index: Int ->
            val searchUser: SearchUser? = searchUserLazyPagingItems[index]
            if (searchUser?.id?.isNotEmpty() == true and (searchUser?.name?.isNotEmpty() == true)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier
                        .heightIn(min = 64.dp, max = 96.dp)
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = mediumBackground(
                                index = index,
                                size = searchUserLazyPagingItems.itemCount
                            )
                        )
                ) {
                    SearchUserListItem(
                        searchUser = searchUser, searchUserClick = {
                            searchUserClick.invoke(it)
                        })
                    if (index < searchUserLazyPagingItems.itemCount - 1) {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),color = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }
        }

        searchUserLazyPagingItems.apply {
            when {
                searchUserLazyPagingItems.loadState.source.refresh is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator(
                                modifier = modifier.align(Alignment.Center)
                            )

                        }
                    }
                }

                loadState.refresh is LoadState.Error || loadState.append is LoadState.Error -> {
                    val errorMessage =
                        (loadState.refresh as? LoadState.Error)?.error?.localizedMessage.orEmpty()
                    item {
                        ErrorItem(message = errorMessage)
                    }
                }

                loadState.source.append is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                        ) {
                            CircularProgressIndicator(
                                modifier = modifier.align(Alignment.BottomCenter)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SearchTextField(
    query: String?,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onQuerySearch: (String) -> Unit,
    queryFromDetail: String?,
    openSpeechDialog: (Boolean) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var queryString by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = queryFromDetail) {
        if (queryFromDetail != null) {
            queryString = queryFromDetail
        }
    }

    LaunchedEffect(key1 = query) {
        if (query != null) {
            queryString = query
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = CenterVertically
    ) {
        TextField(modifier = modifier.fillMaxWidth(),
            value = queryString,
            onValueChange = {
                queryString = it
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.search),
                    modifier = modifier,
                    fontSize = 16.sp,
                    fontFamily = regular,
                    textAlign = TextAlign.Center
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {
                onQuerySearch.invoke(queryString)
                keyboardController?.hide()
            }),
            shape = RoundedCornerShape(64.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = { onBackClick.invoke() }, modifier = modifier.wrapContentSize()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = modifier.wrapContentSize()
                    )
                }
            },
            trailingIcon = {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .animateContentSize(),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = { openSpeechDialog.invoke(true) }, modifier = modifier.wrapContentSize()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.round_mic_24),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = modifier.wrapContentSize()
                        )
                    }
                    if (queryString.isNotEmpty()) {
                        IconButton(
                            onClick = { queryString = "" }, modifier = modifier.wrapContentSize()
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = modifier.wrapContentSize()
                            )
                        }
                    }
                }

            })
    }
}

@Composable
fun EmptySearchScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.wrapContentSize()) {
        EmptyView(modifier = modifier)
    }
}

@Composable
fun EmptySearchUserScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.wrapContentSize()) {
        EmptySearchUserListView()
    }
}

@Composable
fun SearchUserListItem(
    modifier: Modifier = Modifier,
    searchUser: SearchUser,
    searchUserClick: (String) -> Unit
) {
    Row(
        modifier = modifier
            .height(72.dp)
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                searchUserClick.invoke(searchUser.username.orEmpty())
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = CenterVertically
    ) {
        AsyncImage(
            model = if (searchUser.profileImage?.isNotEmpty() == true) searchUser.profileImage else WalliesIcons.DefaultAvatar,
            contentScale = ContentScale.FillBounds,
            contentDescription = "Profile Image",
            modifier = modifier
                .size(32.dp)
                .clip(RoundedCornerShape(64.dp))
                .border(1.dp, Color.DarkGray, shape = RoundedCornerShape(64.dp))
        )
        Spacer(modifier = modifier.width(8.dp))
        Text(
            text = searchUser.username.orEmpty(), maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleSmall
        )
    }
}