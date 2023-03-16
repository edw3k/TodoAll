package com.example.ncaandroid

import android.os.Parcel
import android.os.Parcelable
import java.util.UUID

// This class represents a note in the app and implements the Parcelable interface so that
// the note can be passed between activities using a "putParcelable" or "getParcelable" method.
// Some methods are implemented because the class implements the Parcelable interface.
class Note(): Parcelable {
    var id: UUID = UUID.randomUUID() // Unique ID for the note
    var title: String = "" // Title of the note
    var content: String = "" // Content of the note
    var color: String = "" // Color of the note

    // Constructor for the Parcelable interface that creates a note from a Parcel object (a
    // Parcel object is a container for a message (data and object references) that can be sent
    // through an IBinder) and reads the data from the Parcel object and assigns it to the note
    constructor(parcel: Parcel) : this() {
        title = parcel.readString().toString()
        content = parcel.readString().toString()
        color = parcel.readString().toString()
    }

    // Constructor for the note that takes the title, content and color of the note as parameters
    // without an ID (the ID is generated automatically)
    constructor(title: String, content: String, color: String) : this() {
        this.title = title
        this.content = content
        this.color = color
    }

    // Constructor for the note that takes the ID, title, content and color of the note as
    // parameters
    constructor(id: UUID, title: String, content: String, color: String) : this() {
        this.id = id
        this.title = title
        this.content = content
        this.color = color
    }

    // Method for the Parcelable interface that writes the data from the note to a Parcel object
    override fun describeContents(): Int {
        return 0
    }

    // Method for the Parcelable interface that writes the data from the note to a Parcel object
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(content)
        dest.writeString(color)
    }

    // Companion object for the Parcelable interface
    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note { // Creates a note from a Parcel object
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> { // Creates an array of notes
            return arrayOfNulls(size)
        }
    }
}