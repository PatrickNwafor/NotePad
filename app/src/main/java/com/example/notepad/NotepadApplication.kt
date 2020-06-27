package com.example.notepad

import android.app.Application
import com.example.notepad.data.db.AppDatabase
import com.example.notepad.data.repository.NoteRepository
import com.example.notepad.ui.NoteViewModelFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class NotepadApplication : Application(), KodeinAware{

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@NotepadApplication))

        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { NoteRepository(instance()) }
        bind() from provider { NoteViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }

}