package com.example.todolist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.ui.theme.ToDoListTheme

data class TodoItem(val id: Int, val label: String, val isCompleted: Boolean = false)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListTheme {
                TodoApp()
            }
        }
    }
}

@Composable
fun TodoApp() {
    val context = LocalContext.current
    var tasks by remember { mutableStateOf(listOf<TodoItem>()) }
    var textState by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "My To-Do List",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        // Input row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = textState,
                onValueChange = { textState = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Enter task") },
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (textState.text.isBlank()) {
                    Toast.makeText(context, "Task cannot be empty", Toast.LENGTH_SHORT).show()
                } else {
                    tasks = tasks + TodoItem(
                        id = tasks.size + 1,
                        label = textState.text.trim()
                    )
                    textState = TextFieldValue("")
                }
            }) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Split tasks
        val activeTasks = tasks.filter { !it.isCompleted }
        val completedTasks = tasks.filter { it.isCompleted }

        // Active tasks section
        if (activeTasks.isNotEmpty()) {
            Text("Items", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(activeTasks) { item ->
                    TaskRow(item, onToggle = {
                        tasks = tasks.map {
                            if (it.id == item.id) it.copy(isCompleted = !it.isCompleted) else it
                        }
                    }, onDelete = {
                        tasks = tasks.filter { it.id != item.id }
                    })
                }
            }
        } else {
            Text("No items yet", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Completed tasks section
        if (completedTasks.isNotEmpty()) {
            Text("Completed Items", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(completedTasks) { item ->
                    TaskRow(item, onToggle = {
                        tasks = tasks.map {
                            if (it.id == item.id) it.copy(isCompleted = !it.isCompleted) else it
                        }
                    }, onDelete = {
                        tasks = tasks.filter { it.id != item.id }
                    })
                }
            }
        } else {
            Text("No completed items yet", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun TaskRow(item: TodoItem, onToggle: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isCompleted,
            onCheckedChange = { onToggle() }
        )
        Text(item.label, modifier = Modifier.weight(1f))
        IconButton(onClick = { onDelete() }) {
            Icon(Icons.Default.Close, contentDescription = "Delete Task")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTodo() {
    ToDoListTheme {
        TodoApp()
    }
}
