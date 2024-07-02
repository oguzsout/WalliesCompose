package com.oguzdogdu.walliescompose.features.profiledetail.components

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.domain.model.userdetail.UserDetails
import com.oguzdogdu.walliescompose.features.profiledetail.UserSocialAccountsMenu
import com.oguzdogdu.walliescompose.features.profiledetail.state.ProfileDetailState
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular
import com.oguzdogdu.walliescompose.util.openInstagramProfile
import com.oguzdogdu.walliescompose.util.openPortfolioUrl
import com.oguzdogdu.walliescompose.util.openTwitterProfile

@Composable
fun PersonalAccountMenu(profileDetailState: ProfileDetailState?, context: Context, modifier: Modifier = Modifier) {
    val listItem = mutableListOf<UserSocialAccountsMenu>()
    val openProfileLinks = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {}
    val fullText = context.getString(R.string.connect_with_user, profileDetailState?.userDetails?.name.orEmpty())

    var expanded by remember { mutableStateOf(false) }

    if (profileDetailState?.userDetails?.instagram?.isNotEmpty() == true) {
        listItem.add(
            UserSocialAccountsMenu(
                title = "Instagram", titleIcon = painterResource(
                    id = R.drawable.icons8_instagram
                )
            )
        )
    }
    if (profileDetailState?.userDetails?.twitter?.isNotEmpty() == true) {
        listItem.add(
            UserSocialAccountsMenu(
                title = "Twitter", titleIcon = painterResource(
                    id = R.drawable.icons8_twitterx
                )
            )
        )
    }
    if (profileDetailState?.userDetails?.portfolio?.isNotEmpty() == true) {
        listItem.add(
            UserSocialAccountsMenu(
                title = "Portfolio", titleIcon = painterResource(
                    id = R.drawable.portfolio_svgrepo_com
                )
            )
        )
    }

    Box(modifier = modifier.wrapContentSize()) {
        OutlinedButton(onClick = { expanded = !expanded }) {
            Text(
                text = fullText,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontFamily = medium,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }, offset = DpOffset(x = 48.dp, y = 8.dp)
        ) {
            listItem.forEach {  itemValue ->
                DropdownMenuItem(
                    onClick = {
                        when(itemValue.title) {
                            "Instagram" -> {
                                profileDetailState?.userDetails?.instagram?.openInstagramProfile()
                                    ?.let { openProfileLinks.launch(it) }
                            }
                            "Twitter" -> {
                                profileDetailState?.userDetails?.twitter?.openTwitterProfile()
                                    ?.let { openProfileLinks.launch(it) }

                            }
                            "Portfolio" -> {
                                profileDetailState?.userDetails?.portfolio?.openPortfolioUrl()
                                    ?.let { openProfileLinks.launch(it) }
                            }
                        }

                        expanded = false
                    }, text = {
                        Row(
                            modifier.wrapContentSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(painter = itemValue.titleIcon, contentDescription = "", tint = Color.Unspecified)
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(text = itemValue.title,fontSize = 14.sp, fontFamily = regular,)
                        }
                    }
                )
            }
        }
    }
}
@Preview
@Composable
fun PersonalAccountMenuPreview() {
    PersonalAccountMenu(
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
        ), context = androidx.compose.ui.platform.LocalContext.current
    )
}