package com.example.ncaandroid

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private lateinit var db: AppDatabase

class TaskDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_task_details, container, false)

        // Get the task data from the arguments bundle
        val taskData = arguments?.getSerializable("taskData") as TaskData
        db = AppDatabase.getInstance(requireContext())!!

        // Set the EditTexts with the task data
        view.findViewById<EditText>(R.id.contentValueTextView).setText(taskData.content)
        view.findViewById<EditText>(R.id.priorityValueTextView)
            .setText(taskData.priority.toString())
        view.findViewById<EditText>(R.id.isDoneValueTextView).setText(taskData.isDone.toString())
        view.findViewById<EditText>(R.id.dateValueTextView).setText(taskData.date)
        view.findViewById<EditText>(R.id.telfValueTextView).setText(taskData.telf)
        view.findViewById<EditText>(R.id.webValueTextView).setText(taskData.web)

        val backButton = view.findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        val saveButton = view.findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            //Update the task data
            val task = TaskData(
                taskData.id,
                view.findViewById<EditText>(R.id.contentValueTextView).text.toString(),
                view.findViewById<EditText>(R.id.priorityValueTextView).text.toString().toInt(),
                view.findViewById<EditText>(R.id.isDoneValueTextView).text.toString().toBoolean(),
                view.findViewById<EditText>(R.id.dateValueTextView).text.toString(),
                view.findViewById<EditText>(R.id.telfValueTextView).text.toString(),
                view.findViewById<EditText>(R.id.webValueTextView).text.toString()
            )
           GlobalScope.launch {
               //Update the task in the database
               db.taskDao().update(task)
           }
            // Go back to the previous fragment
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }
}


