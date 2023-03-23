    package com.example.ncaandroid

    import android.content.Context
    import androidx.room.*

    @Database(
        entities = [TaskData::class],
        version = 2,
        exportSchema = false
    )
    abstract class AppDatabase: RoomDatabase() {
        abstract fun taskDao(): TaskDAO

        companion object {
            private var INSTANCE: AppDatabase? = null

            fun getInstance(context: Context): AppDatabase {
                if (INSTANCE == null) {
                    synchronized(AppDatabase::class) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "todoall.db").build()
                    }
                }
                return INSTANCE!!
            }

            fun destroyInstance() {
                INSTANCE = null
            }
        }
    }





    @Dao
    interface TaskDAO {
        @Query("SELECT * FROM tasks")
        fun loadAllTasks(): MutableList<TaskData>

        @Insert
        fun insert(task: TaskData)

        @Query("DELETE FROM tasks WHERE id = :taskId")
        fun deleteById(taskId: Long)

        @Update
        fun update(task: TaskData)

        @Insert
        fun insertAllTasks(tasks: MutableList<TaskData>)

    }