package com.oguzdogdu.walliescompose.features.signup.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.oguzdogdu.walliescompose.ui.theme.regular

@Composable
fun PasswordRuleSetBox(modifier: Modifier = Modifier, title:String,validate:Boolean) {
    val titleOfItem = rememberUpdatedState(newValue = title)
    val iconOfItem = rememberUpdatedState(newValue = validate)
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (iconOfItem.value) Icons.Rounded.Check else Icons.Rounded.Clear,
            contentDescription = "",
            modifier = modifier.size(20.dp),
            tint = if (iconOfItem.value) Color.Green else Color.Red
        )
        Spacer(modifier = modifier.size(8.dp))
        Text(text = titleOfItem.value, fontFamily = regular, color = if (validate) Color.Black else Color.Gray)
    }
}