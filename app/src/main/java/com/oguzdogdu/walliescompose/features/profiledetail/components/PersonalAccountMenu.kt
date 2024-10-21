package com.oguzdogdu.walliescompose.features.profiledetail.components

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.domain.model.userdetail.UserDetails
import com.oguzdogdu.walliescompose.features.profiledetail.UserSocialAccountsMenu
import com.oguzdogdu.walliescompose.features.profiledetail.state.ProfileDetailState
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular
import com.oguzdogdu.walliescompose.util.mediumBackground
import com.oguzdogdu.walliescompose.util.openInstagramProfile
import com.oguzdogdu.walliescompose.util.openPortfolioUrl
import com.oguzdogdu.walliescompose.util.openTwitterProfile
import kotlinx.coroutines.delay

@Composable
fun PersonalAccountMenu(profileDetailState: ProfileDetailState?, context: Context, modifier: Modifier = Modifier) {
    val listItem = mutableListOf<UserSocialAccountsMenu>()
    val openProfileLinks = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {}

    val density = LocalDensity.current
    var offsetX by remember { mutableStateOf(0.dp) }

    var parentWidth by remember { mutableIntStateOf(0) }
    var dropdownWidth by remember { mutableIntStateOf(0) }

    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontFamily = medium
            )
        ) {
            append(context.getString(R.string.connect_with_user, ""))
        }
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.secondaryContainer,
                fontSize = 14.sp,
                fontFamily = medium
            )
        ) {
            append(profileDetailState?.userDetails?.name.orEmpty())
        }
    }

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
                title = "X", titleIcon = painterResource(
                    id = R.drawable.icons8_twitterx
                )
            )
        )
    }
    if (profileDetailState?.userDetails?.portfolio?.isNotEmpty() == true) {
        listItem.add(
            UserSocialAccountsMenu(
                title = "Portfolio", titleIcon = painterResource(
                    id = R.drawable.portfolio
                )
            )
        )
    }

    Column(
        modifier = modifier
            .wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedButton(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .wrapContentSize()
                .onPlaced {
                    parentWidth = it.size.width
                }
        ) {
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = annotatedString,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    textAlign = TextAlign.Center
                )
                Image(
                    painter = painterResource(id = R.drawable.round_arrow_drop_down_24),
                    contentDescription = "DropDown Icon",
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
                .onPlaced {
                    dropdownWidth = it.size.width
                    val popUpWidthPx = parentWidth - dropdownWidth

                    offsetX = with(density) {
                        (popUpWidthPx / 2).toDp()
                    }
                }
                .background(MaterialTheme.colorScheme.background)
                .crop(vertical = 8.dp),
            offset = DpOffset(offsetX, 4.dp),
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp
        ) {
            listItem.forEachIndexed { index, itemValue ->
                val delayMillis = index * 500
                val visible = remember { mutableStateOf(false) }

                LaunchedEffect(expanded) {
                    if (expanded) {
                        visible.value = false
                        delay(delayMillis.toLong())
                        visible.value = true
                    } else {
                        visible.value = false
                    }
                }
                AnimatedDropdownMenuItem(
                    visible = visible.value,
                    listIndex = index,
                    listSize = listItem.size
                ) {
                    DropdownMenuItem(
                        onClick = {
                            when (itemValue.title) {
                                "Instagram" -> {
                                    profileDetailState?.userDetails?.instagram?.openInstagramProfile()
                                        ?.let { openProfileLinks.launch(it) }
                                }

                                "X" -> {
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
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = itemValue.titleIcon,
                                    contentDescription = "",
                                    tint = Color.Unspecified
                                )
                                Spacer(modifier = Modifier.size(4.dp))
                                Text(text = itemValue.title, fontSize = 14.sp, fontFamily = regular)
                            }
                        }
                    )
                }
            }
        }
    }
}

fun Modifier.crop(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp,
): Modifier = this.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    fun Dp.toPxInt(): Int = this.toPx().toInt()

    layout(
        placeable.width - (horizontal * 2).toPxInt(),
        placeable.height - (vertical * 2).toPxInt()
    ) {
        placeable.placeRelative(-horizontal.toPx().toInt(), -vertical.toPx().toInt())
    }
}
@Composable
fun AnimatedDropdownMenuItem(
    visible: Boolean,
    listIndex: Int,
    listSize: Int,
    content: @Composable () -> Unit
) {

    val scale = animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f,
        animationSpec = tween(
            durationMillis = 750,
            easing = EaseInBounce
        ), label = ""
    )

    val alpha = animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 750,
            easing = LinearEasing
        ), label = ""
    )

    val rotation = animateFloatAsState(
        targetValue = if (visible) 0f else 90f,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing
        ), label = ""
    )
    Box(
        modifier = Modifier
            .graphicsLayer {
                this.alpha = alpha.value
                rotationX = rotation.value
                scaleX = scale.value
                scaleY = scale.value
            }
            .clip(mediumBackground(index = listIndex, size = listSize))
            .background(Color.Gray.copy(alpha = 0.2f))
    ) {
        content()
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
            verification = "",
            portfolioList = emptyList(),
            portfolio = null,
            forHire = false
        )
        ), context = androidx.compose.ui.platform.LocalContext.current
    )
}