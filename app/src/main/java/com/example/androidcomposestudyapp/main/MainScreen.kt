package com.example.androidcomposestudyapp.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import coil.compose.AsyncImage
import com.example.androidcomposestudyapp.item.ItemScreenRoute
import com.example.androidcomposestudyapp.main.vm.MainState
import com.example.androidcomposestudyapp.main.vm.MainViewModel
import com.example.androidcomposestudyapp.media.MediaService
import com.example.androidcomposestudyapp.services.NotificationService
import com.example.androidcomposestudyapp.workers.MyWorker
import com.example.domain.entity.Item
import com.example.myapplication.ui.theme.secondaryLight
import org.koin.androidx.compose.koinViewModel
import java.util.concurrent.TimeUnit

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        when (val st = state) {
            is MainState.Content -> {
                ItemList(list = st.list, navController)
            }

            is MainState.Error -> {
                ErrorState(st.message)
            }

            MainState.Loading -> {
                Text(text = "Загрузка...")
            }
        }
    }
}

@Composable
fun ErrorState(message: String) {
    Text(modifier = Modifier.fillMaxWidth(), text = message)
}

@Composable
fun ItemCard(item: Item, navController: NavController) {
    val ctx = LocalContext.current;
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if(isGranted) {
            sendNotification(ctx)
        }
    }
    Row(modifier = Modifier
        .height(120.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        if (item.imageURL != null)
            AsyncImage(model = item.imageURL,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier =  Modifier.size(136.dp))
        else Box(modifier = Modifier
            .fillMaxHeight().width(136.dp)
            .background(Color.Cyan))
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row { Text(item.name, fontWeight = FontWeight.W600, fontSize = 24.sp) }
                Row { Text(if(item.about != null) item.about.toString() else "Нет описания" ) }
            }

            Row(verticalAlignment = Alignment.Bottom) {
                Button(onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val permission = ContextCompat.checkSelfPermission(
                            ctx,
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                        if (permission == PackageManager.PERMISSION_GRANTED) {
                            sendNotification(ctx)
                        } else {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    } else {
                        sendNotification(ctx)
                    }
                    Log.println(Log.INFO, "MainScreen", "Clicked item ${item.id} - ${item.name}")
                    navController.navigate(ItemScreenRoute(item.id.toLong()))
                       WorkManager.getInstance(ctx).enqueueUniquePeriodicWork(
                           "some_name",
                           ExistingPeriodicWorkPolicy.KEEP,
                           PeriodicWorkRequestBuilder<MyWorker>(16, TimeUnit.MINUTES).build()
                        )

                })
                { Text("Перейти") }
            }
        }
    }
}

private fun sendNotification(context: Context) {
    ContextCompat.startForegroundService(
        context,
        Intent(context, MediaService::class.java).apply {
            action = MediaService.STARTFOREGROUND_ACTION
        }
    )
}

@Composable
fun ItemList(list: List<Item>, navController: NavController) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp),  modifier = Modifier.fillMaxSize()) {
        list.forEach { item -> ItemCard(item, navController) }
    }
}
