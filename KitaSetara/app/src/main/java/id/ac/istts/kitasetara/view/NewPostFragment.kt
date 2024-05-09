package id.ac.istts.kitasetara.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.databinding.FragmentNewPostBinding
import id.ac.istts.kitasetara.model.forum.newPost
import id.ac.istts.kitasetara.services.API
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPostFragment : Fragment() {
    private var _binding : FragmentNewPostBinding? = null

    private val binding get() = _binding!!
    private lateinit var postTitle: EditText
    private lateinit var postContent:EditText
    private lateinit var postBtn: Button
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNewPostBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postTitle = binding.postTitleEt
        postContent = binding.postContentMT
        postBtn = binding.postButton
        auth = FirebaseAuth.getInstance()
        var author = auth.currentUser?.displayName.toString() //this returns NULL. Needs to be fixed later -Frans
        if(author.isNullOrBlank()){
            author = Helper.currentUser?.name.toString()
        }
        postBtn.setOnClickListener {
            Toast.makeText(context, "Creating post... ${author}", Toast.LENGTH_SHORT).show()
            if(postTitle.text.toString().isNotEmpty() && postContent.text.toString().isNotEmpty()){
                ioScope.launch {
                    try {
                        //TODO: get author name from user data
                        val post = newPost(postTitle.text.toString(), postContent.text.toString(), author)
                        val response = API.retrofitService.createPost(post)
                        withContext(Dispatchers.Main) {
                            //Reset the form, and move to another intent
                            Toast.makeText(context, "Post created successfully", Toast.LENGTH_SHORT).show()
                            postTitle.text.clear()
                            postContent.text.clear()
                            val action = NewPostFragmentDirections.actionGlobalPostDetailsFragment()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Error creating post", Toast.LENGTH_SHORT).show()
                        }
                        Log.d("NewPostFragment_ERROR", "Error creating post: ${e.message}")
                    }
                }
            }
            else{ //if input is empty
                Toast.makeText(context, "All fields needs to be filled!", Toast.LENGTH_SHORT).show()
            }

        }

        super.onViewCreated(view, savedInstanceState)
    }
}