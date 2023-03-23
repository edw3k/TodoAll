package com.example.ncaandroid

class StaticTasks {
    companion object {
        @JvmStatic
        var tasks: MutableList<TaskData> = mutableListOf()
    }
}