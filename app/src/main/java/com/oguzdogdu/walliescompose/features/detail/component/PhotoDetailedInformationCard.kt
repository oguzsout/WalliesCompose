package com.oguzdogdu.walliescompose.features.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages
import com.oguzdogdu.walliescompose.features.detail.DetailState
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular

@Composable
fun PhotoDetailedInformationCard(
    state: DetailState,
    onSetWallpaperClick: (Boolean) -> Unit,
    onShareClick: (String) -> Unit,
    onDownloadClick: (Boolean) -> Unit,
    onAddFavoriteClick: (FavoriteImages) -> Unit,
    onRemoveFavoriteClick: (FavoriteImages) -> Unit,
    onTagClick:(String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ), shape = RoundedCornerShape(
           16.dp
        ), modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp, max = 360.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = state.detail?.profileimage,
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .height(48.dp)
                                .width(48.dp)
                                .clip(RoundedCornerShape(64.dp))

                        )
                        Column(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(start = 8.dp)
                        ) {
                            Text(
                                text = state.detail?.username.orEmpty(),
                                fontFamily = medium,
                                fontSize = 16.sp
                            )
                            Text(
                                text = state.detail?.portfolio.orEmpty(),
                                fontFamily = regular,
                                fontSize = 12.sp
                            )
                        }
                    }
                    WalliesFavoriteButton(modifier = Modifier, favoriteImages = FavoriteImages(
                        id = state.favorites?.id,
                        url = state.favorites?.url,
                        profileImage = state.favorites?.profileImage,
                        name = state.favorites?.name,
                        portfolioUrl = state.favorites?.portfolioUrl,
                        isChecked = state.favorites?.isChecked ?: false
                    ), addPhotoToFavorites = { favorite ->
                        onAddFavoriteClick.invoke(favorite)
                    }, removePhotoFromFavorites = { favorite ->
                        onRemoveFavoriteClick.invoke(favorite)
                    })
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 16.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                DetailPhotoAttributesRow(modifier = Modifier, detail = state.detail)
                HorizontalDivider(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 16.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                DetailTagsRow(detail = state.detail, onTagClick = {
                    onTagClick.invoke(it)
                })
                DetailTripleActionButtons(setWallpaperButtonClick = {isOpen ->
                    onSetWallpaperClick.invoke(isOpen)
                }, shareButtonClick = {
                    onShareClick.invoke(state.detail?.urls.orEmpty())
                }, downloadButtonClick = {
                    onDownloadClick.invoke(it)
                })
            }
        }
    }
}