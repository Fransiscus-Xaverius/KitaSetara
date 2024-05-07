package id.ac.istts.kitasetara.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.model.forum.Post

class postAdapter(
    val data:ArrayList<Post>
): RecyclerView.Adapter<postAdapter.ViewHolder>(){

    class ViewHolder(val row: View):RecyclerView.ViewHolder(row){
        val titleTv: TextView = row.findViewById(R.id.postTItleTV)
        val authorTv:TextView = row.findViewById(R.id.postAuthorTV)
        val lastCommentTv:TextView = row.findViewById(R.id.lastCommentTV)
        val commentCountTv:TextView = row.findViewById(R.id.commentAmountTV)
        val postedDateTv:TextView = row.findViewById(R.id.postedDateTV)
        val lastCommentAuthorTv:TextView = row.findViewById(R.id.lastCommentAuthorTV)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postAdapter.ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.list_discuss, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: postAdapter.ViewHolder, position: Int) {
        val post = data[position]
        holder.titleTv.text = post.title
        holder.authorTv.text = post.author
        holder.lastCommentTv.text = post.lastComment?.comment
        holder.lastCommentAuthorTv.text = post.lastComment?.username
    }

    override fun getItemCount(): Int {
        return data.size
    }


}