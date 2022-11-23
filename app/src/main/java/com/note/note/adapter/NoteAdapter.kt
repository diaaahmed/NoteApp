package com.note.note.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.note.note.R
import com.note.note.click.NoteItemClickListner
import com.note.note.databinding.ListItemBinding
import com.note.note.models.Note
import kotlin.random.Random

class NoteAdapter(private val context:Context, val click:NoteItemClickListner):RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val noteList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()

    inner class NoteViewHolder(private val item:ListItemBinding):RecyclerView.ViewHolder(item.root)
    {
        fun bind(note:Note) = with(item)
        {
            txTitle.text = note.title
            txTitle.isSelected = true
            txNote.text = note.note
            txDate.text = note.date
            txDate.isSelected = true
            cardView.setBackgroundColor(itemView.resources.getColor(randomColor(), null))

            cardView.setOnClickListener { click.onItemClick(note) }
            cardView.setOnLongClickListener {
                click.onLongItemClick(note,cardView)
                true}
        }

    }

    fun updateList(newList:List<Note>)
    {
        fullList.clear()
        fullList.addAll(newList)

        noteList.clear()
        noteList.addAll(fullList)

        notifyDataSetChanged()
    }

    fun filterList(search:String)
    {
        noteList.clear()

        for(item in fullList)
        {
            if(item.title.lowercase().contains(search.toLowerCase()) ||
                    item.note.lowercase().contains(search.toLowerCase()))
            {
                noteList.add(item)
            }
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder
    {
        return NoteViewHolder(
            ListItemBinding.inflate
                (
                LayoutInflater.from(parent.context),
                parent,
                false))
    }

    override fun getItemCount(): Int = noteList.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int)
    {
        holder.bind(noteList[position])
    }

    fun randomColor():Int{

        val list = ArrayList<Int>()
        list.add(R.color.NoteColor1)
        list.add(R.color.NoteColor2)
        list.add(R.color.NoteColor3)
        list.add(R.color.NoteColor4)
        list.add(R.color.NoteColor5)
        list.add(R.color.NoteColor6)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)

        return list[randomIndex]
    }


}