package com.note.note

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.note.note.databinding.ActivityAddNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class AddNote : AppCompatActivity() {

    private val ui by lazy{
        ActivityAddNoteBinding.inflate(layoutInflater)
    }

    private lateinit var note:com.note.note.models.Note
    private lateinit var oldNote:com.note.note.models.Note

    var isUpdated = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(ui.root)

        try {
            oldNote = intent.getSerializableExtra("current_note") as com.note.note.models.Note
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
                    note = com.note.note.models.Note(oldNote.id, title, noteDes, formatter.format(Date()))
                }
                else
                {
                    note = com.note.note.models.Note(null,title,noteDes,formatter.format(Date()))
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