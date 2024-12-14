package com.example.androidcomposestudyapp.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.data.data.usecase.ItemByIdUseCase
import org.koin.core.context.GlobalContext.get

class LoadWidgetDataWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val useCase: ItemByIdUseCase = get().get()
        runCatching {
            useCase.execute(0)
        }.onSuccess { result ->
            val manager = GlanceAppWidgetManager(applicationContext)
            manager.getGlanceIds(MyWidget::class.java).forEach { id ->

            }
            MyWidget().updateAll(applicationContext)
        }.onFailure {
            return Result.failure()
        }
        return Result.success()
    }
}
