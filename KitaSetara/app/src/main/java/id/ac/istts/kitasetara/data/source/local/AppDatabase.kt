package id.ac.istts.kitasetara.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import id.ac.istts.kitasetara.model.course.Content
import id.ac.istts.kitasetara.model.course.Course
import id.ac.istts.kitasetara.model.course.FinishedContent
import id.ac.istts.kitasetara.model.course.FinishedModule
import id.ac.istts.kitasetara.model.course.Module
import id.ac.istts.kitasetara.model.leaderboard.Leaderboard
import id.ac.istts.kitasetara.model.quiz.Question
import id.ac.istts.kitasetara.model.quotes.QuoteEntity
import id.ac.istts.kitasetara.model.term.Term


@Database(entities = [Course::class, Module::class, Content::class,Term::class,QuoteEntity::class,Question::class, FinishedContent::class, FinishedModule::class, Leaderboard::class], version = 8)
abstract class AppDatabase:RoomDatabase() {

    abstract fun courseDao(): CourseDao
    abstract fun moduleDao(): ModuleDao
    abstract fun contentDao(): ContentDao
    abstract fun termDao(): TermDao
    abstract fun quoteDao() : QuoteDao
    abstract fun questionDao() : QuestionDao
    abstract fun finishedModuleDao() : FinishedModuleDao
    abstract fun finishedContentDao() : FinishedContentDao
    abstract fun leaderboardDao() : LeaderboardDao
}