package com.oguzdogdu.walliescompose.features.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.domain.model.detail.Photo
import com.oguzdogdu.walliescompose.ui.theme.regular
import com.oguzdogdu.walliescompose.util.formatDate
import com.oguzdogdu.walliescompose.util.toFormattedString

@Composable
fun DetailPhotoAttributesRow(modifier: Modifier,detail: Photo?) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.views_text),
                fontFamily = regular,
                fontSize = 12.sp
            )
            Text(
                text = detail?.views?.toFormattedString().orEmpty(),
                fontFamily = regular,
                fontSize = 12.sp
            )
        }
        Column(
            modifier = modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.download_text),
                fontFamily = regular,
                fontSize = 12.sp
            )
            Text(
                text = detail?.downloads?.toFormattedString().orEmpty(),
                fontFamily = regular,
                fontSize = 12.sp
            )
        }
        Column(
            modifier = modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.created_at),
                fontFamily = regular,
                fontSize = 12.sp
            )
            Text(
                text = detail?.createdAt?.formatDate().orEmpty(),
                fontFamily = regular,
                fontSize = 12.sp
            )
        }
        Column(
            modifier = modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.like_text),
                fontFamily = regular,
                fontSize = 12.sp
            )
            Text(
                text = detail?.likes?.toFormattedString().orEmpty(),
                fontFamily = regular,
                fontSize = 12.sp
            )
        }
    }
}