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
        val titleTv: TextView = row.findViewById(R.id.title_tv_post)
        val authorTv:TextView = row.findViewById(R.id.author_tv_post)
        val lastComment:TextView = row.findViewById(R.id.last_comment_tv_post)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postAdapter.ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.post_list_item, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: postAdapter.ViewHolder, position: Int) {
        val post = data[position]
        holder.titleTv.text = post.title
        holder.authorTv.text = post.author
//        holder.lastComment.text = post.date.toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }


}