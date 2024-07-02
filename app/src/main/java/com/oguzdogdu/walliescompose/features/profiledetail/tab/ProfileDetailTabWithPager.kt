package com.oguzdogdu.walliescompose.features.profiledetail.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.profiledetail.CollectionListOfUser
import com.oguzdogdu.walliescompose.features.profiledetail.PhotoListOfUser
import com.oguzdogdu.walliescompose.features.profiledetail.state.ProfileDetailState
import com.oguzdogdu.walliescompose.features.profiledetail.state.UserCollectionState
import com.oguzdogdu.walliescompose.features.profiledetail.state.UserPhotosState
import com.oguzdogdu.walliescompose.ui.theme.medium
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailTabWithPager(
    profileDetailState: ProfileDetailState?,
    userPhotosState: UserPhotosState?,
    userCollectionState: UserCollectionState?,
    onUserPhotoListClick: (String) -> Unit,
    onCollectionItemClick: (String, String) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
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
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .padding(8.dp)
                .wrapContentHeight()
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
                verticalArrangement = Arrangement.Top
            ) {
                when(index) {
                    0 -> {
                        PhotoListOfUser(userPhotosState = userPhotosState, onUserPhotoListClick = {id ->
                            onUserPhotoListClick.invoke(id)
                        },scrollBehavior)
                    }
                    1 -> {
                        CollectionListOfUser(
                            userCollectionState = userCollectionState,
                            onCollectionItemClick = { id, title ->
                                onCollectionItemClick.invoke(id, title)
                            },scrollBehavior
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