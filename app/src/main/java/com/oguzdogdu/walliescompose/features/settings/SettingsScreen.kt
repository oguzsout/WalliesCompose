package com.oguzdogdu.walliescompose.features.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.component.BaseCenteredToolbar
import com.oguzdogdu.walliescompose.features.settings.components.SingleSelectDialog
import com.oguzdogdu.walliescompose.util.OptionLists
import com.oguzdogdu.walliescompose.util.menuRow
import kotlinx.coroutines.launch


@Composable
fun SettingsScreenRoute(
    modifier: Modifier = Modifier, viewModel: SettingsViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val settingsUiState by viewModel.settingsState.collectAsStateWithLifecycle()

    val themes = listOf(
        ThemeValues.LIGHT_MODE.title, ThemeValues.DARK_MODE.title, ThemeValues.SYSTEM_DEFAULT.title
    )
    var themeLocation by remember {
        mutableIntStateOf(0)
    }
    LaunchedEffect(key1 = settingsUiState.getThemeValue) {
        viewModel.handleScreenEvents(SettingsScreenEvent.ThemeChanged)
        themeLocation = if (settingsUiState.getThemeValue.isNullOrEmpty()) {
            2
        } else {
            themes.indexOf(settingsUiState.getThemeValue)
        }
    }

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        BaseCenteredToolbar(modifier = Modifier,
            title = stringResource(id = R.string.settings),
            leftClick = {},
            rightClick = {})
    }) {
        Column(modifier = modifier.padding(it)) {
            if (settingsUiState.openThemeDialog) {
                SingleSelectDialog(modifier = modifier,
                    title = stringResource(id = R.string.choise_theme),
                    optionsList = themes,
                    defaultSelected = themeLocation,
                    submitButtonText = stringResource(id = R.string.ok),
                    dismissButtonText = stringResource(id = R.string.cancel),
                    onSubmitButtonClick = { id ->
                        coroutineScope.launch {
                            viewModel.handleScreenEvents(SettingsScreenEvent.SetNewTheme(themes[id]))
                            viewModel.handleScreenEvents(SettingsScreenEvent.ThemeChanged)
                        }
                    },
                    onDismissRequest = { value ->
                        coroutineScope.launch {
                            viewModel.handleScreenEvents(SettingsScreenEvent.OpenThemeDialog(value))
                        }
                    })

            }
            SettingsScreen(modifier = modifier, clickRow = { value ->
                viewModel.handleScreenEvents(SettingsScreenEvent.OpenThemeDialog(value))
            })

        }
    }
}


@Composable
fun SettingsScreen(modifier: Modifier, clickRow: (Boolean) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val optionList = OptionLists.appOptionsList

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        menuRow(data = optionList, modifier = modifier.fillMaxWidth(), background = { index ->
            when (optionList.size) {
                1 -> Shapes().medium.copy(
                    topStart = CornerSize(16.dp),
                    topEnd = CornerSize(16.dp),
                    bottomStart = CornerSize(16.dp),
                    bottomEnd = CornerSize(16.dp)
                )

                else -> {
                    when (index) {
                        0 -> Shapes().medium.copy(
                            topStart = CornerSize(16.dp),
                            topEnd = CornerSize(16.dp),
                            bottomStart = CornerSize(0.dp),
                            bottomEnd = CornerSize(0.dp)
                        )

                        optionList.size - 1 -> Shapes().medium.copy(
                            topStart = CornerSize(0.dp),
                            topEnd = CornerSize(0.dp),
                            bottomStart = CornerSize(16.dp),
                            bottomEnd = CornerSize(16.dp)
                        )

                        else -> Shapes().medium.copy(
                            topStart = CornerSize(0.dp),
                            topEnd = CornerSize(0.dp),
                            bottomStart = CornerSize(0.dp),
                            bottomEnd = CornerSize(0.dp)
                        )
                    }
                }
            }
        }, itemContent = { profile ->

            Row(
                modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                profile.icon?.let {
                    Image(painter = painterResource(id = it), contentDescription = "")
                }

                profile.titleRes?.let {
                    Text(
                        modifier = modifier.padding(20.dp),
                        text = stringResource(id = it),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }

        }, onClick = {
            when (it) {
                0 -> {
                    coroutineScope.launch {
                        clickRow.invoke(true)
                    }
                }
            }
        })
    }
}
