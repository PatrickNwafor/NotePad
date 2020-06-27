package com.example.notepad.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.notepad.R
import com.example.notepad.data.db.Note
import com.example.notepad.databinding.FragmentAddNoteBinding
import com.example.notepad.util.toast
import kotlinx.android.synthetic.main.category_option_layout.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class AddNoteFragment : Fragment(), KodeinAware {

    // dependency injection initialization
    override val kodein by kodein()
    // View model factory setting which enables the view model class to be instantiated with a NoteRepository class as parameter
    private val factory : NoteViewModelFactory by instance<NoteViewModelFactory>()

    private lateinit var viewModel: NoteViewModel
    private lateinit var binding: FragmentAddNoteBinding
    private var category: String = "Uncategorized"
    private var note: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)
        return  binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Checking if an argument(note) was passed from Home activity
        arguments?.let {
            note = AddNoteFragmentArgs.fromBundle(it).note
            binding.noteTitleEditText.setText(note?.title)
            binding.noteEditText.setText(note?.body)
            note?.let {
                category = note!!.category
            }
            setCheckedCategory()
        }

        binding.saveNoteBtn.setOnClickListener {
            if(note == null)
                saveNote()
            else
                updateNote(note!!)
        }

        binding.include.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            setChangedStatus(checkedId)
        }
    }

    // Setting category values according to the check image radio button
    private fun setCheckedCategory() {
        when(note?.category){
            "Personal" -> binding.include.radioBtnPerson.isChecked = true
            "School" -> binding.include.radioBtnEducation.isChecked = true
            "Work" -> binding.include.radioBtnWork.isChecked = true
            else -> binding.include.radioBtnUncategorized.isChecked = true
        }
    }

    // Setting the category status when a note is sent for an update
    private fun setChangedStatus(checkedId: Int) {
        category = when(checkedId){
            R.id.radio_btn_person -> {
                context?.toast("Personal")
                "Personal"
            }
            R.id.radio_btn_education -> {
                context?.toast("School")
                "School"
            }
            R.id.radio_btn_work -> {
                context?.toast("Work")
                "Work"
            }
            else -> {
                context?.toast("Uncategorized")
                "Uncategorized"
            }
        }
    }

    // Save a new note to the local storage
    private fun saveNote() {

        val title: String = binding.noteTitleEditText.text.trim().toString()
        val body: String = binding.noteEditText.text.trim().toString()

        if(title.isEmpty() || body.isEmpty()){
            context?.toast("Title and Note Fields cannot be empty")
            return
        }

        val note = Note(
            title = title,
            body = body,
            category = category,
            createdAt = getDate()
        )

        viewModel.saveNote(note)
        context?.toast("Note Saved")

        moveToHome()
    }

    // Custom date format to print date in 23/06/2020 format
    private fun getDate(): String {
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formatted = current.format(formatter)

        return formatted!!
    }

    // Update a note
    private fun updateNote(note: Note) {
        val title: String = binding.noteTitleEditText.text.trim().toString()
        val body: String = binding.noteEditText.text.trim().toString()

        if (title.isEmpty() || body.isEmpty()) {
            context?.toast("Title and Note Fields cannot be empty")
            return
        }

        note.createdAt = getDate()
        note.category = category
        note.title = title
        note.body = body

        viewModel.updateNote(note)
        context?.toast("Note Updated")

        moveToHome()
    }

    // Inflating menu to the app bar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.delete_note, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete -> if(note != null) deleteNote(note!!) else context?.toast("Cannot Delete")
        }
        return super.onOptionsItemSelected(item)
    }

    // Delete a note
    private fun deleteNote(note: Note) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Are you sure?")
            setMessage("You cannot undo this operation")
            setPositiveButton("Yes"){_,_ ->
                viewModel.deleteNote(note)
                moveToHome()
            }
            setNegativeButton("No"){_,_ ->

            }
        }.create().show()
    }

    // Move from AddNoteFragment to HomeFragment
    private fun moveToHome(){
        val action = AddNoteFragmentDirections.returnBackHome()
        Navigation.findNavController(requireView()).navigate(action)
    }
}