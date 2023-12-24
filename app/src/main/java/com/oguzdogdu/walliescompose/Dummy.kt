package com.oguzdogdu.walliescompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeDummy() {
    val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Swipe(snackbarHostState = snackbarHostState, scope = scope)
        }

}


@Composable
fun Swipe(snackbarHostState: SnackbarHostState, scope: CoroutineScope) {
    Scaffold(snackbarHost = {
            SnackbarHost(snackbarHostState)
        }, content = {
            Container(onClick = {
                scope.launch {
                    if (it != null) {
                        snackbarHostState.showSnackbar(it)
                    }
                }
            })
        })

}


@Composable
fun Container(onClick: (String?) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        HomeTitleContainer()
        Text(
            text = "lsdşghşsdhgsşdlhgşsdhgşhsdşhgşsdhgşsd",
            modifier = Modifier
                .background(Color.Black)
                .align(Alignment.CenterHorizontally)
                .clickable {
                    onClick.invoke("lsdşghşsdhgsşdlhgşsdhgşhsdşhgşsdhgşsd")
                }
        )
        BaseButtonStyle(onClick = { }, text = "heeeeyyyyy")
        UserProfile()
    }
}
@Composable
fun BaseButtonStyle(
    onClick: () -> Unit,
    text: String
) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            color = Color.Yellow,
            fontSize = 14.sp,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
@Composable
fun HomeTitleContainer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_cancel),
            contentDescription = "Profile Avatar",
            modifier = Modifier
                .size(32.dp)
        )

        Text(
            text = "Title",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterVertically),
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Search",
            modifier = Modifier
                .size(32.dp)
        )
    }
}

@Composable
fun UserProfile() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background), // Profil resmi URL'si
                contentDescription = "Profil Resmi",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier
                .width(8.dp)
                .background(Color.Black))
            Column {
                Text(text = "neom", style = MaterialTheme.typography.bodySmall, color = Color.Black)
                Text(text = "www.neom.com", style = MaterialTheme.typography.bodySmall, color = Color(0xFF6200EE))
            }
            Spacer(
                Modifier
                    .weight(1f)
                    .background(Color.Black))
            Icon(Icons.Filled.Favorite, contentDescription = "Beğen", tint = Color.Black)
        }
        Spacer(modifier = Modifier
            .height(8.dp)
            .background(Color.Black))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(text = "Görüntüleme 2.6M", style = MaterialTheme.typography.bodySmall, color = Color.Black)
            Spacer(modifier = Modifier
                .width(8.dp)
                .background(Color.Black))
            Text(text = "İndirme 28K", style = MaterialTheme.typography.bodySmall, color = Color.Black)
            Spacer(modifier = Modifier
                .width(8.dp)
                .background(Color.Black))
            Text(text = "Oluşturulma Tarihi 28.04.2023", style = MaterialTheme.typography.bodySmall, color = Color.White)
            Spacer(modifier = Modifier
                .width(8.dp)
                .background(Color.Black))
            Text(text = "Beğeni 365", style = MaterialTheme.typography.bodySmall, color = Color.Black)
        }
        Spacer(modifier = Modifier
            .height(8.dp)
            .background(Color.Black))
        Button(onClick = { /*TODO*/ }, modifier = Modifier.padding(16.dp)) {
            Text(text = "Duvar kağıdı olarak ayarla")
        }
    }
}