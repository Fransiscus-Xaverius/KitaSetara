package id.ac.istts.kitasetara.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.ac.istts.kitasetara.model.course.Content
import id.ac.istts.kitasetara.model.course.Course
import id.ac.istts.kitasetara.model.course.Module
import id.ac.istts.kitasetara.model.quotes.QuoteEntity
import id.ac.istts.kitasetara.model.term.Term


@Database(entities = [Course::class, Module::class, Content::class,Term::class,QuoteEntity::class], version = 3)
abstract class AppDatabase:RoomDatabase() {

    abstract fun courseDao(): CourseDao
    abstract fun moduleDao(): ModuleDao
    abstract fun contentDao(): ContentDao
    abstract fun termDao(): TermDao
    abstract fun quoteDao() : QuoteDao
}