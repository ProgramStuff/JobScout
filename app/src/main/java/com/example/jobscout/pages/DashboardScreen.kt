package com.example.jobscout.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.jobscout.Data.AppliedJob
import com.example.jobscout.Data.JobViewModel
import com.example.jobscout.Data.UserViewModel
import com.example.jobscout.R

/*
    viewmodels passed as arguments to access database from different activities
 */

@Composable
fun Dashboard(
    appliedVewModel: AppliedViewModel,
    userViewModel: UserViewModel,
    jobViewModel: JobViewModel
) {
    /*
    Using a nav controller to navigate between to activities.
    Improved user dashboard experience
     */
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "applied") {
        composable("applied") {
            AppliedJobScreen(
                navController,
                appliedVewModel,
                userViewModel,
                jobViewModel
            )
        }
        composable("saved") {
            SavedJobScreen(
                navController,
                appliedVewModel,
                userViewModel,
                jobViewModel
            )
        }
    }
}

/**  APPLIED JOBS **/
@Composable
fun AppliedJobScreen(
    navController: NavHostController,
    appliedVewModel: AppliedViewModel,
    userViewModel: UserViewModel,
    jobViewModel: JobViewModel
) {
    // Fetch data
    val currentUser = userViewModel.loggedInUser
    val appliedJobs = appliedVewModel.appliedJob
    val allJobs = jobViewModel.jobs


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(221, 221, 221)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        DisplayTitle()

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(
                onClick = { navController.navigate("applied") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(98, 114, 84)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .width(150.dp)
            ) {
                Text(text = "Applied Jobs")
            }
            Button(
                onClick = { navController.navigate("saved") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(98, 114, 84)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .width(150.dp)
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
            // Loop through data to display the users applied jobs.
            // Create a JobCard for each row
            for (appliedJob in appliedJobs) {
                if (currentUser != null) {
                    if (appliedJob.userId == currentUser.userId) {
                        for (job in allJobs) {
                            if (job.jobId == appliedJob.jobId && appliedJob.status != "Saved") {
                                Row {
                                    JobCard(
                                        currentUser.userId,
                                        appliedJob.jobId,
                                        job.jobTitle,
                                        appliedJob.status,
                                        job.url,
                                        appliedVewModel
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }

            // ! Keep this spacer at the bottom, it ensures cards are not hidden by nav bar !
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}


/**  SAVED JOBS **/
@Composable
fun SavedJobScreen(
    navController: NavHostController,
    appliedVewModel: AppliedViewModel,
    userViewModel: UserViewModel,
    jobViewModel: JobViewModel
) {
    val currentUser = userViewModel.loggedInUser
    val appliedJobs = appliedVewModel.appliedJob
    val allJobs = jobViewModel.jobs
    val uriHandler = LocalUriHandler.current
    val uid = currentUser?.userId


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(221, 221, 221)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DisplayTitle()
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(
                onClick = { navController.navigate("applied") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(98, 114, 84)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .width(150.dp)
            ) {
                Text(text = "Applied Jobs")
            }
            Button(
                onClick = { navController.navigate("saved") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(98, 114, 84)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .width(150.dp)
            ) {
                Text(text = "Saved Jobs")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(text = "Saved Jobs", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Column {

                // Table Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(top = 8.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Title", fontWeight = FontWeight.Bold, color = Color(118, 136, 91), modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    Text(text = "Link", fontWeight = FontWeight.Bold, color = Color(118, 136, 91), modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    Text(text = "Status", fontWeight = FontWeight.Bold, color = Color(118, 136, 91), modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    Text(text = "Delete", fontWeight = FontWeight.Bold, color = Color(118, 136, 91), modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                }

                // Rows
                Column(
                    modifier = Modifier.padding(bottom = 16.dp)
                        .background(Color(238, 238, 238)),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    for (appliedJob in appliedJobs) {
                        if (currentUser != null) {
                            if (appliedJob.userId == currentUser.userId) {
                                for (job in allJobs) {
                                    if (job.jobId == appliedJob.jobId && appliedJob.status == "Saved") {

                                        var expanded by remember { mutableStateOf(false) }
                                        var selectedStatus by remember { mutableStateOf(appliedJob.status) }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .border(1.dp, Color.Gray)
                                                .padding(4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // Titles
                                            Text(
                                                text = job.jobTitle,
                                                modifier = Modifier.weight(1f),
                                                maxLines = 1,
                                                textAlign = TextAlign.Center,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                modifier = Modifier
                                                    .padding()
                                                    .weight(1f),
                                                maxLines = 1,
                                                textAlign = TextAlign.Center,
                                                overflow = TextOverflow.Ellipsis,
                                                /*
                                                 I am using an annotated to apply different styles to a single string
                                                 and to link the job posting to the title
                                                 */
                                                text = buildAnnotatedString {


                                                    val link =
                                                        LinkAnnotation.Url(
                                                            job.url,
                                                            TextLinkStyles(
                                                                style = SpanStyle(
                                                                    color = Color(98, 114, 84),
                                                                    fontWeight = FontWeight.Bold,
                                                                    fontSize = 20.sp
                                                                ),
                                                                hoveredStyle = SpanStyle(
                                                                    textDecoration = TextDecoration.Underline
                                                                )
                                                            )
                                                        ) {
                                                            val url =
                                                                (it as LinkAnnotation.Url).url
                                                            // log some metrics
                                                            uriHandler.openUri(url)
                                                        }
                                                    withLink(link) {
                                                        append("🔗")
                                                    }
                                                }
                                            )

                                            // Status dropdown
                                            Box(
                                                modifier = Modifier.weight(1f)) {
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Center,
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Text(
                                                        // The text of each dropdown
                                                        text = selectedStatus,
                                                        textAlign = TextAlign.Center,
                                                        fontSize = if (selectedStatus == "Interviewing") 13.sp else 14.sp,
                                                        modifier = if (selectedStatus == "Interviewing") Modifier.clickable { expanded = !expanded }.padding(top = 1.dp)
                                                        else Modifier.clickable { expanded = !expanded }.padding(top = 0.dp)
                                                    )
                                                    Icon(
                                                        imageVector = Icons.Filled.ArrowDropDown,
                                                        contentDescription = null
                                                    )
                                                    DropdownMenu(
                                                        expanded = expanded,
                                                        onDismissRequest = { expanded = false },
                                                        modifier = Modifier.background(Color(238, 238, 238))
                                                    ) {

                                                        // Dropdown options
                                                        val statuses = listOf("Saved", "Applied", "Interviewing", "Hired", "Rejected")
                                                        statuses.forEach { status ->
                                                            DropdownMenuItem( text = {
                                                                Text(text = status)
                                                            },
                                                                onClick = {
                                                                    selectedStatus = status
                                                                    expanded = false
                                                                    if (uid != null) {
                                                                        appliedVewModel.updateAppliedJob(uid, job.jobId, selectedStatus)
                                                                    }
                                                                }
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                            // Delete buttons
                                            TextButton(
                                                modifier = Modifier.weight(1f),
                                                onClick = {
                                                    if (uid != null) {
                                                        appliedVewModel.deleteAppliedJob(
                                                            uid,
                                                            job.jobId
                                                        )
                                                    }
                                                },
                                            ) {
                                                Text(
                                                    modifier = Modifier.padding(top = 0.dp),
                                                    maxLines = 1,
                                                    color = Color.Red,
                                                    textAlign = TextAlign.Center,
                                                    text = "DELETE"
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }

        // spacer?

        // Prev and next buttons
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Button(onClick = { /* Logic goes here */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(98, 114, 84)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .width(100.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
                Text(text = " Prev")
            }

            Text(
                text = "1/1",
                modifier = Modifier.padding(8.dp)
            )

            Button(onClick = { /* Logic goes here */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(98, 114, 84)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .width(100.dp)
            ) {
                Text(text = "Next ")
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

    }
}



@Composable
fun JobCard(
    userId: Int,
    jobId: Int,
    jobTitle: String,
    jobStatus: String,
    jobUrl: String,
    appliedVewModel: AppliedViewModel
) {
    val uriHandler = LocalUriHandler.current

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(Color(238, 238, 238)),
        modifier = Modifier
            .size(width = 260.dp, height = 190.dp),
    ) {

        Text(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),

            /*
             I am using an annotated to apply different styles to a single string
             and to link the job posting to the title
             */
            text = buildAnnotatedString {


                val link =
                    LinkAnnotation.Url(
                        jobUrl,
                        TextLinkStyles(
                            style = SpanStyle(
                                color = Color(98, 114, 84),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            hoveredStyle = SpanStyle(textDecoration = TextDecoration.Underline)
                        )
                    ) {
                        val url = (it as LinkAnnotation.Url).url
                        // log some metrics
                        uriHandler.openUri(url)
                    }
                withLink(link) {
                    append(jobTitle)
                }

                withStyle(
                    // Apply style to the second section of the string
                    style = SpanStyle(
                        fontSize = 15.sp,
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
fun JobStatusSelection(
    currentStatus: String,
    uid: Int,
    jobid: Int,
    appliedVewModel: AppliedViewModel
) {

    var isDropDownExpanded by remember { mutableStateOf(false) }

    // Drop down options for the job status
    val jobStatus = listOf("Applied", "Interviewing", "Hired", "Rejected", "Saved", "Delete")
    var itemPosition by remember { mutableIntStateOf(jobStatus.indexOf(currentStatus)) }


    Row(
        modifier = Modifier
            .padding(start = 15.dp, top = 5.dp, bottom = 0.dp)
            .clickable {
                isDropDownExpanded = true
            }
    ) {
        Text(
            modifier = Modifier.padding(bottom = 0.dp),
            color = Color(118, 136, 91),
            text = "Status: "
        )
        Text(
            // The selected text
            modifier = Modifier.padding(bottom = 0.dp),
            color = Color(118, 136, 91),
            text = jobStatus[itemPosition]
        )
        Icon(
            // Drop down arrow for UX
            Icons.Filled.KeyboardArrowDown,
            contentDescription = "Dropdown arrow",
            tint = Color(98, 114, 84)
        )
        DropdownMenu(modifier = Modifier.background(Color(238, 238, 238)),
            expanded = isDropDownExpanded,
            onDismissRequest = {
                isDropDownExpanded = false
            }) {
            // Create a drop down menu choice for each item
            jobStatus.forEachIndexed { index, status ->
                DropdownMenuItem(
                    text = {
                        Text(text = status)
                    },
                    onClick = {
                        // Collapse the drop down when item clicked
                        isDropDownExpanded = false
                        itemPosition = index
                    })
            }
        }
    }
    // SAVE and DELETE functionality
    Row(modifier = Modifier.padding(start = 15.dp, top = 0.dp)) {
        TextButton(
            modifier = Modifier.padding(top = 0.dp),
            onClick = {
                // using chosen drop down item when save is clicked
                // Updates the applied job status in DB
                if (jobStatus[itemPosition] == "Delete") {
                    appliedVewModel.deleteAppliedJob(uid, jobid)
                } else {
                    appliedVewModel.updateAppliedJob(uid, jobid, jobStatus[itemPosition])
                }
            },
        ) {
            Text(
                modifier = Modifier.padding(top = 0.dp),
                color = Color(98, 114, 84),
                text = "SAVE"
            )
        }
        Spacer(modifier = Modifier.width(90.dp))

    }


}


@Composable
fun DisplayTitle() {

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
    Spacer(
        modifier = Modifier
            .height(30.dp)
    )
    Text(
        modifier = Modifier
            .fillMaxWidth(),
        text = "Job Scout", fontFamily = newFontFamily, fontSize = 40.sp,
        color = Color(98, 114, 84),
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(40.dp))
}




