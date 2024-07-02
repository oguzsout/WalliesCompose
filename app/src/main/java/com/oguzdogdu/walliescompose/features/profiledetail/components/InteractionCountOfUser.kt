package com.oguzdogdu.walliescompose.features.profiledetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.domain.model.userdetail.UserDetails
import com.oguzdogdu.walliescompose.features.profiledetail.state.ProfileDetailState
import com.oguzdogdu.walliescompose.ui.theme.bold
import com.oguzdogdu.walliescompose.ui.theme.regular

@Composable
fun InteractionCountOfUser(profileDetailState: ProfileDetailState?, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(64.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
                text = "${profileDetailState?.userDetails?.followersCount ?: 0}",
                fontFamily = bold,
                fontSize = 18.sp
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
                text = "${profileDetailState?.userDetails?.followingCount ?: 0}",
                fontFamily = bold,
                fontSize = 18.sp
            )
        }
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
                text = "${profileDetailState?.userDetails?.postCount ?: 0}",
                fontFamily = bold,
                fontSize = 18.sp
            )
        }
    }
}

@Preview
@Composable
fun InteractionCountOfUserPreview() {
    InteractionCountOfUser(
        profileDetailState = ProfileDetailState(userDetails =
            UserDetails(
                name = "James",
                bio = null,
                profileImage = null,
                postCount = 100,
                followingCount = 100,
                followersCount = 100,
                portfolioUrl = null,
                location = null,
                username = null,
                totalPhotos = null,
                totalCollections = null,
                instagram = null,
                twitter = null,
                portfolioList = emptyList(),
                portfolio = null,
                forHire = false
            )
        )
    )
}