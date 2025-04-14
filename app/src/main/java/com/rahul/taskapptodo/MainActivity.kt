package com.rahul.taskapptodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.tooling.preview.Preview
import com.rahul.taskapptodo.ui.theme.TaskAppToDoTheme
import com.rahul.taskapptodo.ui.AppNavGraph
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahul.taskapptodo.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskAppToDoTheme {
                val navController = rememberNavController()
                val viewModel: TaskViewModel = hiltViewModel()
                AppNavGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}
