package com.oguzdogdu.walliescompose.features.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.components.BaseCenteredToolbar
import com.oguzdogdu.walliescompose.features.components.ButtonWithRoundCornerShape

@Composable
fun HomeScreenRoute() {
    val context = LocalContext.current
    Column (modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally){
        BaseCenteredToolbar(
            modifier = Modifier,
            title = stringResource(id = R.string.app_name),
            imageLeft = painterResource(id = R.drawable.ic_default_avatar),
            imageRight = painterResource(id = R.drawable.ic_completed),
            imageLeftTint = MaterialTheme.colorScheme.secondary,
            imageRightTint = MaterialTheme.colorScheme.secondary,
            leftClick = {
                Toast.makeText(context, "Nav Icon Click", Toast.LENGTH_SHORT).show()
            },
            rightClick = {
                Toast.makeText(context, "Add Click", Toast.LENGTH_SHORT).show()
            }
        )
      ButtonWithRoundCornerShape(modifier = Modifier, onClick = {
          Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
      }, buttonText = "Çıkış Yap")
    }

}