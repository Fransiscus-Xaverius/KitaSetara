package id.ac.istts.kitasetara.model.course

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "finished_contents")
data class FinishedContent(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id:Int,
    @ColumnInfo(name = "idContent") val idContent:String,
    @ColumnInfo(name = "username") val username:String
) {
}