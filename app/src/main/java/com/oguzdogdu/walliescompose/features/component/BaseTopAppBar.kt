package com.oguzdogdu.walliescompose.features.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.ui.theme.bold
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseCenteredToolbar(
    modifier: Modifier = Modifier,
    title: String?,
    imageRight: Painter? = null,
    imageLeft: Painter? = null,
    imageRightTint: Color? = null,
    imageLeftTint: Color? = null,
    leftClick: () -> Unit?,
    rightClick: () -> Unit?
) {
    Box(modifier = modifier.wrapContentHeight().fillMaxWidth()){
        CenterAlignedTopAppBar(title = {
            Text(
                text = title.orEmpty(),
                color = Color.Unspecified,
                fontFamily = medium,
                fontSize = 24.sp
            )
        }, navigationIcon = {
            IconButton(onClick = {
                leftClick.invoke()
            }) {
                if (imageLeft != null) {
                    if (imageLeftTint != null) {
                        Icon(
                            painter = imageLeft,
                            contentDescription = null,
                            modifier = modifier.size(24.dp),
                            tint = imageLeftTint
                        )
                    }
                }
            }
        }, actions = {
            IconButton(onClick = {
                rightClick.invoke()
            }) {
                if (imageRight != null) {
                    if (imageRightTint != null) {
                        Icon(
                            painter = imageRight,
                            contentDescription = null,
                            modifier = modifier,
                            tint = imageRightTint
                        )
                    }
                }
            }
        }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Unspecified
        )
        )
    }
}