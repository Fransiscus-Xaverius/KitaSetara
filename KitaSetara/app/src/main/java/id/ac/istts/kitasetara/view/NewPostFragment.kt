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

        postBtn.setOnClickListener {
            if(postTitle.text.toString().isNotEmpty() && postContent.text.toString().isNotEmpty()){
                ioScope.launch {
                    try {
                        //TODO: get author name from user data
                        val post = newPost(postTitle.text.toString(), postContent.text.toString(), "authorName")
                        val response = API.retrofitService.createPost(post)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Post created successfully", Toast.LENGTH_SHORT).show()
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