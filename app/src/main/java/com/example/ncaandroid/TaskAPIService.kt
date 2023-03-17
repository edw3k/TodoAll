package com.example.ncaandroid

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET

interface TaskAPIService {
    @GET("db")
    fun getAllTasks(): Call<TaskResponse>

}