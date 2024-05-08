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
import id.ac.istts.kitasetara.MarginItemDecoration
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.adapters.ModulesAdapter
import id.ac.istts.kitasetara.databinding.FragmentDetailCourseBinding
import id.ac.istts.kitasetara.model.course.Course
import id.ac.istts.kitasetara.model.course.Module
import id.ac.istts.kitasetara.viewModel.DetailCourseFragmentViewModel


class DetailCourseFragment : Fragment() {
    private var _binding:FragmentDetailCourseBinding? = null
    private val binding get() = _binding!!

    val viewModel:DetailCourseFragmentViewModel by viewModels<DetailCourseFragmentViewModel>()
    val navArgs:DetailCourseFragmentArgs by navArgs<DetailCourseFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idCourse = navArgs.idCourse.toInt()
//        Log.d("ID", idCourse.toString())

        viewModel.getCourse(idCourse)

//        val selectedCourse = viewModel.course
//        Log.d("course name", selectedCourse.value!!.name)

        val courseObserver: Observer<Course> = Observer{
            binding.txtCourseTitle.text = it.name
            binding.txtCourseDescription.text = it.description
        }
        viewModel.course.observe(viewLifecycleOwner, courseObserver)

        var modules = ArrayList<Module>()

        val modulesAdapter = ModulesAdapter(modules){
            module ->

        }

        val modulesObserver:Observer<List<Module>> = Observer{
            modules.clear()
            modules.addAll(it)
            modulesAdapter.notifyDataSetChanged()
        }
        viewModel.modules.observe(viewLifecycleOwner, modulesObserver)
        viewModel.getCourseModules(idCourse)

        binding.rvModule.adapter = modulesAdapter
        binding.rvModule.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvModule.addItemDecoration(MarginItemDecoration(20))

        binding.ibBackDetailCourse.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }

        //handle onclick
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bottom_home-> {
                    findNavController().navigate(R.id.action_global_homeFragment)
                    true
                }
                R.id.bottom_course-> {
                    findNavController().navigate(R.id.action_global_coursesFragment)
                    true
                }
                R.id.bottom_leaderboard-> {
                    findNavController().navigate(R.id.action_global_leaderboardFragment)
                    true
                }
                R.id.bottom_discuss-> {
                    findNavController().navigate(R.id.action_global_discussFragment)
                    true
                }
                R.id.bottom_profile-> {
                    findNavController().navigate(R.id.action_global_profileFragment)
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.menu.findItem(R.id.bottom_course).isChecked = true
    }
}