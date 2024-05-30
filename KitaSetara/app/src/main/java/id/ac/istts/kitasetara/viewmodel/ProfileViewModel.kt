package id.ac.istts.kitasetara.viewmodel

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.databinding.FragmentProfileBinding
import java.util.UUID

class ProfileViewModel : ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance() //get firebase db instance
    private val storage = FirebaseStorage.getInstance() //get firebase storage instance

    fun saveToFirebaseStorage(uri: Uri, context: Context, binding: FragmentProfileBinding,user: FirebaseUser?){
        val imageName = UUID.randomUUID().toString() //random name using UUID for image name
        val storageRef = storage.reference.child("images/$imageName")
        binding.progressBar.visibility = View.VISIBLE
        Toast.makeText(context,"Updating...(May take a few seconds)", Toast.LENGTH_LONG).show()
        storageRef.putFile(uri)
            .addOnSuccessListener { _ ->
                // Image uploaded successfully, get the URL of the uploaded image to be stored in realtime DB
                storageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                    // Image URL retrieved, now save it to Firebase realtime DB
                    binding.progressBar.visibility = View.GONE
                    saveToDatabase(imageUrl.toString(),context,user)

                }.addOnFailureListener { _ ->
                    // Handle unexpected error
                    Toast.makeText(context,"ERROR OCCURED", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { _ ->
                //handle unexpected error
                Toast.makeText(context,"ERROR OCCURED", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveToDatabase(imageUrl : String,context: Context,user: FirebaseUser?){
        //show loading indicator while trying to save new image

        //get current user id
        val uid = if (Helper.currentUser != null){
            Helper.currentUser!!.id!!
        }else{
            //login with google
            user!!.uid
        }
        // Reference to the "users" node in the Realtime Database
        val usersRef = firebaseDatabase.reference.child("users")
        // Reference to the specific user's node using their user ID
        val userRef = usersRef.child(uid)
        // Update the "imageUrl" attribute of the user node with the new image URL
        userRef.child("imageUrl").setValue(imageUrl)
            .addOnSuccessListener {
                // Image URL updated in Realtime Database successfully
                Toast.makeText(context,"Profile successfully updated!",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { _ ->
                // Handle any errors
                Toast.makeText(context,"Error when saving to database!",Toast.LENGTH_SHORT).show()
            }
    }
}