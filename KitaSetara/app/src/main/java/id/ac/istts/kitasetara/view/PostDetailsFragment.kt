package id.ac.istts.kitasetara.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
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
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class PostDetailsFragment : Fragment () {
    private val model: DiscussFragmentViewModel by viewModels<DiscussFragmentViewModel>()
    private lateinit var authorUsernameTV:TextView
    private lateinit var postTitleTV:TextView
    private lateinit var postDateTV:TextView
    private lateinit var postContentTV:TextView
    private lateinit var postCommentsRV:RecyclerView
    private lateinit var commentPostEt:EditText
    private lateinit var commentPostBtn:Button
    private lateinit var goBackButton:ImageButton
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
        postDateTV = view.findViewById(R.id.postDateTV)
        postCommentsRV = view.findViewById(R.id.rv_post_comment)
        commentPostEt = view.findViewById(R.id.et_post_comment)
        commentPostBtn = view.findViewById(R.id.btn_send_comment)
        goBackButton = view.findViewById(R.id.gobackBtn)
        authorUsernameTV.text = postAuthor
        postTitleTV.text = postTitle
        postContentTV.text = postContent

        model.selectPost(Post(postID.toString(), "", "", "", "", null, null))
        model.getPostDetails()
        model.selectedPost.observe(viewLifecycleOwner) {

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            //Debugging purposes.
            Log.d("PostDetailsFragment", "Post Title: ${it.title.toString()}")
            Log.d("PostDetailsFragment", "Post Content: ${it.content.toString()}")
            Log.d("PostDetailsFragment", "Post Author: ${it.author.toString()}")
            Log.d("PostDetailsFragment", "Post Date: ${it.createdAt.toString()}")

            postTitle = it.title.toString()
            postContent = it.content.toString()
            postAuthor = it.author.toString()
            postDate = it.createdAt.toString()
            var date = Date()
            if(postAuthor.isNullOrBlank()){
                Log.d("PostDetailsFragment", postDate.toString())
            }
            else{
                date = inputFormat.parse(postDate)!!
            }

            val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            outputFormat.timeZone = TimeZone.getDefault()

            authorUsernameTV.text = postAuthor
            postTitleTV.text = postTitle
            postContentTV.text = postContent
            postDateTV.text = outputFormat.format(date)
        }

        val commentAdapter = CommentAdapter(postComments)
        postCommentsRV.adapter = commentAdapter

        model.comments.observe(viewLifecycleOwner) {
            postComments.clear()
            postComments.addAll(it)
//            Log.d("PostDetailsFragment", "Comments: ${postComments.toString()}")
            mainScope.launch {
                commentAdapter.notifyDataSetChanged()
//                Log.d("PostDetailsFragment", "Comments Changed: ${postComments.toString()}")
            }
        }

        postCommentsRV.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)

        goBackButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_global_discussFragment)
        }

        commentPostBtn.setOnClickListener {
            val inputComment: String = commentPostEt.text.toString()
            if(inputComment.isNotEmpty()){
                mainScope.launch {
                    val authorname: String = auth.currentUser?.displayName ?: Helper.currentUser?.name.toString()
                    model.createComment(newComment(postID.toString(), uid , authorname ,inputComment))
                    postComments.add(Comment(postID.toString(), inputComment, uid, authorname, null))
                    commentPostEt.text.clear()
                    commentAdapter.notifyDataSetChanged()

                    //Refresh the comments
//                    model.getPostDetails()
//                    model.comments.observe(viewLifecycleOwner) { comments ->
//                        postComments.clear()
//                        postComments.addAll(comments)
//                        Log.d("PostDetailsFragment", "Comments Changed: ${postComments.toString()}")
//                        val commentAdapter = CommentAdapter(postComments)
//                        postCommentsRV.adapter = commentAdapter
//                        commentAdapter.notifyDataSetChanged()
//                    }
                }
            }
        }


    }
}