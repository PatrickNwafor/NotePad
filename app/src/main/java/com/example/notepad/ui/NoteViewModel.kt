package com.example.notepad.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.notepad.data.db.Note
import com.example.notepad.data.repository.NoteRepository
import com.example.notepad.util.lazyDeferred

class NoteViewModel(
    private val repository: NoteRepository
) : ViewModel(){

    //Save note to db
    fun saveNote(note: Note){
        repository.saveNote(note)
    }

    // Update note in db
    fun updateNote(note: Note){
        repository.updateNote(note)
    }


    // Delete note from db
    fun deleteNote(note: Note){
        repository.deleteNote(note)
    }

    // get all notes
    val notes by lazyDeferred {
        repository.getAllNote()
    }

}