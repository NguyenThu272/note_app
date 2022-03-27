package nguyenxuanthu.demo.noteapp.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nguyenxuanthu.demo.noteapp.R
import nguyenxuanthu.demo.noteapp.config.Config
import nguyenxuanthu.demo.noteapp.databinding.ItemNoteBinding
import nguyenxuanthu.demo.noteapp.entities.Note
import kotlin.collections.ArrayList

class NotesAdapter ( private var notesList: ArrayList<Note>, private val clickListener:OnItemClickListener): RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun searchNotes(filterName:ArrayList<Note>){
        notesList = filterName
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_note,parent,false))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notesList[position],clickListener)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding:ItemNoteBinding = ItemNoteBinding.bind(itemView)

        fun bind(note:Note,clickListener:OnItemClickListener){
            binding.title.text = note.title
            binding.subtitle.text = note.subtitle
            binding.date.text = note.date
            when (note.priority) {
                Config.GREEN -> {
                    binding.priority.setBackgroundResource(R.drawable.green_shape) // View
                }
                Config.YELLOW -> {
                    binding.priority.setBackgroundResource(R.drawable.yellow_shape)
                }
                else -> {
                    binding.priority.setBackgroundResource(R.drawable.red_shape)
                }
            }
            itemView.setOnClickListener {
                clickListener.onItemClick(note)
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }
}
