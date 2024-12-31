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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
                    modifier = Modifier.fillMaxSize()
                ) {
                    StartApp(userViewModel, appliedVewModel, jobViewModel)
                }
            }
        }
    }
}

@Composable
fun StartApp(
    userViewModel: UserViewModel,
    appliedViewModel: AppliedViewModel,
    jobViewModel: JobViewModel
) {

    val jobList = listOf(
        Job(
            jobTitle = "Junior Software Developer",
            jobDescription = "Join \"The Likeable Software Company\" in Halifax, Nova Scotia, as a Junior Software Developer. This role involves contributing to all stages of software development, working collaboratively on client projects, and resolving software issues. Candidates should have a Bachelor’s in Computer Science or equivalent experience, with knowledge of Java, C#, or PHP, and database systems. No professional experience required, making it ideal for motivated learners.",
            url = "https://ca.indeed.com/q-software-developer-l-nova-scotia-jobs.html?vjk=93982bb98748488b",
            summary = "“The Likeable Software Company” is hiring Junior Software Developers. Candidates with knowledge of Java, C#, or PHP, and a Bachelor’s in Computer Science are welcome. No experience is required, and the starting salary is $42,500."
        ),
        Job(
            jobTitle = "Software Engineer",
            jobDescription = "Eastlink, headquartered in Halifax, NS, seeks a Software Engineer for an 18-month on-site role. The position focuses on developing features for internal and external platforms, optimizing applications, and improving software architecture. Candidates should have a Computer Science degree (or equivalent) with 5+ years of experience, including Java frameworks (Spring Boot, MVC, Data JPA, Security), DevOps tools, and RDBMS databases. Strong teamwork, communication, and familiarity with agile methodologies are required.",
            url = "https://ca.indeed.com/viewjob?jk=61b87cd0c2e3a240&tk=1iecvol8s2i3503g&from=serp&vjs=3",
            summary = "Eastlink seeks an experienced Software Engineer for an 18-month on-site position in Halifax, NS. Responsibilities include developing features, optimizing applications, and improving software practices. Candidates need 5+ years of experience, including Java frameworks, DevOps tools, and RDBMS databases. Eastlink offers a supportive and growth-oriented environment."
        ),
        Job(
            jobTitle = "Web Developer",
            jobDescription = "Content Bloom, based in Halifax, NS, is hiring a Web Developer to join its collaborative team. The role involves developing modular, reusable web features, consulting with clients, and working on enterprise-grade projects using CMS platforms like SDL Tridion, Sitecore, and AEM. Candidates need a Bachelor’s in Computer Science, 1+ years of web development experience with Java, React, Angular, or .NET, strong communication skills, and Canadian work authorization. This position offers opportunities for global travel, flexible hours, and exciting perks.",
            url = "https://ca.indeed.com/q-software-developer-l-nova-scotia-jobs.html?vjk=a7a5a9fd0d44c50c",
            summary = "Content Bloom is seeking a Web Developer in Halifax, NS. The role involves consulting with clients, building innovative web solutions, and working with CMS platforms like SDL Tridion and AEM. Candidates need a Computer Science degree and 1+ years of web development experience. Perks include flexible hours, health benefits, RRSP matching, and office dogs."
        ),
        Job(
            jobTitle = "Junior Web Developer",
            jobDescription = "A Dartmouth, NS company is seeking a Junior Web Developer for a full-time, hybrid role. The position involves designing, implementing, and maintaining web applications using front-end technologies like HTML, CSS, JavaScript, and TypeScript, alongside back-end frameworks such as Python and Ruby on Rails. Additional responsibilities include managing databases with PostgreSQL, implementing DevOps practices, and contributing to E-commerce development. Candidates must be proficient in .NET and have strong communication skills. Benefits include health, dental, vision care, paid time off, on-site gym access, and flexible scheduling.",
            url = "https://ca.indeed.com/q-software-developer-l-nova-scotia-jobs.html?vjk=2649a9130b36d7f1",
            summary = "A company in Dartmouth, NS is hiring a Web Developer for a hybrid role with a salary of $50,000–$60,000. Responsibilities include building web applications using .NET, Python, and Ruby on Rails, and managing databases with PostgreSQL. Benefits include health and dental care, paid time off, and access to an on-site gym."
        ),
        Job(
            jobTitle = "Software Engineer",
            jobDescription = "ResMed is seeking a Software Engineer to join their team in Halifax, NS, developing innovative healthcare solutions. Responsibilities include writing and debugging code in C#, ASP.NET, and ASP.NET Core; designing relational databases using SQL Server and Oracle; implementing secure and scalable solutions; and utilizing AWS for cloud-based services. Candidates must hold a Bachelor’s degree in Computer Science or related fields, with 5+ years of experience in object-oriented programming, relational databases, and web development. Preferred qualifications include test automation and healthcare industry experience.",
            url = "https://ca.indeed.com/q-software-developer-l-nova-scotia-jobs.html?vjk=cbde00bdeb5e5bfa",
            summary = "ResMed is hiring a Software Engineer in Halifax, NS, with a salary of up to $120,000 CAD. The role involves developing healthcare solutions using C#, ASP.NET, and AWS, and designing SQL Server and Oracle databases. Candidates need a Bachelor’s degree in Computer Science and 5+ years of experience in programming and web development. Benefits include health coverage, commuter perks, and tuition assistance."
        ),
        Job(
            jobTitle = "Java Backend Developer",
            jobDescription = "We are looking for a Senior Java Developer with a minimum of 6 years of experience for a hybrid role in Halifax, NS. The ideal candidate will have expertise in Java, J2EE, Spring (Spring Boot, Batch, DI), microservices, database and batch processing, and Linux scripting. Responsibilities include contributing to technical design discussions, guiding junior developers, and leading end-to-end deliverables in an agile environment. Preferred skills include Big Data technologies and JMS/MQ knowledge. The role requires 3+ days of in-office work per week.",
            url = "https://ca.indeed.com/jobs?q=software+developer&l=nova+scotia&radius=50&start=20&vjk=4516797340f10277",
            summary = "A Senior Java Developer role in Halifax, NS (hybrid) pays $32.36–$50.56/hour on a contract basis. The role demands expertise in Java, Spring Boot, microservices, database and batch processing, and Linux scripting. Preferred skills include Big Data and JMS/MQ. Benefits include dental, vision, and retirement plans, with in-office work required at least 3 days per week."
        ),
        Job(
            jobTitle = "Customer Service Agent",
            jobDescription = "Scotia Diesel Services Ltd in Antigonish, NS is looking for a full-time Service Advisor to manage customer relations and repair appointments. Responsibilities include scheduling work, interacting with customers, reviewing parts orders, and helping achieve department profit goals. Ideal candidates should have previous customer service experience, strong communication skills, and proficiency in MS Excel.",
            url = "https://ca.indeed.com/viewjob?jk=9c9d45584d38f644&l=nova+scotia&tk=1iemc9gvei8rd80n&from=web&advn=4439858176453229&adid=439640039&ad=-6NYlbfkN0A25J9SG9u1a4qx5mBvuNmJPR8ODFon5hZIWKbC1oNf0kf2R1ZqS-hzyzbPTKWqvttQgm70FUl0jjW8Vxb4LZCMB1ZqduZyM-wxvH1yNTs1lM3Olu1K9JxHQONx6iYwFXf8U62I7_uZxJLo-6Idcwq2TgssR6CsSgFWIMkx-nQ9X6ypIg6ZFVVoaIPjHNKsVZqrjaEoPLTmAvIkZvHpLXlmPslndFnLy7BFGHtjDkQDtxN0UgRyEzg6j7oprRHiqyU0AFJqxVoZQAG8R-1pSuAWgqi31yRvhZQLx3SLYiB6Gg1QM8x9XwJ9eLzX0JheYHdauc-c8H31SEM8FmL5mrUqFHDDAm4mye9wIJw6z3Z0jrmZ2x6SKaYNKMRNm53cyUC0eOKkkm-ewKdKLKkOgvgWKa1t5ccymRrZ3gMfv08tUBe6wLuFtR7cvPdmUC42KXlephoo0u88BcLwZ29isDq3o4fGyEH4kCYlpHguSM6cyi_uQkDt3tNQf7YA1taaMhhhLTmrPjzg5kYA0wwYdjbj&pub=4a1b367933fd867b19b072952f68dceb&camk=nUmJqO2E8rgX9bjPkoBerg%3D%3D&xkcb=SoAt6_M34WQp8yQpcZ0KbzkdCdPP&xpse=SoCu6_I34WQnnMS4dx0IbzkdCdPP&xfps=4c7bb2f6-5f59-4693-a3f9-0d8fa7878aba&vjs=3",
            summary = "Scotia Diesel Services Ltd in Antigonish, NS is hiring a full-time Service Advisor. Responsibilities include scheduling work, managing parts orders, and handling customer interactions. The role requires previous customer service experience, strong communication skills, and proficiency in MS Excel."
        ),
        Job(
            jobTitle = "Customer Service Rep",
            jobDescription = "Kohltech Windows and Entrance Systems in Debert, NS is hiring a full-time Customer Service Representative. This role involves responding to dealer inquiries, providing product solutions, maintaining relationships, preparing quotations, and resolving customer issues. The position requires strong communication, organizational skills, proficiency in Microsoft Office, and a minimum of one year of professional experience. A post-secondary education or equivalent experience is preferred, and experience with carpentry or building supplies is an asset.",
            url = "https://ca.indeed.com/jobs?q=customer+service&l=nova+scotia&from=searchOnDesktopSerp%2Cwhatautocomplete&vjk=4bfff5bea75eda2b&advn=8997913833094522",
            summary = "Kohltech Windows and Entrance Systems is hiring a full-time Customer Service Representative in Debert, NS. The role involves responding to dealer inquiries, offering product solutions, preparing quotations, and collaborating with various departments. Qualifications include strong communication skills, organizational ability, Microsoft Office proficiency, and a year of professional experience."
        ),
        Job(
            jobTitle = "Customer Service Admin",
            jobDescription = "John Ross & Sons Ltd. in Goodwood, NS is hiring a Customer Service Administrator for a full-time role with a pay range of $18–$20/hour. The position involves reception duties, customer service, scaling truck weights, data entry, cash reconciliation, and assisting with administrative tasks across departments. Applicants must have 2-4 years of experience in a cash environment, proficiency in MS Office and Excel, strong organizational skills, and reliable transportation.",
            url = "https://ca.indeed.com/jobs?q=customer+service&l=nova+scotia&from=searchOnDesktopSerp%2Cwhatautocomplete&vjk=52d467d7803de1b7&advn=1961355519378669",
            summary = "A Customer Service Administrator role is available at John Ross & Sons Ltd. in Goodwood, NS, offering $18–$20/hour. The role includes reception duties, customer service, scaling truck weights, data entry, and cash reconciliation. Applicants should have 2-4 years of experience in a cash environment, MS Office proficiency, and reliable transportation."
        ),
        Job(
            jobTitle = "Customer Service Rep",
            jobDescription = "Z's Heating & Cooling in Dartmouth, NS is hiring a Sales Representative, offering pay starting at $18.25/hour, with bonus and commission opportunities. The role involves generating new business, maintaining client relationships, attending appointments across the province, and collaborating with the marketing team to boost brand visibility. Candidates should have proven sales experience, excellent negotiation skills, a clean driving abstract, and fluency in English.",
            url = "https://ca.indeed.com/jobs?q=customer+service&l=nova+scotia&from=searchOnDesktopSerp%2Cwhatautocomplete&vjk=b60747e32850487a&advn=7541169684530205",
            summary = "A Sales Representative position is available at Z's Heating & Cooling in Dartmouth, NS, offering $18.25/hour, with bonus and commission. This full-time role involves generating business, maintaining client relationships, and attending appointments across the province. Candidates need proven sales experience, strong negotiation skills, and a clean driving record."
        )
    )

    jobViewModel.deleteAllRows()

    jobList.forEach { job ->
        jobViewModel.addJob(job)
    }

    jobViewModel.getJobs()

    var isLoggedIn by remember { mutableStateOf(false) }
    var isSignedUp by remember { mutableStateOf(false) }

    if (isLoggedIn) {
        WelcomeScreen(appliedViewModel, jobViewModel, userViewModel)
    } else {
        // function that is called on successful login
        Login(
            onLoginSuccess = { isLoggedIn = true },
            // Successful set the value state to true to navigate to login
            onSignUpSuccess = { isSignedUp = true },
            userViewModel = userViewModel
        )
    }
}





