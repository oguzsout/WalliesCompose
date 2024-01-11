package com.oguzdogdu.walliescompose.features.favorites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.ui.theme.regular
import kotlinx.coroutines.delay


@Composable
fun FavoritesScreenRoute(modifier: Modifier = Modifier,viewModel: FavoritesViewModel = hiltViewModel()) {
    val state = viewModel.moviesState.collectAsStateWithLifecycle()
    var visibleState by remember { mutableStateOf(false) }
    LaunchedEffect(state.value) {
        delay(300)
        visibleState = state.value
    }
    Column {
        Button(onClick = {viewModel.edit(!state.value) }) {
            Text(text = "Göster")
        }
        AnimatedVisibility(
            visible = visibleState,
            enter = expandHorizontally { 20 },
            exit = shrinkHorizontally(
                animationSpec = tween(),
                shrinkTowards = Alignment.End,
            )) {
            EmptyView(modifier = modifier,state = state.value)
        }

    }

}


@Composable
fun EmptyView(modifier: Modifier,state:Boolean) {
    if (state) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                painter = painterResource(id = R.drawable.no_picture),
                contentDescription = ""
            )
            Text(
                text = stringResource(id = R.string.no_picture_text),
                fontSize = 16.sp,
                fontFamily = regular
            )

        }
    }

}