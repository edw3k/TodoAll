/*package com.example.ncaandroid

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDAO {
    @Query("SELECT * FROM tasks")
    fun loadAllTasks(): List<TaskData>

    @Insert
    fun insert(task: TaskData)
}


 */