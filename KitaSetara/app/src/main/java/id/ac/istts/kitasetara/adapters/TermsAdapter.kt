package id.ac.istts.kitasetara.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.model.term.Term

class TermsAdapter(private var terms : List<Term>): RecyclerView.Adapter<TermsAdapter.MyViewHolder>() {
    private var unfilteredTerms = terms
    class MyViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        val tvTerm = row.findViewById<TextView>(R.id.tv_item_terms)
        val tvMeaning = row.findViewById<TextView>(R.id.tv_items_meaning)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_terms,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val term = terms[position]
        holder.tvTerm.text = term.term
        holder.tvMeaning.text = term.meaning
    }

    override fun getItemCount(): Int {
        return terms.size
    }

    fun updateTerms(newTerms: List<Term>) {
        unfilteredTerms = newTerms // Update the unfilteredTerms reference
        terms = newTerms // Also update terms to the newTerms
        notifyDataSetChanged()
    }

    //filter search query
    fun filter(query: String?) {
        terms = if (query.isNullOrEmpty()) {
            // If the query is null or empty, display all items
            unfilteredTerms
        } else {
            // Filter the items based on the query
            unfilteredTerms.filter { term ->
                term.term.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}