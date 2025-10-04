package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.ui.theme.ToDoListTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close

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
    var tasks by remember { mutableStateOf(listOf<TodoItem>()) }
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    Text(
        text = "To-Do List",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(bottom = 16.dp)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
    ) {
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
                if (textState.text.isNotBlank()) {
                    tasks = tasks + TodoItem(id = tasks.size + 1, label = textState.text.trim())
                    textState = TextFieldValue("")
                }
            }) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tasks) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = item.isCompleted,
                        onCheckedChange = {
                            tasks = tasks.map {
                                if (it.id == item.id) it.copy(isCompleted = !it.isCompleted) else it
                            }
                        }
                    )
                    Text(
                        item.label,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { tasks = tasks.filter { it.id != item.id } }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Delete Task"
                        )
                    }
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
    }
    }
