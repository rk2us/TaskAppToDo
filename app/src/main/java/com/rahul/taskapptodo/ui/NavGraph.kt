package com.rahul.taskapptodo.ui

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.gson.Gson
import com.rahul.taskapptodo.data.local.Task
import com.rahul.taskapptodo.ui.screens.AddEditTaskScreen
import com.rahul.taskapptodo.ui.screens.HomeScreen
import com.rahul.taskapptodo.ui.screens.TaskDetailScreen
import com.rahul.taskapptodo.viewmodel.TaskViewModel

// Navigation routes
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddTask : Screen("add")
    object EditTask : Screen("add/{task}")
    object TaskDetail : Screen("detail/{task}")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(navController: NavHostController, viewModel: TaskViewModel) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {


        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onAddClick = { navController.navigate(Screen.AddTask.route) },
                onTaskClick = { task ->
                    val taskJson = Uri.encode(Gson().toJson(task))
                    navController.navigate("detail/$taskJson")
                }
            )
        }

  
        composable(Screen.AddTask.route) {
            AddEditTaskScreen(
                viewModel = viewModel,
                editingTask = null,
                onTaskSaved = { navController.popBackStack() }
            )
        }


        composable(Screen.EditTask.route) { backStackEntry ->
            val taskJson = backStackEntry.arguments?.getString("task")
            val task = Gson().fromJson(taskJson, Task::class.java)

            AddEditTaskScreen(
                viewModel = viewModel,
                editingTask = task,
                onTaskSaved = { navController.popBackStack() }
            )
        }

        
        composable(Screen.TaskDetail.route) { backStackEntry ->
            val taskJson = backStackEntry.arguments?.getString("task")
            val task = Gson().fromJson(taskJson, Task::class.java)

            TaskDetailScreen(
                task = task,
                viewModel = viewModel,
                onEditClick = { taskToEdit ->
                    val json = Uri.encode(Gson().toJson(taskToEdit))
                    navController.navigate("add/$json")
                },
                onDeleteClick = {
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
