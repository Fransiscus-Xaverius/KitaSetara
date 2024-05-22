package id.ac.istts.kitasetara.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ac.istts.kitasetara.model.leaderboard.Leaderboard

@Dao
interface LeaderboardDao {

    @Query("SELECT * FROM leaderboards")
    suspend fun getAll():List<Leaderboard>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(leaderboards: List<Leaderboard>)

    @Query("DELETE FROM leaderboards")
    suspend fun clearLeaderboards()
}