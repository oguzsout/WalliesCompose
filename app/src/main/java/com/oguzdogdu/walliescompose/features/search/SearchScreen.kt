package com.oguzdogdu.walliescompose.features.search

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.domain.model.search.SearchPhoto
import com.oguzdogdu.walliescompose.features.search.components.EmptyView
import com.oguzdogdu.walliescompose.features.search.components.ErrorItem
import com.oguzdogdu.walliescompose.features.search.components.SearchItem
import com.oguzdogdu.walliescompose.ui.theme.regular

@Composable
fun SearchScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    searchPhotoClick: (String) -> Unit,
    queryFromDetail: String?
) {
    val searchState: LazyPagingItems<SearchPhoto> =
        viewModel.searchListState.collectAsLazyPagingItems()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.handleUIEvent(SearchEvent.GetAppLanguageValue)
    }

    Scaffold(modifier = modifier.fillMaxSize()) {
        SearchScreen(paddingValues = it, onQuerySearch = { query ->
            viewModel::handleUIEvent.invoke(
                SearchEvent.EnteredSearchQuery(
                    query = query, language = appLanguage
                )
            )
        }, searchLazyPagingItems = searchState, onBackClick = {
            onBackClick.invoke()
        }, searchPhotoClick = { photoId ->
            searchPhotoClick.invoke(photoId)
        }, queryFromDetail = queryFromDetail.orEmpty())
    }
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    onQuerySearch: (String) -> Unit,
    searchLazyPagingItems: LazyPagingItems<SearchPhoto>,
    onBackClick: () -> Unit,
    searchPhotoClick: (String) -> Unit,
    queryFromDetail: String?
) {
    val isEmptyState by remember {
        derivedStateOf {
            searchLazyPagingItems.itemCount < 1
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchTextField(
            onBackClick = { onBackClick.invoke() },
            onQuerySearch = { onQuerySearch.invoke(it) },
            queryFromDetail = queryFromDetail
        )
        Spacer(modifier = modifier.size(8.dp))
        if (isEmptyState) {
            EmptySearchScreen()
        }
        SearchQueryList(
            searchLazyPagingItems = searchLazyPagingItems,
            searchPhotoClick = { searchPhotoClick.invoke(it) })

    }
}

@Composable
fun SearchQueryList(
    modifier: Modifier = Modifier,
    searchLazyPagingItems: LazyPagingItems<SearchPhoto>,
    searchPhotoClick: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(8.dp),
        state = rememberLazyGridState(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(count = searchLazyPagingItems.itemCount,
            key = searchLazyPagingItems.itemKey { item: SearchPhoto -> item.id.hashCode() },
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
fun SearchTextField(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onQuerySearch: (String) -> Unit,
    queryFromDetail: String?
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var queryString by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = queryFromDetail) {
        if (queryFromDetail?.isNotEmpty() == true) {
            queryString = queryFromDetail.toString()
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
            shape = ShapeDefaults.Medium,
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
            })
    }
}

@Composable
fun EmptySearchScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.wrapContentSize()) {
        EmptyView(modifier = modifier)
    }
}