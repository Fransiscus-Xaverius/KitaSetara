package id.ac.istts.kitasetara

import android.app.Application
import android.content.Context
import androidx.room.Room
import id.ac.istts.kitasetara.data.DefaultCoursesRepository
import id.ac.istts.kitasetara.data.source.local.AppDatabase

class KitaSetaraApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        initRepository(baseContext)
    }
    companion object {
        lateinit var coursesRepository: DefaultCoursesRepository
        fun initRepository(context: Context) {
            val roomDb = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "kitasetara"
            ).build()

            coursesRepository = DefaultCoursesRepository(roomDb)
        }
    }
}