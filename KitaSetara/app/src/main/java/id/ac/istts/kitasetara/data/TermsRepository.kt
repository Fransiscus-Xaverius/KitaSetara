package id.ac.istts.kitasetara.data

import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.ac.istts.kitasetara.data.source.local.AppDatabase
import id.ac.istts.kitasetara.data.source.local.TermDao
import id.ac.istts.kitasetara.model.term.Term
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TermsRepository(private val firebaseDatabase: FirebaseDatabase, private val localDatasource : AppDatabase) {

    // Fetch data from Firebase and save it to Room Database
    suspend fun fetchTermsAndSaveToLocalDatabase() {
        firebaseDatabase.getReference("terms").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val terms = mutableListOf<Term>()
                for (snapshot in dataSnapshot.children) {
                    //get term
                    val term = snapshot.child("term").getValue(String::class.java) ?: ""
                    //get meaning
                    val meaning = snapshot.child("meaning").getValue(String::class.java) ?: ""
                    terms.add(Term(snapshot.key!!, term, meaning))
                }
                // Insert words into Room Database
                CoroutineScope(Dispatchers.IO).launch {
                    localDatasource.termDao().insertAll(terms)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    // Get all words from room/local database
    fun getAllTerms(): LiveData<List<Term>> {
        return localDatasource.termDao().getAll()
    }
}
