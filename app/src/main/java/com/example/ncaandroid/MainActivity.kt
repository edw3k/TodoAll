package com.example.ncaandroid

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.ncaandroid.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding // View binding
    private lateinit var notes: Notes // Notes fragment
    private lateinit var tasks: Tasks // Tasks fragment
    private lateinit var stopwatch: Stopwatch // Stopwatch fragment
    private lateinit var detailViewNote: DetailViewNote // Add note fragment
    private lateinit var activeFragment: Fragment // Active fragment
    private val fragmentManager: FragmentManager = supportFragmentManager // Fragment manager
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the static instance of this activity in order to access it from other classes
        // (e.g. from the fragments) without having to pass the activity as a parameter
        instance = this

        GlobalScope.launch {
            db = AppDatabase.getInstance(applicationContext)!!

            db.taskDao().insert(TaskData(
                null,
                "GOL",
                2,
                true,
                "2021-05-01",
                "644602630",
                "http://timeride.com"
            ))

            //val tasks = db.TaskDAO().loadAllTasks()

        }

        // Remove the title bar
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        // View binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create && show/hide fragments
        if (savedInstanceState == null) { // If the activity is created for the first time
            notes = Notes()
            tasks = Tasks()
            stopwatch = Stopwatch()
            detailViewNote = DetailViewNote()
            activeFragment = tasks // The default fragment is tasks
            // Add fragments and hide them except the active one
            fragmentManager.beginTransaction().apply {
                add(R.id.fragment_container, notes, "notes")
                add(R.id.fragment_container, tasks, "tasks")
                add(R.id.fragment_container, stopwatch, "stopwatch")
                add(R.id.fragment_container, detailViewNote, "detailViewNote")
                hide(notes) // Hide the notes fragment
                hide(stopwatch) // Hide the stopwatch fragment
                hide(detailViewNote) // Hide the add note fragment
                commit()
            }
            // Check the button of the default fragment (tasks)
            binding.bottomNavigationView?.menu?.findItem(R.id.tasks)?.isChecked = true
            binding.navigationRailView?.menu?.findItem(R.id.tasks)?.isChecked = true
        } else { // If the activity is recreated
            // Get the current fragment
            activeFragment = supportFragmentManager.getFragment(savedInstanceState,
                "activeFragment") as Fragment
            // And get all the other ones, check the button of the active fragment
            when (activeFragment) {
                is Notes -> { // If the active fragment is notes
                    notes = activeFragment as Notes
                    tasks = supportFragmentManager.getFragment(savedInstanceState, "tasks")
                            as Tasks
                    stopwatch = supportFragmentManager.getFragment(savedInstanceState,
                        "stopwatch") as Stopwatch
                    detailViewNote = supportFragmentManager.getFragment(savedInstanceState,
                        "detailViewNote") as DetailViewNote
                    binding.bottomNavigationView?.menu?.findItem(R.id.notes)?.isChecked = true
                    binding.navigationRailView?.menu?.findItem(R.id.notes)?.isChecked = true
                }
                is Tasks -> { // If the active fragment is tasks
                    notes = supportFragmentManager.getFragment(savedInstanceState, "notes")
                            as Notes
                    tasks = activeFragment as Tasks
                    stopwatch = supportFragmentManager.getFragment(savedInstanceState,
                        "stopwatch") as Stopwatch
                    detailViewNote = supportFragmentManager.getFragment(savedInstanceState,
                        "detailViewNote") as DetailViewNote
                    binding.bottomNavigationView?.menu?.findItem(R.id.tasks)?.isChecked = true
                    binding.navigationRailView?.menu?.findItem(R.id.tasks)?.isChecked = true
                }
                is Stopwatch -> { // If the active fragment is stopwatch
                    notes = supportFragmentManager.getFragment(savedInstanceState, "notes")
                            as Notes
                    tasks = supportFragmentManager.getFragment(savedInstanceState, "tasks")
                            as Tasks
                    stopwatch = activeFragment as Stopwatch
                    detailViewNote = supportFragmentManager.getFragment(savedInstanceState,
                        "detailViewNote") as DetailViewNote
                    binding.bottomNavigationView?.menu?.findItem(R.id.stopwatch)?.isChecked = true
                    binding.navigationRailView?.menu?.findItem(R.id.stopwatch)?.isChecked = true
                }
                is DetailViewNote -> { // If the active fragment is add note
                    notes = supportFragmentManager.getFragment(savedInstanceState, "notes")
                            as Notes
                    tasks = supportFragmentManager.getFragment(savedInstanceState, "tasks")
                            as Tasks
                    stopwatch = supportFragmentManager.getFragment(savedInstanceState,
                        "stopwatch") as Stopwatch
                    detailViewNote = activeFragment as DetailViewNote
                    binding.bottomNavigationView?.menu?.findItem(R.id.notes)?.isChecked = true
                    binding.navigationRailView?.menu?.findItem(R.id.notes)?.isChecked = true
                }
            }
        }

        // Bottom navigation view
        binding.bottomNavigationView?.setOnItemSelectedListener { item ->
            when (item.itemId) { // When some button is clicked
                R.id.notes -> {
                    fragmentManager.beginTransaction().apply {
                        hide(activeFragment) // Hide the previous fragment
                        show(notes) // Show the notes fragment
                        commit()
                    }
                    activeFragment = notes // Set the active fragment to notes
                    true
                }
                R.id.tasks -> {
                    fragmentManager.beginTransaction().apply {
                        hide(activeFragment) // Hide the previous fragment
                        show(tasks) // Show the tasks fragment
                        commit()
                    }
                    activeFragment = tasks // Set the active fragment to tasks
                    true
                }
                R.id.stopwatch -> {
                    fragmentManager.beginTransaction().apply {
                        hide(activeFragment) // Hide the previous fragment
                        show(stopwatch) // Show the stopwatch fragment
                        commit()
                    }
                    activeFragment = stopwatch // Set the active fragment to stopwatch
                    true
                }
                else -> false
            }

        }

        // Navigation rail view
        binding.navigationRailView?.setOnItemSelectedListener { item ->
            when (item.itemId) { // When some button is clicked
                R.id.notes -> {
                    fragmentManager.beginTransaction().apply {
                        hide(activeFragment) // Hide the previous fragment
                        show(notes) // Show the notes fragment
                        commit()
                    }
                    activeFragment = notes // Set the active fragment to notes
                    true
                }
                R.id.tasks -> {
                    fragmentManager.beginTransaction().apply {
                        hide(activeFragment) // Hide the previous fragment
                        show(tasks) // Show the tasks fragment
                        commit()
                    }
                    activeFragment = tasks // Set the active fragment to tasks
                    true
                }
                R.id.stopwatch -> {
                    fragmentManager.beginTransaction().apply {
                        hide(activeFragment) // Hide the previous fragment
                        show(stopwatch) // Show the stopwatch fragment
                        commit()
                    }
                    activeFragment = stopwatch // Set the active fragment to stopwatch
                    true
                }
                else -> false
            }
        }
    }

    // In order to save the state of the fragments when the activity is recreated (e.g. when the
    // device is rotated) we need to save the state of the fragments in the bundle and then get
    // them back when the activity is recreated (in the onCreate method) and add them to the
    // fragment manager again. If we don't do this, the fragments will be recreated and the data
    // will be lost.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        when (activeFragment) { // We need to do this because otherwise we get an exception
            is Notes -> { // If the active fragment is notes
                supportFragmentManager.putFragment(outState, "tasks", tasks)
                supportFragmentManager.putFragment(outState, "stopwatch", stopwatch)
                supportFragmentManager.putFragment(outState, "detailViewNote", detailViewNote)
            }
            is Tasks -> { // If the active fragment is tasks
                supportFragmentManager.putFragment(outState, "notes", notes)
                supportFragmentManager.putFragment(outState, "stopwatch", stopwatch)
                supportFragmentManager.putFragment(outState, "detailViewNote", detailViewNote)
            }
            is Stopwatch -> { // If the active fragment is stopwatch
                supportFragmentManager.putFragment(outState, "notes", notes)
                supportFragmentManager.putFragment(outState, "tasks", tasks)
                supportFragmentManager.putFragment(outState, "detailViewNote", detailViewNote)
            }
            is DetailViewNote -> { // If the active fragment is add note
                supportFragmentManager.putFragment(outState, "notes", notes)
                supportFragmentManager.putFragment(outState, "tasks", tasks)
                supportFragmentManager.putFragment(outState, "stopwatch", stopwatch)
            }
        }
        // Save the active fragment always
        supportFragmentManager.putFragment(outState, "activeFragment", activeFragment)
    }

    // A function that is called when the user clicks on the add button in the notes fragment
    fun openDetailViewNote() {
        fragmentManager.beginTransaction().apply {
            hide(activeFragment)
            show(detailViewNote)
            commit()
        }
        activeFragment = detailViewNote
    }

    // A function that is called when the user clicks on the back button in the detail view note
    fun closeDetailViewNote() {
        fragmentManager.beginTransaction().apply {
            hide(activeFragment)
            show(notes)
            commit()
        }
        activeFragment = notes
    }

    // We need to override the onBackPressed method in order to handle the back button presses
    // correctly. If the user is in the detail view note fragment, we want to go back to the notes
    // fragment when the user presses the back button. If the user is in the notes fragment, we
    // want to go back to the tasks fragment when the user presses the back button. And if the user
    // is in the tasks fragment we want to exit the app when the user presses the back button.
    @Deprecated("Deprecated in Java") // This is because the method is deprecated in Java
    override fun onBackPressed() {
        when (activeFragment) {
            is DetailViewNote -> closeDetailViewNote() // Close the detail view note fragment
            is Stopwatch, is Notes -> { // Go back to the tasks fragment
                fragmentManager.beginTransaction().apply {
                    hide(activeFragment)
                    show(tasks)
                    commit()
                }
                binding.bottomNavigationView?.menu?.findItem(R.id.tasks)?.isChecked = true
                binding.navigationRailView?.menu?.findItem(R.id.tasks)?.isChecked = true
                activeFragment = tasks
            }
            is Tasks -> { // Show the exit dialog
                val builder = android.app.AlertDialog.Builder(this)
                builder.setTitle("Exit")
                builder.setMessage("Are you sure you want to exit this app?")
                builder.setPositiveButton("Yes") { _, _ ->
                    super.onBackPressed()
                }
                builder.setNegativeButton("No") { _, _ -> }
                builder.setIcon(android.R.drawable.ic_dialog_alert)
                builder.show()
            }
            else -> super.onBackPressed() // Call the super method
        }
    }

    // We use this "static" instance of the activity in order to be able to call the
    // openDetailViewNote function from the notes fragment (because we can't call a non-static
    // function from a static context). This is a workaround for the problem that we can't use
    // the "this" keyword in a static context. The instance is set in the onCreate method.
    companion object {
        @JvmStatic
        var instance: MainActivity? = null
    }
}