package com.example.jobscout.Data

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext




// Entity class
@Entity(tableName = "jobs")
data class Job(
    @PrimaryKey(autoGenerate = true) val jobId: Int = 0,
    val jobTitle: String = "",
    val jobDescription: String = "",
    val url: String = "",
    val summary: String = ""
)

// DAO (Data Access Object)\
@Dao
interface JobDao {
    @Insert
    suspend fun insert(job: Job)

    @Query("SELECT * FROM jobs")
    suspend fun getAllJobs(): List<Job>

    @Delete
    suspend fun delete(job: Job)
}

//View Model (Logic)


class JobViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val jobDao = db.jobDao()

    // Hold job list
    private val _jobs = mutableStateListOf<Job>()
    val jobs: List<Job> get() = _jobs

    init {
        // Initialize by loading Jobs
        viewModelScope.launch {
            getJobs()
        }    }

    private suspend fun getJobs(){
        val jobsFromDb = withContext(Dispatchers.IO){
            jobDao.getAllJobs()
        }
        _jobs.clear()
        _jobs.addAll(jobsFromDb)
    }

    fun addJob(job: Job){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                jobDao.insert(job)
            }
            getJobs()
        }
    }

    fun deleteJob(job: Job){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                jobDao.delete(job)
            }
            getJobs()
        }
    }
}