package id.ac.istts.kitasetara.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.databinding.FragmentHomeBinding
import id.ac.istts.kitasetara.viewmodel.HomeViewModel


class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel : HomeViewModel by viewModels<HomeViewModel>()
//    private val termViewModel : TermViewModel by viewModels<TermViewModel>()

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
        binding.progressBarQuote.visibility = View.VISIBLE
        //still not success
        viewModel.quote.observe(viewLifecycleOwner){
            if (it.isSuccess){
                val quote = it.getOrNull()
                binding.tvQuotetext.text = quote!!.quoteText
                binding.tvAuthor.text = quote.quoteAuthor
                binding.progressBarQuote.visibility = View.GONE
            }
        }
        viewModel.getQuotes()
        viewModel.terms.observe(viewLifecycleOwner){
            //update ui with random term from list of terms
            val randomTerm = it.randomOrNull()
            if (randomTerm != null){
                binding.tvTerms.text = randomTerm.term
                binding.tvTermMeaning.text = randomTerm.meaning
            }else{
                binding.tvTerms.text = getString(R.string.no_terms)
            }
        }


        //handle onclick
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bottom_home-> {
                    view.findNavController().navigate(R.id.action_global_homeFragment)
                    true
                }
                R.id.bottom_course-> {
                    view.findNavController().navigate(R.id.action_global_coursesFragment)
                    true
                }
                R.id.bottom_leaderboard-> {
                    view.findNavController().navigate(R.id.action_global_leaderboardFragment)
                    true
                }
                R.id.bottom_discuss-> {
                    view.findNavController().navigate(R.id.action_global_discussFragment)
                    true
                }
                R.id.bottom_profile-> {
                    view.findNavController().navigate(R.id.action_global_profileFragment)
                    true
                }
                else -> false
            }
        }

        //handle sign out

    }
}