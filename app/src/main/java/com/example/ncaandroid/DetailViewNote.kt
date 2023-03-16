package com.example.ncaandroid

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.RequiresApi

class DetailViewNote : Fragment() {
    private var color = "BLUE" // Color by default
    var viewState: Int = 0 // 0 = Add Note, 1 = View Note, 2 = Edit Note
    private var titleTextView: TextView? = null // Title TextView
    private var colorTexView: TextView? = null // Color TextView
    private lateinit var buttonLeft: Button // Left button
    private lateinit var buttonRight: Button // Right button
    private lateinit var etTitle: EditText // Title EditText
    private lateinit var etContent: EditText // Content EditText
    private lateinit var radioGroup: RadioGroup // RadioGroup
    private lateinit var note: Note // Current note
    private var titleChanged = false // This is necessary for the TextWatcher
    private var contentChanged = false // This is necessary for the TextWatcher
    private var colorChanged = false // This is necessary for the RadioGroup listener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // As in the MainActivity, we need to instantiate the instance variable of the fragment
        // so that we can access it from other classes
        instance = this

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_view_note, container, false)

        // We get the views
        titleTextView = view.findViewById(R.id.title_detailViewNote)
        colorTexView = view.findViewById(R.id.color_detailViewNote)
        buttonLeft = view.findViewById(R.id.btn_cancel)
        buttonRight = view.findViewById(R.id.btn_add)
        etTitle = view.findViewById(R.id.title)
        etContent = view.findViewById(R.id.content)
        radioGroup = view.findViewById(R.id.radioGroup)

        return view // We return the view
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) { // If the state is not null, we get the values
            color = savedInstanceState.getString("color").toString()
            viewState = savedInstanceState.getInt("viewState")
            note = savedInstanceState.getParcelable("note")!!
            titleChanged = savedInstanceState.getBoolean("titleChanged") // TextWatcher
            contentChanged = savedInstanceState.getBoolean("contentChanged") // TextWatcher
            colorChanged = savedInstanceState.getBoolean("colorChanged") // RadioGroup listener
            updateTextViews() // We update the TextViews (necessary for the rotation)
        } else {
            // We assign a value because otherwise it will be null and we will get a
            // NullPointerException when we rotate the screen.
            note = Note()
        }

        // We set the TextWatcher
        // We use this to know if the user has changed the title or the content of the note he was
        // viewing. If he has changed it, the buttons (and its actions) and title will change.
        etTitle.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // When the screen is rotated, the TextWatcher detects that the title has changed
                // and the buttons change. We don't want that, so we use this variable to know if
                // the user has changed the title or not.
                if (viewState == 1) {
                    if (!titleChanged) {
                        changeTexts(2) // We change the viewState to 2 (Edit Note)
                    } else {
                        titleChanged = false
                    }
                }
            }
            // We don't need these methods but we have to implement them because they are abstract
            // in the TextWatcher interface and we can't create an instance of an abstract class.
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        // We do the same for the content
        etContent.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (viewState == 1) {
                    if (!contentChanged) {
                        changeTexts(2) // We change the viewState to 2 (Edit Note)
                    } else {
                        contentChanged = false
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // We set the onClickListener for the radioGroup to change the color of the note when the
        // user selects a color.
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.color_blue -> { // Blue
                    buttonRight.setBackgroundColor(resources.getColor(R.color.blue))
                    buttonLeft.setBackgroundColor(resources.getColor(R.color.blue))
                    buttonRight.setTextColor(resources.getColor(R.color.white))
                    buttonLeft.setTextColor(resources.getColor(R.color.white))
                    color = "BLUE"
                    if (viewState == 1) { // If we are viewing a note, we change the viewState
                        if (!colorChanged) {
                            changeTexts(2) // We change the viewState to 2 (Edit Note)
                        } else {
                            colorChanged = false
                        }
                    }
                }
                R.id.color_red -> { // Red
                    buttonRight.setBackgroundColor(resources.getColor(R.color.red))
                    buttonLeft.setBackgroundColor(resources.getColor(R.color.red))
                    buttonRight.setTextColor(resources.getColor(R.color.white))
                    buttonLeft.setTextColor(resources.getColor(R.color.white))
                    color = "RED"
                    if (viewState == 1) { // If we are viewing a note, we change the viewState
                        if (!colorChanged) {
                            changeTexts(2) // We change the viewState to 2 (Edit Note)
                        } else {
                            colorChanged = false
                        }
                    }
                }
                R.id.color_green -> { // Green
                    buttonRight.setBackgroundColor(resources.getColor(R.color.green))
                    buttonLeft.setBackgroundColor(resources.getColor(R.color.green))
                    buttonRight.setTextColor(resources.getColor(R.color.black))
                    buttonLeft.setTextColor(resources.getColor(R.color.black))
                    color = "GREEN"
                    if (viewState == 1) { // If we are viewing a note, we change the viewState
                        if (!colorChanged) {
                            changeTexts(2) // We change the viewState to 2 (Edit Note)
                        } else {
                            colorChanged = false
                        }
                    }
                }
                R.id.color_yellow -> { // Yellow
                    buttonRight.setBackgroundColor(resources.getColor(R.color.yellow))
                    buttonLeft.setBackgroundColor(resources.getColor(R.color.yellow))
                    buttonRight.setTextColor(resources.getColor(R.color.black))
                    buttonLeft.setTextColor(resources.getColor(R.color.black))
                    color = "YELLOW"
                    if (viewState == 1) { // If we are viewing a note, we change the viewState
                        if (!colorChanged) {
                            changeTexts(2) // We change the viewState to 2 (Edit Note)
                        } else {
                            colorChanged = false
                        }
                    }
                }
            }
        }

        // Button Right Listener (Add Note, Delete Note or Save Note) depending on the viewState
        buttonRight.setOnClickListener {
            when (viewState) { // Depending on the viewState
                0 -> { // Add Note
                    val title = etTitle.text.toString()
                    val content = etContent.text.toString()
                    note = Note(title, content, color)
                    if (title == "") { // If the title is empty, we show an AlertDialog
                        val builder = android.app.AlertDialog.Builder(context)
                        builder.setTitle("No title")
                        builder.setMessage("You must write a title!")
                        builder.setPositiveButton("OK!") { _, _ -> }
                        builder.setIcon(android.R.drawable.ic_dialog_alert)
                        builder.show()
                    } else if (content == "") { // If the content is empty, we show an AlertDialog
                        val builder = android.app.AlertDialog.Builder(context)
                        builder.setTitle("No content")
                        builder.setMessage("You must write a content!")
                        builder.setPositiveButton("OK!") { _, _ -> }
                        builder.setIcon(android.R.drawable.ic_dialog_alert)
                        builder.show()
                    } else { // If the title and the content are not empty, we add the note
                        Notes.instance?.addNote(note)
                        MainActivity.instance?.closeDetailViewNote() // We close the fragment
                    }
                }
                1 -> { // Delete Note
                    // We show an AlertDialog to ask the user if he is sure he wants to delete the
                    // note he is viewing. If he is sure, we delete the note.
                    val builder = android.app.AlertDialog.Builder(context)
                    builder.setTitle("Delete note")
                    builder.setMessage("Are you sure you want to delete this note?")
                    builder.setPositiveButton("Yes") { _, _ -> // If the user is sure
                        Notes.instance?.deleteNote(note)
                        MainActivity.instance?.closeDetailViewNote()
                    }
                    builder.setNegativeButton("No") { _, _ -> } // If the user is not sure
                    builder.setIcon(android.R.drawable.ic_dialog_alert)
                    builder.show()
                }
                2 -> { // Save Note
                    val title = etTitle.text.toString()
                    val content = etContent.text.toString()
                    if (title == "") { // If the title is empty, we show an AlertDialog
                        val builder = android.app.AlertDialog.Builder(context)
                        builder.setTitle("No title")
                        builder.setMessage("You must write a title!")
                        builder.setPositiveButton("OK!") { _, _ -> }
                        builder.setIcon(android.R.drawable.ic_dialog_alert)
                        builder.show()
                    } else if (content == "") { // If the content is empty, we show an AlertDialog
                        val builder = android.app.AlertDialog.Builder(context)
                        builder.setTitle("No content")
                        builder.setMessage("You must write a content!")
                        builder.setPositiveButton("OK!") { _, _ -> }
                        builder.setIcon(android.R.drawable.ic_dialog_alert)
                        builder.show()
                    } else { // If the title and the content are not empty, we save the note
                        val noteID = note.id // We save the id of the current note
                        // We create a new note with the previous UUID, the new title, content and
                        // color and we delete the old note and add the new one.
                        note = Note(noteID, title, content, color)
                        Notes.instance?.updateNote(note) // We update the note
                        MainActivity.instance?.closeDetailViewNote() // We close the fragment
                    }
                }
            }
        }

        // Button Left Listener (Cancel Note, Back to Notes or Discard Changes) depending on the
        // viewState of the fragment (0, 1 or 2)
        buttonLeft.setOnClickListener {
            when (viewState) { // Depending on the viewState
                0 -> { // Cancel Note
                    // If the user is adding a note and he clicks on the button left, we show an
                    // AlertDialog to ask him if he is sure he wants to cancel the note he is
                    // adding. If he is sure, we close the fragment. If he is not sure, we do
                    // nothing.
                    if (etTitle.text.toString() != "" || etContent.text.toString() != "") {
                        val builder = android.app.AlertDialog.Builder(context)
                        builder.setTitle("Cancel")
                        builder.setMessage("Are you sure you want to cancel?")
                        builder.setPositiveButton("Yes") { _, _ ->
                            MainActivity.instance?.closeDetailViewNote() // We close the fragment
                        }
                        builder.setNegativeButton("No") { _, _ -> } // We do nothing
                        builder.setIcon(android.R.drawable.ic_dialog_alert)
                        builder.show()
                    } else { // If the title and the content are empty, we close the fragment
                        MainActivity.instance?.closeDetailViewNote() // We close the fragment
                    }
                }
                1 -> { // Back to Notes
                    MainActivity.instance?.closeDetailViewNote() // We close the fragment
                }
                2 -> { // Discard Changes
                    // If the user is editing a note and he clicks on the button left, we show an
                    // AlertDialog to ask him if he is sure he wants to discard the changes he
                    // made. If he is sure, we close the fragment. If he is not sure, we do
                    // nothing.
                    if (etTitle.text.toString() != note.title ||
                        etContent.text.toString() != note.content ||
                        color != note.color) {
                        val builder = android.app.AlertDialog.Builder(context)
                        builder.setTitle("Discard Changes")
                        builder.setMessage("Are you sure you want to discard the changes?")
                        builder.setPositiveButton("Yes") { _, _ ->
                            MainActivity.instance?.closeDetailViewNote() // We close the fragment
                        }
                        builder.setNegativeButton("No") { _, _ -> } // We do nothing
                        builder.setIcon(android.R.drawable.ic_dialog_alert)
                        builder.show()
                    } else {
                        MainActivity.instance?.closeDetailViewNote() // We close the fragment
                    }
                }
            }
        }
    }

    // This method is called when the fragment is created. It sets the viewState of the fragment
    // depending on the viewState of the MainActivity. This is necessary because when the fragment
    // is created, the viewState of the MainActivity is not yet set. So we set the viewState of the
    // fragment to the viewState of the MainActivity.
    private fun updateTextViews() {
        when (viewState) {
            0 -> { // Add Note
                titleTextView?.text = getString(R.string.add_note)
                colorTexView?.text = getString(R.string.choose_color)
                buttonLeft.text = getString(R.string.cancel)
                buttonRight.text = getString(R.string.add_note)
            }
            1 -> { // View Note
                titleTextView?.text = getString(R.string.detailViewNote)
                colorTexView?.text = getString(R.string.color_DetailViewNote)
                buttonLeft.text = getString(R.string.go_back)
                buttonRight.text = getString(R.string.delete)
            }
            2 -> { // Edit Note
                titleTextView?.text = getString(R.string.edit)
                colorTexView?.text = getString(R.string.choose_color)
                buttonLeft.text = getString(R.string.discard_changes)
                buttonRight.text = getString(R.string.save_changes)
            }
        }
    }

    // This method is called to change the viewState of the fragment.
    fun changeTexts(viewType: Int = 0) {
        when (viewType) {
            0 -> { // Add Note
                buttonLeft.text = getString(R.string.cancel)
                buttonRight.text = getString(R.string.add)
                if (titleTextView != null) { // If the orientation is portrait
                    titleTextView!!.text = getString(R.string.add_note)
                }
                if (colorTexView != null) { // If the orientation is portrait
                    colorTexView!!.text = getString(R.string.choose_color)
                }
                viewState = 0
            }
            1 -> { // View Note
                buttonLeft.text = getString(R.string.go_back)
                buttonRight.text = getString(R.string.delete)
                if (titleTextView != null) { // If the orientation is portrait
                    titleTextView!!.text = getString(R.string.detailViewNote)
                }
                if (colorTexView != null) { // If the orientation is portrait
                    colorTexView!!.text = getString(R.string.color_DetailViewNote)
                }
                viewState = 1
            }
            2 -> { // Edit Note
                buttonLeft.text = getString(R.string.discard_changes)
                buttonRight.text = getString(R.string.save_changes)
                if (titleTextView != null) { // If the orientation is portrait
                    titleTextView!!.text = getString(R.string.edit)
                }
                if (colorTexView != null) { // If the orientation is portrait
                    colorTexView!!.text = getString(R.string.choose_color)
                }
                viewState = 2
            }
        }
    }

    // Method to reset the fragment (when the user clicks on Add Note)
    fun reset() {
        etTitle.setText("")
        etContent.setText("")
        color = "BLUE"
        viewState = 0
        updateTextViews()
    }

    // This method is called from the ListAdapter to set the note that is going to be displayed
    // in the fragment.
    fun updateNote(note: Note) {
        this.note = note // We set the note
        etTitle.setText(note.title)
        etContent.setText(note.content)
        when (note.color) { // We set the color of the note
            "BLUE" -> { // Blue
                buttonRight.setBackgroundColor(resources.getColor(R.color.blue))
                buttonLeft.setBackgroundColor(resources.getColor(R.color.blue))
                radioGroup.check(R.id.color_blue)
            }
            "RED" -> { // Red
                buttonRight.setBackgroundColor(resources.getColor(R.color.red))
                buttonLeft.setBackgroundColor(resources.getColor(R.color.red))
                radioGroup.check(R.id.color_red)
            }
            "GREEN" -> { // Green
                buttonRight.setBackgroundColor(resources.getColor(R.color.green))
                buttonLeft.setBackgroundColor(resources.getColor(R.color.green))
                buttonRight.setTextColor(resources.getColor(R.color.black))
                buttonLeft.setTextColor(resources.getColor(R.color.black))
                radioGroup.check(R.id.color_green)
            }
            "YELLOW" -> { // Yellow
                buttonRight.setBackgroundColor(resources.getColor(R.color.yellow))
                buttonLeft.setBackgroundColor(resources.getColor(R.color.yellow))
                buttonRight.setTextColor(resources.getColor(R.color.black))
                buttonLeft.setTextColor(resources.getColor(R.color.black))
                radioGroup.check(R.id.color_yellow)
            }
        }
    }

    // We save the state of the fragment when the activity is destroyed. We save the viewState of
    // the fragment and the note that is being displayed.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("color", color)
        outState.putInt("viewState", viewState)
        outState.putParcelable("note", note)
        outState.putBoolean("titleChanged", true)
        outState.putBoolean("contentChanged", true)
        outState.putBoolean("colorChanged", true)
    }

    // We need this static variable to access the fragment from other classes.
    companion object {
        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        var instance: DetailViewNote? = null
    }
}