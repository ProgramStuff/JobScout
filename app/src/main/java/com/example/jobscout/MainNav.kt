package com.example.jobscout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColor
import com.example.jobscout.Data.AppliedViewModel
import com.example.jobscout.Data.JobViewModel
import com.example.jobscout.Data.UserViewModel
import com.example.jobscout.pages.Dashboard
import com.example.jobscout.pages.HomeScreen


@Composable
fun WelcomeScreen(appliedViewModel: AppliedViewModel, jobViewModel: JobViewModel, userViewModel: UserViewModel){
//    The bottom nav bar items and icons
    val navItemList = listOf(
        NavItem(name = "Home", Icons.Default.Home),
        NavItem(name = "Dashboard", Icons.Default.AccountCircle),
        )
//    Selected option for current page choice
    var selectedOption by remember { mutableStateOf(value = 0)}
    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar={
            // Nav bar containing the text and icons
            // Updates the selectedOption from index
            NavigationBar(modifier = Modifier.background(Color(238, 238, 238))) {
                navItemList.forEachIndexed{index, navItem ->
                    NavigationBarItem(
                        modifier = Modifier.background(Color(238, 238, 238)),
                        selected = selectedOption==index,
                        onClick = {selectedOption = index},
                        icon = {
                            Icon(imageVector = navItem.icon, contentDescription = "icon", tint = Color(98, 114, 84))
                        },
                        label ={
                            Text(
                                color = Color(98, 114, 84),
                                text = navItem.name)
                        }
                    )
                }
            }
        }
    ){innerPadding ->
        DesignScreen(
            modifier = Modifier.padding(innerPadding),
            selectedOption,
            appliedViewModel,
            jobViewModel,
            userViewModel)
    }
}

@Composable
fun DesignScreen(modifier: Modifier, selectedOption: Int,
                 appliedViewModel: AppliedViewModel,
                 jobViewModel: JobViewModel,
                 userViewModel: UserViewModel) {
    when(selectedOption){
        // When selectedOption is changed change the activity
        0-> HomeScreen(jobViewModel)
        1-> Dashboard(appliedViewModel, userViewModel, jobViewModel)
    }
}

