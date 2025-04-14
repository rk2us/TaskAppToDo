package com.rahul.taskapptodo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rahul.taskapptodo.data.local.Task
import com.rahul.taskapptodo.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    task: Task,
    viewModel: TaskViewModel,
    onEditClick: (Task) -> Unit,
    onDeleteClick: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Title: ${task.name}", style = MaterialTheme.typography.titleLarge)
            Text("Description: ${task.description}", style = MaterialTheme.typography.bodyLarge)
            Text("Due Date: ${task.dueDate}", style = MaterialTheme.typography.bodyMedium)
            Text("Due Time: ${task.dueTime}", style = MaterialTheme.typography.bodyMedium)
            Text("Priority: ${task.priority}", style = MaterialTheme.typography.bodyMedium)
            Text("Category: ${task.category}", style = MaterialTheme.typography.bodyMedium)
            Text("Completed: ${if (task.isCompleted) "Yes" else "No"}", style = MaterialTheme.typography.bodyMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { onEditClick(task) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Edit")
                }

                OutlinedButton(
                    onClick = {
                        viewModel.deleteTask(task)
                        onDeleteClick()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                    Text("Delete")
                }
            }
        }
    }
}
