package id.ac.istts.kitasetara.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.adapters.TermsAdapter
import id.ac.istts.kitasetara.databinding.FragmentTermsBinding
import id.ac.istts.kitasetara.viewmodel.HomeViewModel

class TermsFragment : Fragment() {
    private var _binding : FragmentTermsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels<HomeViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTermsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchviewTerms.clearFocus() //remove cursor from searchview initially

        binding.ivBackTerms.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        //setup rv layout and adapter
        binding.rvTerms.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = TermsAdapter(emptyList())
        binding.rvTerms.adapter = adapter
        viewModel.terms.observe(viewLifecycleOwner){
            adapter.updateTerms(it)
        }

        //handle query on searchbar
        binding.searchviewTerms.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText)
                return true
            }

        })

    }
}