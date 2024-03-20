package com.oguzdogdu.walliescompose.features.glance.widget

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.gson.Gson
import com.oguzdogdu.walliescompose.features.glance.receiver.WalliesGlanceReceiver

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
        val prefs = currentState<Preferences>()
        val deserializedList = prefs[WalliesGlanceReceiver.allPopular] ?: ""
        val list = Gson().fromJson(deserializedList, Array<String>::class.java).asList()
        var randomImage by remember { mutableStateOf<Bitmap?>(null) }

        LaunchedEffect(list) {
            randomImage = context.getRandomImage(list.last())
        }

        Column(
            modifier = GlanceModifier.fillMaxSize().padding(16.dp).appWidgetBackground()
                .background(Color.LightGray).cornerRadius(16.dp),
        ) {
            if (randomImage != null) {
                Image(
                    modifier = GlanceModifier.fillMaxWidth().padding(top = 16.dp),
                    provider = ImageProvider(randomImage!!),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }


    private suspend fun Context.getRandomImage(url: String, force: Boolean = false): Bitmap {
        val request = ImageRequest.Builder(this).data(url).apply {
            if (force) {
                memoryCachePolicy(CachePolicy.DISABLED)
                diskCachePolicy(CachePolicy.DISABLED)
            }
        }.build()

        return when (val result = imageLoader.execute(request)) {
            is ErrorResult -> throw result.throwable
            is SuccessResult -> result.drawable.toBitmap()
        }
    }
}