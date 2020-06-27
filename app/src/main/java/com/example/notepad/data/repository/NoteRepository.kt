package com.example.notepad.data.repository

import androidx.lifecycle.LiveData
import com.example.notepad.data.db.AppDatabase
import com.example.notepad.data.db.Note
import com.example.notepad.util.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(
    private val db: AppDatabase
){

    // save note in db from a secondary thread using coroutine
    fun saveNote(note: Note){
        Coroutines.io {
            db.getNoteDao().saveNote(note)
        }
    }

    // update note in db from a secondary thread using coroutine
    fun updateNote(note: Note){
        Coroutines.io {
            db.getNoteDao().updateNote(note)
        }
    }

    // delete note in db from a secondary thread using coroutine
    fun deleteNote(note: Note){
        Coroutines.io {
            db.getNoteDao().deleteNote(note)
        }
    }

    // get all notes as a live data
    suspend fun getAllNote(): LiveData<List<Note>>{
        return withContext(Dispatchers.IO){
            db.getNoteDao().getNotes()
        }
    }
}