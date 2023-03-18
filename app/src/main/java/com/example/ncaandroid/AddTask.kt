package com.example.ncaandroid

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTask : Fragment() {

    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getInstance(requireContext())!!

        val saveButton = view.findViewById<View>(R.id.saveButton)
        saveButton.setOnClickListener {
            // Get the data from the EditTexts
            val content = view.findViewById<EditText>(R.id.contentValueTextView).text.toString()
            val priority = view.findViewById<android.widget.EditText>(R.id.priorityValueTextView).text.toString().toInt()
            val isDone = view.findViewById<android.widget.EditText>(R.id.isDoneValueTextView).text.toString().toBoolean()
            val date = view.findViewById<android.widget.EditText>(R.id.dateValueTextView).text.toString()
            val telf = view.findViewById<android.widget.EditText>(R.id.telfValueTextView).text.toString()
            val web = view.findViewById<android.widget.EditText>(R.id.webValueTextView).text.toString()

            val task = TaskData(null, content, priority, isDone, date, telf, web)

            GlobalScope.launch {
                //if all the fields are filled, then add the task to the database
                db.taskDao().insert(task)
            }
            // Display a toast message to let the user know the task was added
            Toast.makeText(requireContext(), "Task added", Toast.LENGTH_SHORT).show()

            val backButton = view.findViewById<View>(R.id.backButton)
            backButton.setOnClickListener {
                //go back to the previous fragment
                requireActivity().supportFragmentManager.popBackStack()
            }

        }
    }
}
