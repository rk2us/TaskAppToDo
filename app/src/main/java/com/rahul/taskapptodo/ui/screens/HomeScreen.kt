package com.rahul.taskapptodo.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.rahul.taskapptodo.data.local.Task
import com.rahul.taskapptodo.viewmodel.TaskViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    viewModel: TaskViewModel,
    onAddClick: () -> Unit,
    onTaskClick: (Task) -> Unit
) {
    val allTasks by viewModel.tasks.collectAsState()
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var selectedFilter by remember { mutableStateOf("All") }

    val filteredTasks = allTasks.filter { task ->
        val matchesSearch = task.name.orEmpty().contains(searchQuery.text, ignoreCase = true) ||
                task.description.orEmpty().contains(searchQuery.text, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "All" -> true
            "Completed" -> task.isCompleted
            "Incomplete" -> !task.isCompleted
            "Today" -> task.dueDate == LocalDate.now().toString()
            else -> true
        }

        matchesSearch && matchesFilter
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        },
        topBar = {
            Column {
                TopAppBar(title = { Text("Task Manager") })

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                FilterDropdown(
                    selected = selectedFilter,
                    onSelected = { selectedFilter = it },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    ) { padding ->
        if (filteredTasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No matching tasks. Try changing filters.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(filteredTasks) { task ->
                    TaskItem(task = task, onClick = { onTaskClick(task) })
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(task.name, style = MaterialTheme.typography.titleMedium)
            Text("Due: ${task.dueDate} ${task.dueTime}", style = MaterialTheme.typography.bodySmall)
            Text("Priority: ${task.priority}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun FilterDropdown(selected: String, onSelected: (String) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val filters = listOf("All", "Completed", "Incomplete", "Today")

    Box(modifier = modifier) {
        OutlinedButton(onClick = { expanded = true }) {
            Text("Filter: $selected")
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            filters.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
