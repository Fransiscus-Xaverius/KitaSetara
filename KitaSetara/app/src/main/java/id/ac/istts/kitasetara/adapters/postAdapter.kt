package id.ac.istts.kitasetara.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import androidx.recyclerview.widget.RecyclerView
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.model.forum.Post
import id.ac.istts.kitasetara.view.MainActivity
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class postAdapter(
    val data: ArrayList<Post>,
    private val context: Context
): RecyclerView.Adapter<postAdapter.ViewHolder>(){

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class ViewHolder(val row: View): RecyclerView.ViewHolder(row), View.OnClickListener {
        val titleTv: TextView = row.findViewById(R.id.postTItleTV)
        val authorTv: TextView = row.findViewById(R.id.postAuthorTV)
        val lastCommentTv: TextView = row.findViewById(R.id.lastCommentTV)
        val commentCountTv: TextView = row.findViewById(R.id.commentAmountTV)
        val postedDateTv: TextView = row.findViewById(R.id.postedDateTV)
        val lastCommentAuthorTv: TextView = row.findViewById(R.id.lastCommentAuthorTV)

        init {
            row.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val post = data[position]
                Log.d("postAdapter", "Post: ${post.toString()}")
                Log.d("postAdapter", "Post ID: ${post.id_post}")
                MainActivity.postID = post.id_post
                val action = R.id.action_global_postDetailsFragment
                Navigation.findNavController(v!!).navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.list_discuss, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = data[position]
        Log.d("COMMENTCOUNT",post.amountOfComments.toString())
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        holder.titleTv.text = post.title
        holder.authorTv.text = post.author
        holder.lastCommentTv.text = post.lastComment?.comment
        holder.lastCommentAuthorTv.text = post.lastComment?.username
        holder.commentCountTv.text = post.amountOfComments?.toString()
        var tempDate = inputFormat.parse(post.createdAt)
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        holder.postedDateTv.text = outputFormat.format(tempDate)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
