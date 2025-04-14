package com.rahul.taskapptodo.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rahul.taskapptodo.data.local.Task
import com.rahul.taskapptodo.viewmodel.TaskViewModel
import java.util.*
import androidx.compose.material.icons.filled.*






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    viewModel: TaskViewModel,
    editingTask: Task? = null,
    onTaskSaved: () -> Unit
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val isEditMode = editingTask != null

    var name by remember { mutableStateOf(editingTask?.name ?: "") }
    var description by remember { mutableStateOf(editingTask?.description ?: "") }
    var dueDate by remember { mutableStateOf(editingTask?.dueDate ?: "") }
    var dueTime by remember { mutableStateOf(editingTask?.dueTime ?: "") }
    var priority by remember { mutableStateOf(editingTask?.priority?.toString() ?: "") }
    var category by remember { mutableStateOf(editingTask?.category ?: "") }
    var showCategoryMenu by remember { mutableStateOf(false) }

    val categories = listOf("Work", "Personal", "Study", "Other")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Task" else "Add Task") },
                navigationIcon = {
                    IconButton(onClick = onTaskSaved) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (isEditMode) {
                        IconButton(onClick = {
                            editingTask?.let {
                                viewModel.deleteTask(it)
                                onTaskSaved()
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Task")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                when {
                    name.isBlank() || dueDate.isBlank() || dueTime.isBlank() || priority.isBlank() -> {
                        Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                    }
                    priority.toIntOrNull() == null -> {
                        Toast.makeText(context, "Priority must be a number", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        val task = Task(
                            id = editingTask?.id ?: 0,
                            name = name,
                            description = description,
                            dueDate = dueDate,
                            dueTime = dueTime,
                            priority = priority.toInt(),
                            isCompleted = editingTask?.isCompleted ?: false,
                            category = category
                        )
                        viewModel.insertTask(task)
                        onTaskSaved()
                    }
                }
            }) {
                Text("✅")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 📝 Task Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Task Name*") },
                modifier = Modifier.fillMaxWidth()
            )

            // 🧾 Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            // 📅 Date Picker Field
            OutlinedTextField(
                value = dueDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Due Date*") },
                trailingIcon = {
                    IconButton(onClick = {
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                dueDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }) {
                        Icon(imageVector = Icons.Filled.CalendarToday, contentDescription = "Pick Date")


                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // ⏰ Time Picker Field
            OutlinedTextField(
                value = dueTime,
                onValueChange = {},
                readOnly = true,
                label = { Text("Due Time*") },
                trailingIcon = {
                    IconButton(onClick = {
                        TimePickerDialog(
                            context,
                            { _, hour, minute ->
                                dueTime = String.format("%02d:%02d", hour, minute)
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                        ).show()
                    }) {
                        Icon(imageVector = Icons.Filled.AccessTime, contentDescription = "Pick Time")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // 🔢 Priority
            OutlinedTextField(
                value = priority,
                onValueChange = { if (it.all { c -> c.isDigit() }) priority = it },
                label = { Text("Priority* (1 = High)") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // 🗂️ Category Dropdown
            Box {
                OutlinedButton(onClick = { showCategoryMenu = true }) {
                    Text(if (category.isBlank()) "Select Category" else "Category: $category")
                }

                DropdownMenu(
                    expanded = showCategoryMenu,
                    onDismissRequest = { showCategoryMenu = false }
                ) {
                    categories.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                category = it
                                showCategoryMenu = false
                            }
                        )
                    }
                }
            }
        }
    }
}
