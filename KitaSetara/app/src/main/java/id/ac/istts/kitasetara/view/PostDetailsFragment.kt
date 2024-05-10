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
import androidx.recyclerview.widget.RecyclerView
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.model.forum.Comment
import id.ac.istts.kitasetara.viewmodel.DiscussFragmentViewModel


class PostDetailsFragment : Fragment() {
    private val model: DiscussFragmentViewModel by viewModels<DiscussFragmentViewModel>()

    private lateinit var authorUsernameTV:TextView
    private lateinit var postTitleTV:TextView
    private lateinit var postContentTV:TextView
    private lateinit var postCommentsTV:RecyclerView
    private lateinit var commentPostEt:EditText
    private lateinit var commentPostBtn:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //ISSUE -> VIEWMODEL DOES NOT SEND DATA TO THIS FRAGMENT FROM NEWPOSTFRAGMENT. -Frans
        Log.d("PostDetailsFragment", model.selectedPost.value.toString())
        model.getPostDetails()
        var postTitle = ""
        var postContent = ""
        var postAuthor = ""
        var postDate = ""
        var postComments = ArrayList<Comment>()
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
        }
        model.getPostDetails()
        model.comments.observe(viewLifecycleOwner) {
            postComments = it as? ArrayList<Comment> ?: ArrayList()
        }

        authorUsernameTV = view.findViewById(R.id.authorUsernameTv)
        postTitleTV = view.findViewById(R.id.postTitleTv)
        postContentTV = view.findViewById(R.id.postContentTv)
        postCommentsTV = view.findViewById(R.id.postCommentsRV)
        commentPostEt = view.findViewById(R.id.commentPostEt)
        commentPostBtn = view.findViewById(R.id.sendCommentBtn)



        authorUsernameTV.text = postAuthor
        postTitleTV.text = postTitle
        postContentTV.text = postContent
//        postCommentsTV.adapter = commentAdapter(postComments)


    }
}