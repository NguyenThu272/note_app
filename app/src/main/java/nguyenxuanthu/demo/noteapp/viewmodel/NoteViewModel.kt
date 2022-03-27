package nguyenxuanthu.demo.noteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nguyenxuanthu.demo.noteapp.entities.Note
import nguyenxuanthu.demo.noteapp.repository.NoteRepository
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: NoteRepository):ViewModel(){
    private val response = MutableLiveData<List<Note>>()
    val getAllNotes: LiveData<List<Note>>
        get() = response

    private val responseHighToLow = MutableLiveData<List<Note>>()
    val highToLow: LiveData<List<Note>>
        get() = responseHighToLow

    private val responseLowToHigh = MutableLiveData<List<Note>>()
    val lowToHigh: LiveData<List<Note>>
        get() = responseLowToHigh

    init{
        getNotes()
        getNotesHighToLow()
        getNotesLowToHigh()
    }


    private fun getNotes() = viewModelScope.launch {
        repository.getNotes().let{
            response.postValue(it)
        }
    }

    private fun getNotesHighToLow() = viewModelScope.launch {
        repository.getNotesHighToLow().let{
            responseHighToLow.postValue(it)
        }
    }

    private fun getNotesLowToHigh() = viewModelScope.launch {
        repository.getNotesLowToHigh().let{
            responseLowToHigh.postValue(it)
        }
    }

    suspend fun updateNote(note: Note) =
        withContext(viewModelScope.coroutineContext) {
            repository.update(note)
        }

    suspend fun insertNote(note: Note) =
        withContext(viewModelScope.coroutineContext) {
            repository.insert(note)
        }

    fun deleteNote(id: Int) = viewModelScope.launch {
        repository.delete(id)
    }
}