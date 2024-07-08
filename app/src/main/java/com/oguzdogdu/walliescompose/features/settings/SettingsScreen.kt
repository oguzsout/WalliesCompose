package com.oguzdogdu.walliescompose.features.settings

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.oguzdogdu.walliescompose.util.ReusableMenuRow
import com.oguzdogdu.walliescompose.util.ReusableMenuRowLists.generalOptionList
import com.oguzdogdu.walliescompose.util.ReusableMenuRowLists.storageOptionList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

typealias onSettingsScreenEvent = (SettingsScreenEvent) -> Unit

@Composable
fun SettingsScreenRoute(
    modifier: Modifier = Modifier, viewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsUiState by viewModel.settingsState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = settingsUiState.showSnackBar) {
        when(settingsUiState.showSnackBar) {
            true -> snackState.showSnackbar("Cache directory deleted successfully")
            false -> snackState.showSnackbar("Error deleting cache directory")
            null -> return@LaunchedEffect
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
            SettingsScreen(
                settingsScreenState = settingsUiState,
                context = context,
                onSettingsScreenEvent = viewModel::handleScreenEvents
            )
        }
    }
}

@Composable
fun SettingsScreen(
    settingsScreenState: SettingsScreenState,
    context: Context,
    onSettingsScreenEvent: onSettingsScreenEvent,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val themes = listOf(
        ThemeValues.LIGHT_MODE.title, ThemeValues.DARK_MODE.title, ThemeValues.SYSTEM_DEFAULT.title
    )
    val languages = listOf(LanguageValues.English.title, LanguageValues.Turkish.title)

    var themeLocation by remember { mutableIntStateOf(0) }
    var languageLocation by remember { mutableIntStateOf(0) }
    var selectedLanguage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = settingsScreenState.getThemeValue) {
        onSettingsScreenEvent(SettingsScreenEvent.ThemeChanged)
        themeLocation = if (settingsScreenState.getThemeValue.isNullOrEmpty()) {
            2
        } else {
            themes.indexOf(settingsScreenState.getThemeValue)
        }
    }

    LaunchedEffect(key1 = settingsScreenState.getLanguageValue) {
        onSettingsScreenEvent(SettingsScreenEvent.LanguageChanged)
        languageLocation = if (settingsScreenState.getLanguageValue.isNullOrEmpty()) {
            0
        } else {
            languages.indexOf(settingsScreenState.getLanguageValue)
        }
    }

    if (settingsScreenState.openThemeDialog) {
        SingleSelectDialog(modifier = modifier,
            title = stringResource(id = R.string.choise_theme),
            optionsList = themes,
            defaultSelected = themeLocation,
            submitButtonText = stringResource(id = R.string.ok),
            dismissButtonText = stringResource(id = R.string.cancel),
            onSubmitButtonClick = { id ->
                coroutineScope.launch {
                    onSettingsScreenEvent(SettingsScreenEvent.SetNewTheme(themes[id]))
                    onSettingsScreenEvent(SettingsScreenEvent.ThemeChanged)
                }
            },
            onDismissRequest = { value ->
                coroutineScope.launch {
                    onSettingsScreenEvent(SettingsScreenEvent.OpenThemeDialog(value))
                }
            })

    }

    if (settingsScreenState.openLanguageDialog) {
        SingleSelectDialog(modifier = modifier,
            title = stringResource(id = R.string.choise_language),
            optionsList = languages,
            defaultSelected = languageLocation,
            submitButtonText = stringResource(id = R.string.ok),
            dismissButtonText = stringResource(id = R.string.cancel),
            onSubmitButtonClick = { id ->
                coroutineScope.launch {
                    selectedLanguage = languages[id]
                    onSettingsScreenEvent(SettingsScreenEvent.SetNewLanguage(selectedLanguage))
                    onSettingsScreenEvent(SettingsScreenEvent.LanguageChanged)
                    (context as? Activity)?.recreate()
                }
            },
            onDismissRequest = { value ->
                coroutineScope.launch {
                    onSettingsScreenEvent(SettingsScreenEvent.OpenLanguageDialog(value))
                }
            })
    }

    ListOfSettingsMenu(
        context = context,
        onSettingsScreenEvent = onSettingsScreenEvent,
        coroutineScope = coroutineScope
    )
}

@Composable
fun ListOfSettingsMenu(
    context: Context,
    onSettingsScreenEvent: onSettingsScreenEvent,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(count = 1) { index: Int ->
            Text(
                text = stringResource(R.string.settings_general_header),
                style = MaterialTheme.typography.titleSmall,
            )
            Spacer(modifier = modifier.size(8.dp))
            ReusableMenuRow(data = generalOptionList,
                index = index,
                modifier = modifier.fillMaxWidth(),
                itemContent = { menu ->
                    MenuRowItems(
                        modifier = modifier, menuRow = menu, arrow = true
                    )
                }
            ) {
                when (it) {
                    0 -> {
                        coroutineScope.launch {
                            onSettingsScreenEvent(SettingsScreenEvent.OpenThemeDialog(true))
                        }
                    }

                    1 -> {
                        coroutineScope.launch {
                            onSettingsScreenEvent(SettingsScreenEvent.OpenLanguageDialog(true))
                        }
                    }
                }
            }
            Spacer(modifier = modifier.size(16.dp))
        }
        items(count = 1) { index: Int ->
            Text(
                text = stringResource(R.string.settings_storage_header),
                style = MaterialTheme.typography.titleSmall,
            )
            Spacer(modifier = modifier.size(8.dp))
            ReusableMenuRow(data = storageOptionList,
                index = index,
                modifier = modifier.fillMaxWidth(),
                itemContent = { menu ->
                    MenuRowItems(
                        modifier = modifier, menuRow = menu, arrow = false
                    )
                }
            ) {
                when(it) {
                    0 -> {
                        clearAppCache(context = context, onSettingsScreenEvent = onSettingsScreenEvent)
                    }
                }
            }
        }
    }
}

private fun clearAppCache(context: Context, onSettingsScreenEvent: onSettingsScreenEvent) {
    val cacheDir = context.cacheDir
    when {
        cacheDir.exists() -> {
            try {
                cacheDir.deleteRecursively()
                Log.d("AppCache", "Cache directory deleted successfully")
                onSettingsScreenEvent(SettingsScreenEvent.ClearCached(true))
            } catch (e: Exception) {
                Log.e("AppCache", "Error deleting cache directory: $e")
                onSettingsScreenEvent(SettingsScreenEvent.ClearCached(false))
            }
        }

        else -> Log.d("AppCache", "Cache directory does not exist")
    }
}