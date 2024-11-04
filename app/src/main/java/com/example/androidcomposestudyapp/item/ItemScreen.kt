package com.example.androidcomposestudyapp.item

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androidcomposestudyapp.R
import com.example.androidcomposestudyapp.item.vm.ItemViewModel
import com.example.domain.entity.Item
import com.example.myapplication.details.vm.ItemState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(navController: NavController, viewModel: ItemViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = state.title
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            navController.navigateUp()
                        },
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = ""
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            when (val st = state) {
                is ItemState.Content -> {
                    Content(st.element, viewModel.isRead)
                    if(!viewModel.isRead.value) Progress(viewModel)
                }

                is ItemState.Error -> {
                    Text(text = st.message)
                }

                ItemState.Loading -> {
                    CircularProgressIndicator()
                }
            }
        }
    }
}



@Composable
private fun Content(item: Item?, isRead: MutableState<Boolean>) {
    Box {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (item == null) Text("Элемент не найден")
            else {
                Row {
                    Text(item.name)
                }
                Row {
                    Text(item.about ?: "Нет описания" )
                }
                Row {
                    if(isRead.value) Text("Прочитано")
                    else Text("Не прочитано")
                }
            }
        }
    }
}

@Composable
fun Progress(vm: ItemViewModel) {
    var progressValue by remember { mutableFloatStateOf(0f) }
    val progress = animateFloatAsState(
        targetValue = progressValue,
        animationSpec = tween(
            durationMillis = 10_000,
            easing = LinearEasing
        ),
        finishedListener = {
            vm.markAsRead()
        }, label = ""
    )
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        progress = { progress.value }
    )
    LaunchedEffect(Unit) {
        progressValue = 1f
    }
}
