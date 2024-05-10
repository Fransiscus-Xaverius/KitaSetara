package id.ac.istts.kitasetara.model.quotes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//for ROOM
@Entity(tableName = "quotes")
data class QuoteEntity (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id : String,
    @ColumnInfo(name = "quoteText") val quoteText : String,
    @ColumnInfo(name = "quoteAuthor") val quoteAuthor : String
)