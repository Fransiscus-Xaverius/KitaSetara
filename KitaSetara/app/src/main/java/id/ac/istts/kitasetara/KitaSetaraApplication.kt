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
import id.ac.istts.kitasetara.model.course.Content
import id.ac.istts.kitasetara.model.course.Course
import id.ac.istts.kitasetara.model.course.Module
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

        loadModule(object: ModulesCallback{
            override fun onModulesReceived(modules: List<Module>) {
                Log.d("MODULES", modules.toString())
                ioScope.launch {
                    //Update modules data at room
                    var coursesRepository = KitaSetaraApplication.coursesRepository
                    coursesRepository.clearModules()
                    coursesRepository.insertModules(modules)
                }
            }
        })

        loadContent(object : ContentsCallback{
            override fun onContentsReceived(contents: List<Content>) {
                Log.d("MODULES", contents.toString())
                ioScope.launch {
                    //Update modules data at room
                    var coursesRepository = KitaSetaraApplication.coursesRepository
                    coursesRepository.clearContents()
                    coursesRepository.insertContents(contents)
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

    fun loadModule(callback: ModulesCallback):ArrayList<Module>{
        var modules = ArrayList<Module>()
        databaseReference = FirebaseDatabase.getInstance().getReference("modules")
        databaseReference.get().addOnCompleteListener{
                task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                for (moduleSnapshot in dataSnapshot.children) {
                    if(moduleSnapshot.child("id").getValue(String::class.java) != null){
                        val moduleId = moduleSnapshot.child("id").getValue(String::class.java)
                        val moduleName = moduleSnapshot.child("name").getValue(String::class.java)
                        val moduleDescription = moduleSnapshot.child("description").getValue(String::class.java)
                        val courseId = moduleSnapshot.child("course_id").getValue(String::class.java)

                        val module = Module(moduleId!!.toInt(), moduleName?:"", moduleDescription?:"", courseId?:"")
                        modules.add(module)
                    }
                }
                //memanggil callback function setelah proses selesai
                callback.onModulesReceived(modules)
            } else {
                Log.w("FETCH ERROR", "Can't fetch modules from firebase")
            }
        }
        return modules
    }

    fun loadContent(callback: ContentsCallback):ArrayList<Content>{
        var contents = ArrayList<Content>()
        databaseReference = FirebaseDatabase.getInstance().getReference("module_content")
        databaseReference.get().addOnCompleteListener{
                task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                for (moduleContentSnapshot in dataSnapshot.children) {
                    if(moduleContentSnapshot.child("id").getValue(String::class.java) != null && moduleContentSnapshot.child("module_id").getValue(String::class.java) != null){
                        val contentId = moduleContentSnapshot.child("id").getValue(String::class.java)
                        val contentName = moduleContentSnapshot.child("name").getValue(String::class.java)
                        val contentText = moduleContentSnapshot.child("content").getValue(String::class.java)
                        val moduleId = moduleContentSnapshot.child("module_id").getValue(String::class.java)

                        val content = Content(contentId!!.toInt(), contentName?:"", contentText?:"", moduleId!!.toInt())
                        contents.add(content)
                    }
                }
                //memanggil callback function setelah proses selesai
                callback.onContentsReceived(contents)
            } else {
                Log.w("FETCH ERROR", "Can't fetch contents from firebase")
            }
        }
        return contents
    }

    //Interface for load course callback
    interface CoursesCallback {
        fun onCoursesReceived(courses: List<Course>)
    }

    interface ModulesCallback {
        fun onModulesReceived(modules: List<Module>)
    }

    interface ContentsCallback{
        fun onContentsReceived(contents: List<Content>)
    }
}