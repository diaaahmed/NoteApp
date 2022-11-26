package com.note.note.ui.click

import androidx.cardview.widget.CardView
import com.note.note.data.models.Note

interface NoteItemClickListner {

    fun onItemClick(note: Note)
    fun onLongItemClick(note: Note, cardView: CardView)
}