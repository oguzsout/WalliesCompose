package com.oguzdogdu.walliescompose.features.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.ui.theme.regular

@Composable
fun DetailTripleActionButtons(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.wallpaper),
                tint = Color.White,
                contentDescription = ""
            )
            Spacer(modifier = modifier.padding(horizontal = 8.dp))
            Text(
                text = stringResource(id = R.string.set_wallpaper_text),
                color = Color.White,
                fontFamily = regular,
                fontSize = 12.sp
            )
        }
        Row {
            Icon(painter = painterResource(id = R.drawable.share), contentDescription = "")
            Spacer(modifier = modifier.padding(horizontal = 8.dp))
            Icon(painter = painterResource(id = R.drawable.download), contentDescription = "")
        }
    }
}