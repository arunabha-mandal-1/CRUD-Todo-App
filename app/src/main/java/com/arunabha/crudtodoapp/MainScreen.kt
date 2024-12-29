package com.arunabha.crudtodoapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.arunabha.crudtodoapp.navigation.NavItem
import com.arunabha.crudtodoapp.pages.AddTodo
import com.arunabha.crudtodoapp.pages.FetchedTodos
import com.arunabha.crudtodoapp.pages.MyTodos

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navItemList = listOf(
        NavItem("Add", Icons.Filled.Add),
        NavItem("Tasks", Icons.Filled.List),
        NavItem("Fetched", Icons.Filled.FavoriteBorder)
    )

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = { Icon(navItem.icon, null) },
                        label = { Text(navItem.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // content
        ContentScreen(
            modifier = modifier.padding(innerPadding),
            selectedIndex
        )
    }
}

@Composable
fun ContentScreen(modifier: Modifier, selectedIndex: Int) {
    when (selectedIndex) {
        0 -> AddTodo(modifier)
        1 -> MyTodos(modifier)
        2 -> FetchedTodos(modifier)
    }
}