package com.example.ncaandroid

import android.os.Parcel
import android.os.Parcelable
import java.util.Date
import java.util.UUID

// This is a data class that represents a task that the user can create and add to the list
class Task(): Parcelable {
    var id: UUID = UUID.randomUUID() // Unique ID for the task
    var content: String = "" // Content of the task
    var priority: Priority = Priority.DEFAULT // Priority of the task
    var isDone: Boolean = false // Whether the task is done or not
    var date = Date() // Date the task was created

    // This is a constructor that will be used to create a task with just a content and a priority
    constructor(content: String, priority: Priority) : this() {
        this.content = content
        this.priority = priority
    }

    // This is a constructor that will be used to create a task with the content, priority, whether
    // the task is done or not and the date the task was created
    constructor(content: String, priority: Priority, isDone: Boolean, date: Date) : this() {
        this.content = content
        this.priority = priority
        this.isDone = isDone
        this.date = date
    }

    // This is a constructor that will be used to create a task from a parcel
    constructor(parcel: Parcel) : this() {
        id = UUID.fromString(parcel.readString())
        content = parcel.readString().toString()
        priority = Priority.valueOf(parcel.readString().toString())
        isDone = parcel.readByte() != 0.toByte()
    }

    // This is a function to describe the contents of the parcel
    // We will not be using this function
    override fun describeContents(): Int {
        return 0
    }

    // This is a function that will be used to write the task to a parcel
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id.toString())
        dest.writeString(content)
        dest.writeString(priority.toString())
        dest.writeByte(if (isDone) 1 else 0)
    }

    // This is a companion object that will be used to create a task from a parcel
    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}