package id.ac.istts.kitasetara.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.model.course.Course

class CoursesAdapter(
    val data:ArrayList<Course>,
    val onClickListener:((Course)->Unit)? = null,
):RecyclerView.Adapter<CoursesAdapter.ViewHolder>() {
    class ViewHolder(val row: View):RecyclerView.ViewHolder(row){
        val txtCourseName: TextView = row.findViewById(R.id.txtNameCourses)
        val txtCourseDetail: TextView = row.findViewById(R.id.txtDetailCourses)
        val courseItem: LinearLayout = row.findViewById(R.id.coursesItemLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(parent.context)
        return ViewHolder(itemView.inflate(R.layout.list_courses_courses, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val course = data[position]
        holder.txtCourseName.text = course.name
        holder.txtCourseDetail.text = course.description

        holder.courseItem.setOnClickListener{
            onClickListener?.invoke(course)
        }
    }

    override fun getItemCount(): Int {
        return  data.size
    }

}