package com.example.jobscout.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.example.jobscout.Data.AppliedJob
import com.example.jobscout.Data.JobViewModel
import com.example.jobscout.Data.UserViewModel
import com.example.jobscout.R


@Composable
fun Dashboard( appliedVewModel: AppliedViewModel, userViewModel: UserViewModel, jobViewModel: JobViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "applied") {
        composable("applied") { AppliedJobScreen(navController, appliedVewModel, userViewModel, jobViewModel) }
        composable("saved") { SavedJobScreen(navController, appliedVewModel) }
    }
}


@Composable
fun AppliedJobScreen(navController: NavHostController, appliedVewModel: AppliedViewModel, userViewModel: UserViewModel, jobViewModel: JobViewModel) {
    val currentUser = userViewModel.loggedInUser
    val appliedJobs = appliedVewModel.appliedJob
    jobViewModel
    val allJobs = jobViewModel.jobs







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

        if (appliedJobs.isEmpty()){
            Text(text = "Empty")
        }




            Text(text = "Job Scout", fontFamily = newFontFamily, fontSize = 20.sp, textAlign = TextAlign.Left)
            Spacer(modifier = Modifier.height(25.dp))

            Text(text = "Your Jobs", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            // TODO: Turn buttons into a bar
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
                for (appliedJob in appliedJobs) {
                    if (currentUser != null) {
                        if (appliedJob.userId == currentUser.userId){
                            for (job in allJobs){
                                if (job.jobId == appliedJob.jobId){
                                    Row {
                                        JobCard(
                                            currentUser.userId,
                                            appliedJob.jobId,
                                            job.jobTitle,
                                            appliedJob.status,
                                            job.jobTitle,
                                            "Company Name",
                                            appliedVewModel
                                        )
//                                        TextButton(onClick = {},
//                                            Modifier.padding(end = 0.dp)) {
//                                            Text(modifier = Modifier.padding(end = 0.dp) ,text = "DELETE", color = Color.Red)
//                                        }
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
                // TODO: Delete button
                // TODO: Make title a link to job posting

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
fun JobCard(
    userId: Int,
    jobId: Int,
    jobTitle: String,
    jobStatus: String,
    url: String,
    companyName: String,
    appliedVewModel: AppliedViewModel
) {
    ElevatedCard(
        elevation  = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 260.dp, height = 190.dp),
    ) {

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
                    append("\n" + companyName)
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
        JobStatusSelection(jobStatus, userId, jobId, appliedVewModel)

    }
}


@Composable
fun JobStatusSelection(currentStatus: String, uid: Int, jobid: Int, appliedVewModel: AppliedViewModel) {

    var isDropDownExpanded by remember { mutableStateOf(false) }


    val jobStatus = listOf("Applied", "Interviewing", "Hired", "Rejected")
    var itemPosition by remember { mutableIntStateOf(jobStatus.indexOf(currentStatus)) }


            Row(
                modifier = Modifier
                    .padding(start = 50.dp, top = 15.dp)
                    .clickable {
                        isDropDownExpanded = true
                    }
            ) {
                Spacer(modifier = Modifier.width(30.dp))
                Text(text = jobStatus[itemPosition])
                Icon(
                   Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Dropdown arrow"
                )
            }

            Row {
                Spacer(modifier = Modifier.width(15.dp))
                TextButton(
                    onClick = {
                        appliedVewModel.updateAppliedJob(uid, jobid, jobStatus[itemPosition])
                    },
                    Modifier.padding(bottom = 0.dp)
                ) {
                    Text(text = "SAVE")
                }
                Spacer(modifier = Modifier.width(90.dp))

                // TODO: Add the delete functionality
                TextButton(
                    onClick = {},
                    Modifier.padding(end = 0.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 0.dp),
                        text = "DELETE",
                        color = Color.Red
                    )
                }
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
