package com.example.ncaandroid

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// This is the adapter class for the RecyclerView that will display the tasks
class TasksAdapter(val context: Context)
    : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    // Override the onCreateViewHolder method to inflate the layout for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task_item, parent,
            false)
        return ViewHolder(view)
    }

    // Override the getItemCount method to return the number of items in the list
    override fun getItemCount(): Int {
        return StaticTasks.tasks.size
    }

    // Override the onBindViewHolder method to set the data for each item
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = StaticTasks.tasks[position] // Get the task at the current position
        holder.tv.text = task.content // Set the content of the task
        holder.checkBox.isChecked =
            task.isDone == true // Set the checkbox to checked if the task is done

        if (task.isDone == true) { // If the task is done, then the text will be grayed out
            holder.tv.paintFlags = holder.tv.paintFlags or
                    android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
        } else { // If the task is not done, then the text will not be grayed out
            holder.tv.paintFlags = holder.tv.paintFlags and
                    android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        // Set the background color of the task based on the priority
        when (task.priority) {
            1 -> {
                holder.tv.setTextColor(Color.BLACK)
                holder.tv.setBackgroundColor(Color.GREEN)
            }
            2 -> {
                holder.tv.setTextColor(Color.BLACK)
                holder.tv.setBackgroundColor(Color.YELLOW)
            }
            3 -> {
                holder.tv.setTextColor(Color.WHITE)
                holder.tv.setBackgroundColor(Color.RED)
            }
            else -> {
                holder.tv.setTextColor(Color.WHITE)
                holder.tv.setBackgroundColor(Color.BLUE)
            }
        }

        // When the user clicks the trashButton, the task will be deleted
        holder.trashButton.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val task = StaticTasks.tasks[position]
                val taskDAO = AppDatabase.getInstance(context)?.taskDao()
                taskDAO?.let {
                    GlobalScope.launch {
                        it.deleteById(task.id!!.toLong())
                        MainScope().launch {
                            StaticTasks.tasks.removeAt(position)
                            notifyItemRemoved(position)
                        }
                    }
                }
            }
        }

        // When the user clicks the checkbox
        holder.checkBox.setOnClickListener {
            task.isDone = holder.checkBox.isChecked // Set the isDone property of the task to the
            // value of the checkbox
            if (task.isDone == true) { // If the task is done, then the text will be grayed out
                holder.tv.paintFlags = holder.tv.paintFlags or
                        android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            } else { // If the task is not done, then the text will not be grayed out
                holder.tv.paintFlags = holder.tv.paintFlags and
                        android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        // Launch the TaskDetailsFragment when the user clicks the TextView
        holder.tv.setOnClickListener {
            val fragment = TaskDetailsFragment()
            val args = Bundle()
            args.putSerializable("taskData", task)
            fragment.arguments = args

            val transaction = (context as AppCompatActivity)
                .supportFragmentManager
                .beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://my-json-server.typicode.com/ManelRosPuig/nca-android-2/")
            .addConverterFactory(GsonConverterFactory.create()) .build()
    }

    // This is the ViewHolder class for the RecyclerView
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var checkBox: CheckBox = itemView.findViewById(R.id.checkBox) // The checkbox for the task
        var tv: TextView = itemView.findViewById(R.id.tv) // The textview for the task
        val trashButton: Button = itemView.findViewById(R.id.button_trash) // The trash button fo
        // the task
    }
}