package com.example.notepad.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notepad.data.db.Note
import com.example.notepad.databinding.FragmentHomeBinding
import com.example.notepad.util.Coroutines
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import org.kodein.di.android.x.kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class HomeFragment : Fragment(), KodeinAware {

    // dependency injection initialization
    override val kodein by kodein()
    // View model factory setting which enables the view model class to be instantiated with a NoteRepository class as parameter
    private val factory : NoteViewModelFactory by instance<NoteViewModelFactory>()

    private lateinit var viewModel: NoteViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)
        return  binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Add the notes in the db to the recycler view
        bindUI()

        // Add note btn function
        binding.addNoteBtn.setOnClickListener {
            val action = HomeFragmentDirections.addNoteBtnLocation()
            Navigation.findNavController(it).navigate(action)
        }
    }

    // Getting the noetes from db in a coroutine scope and bind to UI
    private fun bindUI() = Coroutines.main {
        viewModel.notes.await().observe(viewLifecycleOwner, Observer {
            initRecyclerView(it.toNoteItem())
        })
    }

    // Setting up recycler view
    private fun initRecyclerView(noteItem: List<NoteItem>) {

        val mAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(noteItem)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

    // Mapping our note to a NoteItem class so we can pass to our recycler view binding class
    private fun List<Note>.toNoteItem() : List<NoteItem>{
        return this.map {
            NoteItem(it)
        }
    }


}