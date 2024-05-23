package id.ac.istts.kitasetara.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.model.chatbot.Message

class ChatbotAdapter(private val messages : ArrayList<Message>) : RecyclerView.Adapter<ChatbotAdapter.MyViewHolder>() {
    private val VIEW_TYPE_YOU = 1
    private val VIEW_TYPE_BOT = 2
    override fun getItemViewType(position: Int): Int {
        return if (messages[position].sender == "bot") VIEW_TYPE_BOT else VIEW_TYPE_YOU
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        return if (viewType == VIEW_TYPE_YOU) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_me, parent, false)
            MyViewHolder(view)
        } else {//bot message
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_bot,parent,false)
            MyViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message = messages[position]
        holder.tvMessage.text = message.msg
    }

    override fun getItemCount(): Int = messages.size

    class MyViewHolder(row: View) : RecyclerView.ViewHolder(row){
        val tvMessage = row.findViewById<TextView>(R.id.tv_msgbot)
    }
}