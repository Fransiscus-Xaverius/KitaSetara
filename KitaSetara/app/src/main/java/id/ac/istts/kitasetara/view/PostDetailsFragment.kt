package id.ac.istts.kitasetara.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.adapters.CommentAdapter
import id.ac.istts.kitasetara.model.forum.Comment
import id.ac.istts.kitasetara.model.forum.Post
import id.ac.istts.kitasetara.model.forum.newComment
import id.ac.istts.kitasetara.viewmodel.DiscussFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PostDetailsFragment : Fragment() {
    private val model: DiscussFragmentViewModel by viewModels<DiscussFragmentViewModel>()

    private lateinit var authorUsernameTV:TextView
    private lateinit var postTitleTV:TextView
    private lateinit var postContentTV:TextView
    private lateinit var postCommentsRV:RecyclerView
    private lateinit var commentPostEt:EditText
    private lateinit var commentPostBtn:Button
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val postID = MainActivity.postID
//        Log.d("PostDetailsFragment", model.selectedPost.value.toString())
        var postTitle = ""
        var postContent = ""
        var postAuthor = ""
        var postDate = ""
        var postComments = ArrayList<Comment>()

        auth = FirebaseAuth.getInstance()
        var author = auth.currentUser?.displayName //this returns NULL. Needs to be fixed later -Frans
        var uid = auth.currentUser?.uid.toString()
        if(author.isNullOrBlank()){
            author = Helper.currentUser?.name.toString()
            uid = Helper.currentUser?.id.toString()
            Toast.makeText(context, "Author: ${author}", Toast.LENGTH_SHORT).show()
        }else{
            author = author.toString() //safe call for this because type is nullable
        }

        authorUsernameTV = view.findViewById(R.id.authorUsernameTv)
        postTitleTV = view.findViewById(R.id.postTitleTv)
        postContentTV = view.findViewById(R.id.postContentTv)
        postCommentsRV = view.findViewById(R.id.rv_post_comment)
        commentPostEt = view.findViewById(R.id.et_post_comment)
        commentPostBtn = view.findViewById(R.id.btn_send_comment)
        authorUsernameTV.text = postAuthor
        postTitleTV.text = postTitle
        postContentTV.text = postContent


        model.selectPost(Post(postID.toString(), "", "", "", "", null, null))
        model.getPostDetails()
        model.selectedPost.observe(viewLifecycleOwner) {

            //Debugging purposes.
            Log.d("PostDetailsFragment", "Post Title: ${it.title.toString()}")
            Log.d("PostDetailsFragment", "Post Content: ${it.content.toString()}")
            Log.d("PostDetailsFragment", "Post Author: ${it.author.toString()}")
            Log.d("PostDetailsFragment", "Post Date: ${it.createdAt.toString()}")

            postTitle = it.title.toString()
            postContent = it.content.toString()
            postAuthor = it.author.toString()
            postDate = it.createdAt.toString()

            authorUsernameTV.text = postAuthor
            postTitleTV.text = postTitle
            postContentTV.text = postContent

        }

        model.comments.observe(viewLifecycleOwner) {
            postComments.clear()
            postComments.addAll(it)
//            Log.d("PostDetailsFragment", "Comments: ${postComments.toString()}")
            mainScope.launch {
                val commentAdapter = CommentAdapter(postComments)
                postCommentsRV.adapter = commentAdapter
                commentAdapter.notifyDataSetChanged()
//                Log.d("PostDetailsFragment", "Comments Changed: ${postComments.toString()}")
            }
        }


        postCommentsRV.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)


        commentPostBtn.setOnClickListener {
            val inputComment: String = commentPostEt.text.toString()
            if(inputComment.isNotEmpty()){
                mainScope.launch {
                    model.createComment(newComment(postID.toString(), uid ,inputComment))
                    commentPostEt.text.clear()
                    //Refresh the comments
                    model.getPostDetails()
                    model.comments.observe(viewLifecycleOwner) { comments ->
                        postComments.clear()
                        postComments.addAll(comments)
                        Log.d("PostDetailsFragment", "Comments Changed: ${postComments.toString()}")
                        val commentAdapter = CommentAdapter(postComments)
                        postCommentsRV.adapter = commentAdapter
                        commentAdapter.notifyDataSetChanged()
                    }
                }
            }
        }


    }
}