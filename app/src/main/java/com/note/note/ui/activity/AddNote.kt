package com.note.note.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.note.note.data.models.Note
import com.note.note.databinding.ActivityAddNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class AddNote : AppCompatActivity() {

    private val ui by lazy{
        ActivityAddNoteBinding.inflate(layoutInflater)
    }

    private lateinit var note: Note
    private lateinit var oldNote: Note

    var isUpdated = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(ui.root)

        try {
            oldNote = intent.getSerializableExtra("current_note") as Note
            ui.edTitle.setText(oldNote.title)
            ui.edNote.setText(oldNote.note)
            isUpdated = true

        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }

        ui.imgCheck.setOnClickListener {
            val title = ui.edTitle.text.toString()
            val noteDes = ui.edNote.text.toString()

            if (title.isNotEmpty() || noteDes.isNotEmpty())
            {
                val formatter = SimpleDateFormat("EEE,d MMM yyyy HH:mm a")

                if(isUpdated)
                {
                    note = Note(oldNote.id, title, noteDes, formatter.format(Date()))
                }
                else
                {
                    note = Note(null,title,noteDes,formatter.format(Date()))
                }

                val intent = Intent()
                intent.putExtra("note",note)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
            else
            {
                Toast.makeText(this@AddNote, "Please enter some data",
                    Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }
        }

        ui.imgBack.setOnClickListener { onBackPressed() }
    }
}