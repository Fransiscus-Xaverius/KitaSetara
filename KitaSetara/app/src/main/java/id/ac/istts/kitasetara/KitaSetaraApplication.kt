package id.ac.istts.kitasetara

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import id.ac.istts.kitasetara.data.DefaultCoursesRepository
import id.ac.istts.kitasetara.data.DefaultQuotesRepository
import id.ac.istts.kitasetara.data.source.local.AppDatabase

import id.ac.istts.kitasetara.data.source.remote.QuoteService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create


class KitaSetaraApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        initRepository(baseContext)
    }
    companion object {
        lateinit var coursesRepository: DefaultCoursesRepository
        lateinit var quotesRepository: DefaultQuotesRepository
        fun initRepository(context: Context) {
            //create room local database with the name of "kitasetara"
            val roomDb = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "kitasetara"
            ).build()

            //init moshi
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

            //init retrofit with quotes api's base url
            val retrofitQuotes = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl("https://quote-garden.onrender.com/api/v3/").build()
            quotesRepository = DefaultQuotesRepository(retrofitQuotes.create(QuoteService::class.java))
            coursesRepository = DefaultCoursesRepository(roomDb)
        }

    }
}