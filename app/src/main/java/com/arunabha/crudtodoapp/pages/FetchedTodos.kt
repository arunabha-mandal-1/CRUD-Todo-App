package com.arunabha.crudtodoapp.pages

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arunabha.crudtodoapp.data.model.FetchedTodoItem
import com.arunabha.crudtodoapp.viewmodel.FetchedTodosViewModel


@Composable
fun FetchedTodos(modifier: Modifier, viewModel: FetchedTodosViewModel = viewModel()) {
    val data = viewModel.fetchedTodoList.collectAsState()
    val isAllLoaded = viewModel.isAllLoaded.collectAsState()
    val isConnected = observeNetworkConnectivity().value


    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(vertical = 7.dp)
            ) {
                Text(
                    text = "Fetched Tasks",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // if internet is connected or list is partially loaded
            if (isConnected || data.value.isNotEmpty()) {

                // just before fetching data from internet
                if (data.value.isEmpty()) {
                    // show loading
                    Text(
                        text = "Loading...",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center
                    )
                } else {
                    // list/loading
                    if (!isAllLoaded.value) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }

                    // fetched task list
                    LazyColumn(
                        modifier = Modifier.padding(3.dp)
                    ) {
                        items(data.value) { fetchedTodo ->
                            FetchedTodoItemView(fetchedTodo)
                        }
                    }
                }
            } else {
                // no internet connection
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "No Internet Connection!!",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun FetchedTodoItemView(fetchedTodoItem: FetchedTodoItem) {

    var isStrikeThrough by remember { mutableStateOf(fetchedTodoItem.completed) }

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 5.dp)
            .shadow(4.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = fetchedTodoItem.completed!!,
                onCheckedChange = {

                    // change the status, locally
                    isStrikeThrough = !isStrikeThrough!!
                    fetchedTodoItem.completed = !fetchedTodoItem.completed!!
                },
            )

            Text(
                text = fetchedTodoItem.title!!,
                color = MaterialTheme.colorScheme.primary,
                textDecoration = if (isStrikeThrough!!) TextDecoration.LineThrough else TextDecoration.None
            )
        }
    }
}

fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val networkCapabilities =
        connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

@Composable
fun observeNetworkConnectivity(): MutableState<Boolean> {
    val isConnected = remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        isConnected.value = context.isInternetAvailable()
    }
    return isConnected
}

@Preview(showBackground = true)
@Composable
fun FetchedTodoItemPreview() {
    val fetchedTodoItem = FetchedTodoItem(1, 1, "My Task", false)
    FetchedTodoItemView(fetchedTodoItem)
}

@Preview(showBackground = true)
@Composable
fun FetchedTodosPreview() {
    FetchedTodos(Modifier, viewModel())
}