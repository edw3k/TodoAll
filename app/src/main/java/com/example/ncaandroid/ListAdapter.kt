package com.example.ncaandroid

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter for the RecyclerViews
class ListAdapter(private val notes: ArrayList<Note>, private val context: Context)
    : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    // OnCreateViewHolder is called when the RecyclerView needs a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list, parent,
            false)
        return ViewHolder(view)
    }

    // GetItemCount is called to get the number of items in the RecyclerView
    override fun getItemCount(): Int {
        return notes.size
    }

    // OnBindViewHolder is called to display the data at the specified position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position] // Get the note at the specified position
        holder.buttonNote.text = note.title // Set the title of the note
        when (note.color) { // Set the color of the note
            "RED" -> { // If the note is red
                holder.buttonNote.setBackgroundColor(Color.RED)
                holder.buttonNote.setTextColor(Color.WHITE)
            }
            "GREEN" -> { // If the note is green
                holder.buttonNote.setBackgroundColor(Color.GREEN)
                holder.buttonNote.setTextColor(Color.BLACK)
            }
            "YELLOW" -> { // If the note is yellow
                holder.buttonNote.setBackgroundColor(Color.YELLOW)
                holder.buttonNote.setTextColor(Color.BLACK)
            }
            else -> { // If the note is blue
                holder.buttonNote.setBackgroundColor(Color.BLUE)
                holder.buttonNote.setTextColor(Color.WHITE)
            }
        }
        holder.buttonDelete.setOnClickListener { // When the delete button is clicked
            AlertDialog.Builder(context) // Create an alert dialog
                .setTitle("Delete note")
                .setMessage("Are you sure you want to delete this note?") // Ask the user if they
                // are sure they want to delete the note (to prevent accidental deletion)
                .setPositiveButton("Yes") { _, _ -> // If the user clicks yes
                    notes.removeAt(position) // Remove the note from the list
                    notifyItemRemoved(position) // Notify the adapter that the item was removed
                    notifyItemRangeChanged(position, notes.size) // Notify the adapter that the
                    // range of items changed
                }
                .setNegativeButton("No") { _, _ -> } // If the user clicks no, do nothing
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }
        holder.buttonNote.setOnClickListener { // When the note is clicked
            MainActivity.instance?.openDetailViewNote() // Open the detail view of the note
            DetailViewNote.instance?.updateNote(note) // Update the detail view of the note
            DetailViewNote.instance?.changeTexts(1) // Change the viewState to 1 (edit mode)
        }
    }

    // ViewHolder class for the RecyclerView
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val buttonNote = view.findViewById<TextView>(R.id.btn_note) as Button // Note button
        val buttonDelete = view.findViewById<TextView>(R.id.btn_delete) as Button // Delete button
    }
}