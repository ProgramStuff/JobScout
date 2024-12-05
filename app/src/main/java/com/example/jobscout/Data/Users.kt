package com.example.jobscout.Data

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


// Entity class
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val fName: String,
    val lName: String,
    val email: String,
    val password: String,
)

// DAO (Data Access Object)\
@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user_table")
    suspend fun getAllUsers(): List<User>

    @Delete
    suspend fun delete(user: User)
}

// Room Database
@Database(entities = [User::class, AppliedJob::class, Job::class], version = 2)
abstract class AppDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun appliedJobDao(): AppliedJobDao
    abstract fun jobDao(): JobDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Application): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "jobScout"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

//View Model (Logic)
class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val userDao = db.userDao()

    // Hold user list
    private val _users = mutableStateListOf<User>()
    val users: List<User> get() = _users

    init {
        // Initialize by loading users
        getUsers()
    }

    private fun getUsers(){
        try {
            viewModelScope.launch {
                val usersFromDb = withContext(Dispatchers.IO) {
                    userDao.getAllUsers()
                }
                _users.clear()
                _users.addAll(usersFromDb)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun addUser(user: User){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                userDao.insert(user)
            }
            getUsers()
        }
    }

    fun deleteUser(user: User){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                userDao.delete(user)
            }
            getUsers()
        }
    }
}