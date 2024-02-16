package com.oguzdogdu.walliescompose.features.settings

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.settings.components.MenuRowItems
import com.oguzdogdu.walliescompose.features.settings.components.SingleSelectDialog
import com.oguzdogdu.walliescompose.navigation.utils.WalliesIcons
import com.oguzdogdu.walliescompose.util.MenuRow
import com.oguzdogdu.walliescompose.util.ReusableMenuRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf


@Composable
fun SettingsScreenRoute(
    modifier: Modifier = Modifier, viewModel: SettingsViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val settingsUiState by viewModel.settingsState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackState = remember { SnackbarHostState() }

    val themes = listOf(
        ThemeValues.LIGHT_MODE.title, ThemeValues.DARK_MODE.title, ThemeValues.SYSTEM_DEFAULT.title
    )
    val languages = listOf(LanguageValues.English.title, LanguageValues.Turkish.title)
    var themeLocation by remember {
        mutableIntStateOf(0)
    }
    var languageLocation by remember {
        mutableIntStateOf(0)
    }
    var selectedLanguage by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = settingsUiState.getThemeValue) {
        viewModel.handleScreenEvents(SettingsScreenEvent.ThemeChanged)
        themeLocation = if (settingsUiState.getThemeValue.isNullOrEmpty()) {
            2
        } else {
            themes.indexOf(settingsUiState.getThemeValue)
        }
    }
    LaunchedEffect(key1 = settingsUiState.getLanguageValue) {
        viewModel.handleScreenEvents(SettingsScreenEvent.LanguageChanged)
        languageLocation = if (settingsUiState.getLanguageValue.isNullOrEmpty()) {
            0
        } else {
            languages.indexOf(settingsUiState.getLanguageValue)
        }
    }

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        Row(
            modifier = modifier
                .wrapContentWidth()
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.settings_title),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }, snackbarHost = {
        SnackbarHost(
            hostState = snackState
        )
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
            if (settingsUiState.openLanguageDialog) {
                SingleSelectDialog(modifier = modifier,
                    title = stringResource(id = R.string.choise_language),
                    optionsList = languages,
                    defaultSelected = languageLocation,
                    submitButtonText = stringResource(id = R.string.ok),
                    dismissButtonText = stringResource(id = R.string.cancel),
                    onSubmitButtonClick = { id ->
                        coroutineScope.launch {
                            selectedLanguage = languages[id]
                            viewModel.handleScreenEvents(
                                SettingsScreenEvent.SetNewLanguage(
                                    selectedLanguage
                                )
                            )
                            viewModel.handleScreenEvents(SettingsScreenEvent.LanguageChanged)
                            (context as? Activity)?.recreate()
                        }
                    },
                    onDismissRequest = { value ->
                        coroutineScope.launch {
                            viewModel.handleScreenEvents(
                                SettingsScreenEvent.OpenLanguageDialog(
                                    value
                                )
                            )
                        }
                    })
            }
            SettingsScreen(modifier = modifier, openThemeDialog = { value ->
                viewModel.handleScreenEvents(SettingsScreenEvent.OpenThemeDialog(value))
            }, openLanguageDialog = { value ->
                viewModel.handleScreenEvents(SettingsScreenEvent.OpenLanguageDialog(value))

            }, context = context, viewModel = viewModel, showSnackBar = {
                coroutineScope.launch {
                    when (it) {
                        true -> snackState.showSnackbar("Cache directory deleted successfully")
                        false -> snackState.showSnackbar("Error deleting cache directory")
                    }
                }
            })
        }
    }
}

@Composable
fun SettingsScreen(
    modifier: Modifier,
    openThemeDialog: (Boolean) -> Unit,
    openLanguageDialog: (Boolean) -> Unit,
    showSnackBar: (Boolean) -> Unit,
    context: Context,
    viewModel: SettingsViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val optionList = immutableListOf(
        MenuRow(
            icon = WalliesIcons.DarkMode,
            titleRes = R.string.theme_text
        ),
        MenuRow(
            icon = WalliesIcons.Language,
            titleRes = R.string.language_title_text
        ),
        MenuRow(
            icon = WalliesIcons.Cache,
            titleRes = R.string.clear_cache_title
        )
    )
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        items(count = 1) { index: Int ->
            ReusableMenuRow(data = optionList,
                index = index,
                modifier = modifier.fillMaxWidth(),
                itemContent = { menu ->
                    MenuRowItems(
                        modifier = modifier, menuRow = menu
                    )
                },
                onClick = {
                    handleMenuItemClick(
                        itemIndex = it,
                        coroutineScope,
                        openThemeDialog,
                        openLanguageDialog,
                        showSnackBar,
                        context,
                        viewModel
                    )
                })
        }
    }
}

private fun clearAppCache(context: Context, viewModel: SettingsViewModel) {
    val cacheDir = context.cacheDir
    when {
        cacheDir.exists() -> {
            try {
                cacheDir.deleteRecursively()
                Log.d("AppCache", "Cache directory deleted successfully")
                viewModel.handleScreenEvents(SettingsScreenEvent.ClearCached(true))
            } catch (e: Exception) {
                Log.e("AppCache", "Error deleting cache directory: $e")
                viewModel.handleScreenEvents(SettingsScreenEvent.ClearCached(false))
            }
        }

        else -> Log.d("AppCache", "Cache directory does not exist")
    }
}
fun handleMenuItemClick(
    itemIndex: Int,
    coroutineScope: CoroutineScope,
    openThemeDialog: (Boolean) -> Unit,
    openLanguageDialog: (Boolean) -> Unit,
    showSnackBar: (Boolean) -> Unit,
    context: Context,
    viewModel: SettingsViewModel
) {
    when (itemIndex) {
        0 -> {
            coroutineScope.launch {
                openThemeDialog.invoke(true)
            }
        }

        1 -> {
            coroutineScope.launch {
                openLanguageDialog.invoke(true)
            }
        }

        2 -> {
            clearAppCache(context = context, viewModel = viewModel)
            coroutineScope.launch {
                showSnackBar.invoke(viewModel.settingsState.value.cache)
            }
        }
    }
}