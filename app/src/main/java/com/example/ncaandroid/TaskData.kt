package com.example.ncaandroid

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tasks")
data class TaskData(@PrimaryKey(autoGenerate = true) var id: Int?,
                    @SerializedName("content") var content: String?,
                    @SerializedName("priority") var priority: Int?,
                    @SerializedName("isDone") var isDone: Boolean?,
                    @SerializedName("date") var date: String?,
                    @SerializedName("telf") var telf: String?,
                    @SerializedName("web") var web: String?)

