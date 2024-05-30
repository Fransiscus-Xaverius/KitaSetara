package id.ac.istts.kitasetara.viewmodel

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.view.ResetPasswordFragmentDirections

class PasswordViewModel : ViewModel() {
    private val database : FirebaseDatabase = FirebaseDatabase.getInstance() //to access database
    private val usersRef: DatabaseReference =database.getReference("users") //get all data from users table
    fun validateEmail(email:String, context: Context,fragment: Fragment){
        val query = usersRef.orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // User exists
                    //pass email as a navargs to NewPasswordFragment
                    val action = ResetPasswordFragmentDirections.actionResetPasswordFragmentToNewPasswordFragment(email)
                    fragment.findNavController().navigate(action)
                } else {
                    // User with given email does not exist
                    Toast.makeText(context,"Email not registered!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun updatePassword(email:String,fragment: Fragment,view: View,pass:String){
        //update user's password
        val query = usersRef.orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val userId = snapshot.children.first().key //get user id
                    userId?.let {
                        val userRef = usersRef.child(it) //user node
                        userRef.child("password").setValue(pass)
                            .addOnSuccessListener {
                                // password updated successfully
                                Snackbar.make(view,"Password has been reset successfully", Snackbar.LENGTH_SHORT).show()
                                //redirect to login page
                                fragment.findNavController().navigate(R.id.action_global_loginFragment)
                            }
                            .addOnFailureListener {
                                // Failed to update password
                                Snackbar.make(view,"Password reset failed!", Snackbar.LENGTH_SHORT).show()
                            }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Snackbar.make(view,"Oops.. Something wrong happened", Snackbar.LENGTH_SHORT).show()
            }

        })
    }
}