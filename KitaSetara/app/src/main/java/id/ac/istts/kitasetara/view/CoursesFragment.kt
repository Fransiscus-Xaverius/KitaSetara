package id.ac.istts.kitasetara.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.adapters.CoursesAdapter
import id.ac.istts.kitasetara.databinding.FragmentCoursesBinding
import id.ac.istts.kitasetara.model.course.Course
import id.ac.istts.kitasetara.viewModel.CoursesFragmentViewModel


class CoursesFragment : Fragment() {
    private var _binding : FragmentCoursesBinding? = null
    private val binding get() = _binding!!

    val viewModel:CoursesFragmentViewModel by viewModels<CoursesFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val courses = ArrayList<Course>()

        val courseAdapter= CoursesAdapter(courses){
            course ->
            Toast.makeText(requireContext(), "Will be redirected to ${course.name}'s detail", Toast.LENGTH_SHORT).show()
        }


        val coursesObserver:Observer<List<Course>> = Observer{
            courses.clear()
            courses.addAll(it)
            courseAdapter.notifyDataSetChanged()
        }
        viewModel.courses.observe(viewLifecycleOwner, coursesObserver)
        viewModel.getCourses()

        binding.rvCoursesCourses.adapter = courseAdapter
        binding.rvCoursesCourses.layoutManager = GridLayoutManager(requireContext(), 2)


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