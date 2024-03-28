package com.oguzdogdu.walliescompose.features.glance.widget

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.GridCells
import androidx.glance.appwidget.lazy.LazyVerticalGrid
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.glance.receiver.WalliesGlanceReceiver
import com.oguzdogdu.walliescompose.features.home.HomeViewModel
import com.oguzdogdu.walliescompose.features.main.MainActivity
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import kotlinx.coroutines.async

class WalliesWidget : GlanceAppWidget() {

    override val sizeMode: SizeMode = SizeMode.Exact

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content()
        }
    }

    @Composable
    fun Content() {
        val context = LocalContext.current
        val prefs = currentState<Preferences>().toMutablePreferences()
        val deserializedList = prefs[WalliesGlanceReceiver.allPopular] ?: ""
        val randomImages = remember { mutableListOf<Bitmap?>() }

        LaunchedEffect(key1 = true) {
            val listType = object : TypeToken<List<String>>() {}.type
            val newList = Gson().fromJson<List<String>>(deserializedList, listType)
            newList.forEach { url ->
                val bitmap = async { urlToBitmap(url, context) }.await()
                randomImages.add(bitmap)
            }
        }

        Box(
            modifier = GlanceModifier.fillMaxSize().background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (randomImages.isEmpty()) {
                CircularProgressIndicator()
            } else {
                androidx.glance.layout.Column(
                    modifier = GlanceModifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = GlanceModifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            provider = ImageProvider(R.drawable.logo),
                            contentDescription = "Logo",
                            modifier = GlanceModifier.size(48.dp).cornerRadius(32.dp)
                        )
                        androidx.glance.layout.Spacer(modifier = GlanceModifier.size(8.dp))
                        Text(
                            text = context.resources.getString(R.string.app_name),
                            modifier = GlanceModifier
                        )
                    }

                    LazyVerticalGrid(
                        gridCells = GridCells.Fixed(2),
                        modifier = GlanceModifier.fillMaxSize().padding(8.dp),
                        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                    ) {
                        items(randomImages) { bitmap ->
                            if (bitmap != null) {
                                Image(
                                    provider = ImageProvider(bitmap),
                                    contentDescription = "",
                                    contentScale = ContentScale.FillBounds,
                                    modifier = GlanceModifier.padding(
                                        vertical = 4.dp,
                                        horizontal = 8.dp
                                    ).height(120.dp)
                                        .fillMaxWidth().cornerRadius(24.dp).clickable(actionStartActivity<MainActivity>())
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun urlToBitmap(
        imageURL: String,
        context: Context
    ): Bitmap? {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageURL)
            .size(360,240)
            .allowConversionToBitmap(true)
            .allowHardware(false)
            .build()
        return when (val result = loader.execute(request)) {
            is SuccessResult -> result.drawable.toBitmapOrNull()
            is ErrorResult -> null
        }
    }
}