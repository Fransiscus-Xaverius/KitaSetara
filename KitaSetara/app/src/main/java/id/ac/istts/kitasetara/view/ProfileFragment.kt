package id.ac.istts.kitasetara.view

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import id.ac.istts.kitasetara.BuildConfig
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.databinding.FragmentProfileBinding
import java.util.UUID

class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dialog : Dialog
    private val storage = FirebaseStorage.getInstance() //get firebase storage instance
    private val firebaseDatabase = FirebaseDatabase.getInstance() //get firebase db instance
    private val auth = Firebase.auth
    private val user = auth.currentUser //get current user that has logged in with google
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //init auth and gso
        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.CLIENT_ID)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    //launcher for gallery intent
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia() //with contracts, there will be a return of Uri
    ) { uri : Uri? ->
        if (uri != null){
            //display image to imageview
            binding.profileImage.setImageURI(uri)
            //save to firebase
            saveToFirebaseStorage(uri)
        }
    }

    private fun saveToFirebaseStorage(uri:Uri){
        val imageName = UUID.randomUUID().toString() //random name using UUID for image name
        val storageRef = storage.reference.child("images/$imageName")
        binding.progressBar.visibility = View.VISIBLE
        Toast.makeText(requireActivity(),"Updating...(May take a few seconds)",Toast.LENGTH_LONG).show()
        storageRef.putFile(uri)
            .addOnSuccessListener { _ ->
                // Image uploaded successfully, get the URL of the uploaded image to be stored in realtime DB
                storageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                    // Image URL retrieved, now save it to Firebase realtime DB
                    binding.progressBar.visibility = View.GONE
                    saveToDatabase(imageUrl.toString())

                }.addOnFailureListener { _ ->
                    // Handle unexpected error
                    Toast.makeText(requireActivity(),"ERROR OCCURED",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { _ ->
                //handle unexpected error
                Toast.makeText(requireActivity(),"ERROR OCCURED",Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveToDatabase(imageUrl : String){
        //show loading indicator while trying to save new image

        //get current user id
        var uid  = ""

        uid = if (Helper.currentUser != null){
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
                Toast.makeText(requireActivity(),"Profile successfully updated!",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { _ ->
                // Handle any errors
                Toast.makeText(requireActivity(),"Error when saving to database!",Toast.LENGTH_SHORT).show()
            }
    }
    private fun startGallery(){
        //access gallery without needing to request permission beforehand, image only
        launcherIntentGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //handle onclick
        //setup dialog box
        dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.custom_logout_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)
        dialog.setCancelable(false) //the dialog wont close if user click anywhere outside the box

        val btnLogout = dialog.findViewById<Button>(R.id.btn_dialog_logout)
        val btnCancel = dialog.findViewById<Button>(R.id.btn_dialog_cancel)

        btnLogout.setOnClickListener {
            if (Helper.currentUser != null) {//sign out
                Helper.currentUser = null
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            } else {//sign out for user that has logged in with google
                signOutAndStartSignInActivity()
            }
            Toast.makeText(requireActivity(),"Logout success!",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        if (user != null) {//display user name if log in with google
            val userName = user.displayName
            binding.txtUsernameProfile.text = "Welcome, " + userName
            binding.tvEmail.text = user.email
            binding.tvName.text = userName
            binding.tvUsername.text = userName
            val uri = user.photoUrl
            Picasso.get().load(uri).into(binding.profileImage, object : Callback {
                override fun onSuccess() {
                    // Image loaded successfully, hide loading indicator
                    binding.progressBar.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    // Error loading image, hide loading indicator
                    binding.progressBar.visibility = View.GONE
                }
            })
        } else {
            // user has logged in using other method
            binding.txtUsernameProfile.text = "Welcome, ${Helper.currentUser!!.name}"
            binding.tvUsername.text = Helper.currentUser!!.username
            binding.tvEmail.text = Helper.currentUser!!.email
            binding.tvName.text = Helper.currentUser!!.name
            binding.progressBar.visibility = View.GONE
            // check whether user has a profile picture
            if (Helper.currentUser!!.imageUrl != ""){
                //load imageUrl stored in firebase by using Glide
                binding.progressBar.visibility = View.VISIBLE
                Glide.with(requireContext())
                    .load(Helper.currentUser!!.imageUrl)
                    .error(R.drawable.default_profile)
                    .listener(object : RequestListener<Drawable> {
                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            // Hide loading indicator if loading fails
                            binding.progressBar.visibility = View.GONE
                            return false
                        }
                    })
                    .into(binding.profileImage)

            }
        }


        binding.profileImage.setOnClickListener{
            //using photopicker to access gallery images
            // check whether user logged in with google or not
            // if user logged in with google, he/she cant change the profile pic
            if (Helper.currentUser!= null){
                startGallery()
            }
        }
        binding.tvLogout.setOnClickListener {
            dialog.show()
        }
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bottom_home-> {
                    findNavController().navigate(R.id.action_global_homeFragment)
                    true
                }
                R.id.bottom_course-> {
                    findNavController().navigate(R.id.action_global_coursesFragment)
                    true
                }
                R.id.bottom_leaderboard-> {
                    findNavController().navigate(R.id.action_global_leaderboardFragment)
                    true
                }
                R.id.bottom_discuss-> {
                    findNavController().navigate(R.id.action_global_discussFragment)
                    true
                }
                R.id.bottom_profile-> {
                    findNavController().navigate(R.id.action_global_profileFragment)
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.menu.findItem(R.id.bottom_profile).isChecked = true
    }

    private fun signOutAndStartSignInActivity() {
        mAuth.signOut()

        mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
            // redirect to login page
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }
}