package com.example.ncaandroid

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTask : Fragment() {

    private lateinit var db: AppDatabase
    private lateinit var tasks: MutableList<TaskData>
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getInstance(requireContext())

        // Get reference to the RecyclerView and the tasks list
        recyclerView = requireActivity().findViewById(R.id.recyclerview_tasks)
        tasks = (recyclerView.adapter as TasksAdapter).tasks as MutableList<TaskData>

        val saveButton = view.findViewById<View>(R.id.saveButton)
        saveButton.setOnClickListener {
            // Get the data from the EditTexts
            val content = view.findViewById<EditText>(R.id.contentValueTextView).text.toString()
            val priority = view.findViewById<EditText>(R.id.priorityValueTextView).text.toString().toInt()
            val isDone = view.findViewById<EditText>(R.id.isDoneValueTextView).text.toString().toBoolean()
            val date = view.findViewById<EditText>(R.id.dateValueTextView).text.toString()
            val telf = view.findViewById<EditText>(R.id.telfValueTextView).text.toString()
            val web = view.findViewById<EditText>(R.id.webValueTextView).text.toString()

            // Create a new TaskData object and add it to the list of tasks
            val newTask = TaskData(null,content, priority, isDone, date, telf, web)
            tasks.add(newTask)
            GlobalScope.launch {
                // Add the new task to the database
                db.taskDao().insert(newTask)
            }

            // Notify the adapter that the data has changed
            recyclerView.adapter?.notifyDataSetChanged()

            // Return to the Tasks fragment
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}
