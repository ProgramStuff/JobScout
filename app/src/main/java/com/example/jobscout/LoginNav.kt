package com.example.jobscout

import android.database.sqlite.SQLiteOpenHelper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.activity.ComponentActivity
import com.example.jobscout.Data.UserViewModel
import com.example.jobscout.pages.LoginScreen
import com.example.jobscout.pages.RegisterScreen


// Login takes two lambda function or a function with no return value or parameters
// This allows redirect to WelcomeScreen after login and
// Redirect to login after successful signup
@Composable
fun Login (modifier: Modifier= Modifier,
           onLoginSuccess: () -> Unit,
           onSignUpSuccess: () -> Unit,
           userViewModel: UserViewModel
){

//    The bottom nav bar items and icons
    val navItemList = listOf(
        NavItem(name = "Login", Icons.Default.AccountCircle),
        NavItem(name = "Sign Up", Icons.Default.Create),
    )
//    Selected option for current page choice
    var selectedOption by remember { mutableStateOf(value = 0)}
    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar={
            // Nav bar containing the text and icons
            // Updates the selectedOption from index
            NavigationBar {
                navItemList.forEachIndexed{index, navItem ->
                    NavigationBarItem(selected = selectedOption==index,
                        onClick = {selectedOption = index},
                        icon = {
                            Icon(imageVector = navItem.icon, contentDescription = "icon")
                        },
                        label ={
                            Text(text = navItem.name)
                        }
                    )
                }
            }
        }
    ){innerPadding ->
        LoginNav(modifier = Modifier.padding(innerPadding),
            selectedOption,
            onLoginSuccess,
            // Redirect to login
            onSignUpSuccess = { selectedOption = 0}, userViewModel)
    }
}

//LoginNav defines mutable array of null User to share and save user info between login and signup
@Composable
fun LoginNav(modifier: Modifier,
             selectedOption: Int,
             onLoginSuccess: () -> Unit,
             onSignUpSuccess: () -> Unit,
             userViewModel: UserViewModel
) {

    when(selectedOption){

        // When selectedOption is changed change the activity
        0-> LoginScreen(onLoginSuccess = onLoginSuccess, userViewModel = userViewModel)
        1-> RegisterScreen(onSignUpSuccess = onSignUpSuccess, userViewModel = userViewModel)
    }
}

