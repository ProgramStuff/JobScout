package com.example.jobscout

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.*
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import com.example.jobscout.Data.AppDatabase
import com.example.jobscout.Data.AppliedJob
import com.example.jobscout.Data.AppliedJobDao
import com.example.jobscout.Data.AppliedViewModel
import com.example.jobscout.Data.Job
import com.example.jobscout.Data.JobDao
import com.example.jobscout.Data.JobViewModel
import com.example.jobscout.Data.User
import com.example.jobscout.Data.UserViewModel
import com.example.jobscout.ui.theme.JobScoutTheme









class MainActivity : ComponentActivity() {
    // All ViewModel are instantiated and passed from MainActivity
    private val userViewModel: UserViewModel by viewModels()
    private val jobViewModel: JobViewModel by viewModels()
    private val appliedVewModel: AppliedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JobScoutTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StartApp(userViewModel, appliedVewModel, jobViewModel)
                }
            }
        }
    }
}

@Composable
fun StartApp(userViewModel: UserViewModel, appliedViewModel: AppliedViewModel, jobViewModel: JobViewModel) {
    var isLoggedIn by remember {mutableStateOf(false)}
    var isSignedUp by remember {mutableStateOf(false)}

    if (isLoggedIn) {
        WelcomeScreen(appliedViewModel, jobViewModel, userViewModel)
    } else {
        // The lambda function that is called on successful login
        Login(onLoginSuccess = {isLoggedIn = true},
            // Successful set the value state to true to navigate to login
            onSignUpSuccess = { isSignedUp = true },
            userViewModel = userViewModel
        )
    }
}