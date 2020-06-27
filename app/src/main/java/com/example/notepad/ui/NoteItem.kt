package com.example.notepad.ui

import android.graphics.Color
import android.view.View
import androidx.navigation.Navigation
import com.example.notepad.R
import com.example.notepad.data.db.Note
import com.example.notepad.databinding.NoteViewBinding
import com.example.notepad.util.toast
import com.xwray.groupie.viewbinding.BindableItem

class NoteItem(
    private val note: Note
) : BindableItem<NoteViewBinding>(){
    override fun getLayout() = R.layout.note_view

    override fun bind(viewBinding: NoteViewBinding, position: Int) {
        when(note.category){
            "Personal" -> viewBinding.colorIdentifier.setBackgroundColor(Color.YELLOW)
            "Work" -> viewBinding.colorIdentifier.setBackgroundColor(Color.GREEN)
            "School" -> viewBinding.colorIdentifier.setBackgroundColor(Color.BLUE)
            else -> viewBinding.colorIdentifier.setBackgroundColor(Color.RED)
        }

        viewBinding.noteTitleTextView.text = note.title
        viewBinding.noteTextView.text = note.body
        viewBinding.categoryTextView.text = note.category
        viewBinding.noteDateTextView.text = note.createdAt

        viewBinding.noteCard.setOnClickListener {
            it.context.toast(note.category)
            val action = HomeFragmentDirections.addNoteBtnLocation()
            action.note = note
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun initializeViewBinding(view: View): NoteViewBinding {
        return NoteViewBinding.bind(view)
    }

}