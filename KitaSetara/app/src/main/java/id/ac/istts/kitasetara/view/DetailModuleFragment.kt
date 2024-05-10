package id.ac.istts.kitasetara.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.istts.kitasetara.adapters.ContentsAdapter
import id.ac.istts.kitasetara.databinding.FragmentDetailModuleBinding
import id.ac.istts.kitasetara.model.course.Content
import id.ac.istts.kitasetara.model.course.Module
import id.ac.istts.kitasetara.viewmodel.DetailModuleViewModel


class DetailModuleFragment : Fragment() {
    private var _binding: FragmentDetailModuleBinding? = null
    private val binding get() = _binding!!

    val navArgs:DetailModuleFragmentArgs by navArgs<DetailModuleFragmentArgs>()
    val viewModel:DetailModuleViewModel by viewModels<DetailModuleViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailModuleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idModule = navArgs.idModule.toInt()
        val courseName = navArgs.courseName
        val moduleNumber = navArgs.moduleNumber
        val moduleContents = ArrayList<Content>()
        val contentsAdapter = ContentsAdapter(moduleContents){
            content ->

        }

        viewModel.getModule(idModule)
        val moduleObserver:Observer<Module> = Observer {
            binding.tvModuleCourseName.text = courseName
            binding.tvModuleName.text = "Module ${moduleNumber} | ${it.name}"
            binding.txtModuleDescription.text = it.description
        }
        viewModel.module.observe(viewLifecycleOwner, moduleObserver)

        val contentsObserver:Observer<List<Content>> = Observer {
            moduleContents.clear()
            moduleContents.addAll(it)
            contentsAdapter.notifyDataSetChanged()
        }
        viewModel.contents.observe(viewLifecycleOwner, contentsObserver)
        viewModel.getModuleContents(idModule)


        binding.rvContent.adapter = contentsAdapter
        binding.rvContent.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.ibBackDetailModule.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

}