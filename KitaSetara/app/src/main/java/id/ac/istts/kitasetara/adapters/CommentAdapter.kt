package id.ac.istts.kitasetara.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.model.forum.Comment

class CommentAdapter (
    val data:ArrayList<Comment>
): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    class ViewHolder(val row: View):RecyclerView.ViewHolder(row){
        val commentAuthorTV: TextView = row.findViewById(R.id.commentAuthorTV)
        val commentContentTV:TextView = row.findViewById(R.id.commentContentTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.list_comment_item, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = data[position]
        holder.commentAuthorTV.text = comment.author
        holder.commentContentTV.text = comment.comment
    }

}