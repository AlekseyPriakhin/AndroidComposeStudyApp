package com.example.androidcomposestudyapp.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androidcomposestudyapp.item.vm.ItemViewModel
import com.example.androidcomposestudyapp.main.ErrorState
import com.example.androidcomposestudyapp.main.MainScreenRoute
import com.example.domain.entity.Item
import com.example.myapplication.details.vm.ItemState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ItemScreen(navController: NavController,  viewModel: ItemViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        when (val st = state) {
            is ItemState.Content -> {
                Item(navController, st.element)
            }

            is ItemState.Error -> {
                ErrorState(st.message)
            }

            ItemState.Loading -> {
                Text(text = "Загрузка...")
            }
        }
    }
}

@Composable
private fun Item(navController: NavController, item: Item?) {
    if(item == null) return Text("Not found")


    Box {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row {
                Button(onClick = {
                    navController.navigate(MainScreenRoute)
                }) { Text("Назад") }
            }
            Row {
                Text(item.name)
            }
            Row {
                Text(item.about ?: "Нет описания" )
            }
        }
    }
}