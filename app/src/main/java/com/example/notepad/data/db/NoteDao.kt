package com.example.notepad.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Insert
    fun saveNote(note: Note)

    @Query("SELECT * FROM Note ORDER by id")
    fun getNotes() : LiveData<List<Note>>

    @Update
    fun updateNote(note: Note)

    @Delete
    fun deleteNote(note: Note)
}