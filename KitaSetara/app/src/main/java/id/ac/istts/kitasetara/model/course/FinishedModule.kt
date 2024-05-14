package id.ac.istts.kitasetara.model.course

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "finished_modules")
data class FinishedModule(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id:Int,
    @ColumnInfo(name = "idModule") val idModule:String,
    @ColumnInfo(name = "username") val username:String
) {
}