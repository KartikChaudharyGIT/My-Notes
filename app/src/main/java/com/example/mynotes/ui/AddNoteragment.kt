package com.example.mynotes.ui


import android.app.AlertDialog
import android.os.Bundle
import android.renderscript.Script
import android.view.*
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.mynotes.R
import com.example.mynotes.db.Note
import com.example.mynotes.db.NoteDatabase
import kotlinx.android.synthetic.main.fragment_add_notefragment.*
import kotlinx.coroutines.launch

class AddNoteragment : Basefragment() {

    private var note: Note? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_notefragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            note = Add
            noteeditText2.setText(note?.note)
            titleeditText.setText(note?.title)
        }


        floatingActionButton2.setOnClickListener { view ->

            val notetitle = titleeditText.text.toString().trim()
            val notebody = noteeditText2.text.toString().trim()

            if (notetitle.isEmpty()) {
                titleeditText.error = "Title required"
                titleeditText.requestFocus()
                return@setOnClickListener
            }

            launch {

                context?.let {
                    val mNote = Note(notetitle, notebody)
                    if (note == null) {


                        NoteDatabase(it).getNoteDao().addNote(mNote)
                        it.toast("Note Saved")
                    } else {
                        mNote.id = note!!.id

                        NoteDatabase(it).getNoteDao().updateNote(mNote)
                        it.toast("Note updates")
                    }
                    val actions = AddNoteragmentDirections.actionAddNoteragmentToHomeFragment()
                    Navigation.findNavController(view).navigate(actions)
                }
            }
        }
    }

    private fun deleteNote() {
        AlertDialog.Builder(context).apply {
            setTitle("Are you sure")
            setMessage("You cannot undo this operation")
            setPositiveButton("Yes") { _, _ ->
                launch {
                    NoteDatabase(context).getNoteDao().deleteNote(note!!)
                    val actions = AddNoteragmentDirections.actionAddNoteragmentToHomeFragment()
                    Navigation.findNavController(view!!).navigate(actions)
                }
            }
            setNegativeButton("No") { _, _ ->

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> if (note != null) deleteNote() else context?.toast("Cannot Delete")
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }
}


