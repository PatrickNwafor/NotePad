package com.example.notepad.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Note(
    var title: String,
    var body: String,
    var category: String,
    var createdAt: String
): Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}