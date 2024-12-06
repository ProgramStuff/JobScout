package com.example.jobscout.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jobscout.Data.AppliedViewModel
import kotlinx.coroutines.Job
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.jobscout.R


@Composable
fun Dashboard( appliedVewModel: AppliedViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "applied") {
        composable("applied") { AppliedJobScreen(navController, appliedVewModel) }
        composable("saved") { SavedJobScreen(navController, appliedVewModel) }
    }
}


@Composable
fun AppliedJobScreen(navController: NavHostController, appliedVewModel: AppliedViewModel) {
    val appliedJobs = appliedVewModel.appliedJob
    // Google fonts package provider
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    val fontName = GoogleFont("Lobster Two")

    val newFontFamily = FontFamily(
        Font(googleFont = fontName, fontProvider = provider)
    )



    val userId = 1
    val jobId = 1
    val jobTitle = "Software Developer"
    val jobStatus = "Interviewing"
    val url = "https://ca.indeed.com/q-software-developer-l-nova-scotia-jobs.html?vjk=51e57b4068be6a1c"
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Job Scout", fontFamily = newFontFamily, fontSize = 20.sp, textAlign = TextAlign.Left)
            Spacer(modifier = Modifier.height(25.dp))

            Text(text = "Your Jobs", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

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
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Text(text = "Applied Jobs", style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Row {
                    JobCard(userId, jobId, jobTitle, jobStatus, url)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    JobCard(userId, jobId, jobTitle, jobStatus, url)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    JobCard(userId, jobId, jobTitle, jobStatus, url)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    JobCard(userId, jobId, jobTitle, jobStatus, url)
                }

                // ! Keep this spacer at the bottom, it ensures cards are not hidden by nav bar !
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun SavedJobScreen(navController: NavHostController, appliedVewModel: AppliedViewModel) {

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



@Composable
fun JobCard(userId: Int, jobId: Int, jobTitle: String, jobStatus: String, url: String) {
    ElevatedCard(
        elevation  = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 260.dp, height = 150.dp),
    ) {
        // TODO: Use text format to put link and status side by side
        // TODO: Clicking status opens a drop down to change
        // TODO: Add button to update changed status

        Text(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),

            // I am using an annotated to apply different styles to a single string
            text = buildAnnotatedString {
                withStyle(
                    // Apply style to the first section of the string
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                ) {
                    append(jobTitle)
                }
                withStyle(
                    // Apply style to the second section of the string
                    style = SpanStyle(
                        fontSize = 15.sp,
                    )
                ) {
                    append("\nGeneral Dynamics")
                }
                withStyle(
                    // Apply style to the second section of the string
                    style = SpanStyle(
                        fontSize = 15.sp,
                        background = Color.LightGray,
                    )
                ) {
                    append("\nFull-time")
                }
            }
        )
        JobStatusSelection(jobStatus)
    }
}


@Composable
fun JobStatusSelection(currentStatus: String) {

    var isDropDownExpanded by remember { mutableStateOf(false) }


    val jobStatus = listOf("Applied", "Interviewing", "Hired", "Rejected")
    var itemPosition by remember { mutableIntStateOf(jobStatus.indexOf(currentStatus)) }


            Row(
                modifier = Modifier
                    .padding(start = 20.dp, top = 15.dp)
                    .clickable {
                        isDropDownExpanded = true
                    }
            ) {
                TextButton(onClick = { /*TODO*/ }, Modifier.padding(bottom = 15.dp)) {
                    Text(text = "Save")
                }
                Spacer(modifier = Modifier.width(25.dp))
                Text(text = jobStatus[itemPosition])
                Icon(
                   Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Dropdown arrow"
                )
            }

            DropdownMenu(
                expanded = isDropDownExpanded,

                onDismissRequest = {
                    isDropDownExpanded = false
                }) {
                Spacer(modifier = Modifier.width(150.dp))
                jobStatus.forEachIndexed { index, status ->
                    DropdownMenuItem(
                        text = {
                        Text(text = status)
                    },
                        onClick = {
                            isDropDownExpanded = false
                            itemPosition = index
                        })
                }
            }
}