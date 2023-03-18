package com.example.ncaandroid

import androidx.room.Update
import retrofit2.Call
import retrofit2.http.*

interface TaskAPIService {
    @GET("db")
    fun getAllTasks(): Call<TaskResponse>

}