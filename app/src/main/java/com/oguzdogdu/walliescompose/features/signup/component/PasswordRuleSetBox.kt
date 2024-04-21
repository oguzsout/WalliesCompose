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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.features.signup.PasswordRuleSetContainer
import com.oguzdogdu.walliescompose.ui.theme.regular

@Composable
fun PasswordRuleSetBox(modifier: Modifier = Modifier, passwordRuleSetContainer: PasswordRuleSetContainer) {
    val titleOfItem = rememberUpdatedState(newValue = passwordRuleSetContainer.title)
    val iconOfItem = rememberUpdatedState(newValue = passwordRuleSetContainer.valid)
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (iconOfItem.value) Icons.Rounded.Check else Icons.Rounded.Clear,
            contentDescription = "",
            modifier = modifier.size(16.dp),
            tint = if (iconOfItem.value) Color.Green else Color.Red
        )
        Spacer(modifier = modifier.size(8.dp))
        Text(
            text = stringResource(id = titleOfItem.value),
            maxLines = 1,
            fontFamily = regular,
            fontSize = 14.sp,
            color = if (iconOfItem.value) MaterialTheme.colorScheme.onPrimaryContainer else Color.Gray
        )
    }
}