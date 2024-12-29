package com.arunabha.crudtodoapp.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arunabha.crudtodoapp.data.model.TodoItem
import com.arunabha.crudtodoapp.ui.theme.CRUDTodoAppTheme
import com.arunabha.crudtodoapp.viewmodel.AddTodoViewModel
import com.arunabha.crudtodoapp.viewmodel.AddTodoViewModelFactory
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AddTodo(modifier: Modifier) {

    val viewModel: AddTodoViewModel = AddTodoViewModelFactory(LocalContext.current).create(AddTodoViewModel::class.java)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // to request focus on a particular field
    val titleFocusRequester = remember { FocusRequester() }
    val descFocusRequester = remember { FocusRequester() }

    val title = viewModel.titleText.collectAsState() // task title
    val desc = viewModel.descText.collectAsState() // task desc

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
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
                    text = "Add Task",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {

                // Title of the task
                OutlinedTextField(
                    label = { Text("Title") },
                    value = title.value,
                    onValueChange = { viewModel.setTitle(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(titleFocusRequester),
                    shape = RoundedCornerShape(5.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                )

                // Description of the task
                OutlinedTextField(
                    label = { Text("Description") },
                    value = desc.value,
                    onValueChange = { viewModel.setDesc(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .focusRequester(descFocusRequester),
                    shape = RoundedCornerShape(5.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                // Add button
                Button(
                    enabled = title.value.isNotEmpty() && desc.value.isNotEmpty() && title.value.isNotBlank() && desc.value.isNotBlank(),
                    modifier = Modifier
                        .align(Alignment.End)
                        .width(100.dp)
                        .padding(top = 15.dp),
                    onClick = {

                        // capture date from local system
                        val currentDate = LocalDate.now() // Get the current date
                        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy") // Define the pattern
                        val date = currentDate.format(formatter) // store it in String format

                        // print date in logcat to debug
                        Log.d("cde", date)

                        // create todoItem using available data
                        val todoItem = TodoItem(null, title.value.trim(), desc.value.trim(), date, false)

                        // call addTodo in coroutineScope cuz it's a suspend function
                        coroutineScope.launch {
                            val flag = viewModel.addTodo(todoItem)

                            // if added successfully
                            if(flag){
                                Toast.makeText(context, "Task added!", Toast.LENGTH_SHORT).show() // show a toast msg
                                viewModel.setTitle("")
                                viewModel.setDesc("")
                                titleFocusRequester.requestFocus()
                            }

                            // if failed to add
                            else{
                                Toast.makeText(context, "Failed to add task!", Toast.LENGTH_SHORT).show() // show a toast msg
                            }
                        }
                    }
                ) {
                    Text(
                        text = "Add",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddTodoPreview() {
    CRUDTodoAppTheme {
        AddTodo(Modifier)
    }
}