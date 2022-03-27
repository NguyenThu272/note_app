package nguyenxuanthu.demo.noteapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import nguyenxuanthu.demo.noteapp.dao.NoteDao
import nguyenxuanthu.demo.noteapp.entities.Note

@Database(entities = [Note::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao

    companion object {
        @Volatile
        private var notesDatabase: NotesDatabase? = null
        fun getDatabase(context: Context): NotesDatabase {
            synchronized(this){
            if (notesDatabase == null) {
                notesDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    "notes_db"
                ).build()
            }
            return notesDatabase!!
            }
        }
    }
}