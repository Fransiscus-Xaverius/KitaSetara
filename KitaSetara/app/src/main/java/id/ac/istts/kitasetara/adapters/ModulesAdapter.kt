package id.ac.istts.kitasetara.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.model.course.Module

class ModulesAdapter(
    val data:ArrayList<Module>,
    val onClickListener:((Module)->Unit)?=null
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

        holder.itemLayout.setOnClickListener {
            onClickListener?.invoke(module)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}