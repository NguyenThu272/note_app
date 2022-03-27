package nguyenxuanthu.demo.noteapp.dao

import androidx.room.*
import nguyenxuanthu.demo.noteapp.entities.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    suspend fun getAllNotes(): List<Note>

    @Query("SELECT * FROM notes ORDER BY priority DESC")
    suspend fun highToLow(): List<Note>

    @Query("SELECT * FROM notes ORDER BY priority ASC")
    suspend fun lowToHigh(): List<Note>

    @Insert
    suspend fun insertNote(note: Note) : Long

    @Update
    suspend fun updateNote(note: Note) : Int

    @Query("DELETE FROM notes WHERE id=:id")
    suspend fun deleteNote(id: Int)
}