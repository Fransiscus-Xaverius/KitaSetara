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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.adapters.CommentAdapter
import id.ac.istts.kitasetara.model.forum.Comment
import id.ac.istts.kitasetara.model.forum.Post
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

        authorUsernameTV = view.findViewById(R.id.authorUsernameTv)
        postTitleTV = view.findViewById(R.id.postTitleTv)
        postContentTV = view.findViewById(R.id.postContentTv)
        postCommentsRV = view.findViewById(R.id.postCommentsRV)
        commentPostEt = view.findViewById(R.id.commentPostEt)
        commentPostBtn = view.findViewById(R.id.sendCommentBtn)
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
            Log.d("PostDetailsFragment", "Post Date: ${it.date.toString()}")

            postTitle = it.title.toString()
            postContent = it.content.toString()
            postAuthor = it.author.toString()
            postDate = it.date.toString()

            authorUsernameTV.text = postAuthor
            postTitleTV.text = postTitle
            postContentTV.text = postContent

        }

        model.comments.observe(viewLifecycleOwner) {
            postComments = it as? ArrayList<Comment> ?: ArrayList()
            Log.d("PostDetailsFragment", "Comments: ${postComments.toString()}")
            mainScope.launch {
                val commentAdapter = CommentAdapter(postComments)
                postCommentsRV.adapter = commentAdapter
                commentAdapter.notifyDataSetChanged()
            }
        }


        postCommentsRV.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)


        commentPostBtn.setOnClickListener{
//            val inputComment: String = commentPostEt.text.toString()
//            if(inputComment.isNotEmpty()){
//                model.createComment(Comment("", postID.toString(), inputComment, "", ""))
//            }
        }

    }
}