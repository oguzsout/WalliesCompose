package com.oguzdogdu.walliescompose.features.profiledetail.components

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.domain.model.userdetail.UserDetails
import com.oguzdogdu.walliescompose.features.profiledetail.state.ProfileDetailState
import com.oguzdogdu.walliescompose.ui.theme.bold
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular


@Composable
fun PersonalInfoOfUser(
    profileDetailState: ProfileDetailState?,
    context: Context,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (profileDetailState?.userDetails?.name?.isNotEmpty() == true) {
            Row(
                modifier = Modifier.animateContentSize(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = profileDetailState.userDetails.name,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 20.sp,
                    fontFamily = bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(8.dp))
                AnimatedVisibility(visible = profileDetailState.userDetails.verification?.isNotEmpty() == true) {
                    UserVerificationBadge(
                        visible = true,
                        petalColor = colorResource(R.color.lush_green),
                        centerColor = Color.White,
                        checkmarkColor = Color.White
                    )
                }
                Spacer(modifier = Modifier.size(4.dp))
            }
        }
        if (profileDetailState?.userDetails?.bio?.isNotEmpty() == true) {
            Text(
                text = profileDetailState.userDetails.bio,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 14.sp,
                fontFamily = regular,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = modifier.size(4.dp))
        }

        if (profileDetailState?.userDetails?.location?.isNotEmpty() == true) {
            Row(
                modifier = modifier
                    .wrapContentSize()
                    .align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = "Location Icon",
                    modifier = Modifier.wrapContentSize(),
                    tint = Color.Red
                )
                Spacer(modifier = modifier.size(4.dp))
                Text(
                    modifier = modifier,
                    text = profileDetailState.userDetails.location,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 14.sp,
                    fontFamily = medium,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = modifier.size(4.dp))
            }
        }
        Spacer(modifier = modifier.size(8.dp))
        if (profileDetailState?.userDetails?.portfolioList?.isNotEmpty() == true) {
            PersonalAccountMenu(
                profileDetailState = profileDetailState,
                context = context,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
@Preview
@Composable
fun PersonalInfoOfUserPreview() {
    PersonalInfoOfUser(
        profileDetailState = ProfileDetailState(userDetails =
        UserDetails(
            name = "James",
            bio = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
            profileImage = null,
            postCount = 100,
            followingCount = 100,
            followersCount = 100,
            portfolioUrl = null,
            location = "England",
            username = null,
            totalPhotos = null,
            totalCollections = null,
            instagram = null,
            twitter = null,
            verification = "",
            portfolioList = emptyList(),
            portfolio = null,
            forHire = false
        )
        ), context = androidx.compose.ui.platform.LocalContext.current
    )
}