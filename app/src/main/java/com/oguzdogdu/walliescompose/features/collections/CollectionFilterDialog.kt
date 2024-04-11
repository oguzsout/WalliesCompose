package com.oguzdogdu.walliescompose.features.collections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.ui.theme.medium
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    typeOfFilters: List<String>,
    isOpen: Boolean,
    choisedFilter:Int,
    onItemClick: (Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var openBottomSheet by remember { mutableStateOf(isOpen) }
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = isOpen) {
        openBottomSheet = isOpen
    }

    if (openBottomSheet) {
        ModalBottomSheet(
            modifier = modifier,
            sheetState = bottomSheetState,
            onDismissRequest = {
                scope.launch {
                    onDismiss.invoke()
                    bottomSheetState.hide()
                }.invokeOnCompletion { openBottomSheet = false }
            },
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BottomSheetContent(typeOfFilters = typeOfFilters, clickedItem = {
                    scope.launch {
                        onItemClick.invoke(it)
                        bottomSheetState.hide()
                    }.invokeOnCompletion { openBottomSheet = false }
                }, onDismiss = {
                    onDismiss.invoke()
                }, choisedFilter = choisedFilter)
            }
        }
    }
}

@Composable
fun BottomSheetContent(
    modifier: Modifier = Modifier,
    typeOfFilters: List<String>,
    clickedItem: (Int) -> Unit,
    onDismiss: () -> Unit,
    choisedFilter: Int
) {
    var selectedOption by rememberSaveable {
        mutableStateOf(typeOfFilters[choisedFilter])
    }
    var indexOfFilter by remember {
        mutableIntStateOf(0)
    }

    var showButton by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .navigationBarsPadding(),
    ) {
        Column(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 8.dp),
            ) {
                itemsIndexed(typeOfFilters) { index, item ->
                    FilterRow(
                        modifier = modifier,
                        title = item,
                        selected = item == selectedOption,
                        clickButton = {
                            selectedOption = it
                            indexOfFilter = index
                            showButton = true
                        }
                    )
                }
            }
            if (showButton) {
                Button(
                    onClick = {
                        clickedItem.invoke(indexOfFilter)
                        onDismiss.invoke()
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.text_apply),
                        fontSize = 14.sp,
                        fontFamily = medium,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun FilterRow(modifier: Modifier, title: String, selected: Boolean, clickButton: (String) -> Unit) {
    val selectedFilterTitle = rememberUpdatedState(newValue = title)
    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    clickButton.invoke(selectedFilterTitle.value)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            RadioButton(
                selected = selected,
                onClick = { clickButton.invoke(selectedFilterTitle.value) },
                colors = RadioButtonColors(
                    selectedColor = MaterialTheme.colorScheme.secondaryContainer,
                    unselectedColor = Color.Gray,
                    disabledSelectedColor = Color.Transparent,
                    disabledUnselectedColor = Color.Transparent
                )
            )
            Text(selectedFilterTitle.value,style = MaterialTheme.typography.titleSmall)
        }
    }
}