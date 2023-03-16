package com.example.ncaandroid

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import androidx.fragment.app.Fragment

class Stopwatch : Fragment() {
    private lateinit var chronometer: Chronometer // Stopwatch
    private var running: Boolean = false // Running state of stopwatch
    private var pauseOffset: Long = 0 // Time when stopwatch was paused
    private lateinit var startPause: Button // Start/Pause button
    private lateinit var reset: Button // Reset button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If the user has been using the stopwatch, the stopwatch will continue
        // to run in the background
        if (savedInstanceState != null) {
            running = savedInstanceState.getBoolean("running") // Get running state
            pauseOffset = savedInstanceState.getLong("pauseOffset") // Get pause offset
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_stopwatch, container, false)

        // Find the chronometer, start/pause button, and reset button
        chronometer = view.findViewById(R.id.chronometer)
        startPause = view.findViewById(R.id.startPause)
        reset = view.findViewById(R.id.reset)

        // If the user has been using the stopwatch, then the stopwatch will continue to run
        if (savedInstanceState != null) {
            chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
            if (running) { // If the stopwatch was running, then the stopwatch will continue to run
                chronometer.start()
                startPause.text = getString(R.string.pause)
            } else { // If the stopwatch was paused, then the stopwatch will continue to be paused
                chronometer.stop()
                startPause.text = getString(R.string.start)
            }
        }

        // When the user clicks the start/pause button, the stopwatch will start or pause
        startPause.setOnClickListener {
            if (running) {
                startPause.text = getString(R.string.start)
                pauseChronometer()
            } else {
                startPause.text = getString(R.string.pause)
                startChronometer()
            }
        }

        // When the user clicks the reset button, the stopwatch will reset
        reset.setOnClickListener {
            resetChronometer()
        }

        return view // Return the view
    }

    // Start the stopwatch
    private fun startChronometer() {
        if (!running) {
            chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
            chronometer.start()
            running = true
            startPause.text = getString(R.string.pause)
        }
    }

    // Pause the stopwatch
    private fun pauseChronometer() {
        if (running) {
            chronometer.stop()
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
            running = false
            startPause.text = getString(R.string.start)
        }
    }

    // Reset the stopwatch
    private fun resetChronometer() {
        chronometer.base = SystemClock.elapsedRealtime()
        pauseOffset = 0
        if (running) {
            startPause.text = getString(R.string.pause)
        } else {
            startPause.text = getString(R.string.start)
        }
    }

    // When the user leaves the fragment, or the app is closed, the stopwatch will be paused
    override fun onStop() {
        super.onStop()
        if (running) {
            chronometer.stop()
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
        }
    }

    // If the user changes the orientation of the screen or move between fragments,
    // save the state of the stopwatch
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("running", running)
        outState.putLong("pauseOffset", pauseOffset)
    }

    // If the user has been using the stopwatch, the state of the stopwatch will be restored,
    // even if the user closes the app. If the user had the stopwatch running, then the stopwatch
    // will continue to run, and if the user had the stopwatch paused, then the stopwatch will
    // continue to be paused.
    override fun onStart() {
        super.onStart()
        if (running) {
            chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
            chronometer.start()
            startPause.text = getString(R.string.pause)
        } else {
            chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
            startPause.text = getString(R.string.start)
        }
    }
}