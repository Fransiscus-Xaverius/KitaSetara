package id.ac.istts.kitasetara.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.adapters.CoursesAdapter
import id.ac.istts.kitasetara.databinding.FragmentCoursesBinding
import id.ac.istts.kitasetara.model.course.Course
import id.ac.istts.kitasetara.viewmodel.CoursesFragmentViewModel


class CoursesFragment : Fragment() {
    private var _binding : FragmentCoursesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CoursesFragmentViewModel by viewModels<CoursesFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val courses = ArrayList<Course>()

        val courseAdapter= CoursesAdapter(courses){
            course ->
            val action = CoursesFragmentDirections.actionCoursesFragmentToDetailCourseFragment(course.id.toString())
            findNavController().navigate(action)
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