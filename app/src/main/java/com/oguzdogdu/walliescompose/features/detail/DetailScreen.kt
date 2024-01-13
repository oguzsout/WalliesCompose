package com.oguzdogdu.walliescompose.features.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.oguzdogdu.walliescompose.R


@Composable
fun DetailScreenRoute(
    modifier: Modifier = Modifier,
    detailViewModel: DetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state = detailViewModel.userInfo.value
    Scaffold(modifier = modifier
        .fillMaxSize(), topBar = {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_small),
                contentDescription = "",
                tint = Color.Unspecified,
                modifier = modifier
                    .wrapContentSize()
                    .clickable {
                        onBackClick.invoke()
                    })

            Text(
                text = stringResource(id = R.string.collections_title),
                style = MaterialTheme.typography.titleMedium,
            )
            Icon(
                painter = painterResource(id = R.drawable.info),
                contentDescription = "",
                tint = Color.Unspecified,
                modifier = modifier
                    .wrapContentSize()
                    .clickable {

                    })
        }
    }){
        Column(modifier = modifier
            .padding(it)
            .fillMaxSize()) {
            Text(text = "Detail $state")
        }
    }
}