package com.arunabha.crudtodoapp.pages

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arunabha.crudtodoapp.data.model.TodoItem
import com.arunabha.crudtodoapp.ui.theme.CRUDTodoAppTheme
import com.arunabha.crudtodoapp.viewmodel.MyTodosViewModel
import com.arunabha.crudtodoapp.viewmodel.MyTodosViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun MyTodos(modifier: Modifier) {

    val viewModel: MyTodosViewModel =
        MyTodosViewModelFactory(LocalContext.current).create(MyTodosViewModel::class.java)
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.todos.collectAsState(initial = emptyList()) // list of task as state
    val context = LocalContext.current

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(vertical = 7.dp)
            ) {
                Text(
                    text = "Tasks",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // to show all the tasks
            LazyColumn(
                modifier = Modifier.padding(3.dp)
            ) {
                items(state.value) { todo ->
                    TodoItemView(
                        todoItem = todo,

                        // delete task on delete icon click
                        onDeleteClicked = {
                            coroutineScope.launch {
                                val flag = viewModel.deleteTodo(todo)

                                // if deleted successfully
                                if (flag) {
                                    Toast.makeText(context, "Task deleted!", Toast.LENGTH_SHORT)
                                        .show()
                                }

                                // if failed to delete
                                else {
                                    Toast.makeText(
                                        context,
                                        "Failed to delete task!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },

                        // when task completed
                        onCheckedClick = {

                            // change the status
                            todo.isCompleted = !todo.isCompleted


                            coroutineScope.launch {
                                val flag = viewModel.updateTodo(todo)

                                // to debug
                                Log.d("abc", state.value.toString())
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TodoItemView(
    todoItem: TodoItem,
    onDeleteClicked: () -> Unit,
    onCheckedClick: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(todoItem.isCompleted) }

    // contains total item
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 5.dp)
            .shadow(4.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(
                        enabled = true,
                        checked = isChecked,
                        onCheckedChange = {
                            isChecked = !isChecked

                            // To change the value in the database
                            onCheckedClick()
                        }
                    )

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = todoItem.title,
                            color = MaterialTheme.colorScheme.primary,
                            style = if (isChecked) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle(
                                textDecoration = TextDecoration.None
                            )
                        )

                        Text(
                            text = todoItem.date!!,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(top = 3.dp)
                        )
                    }
                }

                // icon to expand/shrink the todoItem
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Expand/Shrink",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .weight(0.15f)
                        .clickable { expanded = !expanded }
                )
            }

            // if expanded, only then show this content
            if (expanded) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = todoItem.desc,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                        lineHeight = 16.sp
                    )

                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red,
                        modifier = Modifier
                            .weight(0.15f)
                            .clickable { onDeleteClicked() }
                    )
                }
            }
        }

    }
}


@Preview(
    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun MyTodosPreview() {
    CRUDTodoAppTheme {
        MyTodos(Modifier)
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TodoItemPreview() {
    CRUDTodoAppTheme {
        TodoItemView(
            TodoItem(
                0,
                "My Important Task",
                "I will have do this by Monday. But I am on a leave next week, I will try to complete this in this weak only! I will complete it anyways!!",
                "10/11/2025",
                false
            ),
            onDeleteClicked = {},
            onCheckedClick = {}
        )
    }
}