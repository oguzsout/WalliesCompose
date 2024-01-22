package com.oguzdogdu.walliescompose.features.search

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.launch

@Composable
fun SearchScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val searchState: LazyPagingItems<SearchPhoto> =
        viewModel.searchListState.collectAsLazyPagingItems()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()

    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        viewModel.handleUIEvent(SearchEvent.GetAppLanguageValue)
    }

    val contextForToast = LocalContext.current.applicationContext

    Column {
        SearchScreen(modifier = modifier, onQuerySearch = { query ->
            viewModel.handleUIEvent(
                SearchEvent.EnteredSearchQuery(
                    query = query, language = appLanguage
                )
            )
        }, searchLazyPagingItems = searchState, onBackClick = {
            onBackClick.invoke()
        }, searchPhotoClick = {
            Toast.makeText(contextForToast, it, Toast.LENGTH_LONG).show()
        })
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    modifier: Modifier,
    onQuerySearch: (String) -> Unit,
    searchLazyPagingItems: LazyPagingItems<SearchPhoto>,
    onBackClick: () -> Unit,
    searchPhotoClick: (String) -> Unit
) {
    var queryString by remember {
        mutableStateOf("")
    }
    val isEmptyState by remember {
        derivedStateOf {
            searchLazyPagingItems.itemCount < 1
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = CenterVertically
        ) {
            IconButton(
                onClick = { onBackClick.invoke() }, modifier = modifier.wrapContentSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = modifier.wrapContentSize()
                )
            }
            Spacer(modifier = modifier.size(8.dp))
            TextField(modifier = modifier.fillMaxWidth(),
                value = queryString,
                onValueChange = { queryString = it },
                placeholder = { Text(stringResource(R.string.search)) },
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

        Spacer(modifier = modifier.size(8.dp))

        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isEmptyState) {
                EmptyView(modifier = modifier)
            }
            when {
                searchLazyPagingItems.loadState.refresh is LoadState.Loading -> {
                    Box(
                        modifier = modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator(
                            modifier = modifier.align(Alignment.Center)
                        )

                    }
                }

                searchLazyPagingItems.loadState.refresh is LoadState.Error ||
                        searchLazyPagingItems.loadState.append is LoadState.Error -> {
                    ErrorItem(modifier = modifier, message = "Error")
                }

                searchLazyPagingItems.loadState.refresh is LoadState.NotLoading -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = modifier.padding(horizontal = 8.dp),
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
                                    onCollectionClick = { searchPhotoClick.invoke(it) })
                            }
                        }
                    }
                }
            }
        }
    }
}