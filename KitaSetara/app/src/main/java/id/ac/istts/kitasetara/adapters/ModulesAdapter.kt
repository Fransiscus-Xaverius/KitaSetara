package id.ac.istts.kitasetara.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.model.course.FinishedModule
import id.ac.istts.kitasetara.model.course.Module

class ModulesAdapter(
    val data:ArrayList<Module>,
    val finishedData:ArrayList<FinishedModule>,
    val onClickListener:((Module, Int)->Unit)?=null
):RecyclerView.Adapter<ModulesAdapter.ViewHolder>() {
    class ViewHolder(val row:View):RecyclerView.ViewHolder(row){
        val txtNumber:TextView = row.findViewById(R.id.tvModuleItemNumber)
        val txtName:TextView = row.findViewById(R.id.tvModuleItemName)
        val itemLayout:ConstraintLayout = row.findViewById(R.id.moduleItemLayout)

        val statusIcon:ImageView = row.findViewById(R.id.ivModuleItemIconStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        return ViewHolder(itemView.inflate(R.layout.list_module_item, parent, false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val module = data[position]
        holder.txtName.text = module.name
        holder.txtNumber.text = "Module "+(position+1)

        //Display icon status for modules that unlocked and locked
        //Module that is unlocked is the module that has it's previous module mark as finished
        if(position == 0){//first module always unlocked but can be finished or not finished
            if(moduleFinished(position)){//finished module status
                holder.statusIcon.setImageResource(R.drawable.check)
            }else{
                holder.statusIcon.setImageResource(R.drawable.circle_play_icon)
            }
        }else if(moduleFinished(position-1)){//if previous module finished, this module unlocked
            if(moduleFinished(position)){//finished module status
                holder.statusIcon.setImageResource(R.drawable.check)
            }else{
                holder.statusIcon.setImageResource(R.drawable.circle_play_icon)
            }
        }else{
            holder.statusIcon.setImageResource(R.drawable.locked_icon)
        }

        holder.itemLayout.setOnClickListener {
            onClickListener?.invoke(module, holder.adapterPosition+1)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    fun moduleFinished(dataPosition: Int):Boolean{
        for (item in finishedData){
            if(data[dataPosition].id.toString() == item.idModule && Helper.currentUser!!.username == item.username){
                return true
            }
        }
        return false
    }
}