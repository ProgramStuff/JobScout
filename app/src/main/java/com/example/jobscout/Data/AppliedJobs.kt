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

    @Query("SELECT * FROM applied_jobs WHERE userId = :uid")
    suspend fun getAllApplied(uid: Int): List<AppliedJob>

    @Query("UPDATE applied_jobs SET status = :newStatus WHERE userId = :uid AND jobId = :jid")
    suspend fun updateStatus(uid: Int, jid: Int, newStatus: String)

    @Delete
    suspend fun delete(applied: AppliedJob)
}

//View Model (Logic)

class AppliedViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val appliedJobDao = db.appliedJobDao()

    // Hold job list
    private val _appliedJobs = mutableStateListOf<AppliedJob>()
    val appliedJob: List<AppliedJob> get() = _appliedJobs



    // SetUid needs to be called after user is logged in
    private var currentUid: Int = 0

    fun setUid(uid: Int) {
        currentUid = uid
        getAppliedJobs(uid)
    }

    private fun getAppliedJobs(uid: Int){
        viewModelScope.launch {
            val appliedJobsFromDb = withContext(Dispatchers.IO){
                appliedJobDao.getAllApplied(uid)
            }
            _appliedJobs.clear()
            _appliedJobs.addAll(appliedJobsFromDb)
        }
    }

    fun addAppliedJob(appliedJob: AppliedJob, uid: Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                appliedJobDao.insert(appliedJob)
            }
            getAppliedJobs(uid)
        }
    }

    fun deleteAppliedJob(appliedJob: AppliedJob, uid: Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                appliedJobDao.delete(appliedJob)
            }
            getAppliedJobs(uid)
        }
    }

    private fun updateAppliedJob(uid: Int, jid: Int, newStatus: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                appliedJobDao.updateStatus(uid, jid, newStatus)
            }
            getAppliedJobs(uid)
        }
    }

}
