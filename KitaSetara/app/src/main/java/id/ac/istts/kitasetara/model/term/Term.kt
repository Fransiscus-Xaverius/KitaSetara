package id.ac.istts.kitasetara.model.term

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "terms")
data class Term (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id : String,
    @ColumnInfo(name = "term") val term : String,
    @ColumnInfo(name = "meaning") val meaning : String
)