package id.ac.istts.kitasetara.viewmodel

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.model.forum.User
import id.ac.istts.kitasetara.pref.UserPreference
import id.ac.istts.kitasetara.pref.dataStore
import kotlinx.coroutines.runBlocking

class LoginViewModel : ViewModel() {

    fun loginUser(view: View, username : String, password:String, firebaseDatabase: FirebaseDatabase, databaseReference: DatabaseReference, context: Context){
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val userData = userSnapshot.getValue(User::class.java)

                        if (userData != null && userData.password == password){
                            //login successful
                            //set currentuser
                            Helper.currentUser = userData
                            //save session
                            val pref = UserPreference.getInstance(context.dataStore)
                            runBlocking { pref.saveSession(userData) }
                            Helper.showSnackbar(view, "Login successful!")
                            userSnapshot.ref.child("loggedIn").setValue(true)
                            //redirect to home fragment
                            view.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        }else Helper.showSnackbar(view, "Username or password is incorrect!")

                    }
                }else{
                    Helper.showSnackbar(view, "Username or password is incorrect!")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Helper.showSnackbar(view, "Database Error : ${error.message}")
            }
        })
    }
}