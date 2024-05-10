package id.ac.istts.kitasetara.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.model.course.Content

class ContentsAdapter(
    val data:ArrayList<Content>,
    val onClickListener: ((Content, Int, Int)->Unit)?=null,
):RecyclerView.Adapter<ContentsAdapter.ViewHolder>(){
    class ViewHolder(val row:View):RecyclerView.ViewHolder(row) {
        val txtTitle:TextView = row.findViewById(R.id.tvContentItemName)
        val icon:ImageView = row.findViewById(R.id.ivContentItemIconStatus)
        val itemLayout:ConstraintLayout = row.findViewById(R.id.contentItemLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(parent.context)
        return ViewHolder(itemView.inflate(R.layout.list_content_item, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val content = data[position]
        holder.txtTitle.text = content.name
        if(position == 0){
            holder.icon.setImageResource(R.drawable.book_open_icon)
        }else{
            holder.icon.setImageResource(R.drawable.locked_icon)
        }

        holder.itemLayout.setOnClickListener {
            onClickListener?.invoke(content, holder.adapterPosition, data.size)
        }

    }

}