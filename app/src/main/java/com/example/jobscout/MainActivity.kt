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
import com.example.jobscout.Data.Job
import com.example.jobscout.Data.JobDao
import com.example.jobscout.Data.JobViewModel
import com.example.jobscout.Data.User
import com.example.jobscout.Data.UserViewModel
import com.example.jobscout.ui.theme.JobScoutTheme









class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private val jobViewModel: JobViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JobScoutTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserApp(userViewModel, jobViewModel)
                }
            }
        }
    }
}

@Composable
fun UserApp(userViewModel: UserViewModel, jobViewModel: JobViewModel) {
    val users = userViewModel.users

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)){
        var fName by remember {mutableStateOf("")}
        var lName by remember {mutableStateOf("")}
        var email by remember {mutableStateOf("")}
        var password by remember {mutableStateOf("")}

        OutlinedTextField(value = fName, onValueChange = {fName = it},
            label = { Text(text = "Enter First name: ")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = lName, onValueChange = {lName = it},
            label = { Text(text = "Enter Last name: ")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = email, onValueChange = {email = it},
            label = { Text(text = "Enter email: ")},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = password, onValueChange = {password = it},
            label = { Text(text = "Enter your password: ")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (fName.isNotBlank() && lName.isNotBlank() && email.isNotBlank() && password.isNotBlank()){
                    val user = User(fName = fName, lName = lName, email = email, password = password)
                    userViewModel.addUser(user)
                    fName = ""
                    lName = ""
                    email = ""
                    password = ""
                }
            },
        ) {
            Text(text = "Add User")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "User List", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(users) { user ->
                UserItem(user, userViewModel)

            }
        }

    }
}

@Composable
fun UserItem(user: User, userViewModel: UserViewModel) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ){
        Text(text = user.fName, modifier = Modifier.weight(1f))
        Text(text = user.lName, modifier = Modifier.weight(1f))
        Text(text = user.email, modifier = Modifier.weight(1f))
        Text(text = user.password, modifier = Modifier.weight(1f))


        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {userViewModel.deleteUser(user)},
            modifier = Modifier.padding(start = 8.dp)
        ){
            Text(text = "Delete")
        }

    }

}