package nguyenxuanthu.demo.noteapp.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import nguyenxuanthu.demo.noteapp.R
import nguyenxuanthu.demo.noteapp.adapters.NotesAdapter
import nguyenxuanthu.demo.noteapp.config.Config
import nguyenxuanthu.demo.noteapp.databinding.FragmentMainBinding
import nguyenxuanthu.demo.noteapp.entities.Note
import nguyenxuanthu.demo.noteapp.listener.MainActivityListener
import nguyenxuanthu.demo.noteapp.viewmodel.NoteViewModel

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main), NotesAdapter.OnItemClickListener {

    private var _binding: FragmentMainBinding?=null
    private val binding get() = _binding!!
    lateinit var viewModel:NoteViewModel
    private var mainActivityListener:MainActivityListener? = null
    private lateinit var noteAdapter: NotesAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is MainActivityListener){
            mainActivityListener = context
        }
        //mainActivityListener= context as MainActivityListener   // cach 2
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        setUpFilter()
        setUpRecyclerView()
        binding.newNote.setOnClickListener {
            val createNewFragment = DetailNoteFragment.newInstance(Config.ACTION_CREATE,null)
            mainActivityListener!!.onOpenFragment(createNewFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mainActivityListener = null
    }

    private var notesList = ArrayList<Note>()
    private fun setUpFilter() {
        binding.noFilter.setBackgroundResource(R.drawable.filter_selected_shape)
        viewModel.getAllNotes.observe(this, Observer {
            notesList = it as ArrayList<Note>
            setUpRecyclerView()
        })

        binding.noFilter.setOnClickListener {
            binding.noFilter.setBackgroundResource(R.drawable.filter_selected_shape)
            binding.highToLow.setBackgroundResource(R.drawable.filter_item_shape)
            binding.lowToHigh.setBackgroundResource(R.drawable.filter_item_shape)
            viewModel.getAllNotes.observe(this, Observer {
                notesList = it as ArrayList<Note>
                setUpRecyclerView()
            })
        }
        binding.highToLow.setOnClickListener {
            binding.highToLow.setBackgroundResource(R.drawable.filter_selected_shape)
            binding.noFilter.setBackgroundResource(R.drawable.filter_item_shape)
            binding.lowToHigh.setBackgroundResource(R.drawable.filter_item_shape)
            viewModel.highToLow.observe(this, Observer {
                notesList = it as ArrayList<Note>
                setUpRecyclerView()
            })
        }
        binding.lowToHigh.setOnClickListener {
            binding.lowToHigh.setBackgroundResource(R.drawable.filter_selected_shape)
            binding.highToLow.setBackgroundResource(R.drawable.filter_item_shape)
            binding.noFilter.setBackgroundResource(R.drawable.filter_item_shape)
            viewModel.lowToHigh.observe(this, Observer {
                notesList = it as ArrayList<Note>
                setUpRecyclerView()
            })

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUpRecyclerView() {
        noteAdapter = NotesAdapter(notesList ,this)
        noteAdapter.notifyDataSetChanged()
        binding.notesRecycler.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = noteAdapter
        }
    }

    override fun onItemClick(note: Note) {
        val createNewFragment = DetailNoteFragment.newInstance(Config.ACTION_UPDATE,note)
        mainActivityListener!!.onOpenFragment(createNewFragment)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_note_menu,menu)
        val menuItem = menu.findItem(R.id.app_bar_search)
        val searchView = menuItem.actionView as SearchView
        searchView.apply {
            queryHint = "Tìm kiếm ghi chú tại đây ... "
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    notesSearch(newText)
                    return false
                }
            })
        }
    }

    private fun notesSearch(newText:String?){
        val filterName = ArrayList<Note>()
        if(newText != null || newText != ""){
            for(note in notesList){
                if(note.title.contains(newText!!) || note.subtitle.contains(newText!!)){
                    filterName.add(note)
                }
            }
            this.noteAdapter.searchNotes(filterName)
        }

    }
}