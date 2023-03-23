package com.example.ncaandroid

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTask : Fragment() {

    private lateinit var db: AppDatabase

    // private lateinit var tasks: MutableList<TaskData>
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getInstance(requireContext())

        // Get reference to the RecyclerView and the tasks list
        recyclerView = requireActivity().findViewById(R.id.recyclerview_tasks)
        // tasks = (recyclerView.adapter as TasksAdapter).tasks as MutableList<TaskData>

        //add data validation
        val saveButton = view.findViewById<View>(R.id.saveButton)
        saveButton.setOnClickListener {
            // Get the data from the EditTexts
            val content = view.findViewById<EditText>(R.id.contentValueTextView).text.toString()

            var priority = 0
            val priorityEditText = view.findViewById<EditText>(R.id.priorityValueTextView)
            if (priorityEditText.text.isNotBlank()) {
                priority = priorityEditText.text.toString().toIntOrNull() ?: 0
            }

            val isDoneEditText = view.findViewById<Spinner>(R.id.isDoneValueTextView)
            val isDone = isDoneEditText.selectedItem.toString().toBoolean()



            val dateEditText = view.findViewById<EditText>(R.id.dateValueTextView)
            val date = dateEditText.text.toString().takeIf { it.isNotBlank() }

            val telfEditText = view.findViewById<EditText>(R.id.telfValueTextView)
            val telf = telfEditText.text.toString()
                .takeIf { it.isNotBlank() && it.matches(Regex("\\d{9}")) }

            val webEditText = view.findViewById<EditText>(R.id.webValueTextView)
            val web = webEditText.text.toString()
                .takeIf { it.isNotBlank() && it.matches(Regex("https?://.*")) }

            // Validate the input data
            if (content.isBlank()) {
                view.findViewById<EditText>(R.id.contentValueTextView).error = "Content is required"
                return@setOnClickListener
            }

            if (priority !in 1..3) {
                view.findViewById<EditText>(R.id.priorityValueTextView).error =
                    "Priority should be between 1 and 3"
                return@setOnClickListener
            }

            if (isDoneEditText.selectedItem.toString().isBlank()) {
                view.findViewById<EditText>(R.id.isDoneValueTextView).error =
                    "This field is required"
                return@setOnClickListener
            }

            if (dateEditText.text.isBlank()) {
                view.findViewById<EditText>(R.id.dateValueTextView).error =
                    "This field is required"
                return@setOnClickListener
            }

            if (telfEditText.text.isBlank()) {
                view.findViewById<EditText>(R.id.telfValueTextView).error =
                    "This field is required"
                return@setOnClickListener
            }

            if (!telfEditText.text.matches(Regex("\\d{9}"))) {
                view.findViewById<EditText>(R.id.telfValueTextView).error =
                    "Phone number should have 9 digits"
                return@setOnClickListener
            }

            if (webEditText.text.isBlank()) {
                view.findViewById<EditText>(R.id.webValueTextView).error =
                    "This field is required"
                return@setOnClickListener
            }

            if (!webEditText.text.matches(Regex("https?://.*"))) {
                view.findViewById<EditText>(R.id.webValueTextView).error =
                    "Website should start with www:// or https://"
                return@setOnClickListener
            }


            // Create a new task and add it to the list
            val newTask = TaskData(null, content, priority, isDone, date, telf, web)
            StaticTasks.tasks.add(newTask)

            // Save the new task to the database
            GlobalScope.launch {
                db.taskDao().insert(newTask)
            }

            // Notify the adapter that the data has changed
            recyclerView.adapter?.notifyDataSetChanged()

            // Return to the Tasks fragment
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}
