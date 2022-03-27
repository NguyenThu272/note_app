package nguyenxuanthu.demo.noteapp.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.TextWatcher
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nguyenxuanthu.demo.noteapp.R
import nguyenxuanthu.demo.noteapp.config.Config
import nguyenxuanthu.demo.noteapp.databinding.DeleteButtonSheetBinding
import nguyenxuanthu.demo.noteapp.databinding.FragmentDetailNoteBinding
import nguyenxuanthu.demo.noteapp.entities.Note
import nguyenxuanthu.demo.noteapp.listener.MainActivityListener
import nguyenxuanthu.demo.noteapp.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.Inflater

@AndroidEntryPoint
class DetailNoteFragment : Fragment(R.layout.fragment_detail_note) {

    private var _binding: FragmentDetailNoteBinding?=null
    private val binding get() = _binding!!
    lateinit var viewModel:NoteViewModel
    private var mainActivityListener: MainActivityListener? = null

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
        _binding = FragmentDetailNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var note:Note?= null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        setUpPriority()
        note =  arguments?.getSerializable("note") as Note?
        if(note!=null){
            setUpEditText(note!!)
        }
        setUpSaveNoteButton()

    }

    private var priority = Config.GREEN
    private fun setUpPriority() {
        binding.greenPriority.setOnClickListener {
            binding.greenPriority.setImageResource(R.drawable.ic_done) // ImageView
            binding.yellowPriority.setImageResource(0)
            binding.redPriority.setImageResource(0)
            priority = Config.GREEN
        }
        binding.yellowPriority.setOnClickListener {
            binding.yellowPriority.setImageResource(R.drawable.ic_done)
            binding.greenPriority.setImageResource(0)
            binding.redPriority.setImageResource(0)
            priority = Config.YELLOW
        }
        binding.redPriority.setOnClickListener {
            binding.redPriority.setImageResource(R.drawable.ic_done)
            binding.yellowPriority.setImageResource(0)
            binding.greenPriority.setImageResource(0)
            priority = Config.RED
        }
    }

    private fun setUpEditText(note:Note) {
        binding.noteTitle.setText(note.title)
        binding.noteSubtitle.setText(note.subtitle)
        binding.noteData.setText(note.noteText)
        when (note.priority) {
            Config.GREEN -> {
                binding.greenPriority.setImageResource(R.drawable.ic_done) // ImageView
                binding.yellowPriority.setImageResource(0)
                binding.redPriority.setImageResource(0)
                priority = Config.GREEN
            }
            Config.YELLOW -> {
                binding.yellowPriority.setImageResource(R.drawable.ic_done)
                binding.greenPriority.setImageResource(0)
                binding.redPriority.setImageResource(0)
                priority = Config.YELLOW
            }
            else -> {
                binding.redPriority.setImageResource(R.drawable.ic_done)
                binding.yellowPriority.setImageResource(0)
                binding.greenPriority.setImageResource(0)
                priority = Config.RED
            }
        }
    }

    lateinit var title:String
    lateinit var subtitle:String
    lateinit var notes:String
    private fun setUpSaveNoteButton() {
            binding.saveNote.setOnClickListener {
                title = binding.noteTitle.text.trim().toString()
                subtitle = binding.noteSubtitle.text.trim().toString()
                notes = binding.noteData.text.trim().toString()
                if(title!="" || subtitle!="" || notes!="") {
                if (arguments!!.get("action") == Config.ACTION_CREATE) {
                    createNotes(title, subtitle, notes)
                } else if (arguments!!.get("action") == Config.ACTION_UPDATE) {
                    updateNotes(title, subtitle, notes)
                }
                closeFragment()
            }
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun createNotes(title: String, subtitle: String, notes: String) {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        CoroutineScope(Dispatchers.Main).launch{
            val result = viewModel.insertNote(Note(0,title,subtitle,currentDate,notes,priority))
            if(result > -1){
                Toast.makeText(requireContext(),"Tạo ghi chú thành công!",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),"Tạo ghi chú không thành công!",Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateNotes(title: String, subtitle: String, notes: String) {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        CoroutineScope(Dispatchers.Main).launch{
            val result = viewModel.updateNote(Note(note!!.id,title,subtitle,currentDate,notes,priority))
            if(result>-1){
                Toast.makeText(requireContext(),"Ghi chú cập nhật thành công!",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),"Ghi chú cập nhật không thành công!",Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun closeFragment() {
        mainActivityListener!!.onOpenFragment(MainFragment())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.delete_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.delete){
            val dialogBinding:DeleteButtonSheetBinding =  DeleteButtonSheetBinding.inflate(
                LayoutInflater.from(requireContext()))
            val sheetDialog = BottomSheetDialog(requireContext()).apply {
                setContentView(dialogBinding.root)
            }

            dialogBinding.deleteYes.setOnClickListener {
                if(note == null){
                    Toast.makeText(requireContext(),"Ghi chú này không có dữ liệu\n Hãy tạo mới nhé!",Toast.LENGTH_SHORT).show()
                }else{
                    viewModel.deleteNote(note!!.id)
                    Toast.makeText(requireContext(),"Xóa ghi chú thành công!",Toast.LENGTH_SHORT).show()
                    sheetDialog.dismiss()
                    closeFragment()
                }
            }
            dialogBinding.deleteNo.setOnClickListener {
                sheetDialog.dismiss()
            }
            sheetDialog.show() // set show() tren bien sheetDialog thi sheetDialog.dismiss() duoi khong goi duoc
        }else if(item.itemId == R.id.home){
            closeFragment()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(action:String,note: Note?) =
            DetailNoteFragment().apply {
                arguments = Bundle().apply {
                    putString("action",action)
                    putSerializable("note", note)
                }
            }
    }

}