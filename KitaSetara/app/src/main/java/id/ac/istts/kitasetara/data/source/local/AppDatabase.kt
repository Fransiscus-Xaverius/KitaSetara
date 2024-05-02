package id.ac.istts.kitasetara.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import id.ac.istts.kitasetara.model.course.Course
import id.ac.istts.kitasetara.model.course.Module


@Database(entities = [Course::class, Module::class], version = 1)
abstract class AppDatabase:RoomDatabase() {

    abstract fun courseDao(): CourseDao
    abstract fun moduleDao(): ModuleDao
    abstract fun contentDao(): ContentDao

}