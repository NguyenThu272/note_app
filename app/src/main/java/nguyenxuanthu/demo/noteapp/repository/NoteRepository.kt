package nguyenxuanthu.demo.noteapp.repository

import android.app.Application
import nguyenxuanthu.demo.noteapp.dao.NoteDao
import nguyenxuanthu.demo.noteapp.database.NotesDatabase
import nguyenxuanthu.demo.noteapp.entities.Note
import javax.inject.Inject

class NoteRepository @Inject constructor(database: NotesDatabase){
    companion object {
        lateinit var noteDao: NoteDao
        private var noteRepository: NoteRepository? = null
        fun getRepository(database: NotesDatabase): NoteRepository{
            synchronized(this){
                if (noteRepository == null) {
                    noteRepository = NoteRepository(database)
                    noteDao = database.noteDao
                }
                return noteRepository!!
            }
        }
    }
    suspend fun getNotes() = noteDao.getAllNotes()

    suspend fun getNotesHighToLow() = noteDao.highToLow()

    suspend fun getNotesLowToHigh() = noteDao.lowToHigh()

    suspend fun insert(note: Note):Long{
        return noteDao.insertNote(note)
    }

    suspend fun update(note: Note):Int{
        return noteDao.updateNote(note)
    }

    suspend fun delete(id: Int) {
        noteDao.deleteNote(id)
    }

}