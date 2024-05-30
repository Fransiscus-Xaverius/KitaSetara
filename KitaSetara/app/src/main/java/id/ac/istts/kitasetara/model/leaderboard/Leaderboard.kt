package id.ac.istts.kitasetara.model.leaderboard

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "leaderboards")
data class Leaderboard(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id:String,
    @ColumnInfo(name = "score") val score : Int,
    @ColumnInfo(name = "userId") val userid : String,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "photoUrl") val photoUrl : String,
)
