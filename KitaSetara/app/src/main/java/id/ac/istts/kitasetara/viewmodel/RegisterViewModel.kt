package id.ac.istts.kitasetara.viewmodel

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.model.forum.User

class RegisterViewModel : ViewModel() {
    fun signupUser(view: View, username: String, name : String, password: String, email: String,databaseReference: DatabaseReference,context: Context) {
        databaseReference.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        //there is no user with given username yet in the DB
                        val id = databaseReference.push().key //create unique id for new user
                        val userData = User(id, username,name ,password, email, false)
                        //child() is filled with id!! because the id will always be unique
                        databaseReference.child(id!!).setValue(userData)
                        Toast.makeText(context, "Register success!!", Toast.LENGTH_SHORT).show()
                        view.findNavController()
                            .navigate(R.id.action_registerFragment_to_loginFragment)
                    } else {
                        Toast.makeText(context, "Username already exists!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Database Error : ${error.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }
}