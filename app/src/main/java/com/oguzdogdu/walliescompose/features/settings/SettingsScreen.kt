package com.oguzdogdu.walliescompose.features.settings

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.component.BaseCenteredToolbar
import com.oguzdogdu.walliescompose.navigation.utils.IconResource
import com.oguzdogdu.walliescompose.navigation.utils.WalliesIcons
import com.oguzdogdu.walliescompose.ui.theme.WalliesComposeTheme
import com.oguzdogdu.walliescompose.util.OptionLists
import com.oguzdogdu.walliescompose.util.menuRow
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SettingsScreenRoute(
    modifier: Modifier = Modifier, viewModel: SettingsViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val settingsUiState by viewModel.settingsState.collectAsStateWithLifecycle()
    val context = LocalContext.current

        Scaffold(modifier = modifier.fillMaxSize(), topBar = {
            BaseCenteredToolbar(modifier = Modifier,
                title = stringResource(id = R.string.settings),
                leftClick = {},
                rightClick = {})
        }) {
            Column(modifier = modifier.padding(it)) {
                if (settingsUiState.openThemeDialog) {
                    val themes = listOf(
                        ThemeValues.LIGHT_MODE.title,
                        ThemeValues.DARK_MODE.title,
                        ThemeValues.SYSTEM_DEFAULT.title
                    )

                    SingleSelectDialog(modifier = modifier,
                        title = stringResource(id = R.string.choise_theme),
                        optionsList = themes,
                        defaultSelected = 0,
                        submitButtonText = stringResource(id = R.string.ok),
                        dismissButtonText = stringResource(id = R.string.cancel),
                        onSubmitButtonClick = { id ->
                            coroutineScope.launch {
                                viewModel.handleScreenEvents(SettingsScreenEvent.SetNewTheme(themes[id]))
                                Toast.makeText(context, "Click: ${themes.getOrNull(id)}", Toast.LENGTH_SHORT).show()
                                viewModel.handleScreenEvents(SettingsScreenEvent.ThemeChanged)
                            }
                        },
                        onDismissRequest = {
                            coroutineScope.launch {
                                viewModel.handleScreenEvents(SettingsScreenEvent.OpenThemeDialog(false))
                                Toast.makeText(context, "Click: Ondismiss", Toast.LENGTH_SHORT).show()
                            }
                        })

                }
                SettingsScreen(modifier = modifier)

            }
        }
    }


@Composable
fun SettingsScreen(modifier: Modifier, viewModel: SettingsViewModel = hiltViewModel()) {
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
                        viewModel.handleScreenEvents(SettingsScreenEvent.OpenThemeDialog(true))
                    }
                }
            }
        })
    }

}


@Composable
fun SingleSelectDialog(
    modifier: Modifier,
    title: String,
    optionsList: List<String>,
    defaultSelected: Int,
    submitButtonText: String,
    dismissButtonText: String,
    onSubmitButtonClick: (Int) -> Unit,
    onDismissRequest: () -> Unit
) {

    var selectedOption by remember {
        mutableIntStateOf(defaultSelected)
    }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = modifier.padding(16.dp)) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                )

                LazyColumn {
                    items(optionsList) {
                        RadioButton(
                            modifier = modifier, it, optionsList[selectedOption]
                        ) { selectedValue ->
                            selectedOption = optionsList.indexOf(selectedValue)
                        }
                    }
                }

                Spacer(modifier = modifier.height(10.dp))

                Row(
                    modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ) {
                    Button(
                        modifier = modifier
                            .height(48.dp)
                            .wrapContentWidth(), onClick = {
                            onSubmitButtonClick.invoke(selectedOption)
                            onDismissRequest.invoke()
                        }, shape = MaterialTheme.shapes.extraLarge
                    ) {
                        Text(text = submitButtonText, style = MaterialTheme.typography.titleSmall)
                    }

                    Button(
                        modifier = modifier
                            .height(48.dp)
                            .wrapContentWidth(),
                        colors = ButtonDefaults.elevatedButtonColors(),
                        onClick = {
                            onDismissRequest.invoke()
                        },
                        shape = MaterialTheme.shapes.extraLarge
                    ) {
                        Text(text = dismissButtonText, style = MaterialTheme.typography.titleSmall)
                    }
                }
            }
        }
    }
}

@Composable
fun RadioButton(
    modifier: Modifier, text: String, selectedValue: String, onClickListener: (String) -> Unit
) {
    Row(
        modifier
            .fillMaxWidth()
            .selectable(selected = (text == selectedValue), onClick = {
                onClickListener(text)
            }), verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = (text == selectedValue), onClick = {
            onClickListener(text)
        })
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            modifier = modifier.padding(start = 2.dp)
        )
    }
}
