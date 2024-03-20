package com.oguzdogdu.walliescompose.features.glance.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.action.ActionCallback

class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        WalliesWidget().update(context, glanceId)

        GlanceAppWidgetManager(context).getAppWidgetSizes(glanceId).let { size ->

        }
    }
}