package id.ac.istts.kitasetara.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import id.ac.istts.kitasetara.model.course.Content
import id.ac.istts.kitasetara.model.course.Module

@Dao
interface ContentDao {

    @Query("SELECT * FROM contents WHERE module_id = :id")
    fun getAllContentByModuleId(id:Int):List<Content>

    @Query("SELECT * FROM contents WHERE id = :id")
    fun getContent(id:Int): Content


    @Query("DELETE FROM contents")
    fun clearContents()

    @Insert
    fun insertMany(contents:List<Content>)

}