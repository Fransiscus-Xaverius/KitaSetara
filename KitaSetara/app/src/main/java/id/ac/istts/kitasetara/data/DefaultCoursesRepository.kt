package id.ac.istts.kitasetara.data

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.data.source.local.AppDatabase
import id.ac.istts.kitasetara.model.course.Content
import id.ac.istts.kitasetara.model.course.Course
import id.ac.istts.kitasetara.model.course.FinishedContent
import id.ac.istts.kitasetara.model.course.FinishedModule
import id.ac.istts.kitasetara.model.course.Module
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DefaultCoursesRepository(
    private val firebaseDatabase:FirebaseDatabase,
    private val localDataSource:AppDatabase
) {
    fun getAllCourses():List<Course> {
        return localDataSource.courseDao().getAll()
    }

    fun getCourseById(id:Int): Course {
        return localDataSource.courseDao().getById(id)
    }

    fun getAllCourseModule(idCourse:Int):List<Module>{
        return  localDataSource.moduleDao().getAllModulesByCourseId(idCourse)
    }

    fun insertCourses(courses:List<Course>){
        localDataSource.courseDao().insertMany(courses)
    }

    fun clearAllCourses(){
        localDataSource.courseDao().clearCourses()
    }

    fun getModule(idModule:Int):Module{
        return  localDataSource.moduleDao().getModule(idModule)
    }

    fun getAllModuleContent(idModule:Int):List<Content>{
        return  localDataSource.contentDao().getAllContentByModuleId(idModule)
    }

    fun insertModules(modules:List<Module>){
        localDataSource.moduleDao().insertMany(modules)
    }

    fun clearModules(){
        localDataSource.moduleDao().clearModules()
    }

    fun getContent(idContent:Int):Content{
        return  localDataSource.contentDao().getContent(idContent)
    }

    fun insertContents(contents:List<Content>){
        localDataSource.contentDao().insertMany(contents)
    }

    fun clearContents(){
        localDataSource.contentDao().clearContents()
    }

    suspend fun getFinishedContents():List<FinishedContent>{
        return localDataSource.finishedContentDao().getAllFinishedContents()
    }

    suspend fun fetchFinishedContentDataFromFirebaseAndInsertToRoom() {
        val data = fetchFinishedContentDataFromFirebase()
        withContext(Dispatchers.IO) {
            // Insert data into Room database
            localDataSource.finishedContentDao().clearFinishedContents()
            localDataSource.finishedContentDao().insertMany(data)
        }
    }

    suspend fun fetchFinishedContentDataFromFirebase(): List<FinishedContent> {
        return withContext(Dispatchers.IO) {
            val database = FirebaseDatabase.getInstance()
            val firebaseReference = database.getReference("finished_contents")

            try {
                val dataSnapshot = firebaseReference.get().await()
                val dataList = mutableListOf<FinishedContent>()

                for (snapshot in dataSnapshot.children){
                    val id = snapshot.child("id").getValue(Long::class.java)
                    val username = snapshot.child("username").getValue(String::class.java)
                    val idCon = snapshot.child("idContent").getValue(String::class.java)

                    if(id != null && username != null && username == Helper.currentUser!!.username && idCon != null){
//                        Log.d("C", id.toString())
                        dataList.add(FinishedContent(id.toInt(), idCon?:"", username?:""))
                    }
                }
                dataList
            } catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
                emptyList() // or throw an exception if needed
            }
        }
    }

    suspend fun saveFinishedContent(idContent: Int){
        var databaseReference:DatabaseReference = firebaseDatabase.getReference("finished_contents")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var dataAlreadyExist = false
                var lastId = 0

                for (snapshot in dataSnapshot.children){
                    val username = snapshot.child("username").getValue(String::class.java)
                    val idCon = snapshot.child("idContent").getValue(String::class.java)

//                    Log.d("CEK", "${username} - ${idCon} - ${username == Helper.currentUser?.username && idCon == idContent.toString()}")
                    if(username == Helper.currentUser?.username && idCon == idContent.toString()){
                        dataAlreadyExist = true
                    }

                    val id = snapshot.child("id").getValue(Long::class.java)
                    if(id != null){
                        lastId = id.toInt()
                    }
                }

                //insert data finished content baru
                if(!dataAlreadyExist){
                    val id = databaseReference.push().key //create unique id untuk record baru
                    val finishedContent = FinishedContent(lastId+1, idContent.toString(), Helper.currentUser!!.username)
                    //insert data ke dalam unique id yang telah dibuat
                    databaseReference.child(id!!).setValue(finishedContent)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                //handle error
                Log.e("FINISHED CONTENT", "Database Error : ${error.message}")
            }
        })

    }

    suspend fun getFinishedModules():List<FinishedModule>{
        return localDataSource.finishedModuleDao().getAllFinishedModules()
    }

    suspend fun fetchFinishedModuleDataFromFirebaseAndInsertToRoom() {
        val data = fetchFinishedModuleDataFromFirebase()
        withContext(Dispatchers.IO) {
            // Insert data into Room database
            localDataSource.finishedModuleDao().clearFinishedModules()
            localDataSource.finishedModuleDao().insertMany(data)
        }
    }


    suspend fun fetchFinishedModuleDataFromFirebase(): List<FinishedModule> {
        return withContext(Dispatchers.IO) {
            val database = FirebaseDatabase.getInstance()
            val firebaseReference = database.getReference("finished_modules")

            try {
                val dataSnapshot = firebaseReference.get().await()
                val dataList = mutableListOf<FinishedModule>()

                for (snapshot in dataSnapshot.children){
                    val id = snapshot.child("id").getValue(Long::class.java)
                    val username = snapshot.child("username").getValue(String::class.java)
                    val idModule = snapshot.child("idModule").getValue(String::class.java)

                    if(id != null && username != null && username == Helper.currentUser!!.username && idModule != null){
                        dataList.add(FinishedModule(id.toInt(), idModule?:"", username?:""))
                    }
                }
                dataList
            } catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
                emptyList() // or throw an exception if needed
            }
        }
    }


    suspend fun saveFinishedModule(idModule: Int){
        var databaseReference:DatabaseReference = firebaseDatabase.getReference("finished_modules")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var dataAlreadyExist = false
                var lastId = 0

                for (snapshot in dataSnapshot.children){
                    val username = snapshot.child("username").getValue(String::class.java)
                    val idModule = snapshot.child("idModule").getValue(String::class.java)

                    Log.d("CEK", "${username} - ${idModule} - ${username == Helper.currentUser?.username && idModule == idModule.toString()}")
                    if(username == Helper.currentUser?.username && idModule == idModule.toString()){
                        dataAlreadyExist = true
                    }

                    val id = snapshot.child("id").getValue(Long::class.java)
                    if(id != null){
                        lastId = id.toInt()
                    }
                }

                //insert data finished module yang baru
                if(!dataAlreadyExist){
                    val id = databaseReference.push().key //create unique id untuk record baru
                    val finishedModule = FinishedModule(lastId+1, idModule.toString(), Helper.currentUser!!.username)
                    //insert data ke dalam unique id yang telah dibuat
                    databaseReference.child(id!!).setValue(finishedModule)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                //handle error
                Log.e("FINISHED MODULE", "Database Error : ${error.message}")
            }
        })

    }


}