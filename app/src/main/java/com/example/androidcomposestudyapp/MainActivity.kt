package com.example.androidcomposestudyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidcomposestudyapp.item.ItemScreen
import com.example.androidcomposestudyapp.item.ItemScreenRoute
import com.example.androidcomposestudyapp.main.MainScreen
import com.example.androidcomposestudyapp.main.MainScreenRoute
import com.example.myapplication.ui.theme.MyApplicationTheme
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            KoinContext {
                MyApplicationTheme {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()
                        NavHost(navController = navController, startDestination = MainScreenRoute) {
                            composable<MainScreenRoute> { MainScreen(navController) }
                            composable<ItemScreenRoute> { ItemScreen(navController) }
                        }
                    }
                }
            }
        }
    }
}