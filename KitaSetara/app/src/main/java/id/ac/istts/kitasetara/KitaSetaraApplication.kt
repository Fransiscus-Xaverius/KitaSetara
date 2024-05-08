package id.ac.istts.kitasetara

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import id.ac.istts.kitasetara.data.DefaultCoursesRepository
import id.ac.istts.kitasetara.data.DefaultQuotesRepository
import id.ac.istts.kitasetara.data.source.local.AppDatabase

import id.ac.istts.kitasetara.data.source.remote.QuoteService
import id.ac.istts.kitasetara.model.course.Course
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create


class KitaSetaraApplication:Application() {
    private lateinit var databaseReference: DatabaseReference
    val ioScope:CoroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        initRepository(baseContext)

        //load courses data from firebase
        loadCourses(object : CoursesCallback {
            // callback from load courses
            override fun onCoursesReceived(courses: List<Course>) {
                Log.d("COURSES", courses.toString())

                ioScope.launch {
                    //Update courses data at room
                    var coursesRepository = KitaSetaraApplication.coursesRepository
                    coursesRepository.clearAllCourses()
                    coursesRepository.insertCourses(courses)
                }
            }
        })

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

    //Function for loading data from firebase
    fun loadCourses(callback: CoursesCallback):ArrayList<Course>{
        var courses = ArrayList<Course>()

        databaseReference = FirebaseDatabase.getInstance().getReference("courses")
        databaseReference.get().addOnCompleteListener{
            task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                for (courseSnapshot in dataSnapshot.children) {
                    if(courseSnapshot.child("id").getValue(String::class.java) != null){
                        val courseId = courseSnapshot.child("id").getValue(String::class.java)
                        val courseName = courseSnapshot.child("name").getValue(String::class.java)
                        val courseDescription = courseSnapshot.child("description").getValue(String::class.java)

                        // Create course object and add it to the courses list
                        val course = Course(courseId!!.toInt(), courseName?:"", courseDescription?:"")
                        courses.add(course)
                    }
                }
                //memanggil callback function setelah proses selesai
                callback.onCoursesReceived(courses)
            } else {
                Log.w("FETCH ERROR", "Can't fetch courses from firebase")
            }
        }

        return courses
    }

    //Interface for load course callback
    interface CoursesCallback {
        fun onCoursesReceived(courses: List<Course>)
    }
}