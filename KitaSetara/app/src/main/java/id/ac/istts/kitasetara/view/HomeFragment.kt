package id.ac.istts.kitasetara.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.databinding.FragmentHomeBinding
import id.ac.istts.kitasetara.model.course.Course
import id.ac.istts.kitasetara.viewmodel.CoursesFragmentViewModel
import id.ac.istts.kitasetara.viewmodel.HomeViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels<HomeViewModel>()
    val courseViewModel: CoursesFragmentViewModel by viewModels<CoursesFragmentViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //daily quotes from ROOM
        viewModel.getQuotes()
        viewModel.quotes.observe(viewLifecycleOwner) {
            val quote = it.randomOrNull() //get one random quote to be displayed
            binding.tvQuotetext.text = quote!!.quoteText
            binding.tvAuthor.text = quote.quoteAuthor
        }

        //daily terms
        viewModel.terms.observe(viewLifecycleOwner) {
            //update ui with random term from list of terms
            val randomTerm = it.randomOrNull()
            if (randomTerm != null) {
                binding.tvTerms.text = randomTerm.term
                binding.tvTermMeaning.text = randomTerm.meaning
            } else {
                binding.tvTerms.text = getString(R.string.no_terms)
            }
        }

        //recent course
        val recentCourses = arrayListOf<Course>()
        courseViewModel.getCourses() //fill courses data with latest update
        courseViewModel.courses.observe(viewLifecycleOwner) {
            recentCourses.addAll(it.takeLast(2))  //fetch 2 last published courses
            if (recentCourses.isNotEmpty()) {
                binding.tvRecentcourse1.text = recentCourses[0].name
                binding.tvDesc1.text = recentCourses[0].description
                binding.tvRecentcourse2.text = recentCourses[1].name
                binding.tvDesc2.text = recentCourses[1].description
            }
        }

        binding.clRecent1.setOnClickListener {
            if (recentCourses.isNotEmpty()){
                val action = HomeFragmentDirections.actionGlobalDetailCourseFragment2(recentCourses[0].id.toString())
                findNavController().navigate(action)
            }
        }
        binding.clRecent2.setOnClickListener {
            if (recentCourses.isNotEmpty()){
                val action = HomeFragmentDirections.actionGlobalDetailCourseFragment2(recentCourses[1].id.toString())
                findNavController().navigate(action)
            }
        }

        //handle onclick of bottomnav
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_home -> {
                    view.findNavController().navigate(R.id.action_global_homeFragment)
                    true
                }

                R.id.bottom_course -> {
                    view.findNavController().navigate(R.id.action_global_coursesFragment)
                    true
                }

                R.id.bottom_leaderboard -> {
                    view.findNavController().navigate(R.id.action_global_leaderboardFragment)
                    true
                }

                R.id.bottom_discuss -> {
                    view.findNavController().navigate(R.id.action_global_discussFragment)
                    true
                }

                R.id.bottom_profile -> {
                    view.findNavController().navigate(R.id.action_global_profileFragment)
                    true
                }

                else -> false
            }
        }

    }
}