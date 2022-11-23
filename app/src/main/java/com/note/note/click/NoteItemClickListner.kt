package com.note.note.click

import androidx.cardview.widget.CardView
import com.note.note.models.Note

interface NoteItemClickListner {

    fun onItemClick(note: Note)
    fun onLongItemClick(note: Note, cardView: CardView)
}