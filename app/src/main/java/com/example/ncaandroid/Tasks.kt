package com.example.ncaandroid

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class Tasks : Fragment() {
    public var tasks: List<TaskData> = listOf() // List of tasks
    public var recyclerView: RecyclerView? = null // RecyclerView
    private var priority: Priority = Priority.DEFAULT // Priority of task
    private var addButtonPortrait: Button? = null // Add button (portrait mode)
    private lateinit var etContent: EditText // EditText for content of task
    private lateinit var priorityButton: Button // Button for priority of task
    private var orderByButton: Button? = null // Button for ordering tasks
    private var orderByButtonLandscape: Button? = null // Button for ordering tasks (landscape mode)
    private var styleChanged = -1 // Necessary for saving the state of the app
    private lateinit var db: AppDatabase


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)

        // Find the RecyclerView
        recyclerView = view.findViewById(R.id.recyclerview_tasks) as RecyclerView?

        // Set up the RecyclerView adapter
        val adapter = TasksAdapter(tasks, requireContext())
        recyclerView?.adapter = adapter
        db = AppDatabase.getInstance(requireContext())!!

        if (tasks.isEmpty()) {
            GlobalScope.launch {
                val call = getRetrofit().create(TaskAPIService::class.java)
                    .getAllTasks().execute()
                val taskReponse = call.body() as TaskResponse
                val tasksFromApi = taskReponse.tasks

                val tasksFromDb = db.taskDao().loadAllTasks()

                // Merge tasks from the API and the local database
                tasks = tasksFromApi + tasksFromDb

                MainScope().launch {
                    recyclerView?.adapter = TasksAdapter(tasks, requireContext())
                }
            }
        }


        // Set up the RecyclerView layout manager
        val layoutManager = LinearLayoutManager(activity)
        recyclerView?.layoutManager = layoutManager

        // Find the views

        addButtonPortrait = view.findViewById(R.id.add_btn) as Button?
        etContent = view.findViewById(R.id.taskContent) as EditText
        priorityButton = view.findViewById(R.id.priorityButton) as Button
        orderByButton = view.findViewById(R.id.orderByButton) as Button?
        orderByButtonLandscape = view.findViewById(R.id.orderByButtonLandscape) as Button?
        val searchView = view.findViewById(R.id.searchView) as SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //TODO: backend search
                filterTasks(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //TODO: backend search
                filterTasks(newText)
                return false
            }
        })

        val addButton = view.findViewById(R.id.add_btn) as Button
        addButton.setOnClickListener {
            onAddButtonClicked()
        }


        return view // Return the view
    }


    @SuppressLint("NotifyDataSetChanged") // Suppress the warning about notifyDataSetChanged()
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

    }
    private fun filterTasks(searchText: String) {
        val filteredTasks = tasks.filter { task ->
            task.content?.contains(searchText, true) ?: false
        }
        recyclerView?.adapter = TasksAdapter(filteredTasks, requireContext())
    }

    private fun onAddButtonClicked() {
        val addTaskFragment = AddTask()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, addTaskFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

/*
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("tasks", tasks) // Save the tasks
        outState.putInt("styleChanged", styleChanged) // Save the styleChanged variable
    }
    @SuppressLint("NotifyDataSetChanged") // Suppress the warning about notifyDataSetChanged()
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) { // If the user has been using the app, then the tasks will continue to be displayed
            tasks = savedInstanceState.getParcelableArrayList<Task>("tasks") as ArrayList<Task> // Get the tasks
            styleChanged = savedInstanceState.getInt("styleChanged") // Get the styleChanged variable
        }
        // Change the background color of the priority button depending on the priority of the task
        when (styleChanged) {
            0 -> { // Blue theme
                etContent.setBackgroundColor(Color.BLUE)
                etContent.setTextColor(Color.WHITE)
                etContent.setHintTextColor(Color.WHITE)
                priority = Priority.DEFAULT
            }
            1 -> { // Green theme
                etContent.setBackgroundColor(Color.GREEN)
                etContent.setTextColor(Color.BLACK)
                etContent.setHintTextColor(Color.BLACK)
                priority = Priority.LOW
            }
            2 -> { // Yellow theme
                etContent.setBackgroundColor(Color.YELLOW)
                etContent.setTextColor(Color.BLACK)
                etContent.setHintTextColor(Color.BLACK)
                priority = Priority.MEDIUM
            }
            3 -> { // Red theme
                etContent.setBackgroundColor(Color.RED)
                etContent.setTextColor(Color.WHITE)
                etContent.setHintTextColor(Color.WHITE)
                priority = Priority.HIGH
            }
            else -> { // Default theme
                etContent.setBackgroundColor(Color.WHITE)
                etContent.setTextColor(Color.BLACK)
                etContent.setHintTextColor(Color.parseColor("#808080"))
                priority = Priority.DEFAULT
            }
        }
        // Find the RecyclerView and set its layout manager and adapter
        recyclerView = view?.findViewById(R.id.recyclerview_tasks)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = context?.let { TasksAdapter(tasks, it) }
        // Set the onClickListener for the priority button
        addButton?.setOnClickListener {
            if (etContent.text.toString() != "") { // If the EditText is not empty
                // Add the task to the list
                tasks.add(Task(etContent.text.toString(), priority))
                recyclerView?.adapter?.notifyItemInserted(tasks.size - 1) // Update the RecyclerView
                etContent.text.clear() // Clear the EditText
                recyclerView?.scrollToPosition(tasks.size - 1) // Scroll to the bottom of the RecyclerView
                etContent.clearFocus() // Clear the focus of the EditText
                // Set the default theme for the EditText
                etContent.setTextColor(Color.BLACK)
                etContent.setBackgroundColor(Color.WHITE)
                etContent.setHintTextColor(Color.parseColor("#808080"))
                styleChanged = -1 // Necessary for saving the state of the app
                priority = Priority.DEFAULT
            } else { // If the EditText is empty
                // Show a AlertDialog to the user to inform him that the EditText is empty
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Error")
                builder.setMessage("You must enter a task")
                builder.setPositiveButton("OK", null)
                builder.setIcon(android.R.drawable.ic_dialog_alert)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
        // The same with the addButtonPortrait button
        addButtonPortrait?.setOnClickListener {
            if (etContent.text.toString() != "") {
                tasks.add(Task(etContent.text.toString(), priority))
                recyclerView?.adapter?.notifyItemInserted(tasks.size - 1)
                etContent.text.clear()
                recyclerView?.scrollToPosition(tasks.size - 1)
                etContent.clearFocus()
                etContent.setTextColor(Color.BLACK)
                etContent.setBackgroundColor(Color.WHITE)
                etContent.setHintTextColor(Color.parseColor("#808080"))
                styleChanged = -1
                priority = Priority.DEFAULT
            } else {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Error")
                builder.setMessage("You must enter a task")
                builder.setPositiveButton("OK", null)
                builder.setIcon(android.R.drawable.ic_dialog_alert)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
        // Set the icon for the priorityButton
        priorityButton.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_baseline_flag_24, 0, 0, 0)
        // Set the onClickListener for the orderByButton
        orderByButton?.setOnClickListener {
            if (orderByButton!!.text == "Order By Priority") { // If the button text is "Order By Priority"
                tasks.sortBy { it.priority } // Sort the list by priority
                recyclerView?.adapter?.notifyDataSetChanged() // Update the RecyclerView
                orderByButton!!.text = getString(R.string.order_by_date) // Change the button text
            } else {
                tasks.sortBy { it.date } // Sort the list by date
                recyclerView?.adapter?.notifyDataSetChanged() // Update the RecyclerView
                orderByButton!!.text = getString(R.string.order_by) // Change the button text
            }
        }
        // The same with the orderByButtonLandscape button
        orderByButtonLandscape?.setOnClickListener {
            if (orderByButtonLandscape!!.text == "Order By Priority") {
                tasks.sortBy { it.priority }
                recyclerView?.adapter?.notifyDataSetChanged()
                orderByButtonLandscape!!.text = getString(R.string.order_by_date)
            } else {
                tasks.sortBy { it.date }
                recyclerView?.adapter?.notifyDataSetChanged()
                orderByButtonLandscape!!.text = getString(R.string.order_by)
            }
        }
        // Set the onClickListener for the priorityButton
        priorityButton.setOnClickListener {
            // Create a AlertDialog to let the user choose the priority of the task
            // The AlertDialog has 4 buttons, one for each priority (default, low, medium, high)
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Choose priority")
            val priorities = arrayOf("0 - Default", "1 - Low", "2 - Medium", "3 - High")
            builder.setItems(priorities) { _, which ->
                when (which) {
                    0 -> { // Default priority
                        priority = Priority.DEFAULT
                        etContent.setTextColor(Color.WHITE)
                        etContent.setBackgroundColor(Color.BLUE)
                        etContent.setHintTextColor(Color.WHITE)
                        styleChanged = 0
                    }
                    1 -> { // Low priority
                        priority = Priority.LOW
                        etContent.setTextColor(Color.BLACK)
                        etContent.setBackgroundColor(Color.GREEN)
                        etContent.setHintTextColor(Color.BLACK)
                        styleChanged = 1
                    }
                    2 -> { // Medium priority
                        priority = Priority.MEDIUM
                        etContent.setTextColor(Color.BLACK)
                        etContent.setBackgroundColor(Color.YELLOW)
                        etContent.setHintTextColor(Color.BLACK)
                        styleChanged = 2
                    }
                    3 -> { // High priority
                        priority = Priority.HIGH
                        etContent.setTextColor(Color.WHITE)
                        etContent.setBackgroundColor(Color.RED)
                        etContent.setHintTextColor(Color.WHITE)
                        styleChanged = 3
                    }
                }
            }
            builder.setIcon(R.drawable.ic_baseline_flag_24_black)
            val dialog = builder.create()
            dialog.show()
        }
    }
     */

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://my-json-server.typicode.com/ManelRosPuig/nca-android-2/")
            .addConverterFactory(GsonConverterFactory.create()) .build()
    }




}