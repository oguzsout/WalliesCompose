package com.oguzdogdu.walliescompose.features.glance.receiver

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.google.gson.Gson
import com.oguzdogdu.walliescompose.WalliesApplication
import com.oguzdogdu.walliescompose.features.glance.widget.WalliesWidget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WalliesGlanceReceiver : GlanceAppWidgetReceiver() {

    @Inject
    lateinit var application: WalliesApplication

    override val glanceAppWidget: GlanceAppWidget = WalliesWidget()

    private val coroutineScope = MainScope()

    private fun observeData(context: Context) {

        coroutineScope.launch {

            val allPopulars = application.imagesList

            val serializedList = Gson().toJson(allPopulars)

            val glanceId = GlanceAppWidgetManager(context).getGlanceIds(WalliesWidget::class.java)
                .firstOrNull()

            if (glanceId != null) {
                updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { prefs ->
                    prefs.toMutablePreferences().apply {
                        this[allPopular] = serializedList.trim()
                    }
                }
                glanceAppWidget.update(context, glanceId)
            }
        }
    }

    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        observeData(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        observeData(context)
    }

    companion object {
        val allPopular = stringPreferencesKey("all_popular")
    }
}
