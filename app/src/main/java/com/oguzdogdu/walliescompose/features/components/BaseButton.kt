package com.oguzdogdu.walliescompose.features.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ButtonWithRoundCornerShape(modifier: Modifier,onClick: () -> Unit, buttonText: String) {
    Button(modifier = modifier.wrapContentSize(),onClick = {
            onClick.invoke()
        }, shape = RoundedCornerShape(20.dp)) {
            Text(text = buttonText)
        }

}