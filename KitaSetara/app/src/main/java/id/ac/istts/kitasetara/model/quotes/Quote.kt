package id.ac.istts.kitasetara.model.quotes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "quotes")
data class Quote(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id") var id:Int ,
    @ColumnInfo(name ="quoteText") var quoteText : String,
    @ColumnInfo(name = "quoteAuthor") var quoteAuthor : String
)
