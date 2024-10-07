package com.example.androidcomposestudyapp.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.androidcomposestudyapp.main.vm.MainState
import com.example.androidcomposestudyapp.main.vm.MainViewModel
import com.example.domain.entity.User
import com.example.myapplication.ui.theme.secondaryLight
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        when (val st = state) {
            is MainState.Content -> {
                UserList(list = st.list)
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
fun UserCard(user: User) {
    Row(modifier = Modifier
        .height(120.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Column {
            if(user.avatar != null)
                AsyncImage(user.avatar,
                    "user ${user.name} avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxHeight().width(120.dp))
            else Box(modifier = Modifier
                .fillMaxHeight().width(120.dp)
                .background(secondaryLight))
        }
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row { Text(user.name, fontWeight = FontWeight.W600, fontSize = 24.sp) }
                Row { Text(if(user.about != null) user.about.toString() else "Нет описания" ) }
            }

            Row(verticalAlignment = Alignment.Bottom) {
                Button(onClick = {
                    Log.println(Log.INFO, "MainScreen", "Clicked user ${user.id} - ${user.name}")
                })
                { Text("Скачать") }
            }
        }
    }
}

@Composable
fun UserList(list: List<User>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp),  modifier = Modifier.fillMaxSize()) {
        list.forEach { user -> UserCard(user) }
    }
}