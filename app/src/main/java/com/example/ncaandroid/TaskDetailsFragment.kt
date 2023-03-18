package com.example.ncaandroid

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView


class TaskDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_task_details, container, false)

        // Get the task data from the arguments bundle
        val taskData = arguments?.getSerializable("taskData") as TaskData

        // Set the text views to display the task data
        view.findViewById<EditText>(R.id.contentValueTextView).text = Editable.Factory.getInstance().newEditable(taskData.content)
        view.findViewById<EditText>(R.id.priorityValueTextView).text = Editable.Factory.getInstance().newEditable(taskData.priority.toString())
        view.findViewById<EditText>(R.id.isDoneValueTextView).text = Editable.Factory.getInstance().newEditable(taskData.isDone.toString())
        view.findViewById<EditText>(R.id.dateValueTextView).text = Editable.Factory.getInstance().newEditable(taskData.date)
        view.findViewById<EditText>(R.id.telfValueTextView).text = Editable.Factory.getInstance().newEditable(taskData.telf)
        view.findViewById<EditText>(R.id.webValueTextView).text = Editable.Factory.getInstance().newEditable(taskData.web)
        val backButton = view.findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }
}

