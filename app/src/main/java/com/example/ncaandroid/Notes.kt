package com.example.ncaandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Notes : Fragment() {
    private var notes = ArrayList<Note>() // List of notes
    private var verticalRecyclerView: RecyclerView? = null // RecyclerView for vertical layout
    private var horizontalRecyclerView: RecyclerView? = null // RecyclerView for horizontal layout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // As in the MainActivity, we need an instance of this class
        instance = this

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("notes", notes) // Save the notes
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // If the user has been using the notes, we will restore the state of the notes
        if (savedInstanceState != null) {
            notes = savedInstanceState.getParcelableArrayList<Note>("notes") as ArrayList<Note>
        } else { // If the user has not been using the notes, we will create some notes
            notes.add(Note("Shopping list",
                "Shopping list for the week\n" +
                        "1. Milk\n" +
                        "2. Bread\n" +
                        "3. Eggs\n" +
                        "4. Cheese\n" +
                        "5. Butter\n" +
                        "6. Chicken\n" +
                        "7. Beef\n" +
                        "8. Pork\n" +
                        "9. Fish\n" +
                        "10. Vegetables\n" +
                        "11. Fruits\n" +
                        "12. Cereals\n" +
                        "13. Pasta\n" +
                        "14. Rice\n",
                "GREEN"))
            notes.add(Note("Reminders",
                "Reminders for the week\n" +
                        "1. Call mom\n" +
                        "2. Call dad\n" +
                        "3. Call sister\n" +
                        "4. Study PHP\n" +
                        "5. Study Kotlin\n" +
                        "6. Finish NCA\n",
                "YELLOW"))
            notes.add(Note("Passwords",
                "Passwords for the week\n" +
                        "1. Facebook: neliitoOwnsYou\n" +
                        "2. Instagram: iHateiOS\n" +
                        "3. Twitter: phpEz\n" +
                        "4. Gmail: sistemesEasyWin\n" +
                        "5. Yahoo: whoUsesThis\n" +
                        "6. Outlook: iLoveAndroid\n",
                "RED"))
            notes.add(Note("Series to watch",
                "Series to watch\n" +
                        "1. The Big Bang Theory\n" +
                        "2. The Flash\n" +
                        "3. Arrow\n" +
                        "4. The Walking Dead\n" +
                        "5. The 100\n" +
                        "6. Lucifer\n" +
                        "7. The Good Doctor\n" +
                        "8. The Good Place\n" +
                        "9. The Mandalorian\n" +
                        "10. The Witcher\n",
                "BLUE"))

        }

        // If the orientation is vertical, we will use a vertical recycler view
        // We get the recycler view from the layout
        verticalRecyclerView = view?.findViewById(R.id.verticalRecyclerView)
        // We set the layout manager that is a grid layout manager with 2 columns
        verticalRecyclerView?.layoutManager = GridLayoutManager(context, 2)
        verticalRecyclerView?.adapter = context?.let { ListAdapter(notes, it) } // Adapter

        // If the orientation is horizontal, we will use a horizontal recycler view
        // We get the recycler view from the layout
        horizontalRecyclerView = view?.findViewById(R.id.horizontalRecyclerView)
        // We set the layout manager that is a grid layout manager with 1 column
        horizontalRecyclerView?.layoutManager = GridLayoutManager(context, 1,
            GridLayoutManager.HORIZONTAL, false)
        horizontalRecyclerView?.adapter = context?.let { ListAdapter(notes, it) } // Adapter

        // We get the button from the layout
        val addButton: Button? = view?.findViewById(R.id.add_btn) // Add button
        addButton?.setOnClickListener { // When the user clicks the button
            MainActivity.instance?.openDetailViewNote() // We open the detail view
            DetailViewNote.instance?.reset() // We reset the detail view
            DetailViewNote.instance?.changeTexts(0) // We set the viewType to 0 (Add Note)
        }
    }

    // This function is to add a note and update the recycler view
    fun addNote(note: Note) {
        notes.add(note)
        verticalRecyclerView?.adapter?.notifyItemInserted(notes.size - 1)
        horizontalRecyclerView?.adapter?.notifyItemInserted(notes.size - 1)
    }

    // This function is to edit a note and update the recycler view
    fun deleteNote(note: Note) {
        val index = notes.indexOf(note)
        notes.remove(note)
        verticalRecyclerView?.adapter?.notifyItemRemoved(index)
        horizontalRecyclerView?.adapter?.notifyItemRemoved(index)
    }

    // This function is to update a note and update the recycler view
    fun updateNote(note: Note) {
        for (i in 0 until notes.size) {
            if (notes[i].id == note.id) {
                notes[i] = note
                verticalRecyclerView?.adapter?.notifyItemChanged(i)
                horizontalRecyclerView?.adapter?.notifyItemChanged(i)
                break
            }
        }
    }

    // This is the static instance of this class that we will use in other classes
    companion object {
        @JvmStatic
        var instance: Notes? = null
    }
}