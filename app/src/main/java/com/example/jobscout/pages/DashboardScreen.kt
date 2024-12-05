package com.example.jobscout.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jobscout.Data.AppliedViewModel


@Composable
fun Dashboard(appliedVewModel: AppliedViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "applied") {
        composable("applied") { AppliedJobScreen(navController, appliedVewModel) }
        composable("saved") { SavedJobScreen(navController) }
    }
}


@Composable
fun AppliedJobScreen(navController: NavHostController, appliedVewModel: AppliedViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Button(onClick = { navController.navigate("applied") },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Applied Jobs")
                }
                Button(onClick = { navController.navigate("saved") },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Saved Jobs")
                }
            }
            Text(text = "Applied Jobs", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SavedJobScreen(navController: NavHostController) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Button(onClick = { navController.navigate("applied") },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Applied Jobs")
                }
                Button(onClick = { navController.navigate("saved") },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Saved Jobs")
                }
            }
            Text(text = "Saved Jobs", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}