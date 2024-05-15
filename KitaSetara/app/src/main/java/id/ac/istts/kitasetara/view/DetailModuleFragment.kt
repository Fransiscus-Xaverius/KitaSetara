package id.ac.istts.kitasetara.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.KitaSetaraApplication
import id.ac.istts.kitasetara.adapters.ContentsAdapter
import id.ac.istts.kitasetara.data.DefaultCoursesRepository
import id.ac.istts.kitasetara.databinding.FragmentDetailModuleBinding
import id.ac.istts.kitasetara.model.course.Content
import id.ac.istts.kitasetara.model.course.FinishedContent
import id.ac.istts.kitasetara.model.course.Module
import id.ac.istts.kitasetara.viewmodel.DetailModuleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
        val finishedContents = ArrayList<FinishedContent>()

        val contentsAdapter = ContentsAdapter(moduleContents, finishedContents){
            content,pos,max ->
            var open = false
            for (item in finishedContents){
                if(content.id.toString() == item.idContent && Helper.currentUser!!.username == item.username){
                    open = true
                }
            }
            if(open || pos == 1){
                val action = DetailModuleFragmentDirections.actionDetailModuleFragmentToDetailContentFragment(content.id.toString(), pos, max, moduleContents.toTypedArray(), idModule.toString())
                findNavController().navigate(action)
            }else{
                Snackbar.make(requireContext(), view, "Please Complete Previous Content First", Snackbar.LENGTH_SHORT).show()
            }
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

        //load content progress
        val coursesRepository:DefaultCoursesRepository = KitaSetaraApplication.coursesRepository
        binding.spinner.visibility = View.VISIBLE
        binding.rvContent.visibility = View.INVISIBLE
        CoroutineScope(Dispatchers.Main).launch {
            coursesRepository.fetchFinishedContentDataFromFirebaseAndInsertToRoom()
            // Fetch data from Room after insertion is complete
            val newData = withContext(Dispatchers.IO) {
                coursesRepository.getFinishedContents()
            }
//            Log.d("Finished",  newData.toString())
            finishedContents.clear()
            finishedContents.addAll(newData)
            contentsAdapter.notifyDataSetChanged()
//            Log.d("finishedcontents", finishedContents.toString())

            // Hide spinner
            binding.spinner.visibility = View.GONE
            binding.rvContent.visibility = View.VISIBLE
        }


        binding.ibBackDetailModule.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

}