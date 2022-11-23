 package com.note.note

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.note.note.adapter.NoteAdapter
import com.note.note.click.NoteItemClickListner
import com.note.note.database.NoteDatabase
import com.note.note.databinding.ActivityMainBinding
import com.note.note.models.Note
import com.note.note.models.NoteViewModel

 class MainActivity : AppCompatActivity(), NoteItemClickListner,PopupMenu.OnMenuItemClickListener
 {
     private val ui by lazy{
         ActivityMainBinding.inflate(layoutInflater)
     }

     private val adapter by lazy{
         NoteAdapter(this,this)
     }

     lateinit var database: NoteDatabase

     lateinit var selectedNote:Note
     lateinit var viewModel:NoteViewModel

     private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
         result->
         if(result.resultCode == Activity.RESULT_OK)
         {
             val note = result.data?.getSerializableExtra("note") as Note

             if(note != null)
             {
                 viewModel.updateNote(note)
             }
         }
     }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(ui.root)

        initUi()

        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        viewModel.allNotes.observe(this) { list ->

            list?.let {
                adapter.updateList(list)
            }

        }

        database = NoteDatabase.getDatabase(this)


    }

     private fun initUi()
     {
         ui.recyclerView.setHasFixedSize(true)
         ui.recyclerView.layoutManager = StaggeredGridLayoutManager(2,LinearLayout.VERTICAL)
         ui.recyclerView.adapter = adapter

         val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
             result->
             if(result.resultCode == Activity.RESULT_OK)
             {
                 val note = result.data?.getSerializableExtra("note") as Note
                 if(note != null)
                 {
                     viewModel.insertNote(note)
                 }
             }
         }

         ui.fbAddNote.setOnClickListener {
             val intent = Intent(this,AddNote::class.java)
             getContent.launch(intent)
         }

         ui.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
             androidx.appcompat.widget.SearchView.OnQueryTextListener {
             override fun onQueryTextSubmit(p0: String?): Boolean
             {
                 return false
             }

             override fun onQueryTextChange(searchText: String?): Boolean
             {
                 if(searchText != null)
                 {
                     adapter.filterList(searchText)
                 }
                 return true
             }

         })
     }

     override fun onItemClick(note: Note)
     {
         val intent = Intent(this@MainActivity,AddNote::class.java)
         intent.putExtra("current_note",note)
         updateNote.launch(intent)
     }

     override fun onLongItemClick(note: Note, cardView: CardView)
     {
         selectedNote = note
         popUpDisplay(cardView)
     }

     private fun popUpDisplay(cardView: CardView) {
         val popUp = PopupMenu(this,cardView)
         popUp.setOnMenuItemClickListener (this@MainActivity)
         popUp.inflate(R.menu.pop_up_menu)
         popUp.show()
     }

     override fun onMenuItemClick(item: MenuItem?): Boolean
     {
         if(item?.itemId == R.id.delete_noe)
         {
             viewModel.deleteNote(selectedNote)
             return true
         }

         return false
     }
 }