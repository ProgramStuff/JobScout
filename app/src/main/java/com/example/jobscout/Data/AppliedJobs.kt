package com.example.jobscout.Data

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



/*
    Entity class with foreign keys to reference users and jobs tables
    The FK will be updated and deleted if they are in the parent columns
 */

@Entity(tableName = "applied_jobs",
    foreignKeys = [
        ForeignKey(
            entity = Job::class,
            parentColumns = ["jobId"],
            childColumns = ["jobId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AppliedJob(
    @PrimaryKey(autoGenerate = true) val appliedId: Int = 0,
    val userId: Int,
    val jobId: Int,
    val dateApplied: String,
    val status: String
)

// DAO (Data Access Object)\
@Dao
interface AppliedJobDao {
    @Insert
    suspend fun insert(applied: AppliedJob)

    @Query("SELECT * FROM applied_jobs")
    suspend fun getAllApplied(): List<AppliedJob>

    @Query("UPDATE applied_jobs SET status = :newStatus WHERE userId = :uid AND jobId = :jid")
    suspend fun updateStatus(uid: Int, jid: Int, newStatus: String)

    @Delete
    suspend fun delete(applied: AppliedJob)

    @Query("DELETE FROM applied_jobs WHERE userId = :uid AND jobId = :jobId")
    suspend fun deleteAppliedJob(uid: Int, jobId: Int)
}


//View Model (Logic)

class AppliedViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val appliedJobDao = db.appliedJobDao()

    // Hold job list
    private val _appliedJobs = mutableStateListOf<AppliedJob>()
    val appliedJob: List<AppliedJob> get() = _appliedJobs

    init {
        // Initialize by loading users
        getAppliedJobs()
    }


    // SetUid needs to be called after user is logged in
    private var currentUid: Int = 1

    // Set the current user and get applied jobs
    fun setCurrentUser(user: User) {
        currentUid = user.userId
        getAppliedJobs()
    }

    fun setUid(uid: Int) {
        currentUid = uid
        getAppliedJobs()
    }

    private fun getAppliedJobs(){
        try {
            viewModelScope.launch {
                val appliedJobsFromDb = withContext(Dispatchers.IO) {
                    appliedJobDao.getAllApplied()
                }
                _appliedJobs.clear()
                _appliedJobs.addAll(appliedJobsFromDb)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun addAppliedJob(appliedJob: AppliedJob){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                appliedJobDao.insert(appliedJob)
            }
            getAppliedJobs()
        }
    }

    fun deleteAppliedJob(uid: Int, jobId: Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                appliedJobDao.deleteAppliedJob(uid, jobId)
            }
            getAppliedJobs()
        }
    }

    private fun updateAppliedJob(uid: Int, jid: Int, newStatus: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                appliedJobDao.updateStatus(uid, jid, newStatus)
            }
            getAppliedJobs()
        }
    }

    // Clear applied jobs when user logs out
//    fun clearAppliedJobs() {
//        _appliedJobs.clear()
//        currentUid = null
//    }
}
