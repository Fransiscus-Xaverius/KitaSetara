package id.ac.istts.kitasetara.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.model.leaderboard.Leaderboard


class LeaderboardsAdapter(
    val data: ArrayList<Leaderboard>
):RecyclerView.Adapter<LeaderboardsAdapter.ViewHolder>() {
    class ViewHolder(val row: View):RecyclerView.ViewHolder(row) {
        val txtPosition = row.findViewById<TextView>(R.id.tvLeaderboardItemPosition)
        val txtName = row.findViewById<TextView>(R.id.tvLeaderboardItemName)
        val txtScore = row.findViewById<TextView>(R.id.tvLeaderboardItemScore)
        val profilePict = row.findViewById<ImageView>(R.id.ivLeaderboardItemProfile)
        val itemLayout = row.findViewById<LinearLayout>(R.id.leaderboardItemLayout)
        val medalIcon = row.findViewById<ImageView>(R.id.ivMedal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        return ViewHolder(itemView.inflate(R.layout.list_leaderboard_item, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val leaderboard = data[position]

        holder.txtPosition.text = (position+1).toString()
        holder.txtName.text = leaderboard.name
        holder.txtScore.text = leaderboard.score.toString()

        if (leaderboard.photoUrl != null && leaderboard.photoUrl != ""){
            // Load profile picture using Picasso
            Picasso.get()
                .load(leaderboard.photoUrl) // url
                .placeholder(R.drawable.baseline_person_24) // Optional: Placeholder image while loading
                .error(R.drawable.default_profile) // Optional: Error image if loading fails
                .into(holder.profilePict)
        }else{
            holder.profilePict.setImageResource(R.drawable.default_profile)
        }

        holder.medalIcon.visibility = View.GONE
        holder.txtPosition.visibility = View.VISIBLE

        //set place position in leaderboard
        if(position+1 == 1){//1st place
            holder.itemLayout.setBackgroundResource(R.drawable.background_leaderboard1st)
            holder.medalIcon.visibility = View.VISIBLE
            holder.medalIcon.setImageResource(R.drawable.icon_1st)
            holder.txtPosition.visibility = View.GONE
//            holder.itemLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.leaderboardGold))
        }else if(position+1 == 2){//2nd place
            holder.itemLayout.setBackgroundResource(R.drawable.background_leaderboard2nd)
            holder.medalIcon.visibility = View.VISIBLE
            holder.medalIcon.setImageResource(R.drawable.icon_2nd)
            holder.txtPosition.visibility = View.GONE
        }else if(position+1 == 3){//3rd place
            holder.itemLayout.setBackgroundResource(R.drawable.background_leaderboard3nd)
            holder.medalIcon.visibility = View.VISIBLE
            holder.medalIcon.setImageResource(R.drawable.icon_3rd)
            holder.txtPosition.visibility = View.GONE
        }else{//4th place and so on
            holder.itemLayout.setBackgroundResource(R.drawable.background_leaderboardother)
        }


    }
}