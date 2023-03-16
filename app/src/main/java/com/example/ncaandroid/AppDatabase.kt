package com.example.ncaandroid

import android.content.Context
import androidx.room.*
import com.example.ncaandroid.TaskDAO

@Database(
    entities = [TaskData::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun TaskDAO(): TaskDAO

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "todoall.db").build()
                } }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}




@Dao
interface TaskDAO {
    @Query("SELECT * FROM taskdata")
    fun loadAllTasks(): List<TaskData>

    @Insert
    fun insert(task: TaskData)
}