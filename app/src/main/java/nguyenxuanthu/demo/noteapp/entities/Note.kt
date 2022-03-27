package nguyenxuanthu.demo.noteapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "subtitle")
    var subtitle: String,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "note_text")
    var noteText: String,

    @ColumnInfo(name = "priority")
    var priority: Int,

):Serializable