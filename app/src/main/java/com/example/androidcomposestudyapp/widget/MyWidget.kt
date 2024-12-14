package com.example.androidcomposestudyapp.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartService
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.unit.ColorProvider
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.domain.entity.Item
import com.example.data.data.usecase.ItemByIdUseCase
import com.example.data.data.usecase.ItemsUseCase
import com.example.androidcomposestudyapp.MainActivity
import org.koin.core.context.GlobalContext.get

class MyWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val state = currentState<Preferences>()
            val listUseCase = remember { get().get<ItemsUseCase>() }
            var elements by remember { mutableStateOf<List<Item>>(emptyList()) }
            var elementIdState by remember { mutableStateOf<Long?>(null) }
            val elementId = state[elementIdKey]
            val title = state[titleKey]
            val description = state[descriptionKey]
            val imageUrl = state[imageUrlKey]
            var image by remember(imageUrl) { mutableStateOf<Bitmap?>(null) }
            LaunchedEffect(imageUrl) {
                if (imageUrl != null) {
                    image = context.getImage(imageUrl)
                }
            }
            LaunchedEffect(elementIdState) {
                updateAppWidgetState(context, id) { prefs ->
                    val elId = elementIdState
                    if(elId != null) {
                        prefs[elementIdKey] = elId
                    }
                }
                update(context, id)
            }
            LaunchedEffect(state) {
                if (title == null && elementId != null) {
                    val useCase: ItemByIdUseCase = get().get()
                    runCatching { useCase.execute(elementId.toInt()) }
                        .onSuccess { result ->
                            if(result != null) {
                                updateAppWidgetState(context, id) { prefs ->
                                    prefs[titleKey] = result.name
                                    prefs[descriptionKey] = result.about.orEmpty()
                                    prefs[imageUrlKey] = result.imageURL.orEmpty()
                                }
                                update(context, id)
                            }
                        }
                }
            }
            LaunchedEffect(elements) {
                elements = listUseCase.execute(Unit)
            }
            if(elementId == null) {
                LazyColumn(
                    modifier = GlanceModifier.fillMaxSize()
                        .background(ColorProvider(color = Color(0xffffffff)))
                        .clickable(actionStartService(Intent()))
                ) {
                    elements.forEach {
                        item {
                            Text(
                                modifier = GlanceModifier.fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .clickable {
                                        elementIdState = it.id.toLong()
                                    },
                                text = it.name
                            )
                        }
                    }
                }
            } else if (title == null) {
                Column(
                    modifier = GlanceModifier.fillMaxSize()
                        .background(ColorProvider(color = Color(0xffffffff)))
                        .clickable(actionStartActivity<MainActivity>())
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = GlanceModifier.fillMaxSize()
                        .background(ColorProvider(color = Color(0xffffffff)))
                        .clickable(actionStartActivity<MainActivity>()),
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (image != null) {
                        Image(
                            modifier = GlanceModifier.fillMaxWidth(),
                            provider = ImageProvider(image!!),
                            contentDescription = ""
                        )
                    }
                    Text(
                        title
                    )
                    Text(description.orEmpty())
                }
            }
        }
    }

    private suspend fun Context.getImage(url: String, force: Boolean = false): Bitmap? {
        val request = ImageRequest.Builder(this).data(url).apply {
            if (force) {
                memoryCachePolicy(CachePolicy.DISABLED)
                diskCachePolicy(CachePolicy.DISABLED)
            }
        }.build()

        // Request the image to be loaded and throw error if it failed
        return when (val result = imageLoader.execute(request)) {
            is ErrorResult -> throw result.throwable
            is SuccessResult -> result.drawable.toBitmapOrNull()
        }
    }

    companion object {
        val elementIdKey = longPreferencesKey("element_id")
        val titleKey = stringPreferencesKey("title_key")
        val descriptionKey = stringPreferencesKey("description_key")
        val imageUrlKey = stringPreferencesKey("image_url_key")
    }
}