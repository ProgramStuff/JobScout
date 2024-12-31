package com.example.jobscout.pages

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobscout.Data.AppliedJob
import com.example.jobscout.Data.AppliedViewModel
import com.example.jobscout.Data.Job
import com.example.jobscout.Data.JobViewModel
import com.example.jobscout.Data.UserViewModel
import com.example.jobscout.R

@Composable
fun HomeScreen(jobViewModel: JobViewModel, userViewModel: UserViewModel, appliedViewModel: AppliedViewModel) {
    // Get a listing of all job postings by taking the view model's jobs property
    val jobPostings = jobViewModel.jobs

    // Mutable state to hold the selected job
    var selectedJob by remember { mutableStateOf<Job?>(null) }
    var searchQuery by remember { mutableStateOf("") }


    // Filter the job postings based on the search query
    val filteredJobPostings = jobPostings.filter {
        // Check if the job title or summary contains the search query
        it.jobTitle.contains(searchQuery, ignoreCase = true) ||
                it.summary.contains(searchQuery, ignoreCase = true)
    }

    // Adding google font for prettier text
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )
    val fontName = GoogleFont("Lobster Two")
    val appFontFamily = FontFamily(Font(googleFont = fontName, fontProvider = provider))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDDDDDD)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Job Scout",
            fontFamily = appFontFamily,
            fontSize = 40.sp,
            color = Color(0xFF627254),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))

        // Search TextField
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = { Text(text = "Search for a job") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Crossfade allows for switching between two composables with a smooooooth animation
        Crossfade(targetState = selectedJob) { job: Job? ->
            if (job == null) {
                println("Job Listings")
                JobList(
                    // If search query is empty, show all job postings, otherwise show filtered job postings
                    jobPostings = filteredJobPostings,
                    onJobSelected = { selectedJob = it }
                )
            } else {
                // Show job details
                println("Job details")
                JobDetails(
                    job = job,
                    appliedViewModel = appliedViewModel,
                    user = userViewModel,
                    onBack = { selectedJob = null }
                )
            }
        }
    }
}

// JobList composable to display a list of job postings
@Composable
fun JobList(jobPostings: List<Job>, onJobSelected: (Job) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 100.dp)
    ) {
        // Iterate through the list of job postings and display each one
        items(jobPostings) { job ->
            JobListItem(job = job, onClick = { onJobSelected(job) })
        }
    }
}

// JobListItem composable to display information of a single job posting
@Composable
fun JobListItem(job: Job, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(
            text = job.jobTitle,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = job.summary,
            fontSize = 14.sp,
            color = Color(0xFF666666),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

// JobDetails composable to display the details of a single job posting
@Composable
fun JobDetails(job: Job, appliedViewModel: AppliedViewModel, user: UserViewModel, onBack: () -> Unit) {
    // Get the user ID of the logged in user
    val userId = user.loggedInUser?.userId

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = job.jobTitle,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = job.jobDescription,
            fontSize = 16.sp,
            color = Color(0xFF666666)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Go back",
            fontSize = 16.sp,
            color = Color(0xFF007BFF),
            modifier = Modifier.clickable { onBack() }
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Row for buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Save Job button
            Button(
                onClick = {
                    userId?.let {
                        AppliedJob(
                            userId = it,
                            jobId = job.jobId,
                            dateApplied = getCurrentDate(),
                            status = "Saved"
                        )
                    }?.let {
                        appliedViewModel.addAppliedJob(
                            it
                        )
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Save Job")
            }

            // Apply to Job button
            Button(
                onClick = {
                    userId?.let {
                        AppliedJob(
                            userId = it,
                            jobId = job.jobId,
                            dateApplied = getCurrentDate(),
                            status = "Applied"
                        )
                    }?.let {
                        appliedViewModel.addAppliedJob(
                            it
                        )
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Apply to Job")
            }
        }
    }
}

// Helper function to get the current date
fun getCurrentDate(): String {
    val formatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
    return formatter.format(java.util.Date())
}