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
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.adapters.LeaderboardsAdapter
import id.ac.istts.kitasetara.databinding.FragmentLeaderboardBinding
import id.ac.istts.kitasetara.model.leaderboard.Leaderboard
import id.ac.istts.kitasetara.viewmodel.LeaderboardViewModel

class LeaderboardFragment : Fragment() {

    private var _binding : FragmentLeaderboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel:LeaderboardViewModel by viewModels<LeaderboardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val leaderboards = ArrayList<Leaderboard>()
        val leaderboardsAdapter = LeaderboardsAdapter(leaderboards)

        //initial value of user leaderboard
        binding.tvLeaderboardName.text = Helper.currentUser!!.name
        binding.tvLeaderboardScore.text = "0"
        binding.tvLeaderBoardPosition.text = "-"

        //observe all leaderboard data
        val leaderboardsObserver: Observer<List<Leaderboard>> = Observer{
            leaderboards.clear()
            leaderboards.addAll(it)
//            Log.d("leaderboards", it.toString())
            leaderboardsAdapter.notifyDataSetChanged()
            viewModel.getPlaceInLeaderboard(leaderboards)
        }
        viewModel.leaderboards.observe(viewLifecycleOwner, leaderboardsObserver)
        viewModel.loadLeaderboards()
        viewModel.getLeaderboards()

        binding.rvLeaderboard.adapter = leaderboardsAdapter
        binding.rvLeaderboard.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        //observer user leaderboard
        val userPositionLeaderboardObserver: Observer<Leaderboard> = Observer{
            //show current user's score in leaderboard
            if(it.id == "-"){//user hasn't played quiz yet
                binding.tvLeaderboardName.text = Helper.currentUser!!.name
                binding.tvLeaderboardScore.text = "0"
                binding.tvLeaderBoardPosition.text = "-"
            }else{
                binding.tvLeaderboardName.text = it.name
                binding.tvLeaderboardScore.text = it.score.toString()
            }

            // Load profile picture using Picasso
            if(Helper.currentUser!!.imageUrl != null && Helper.currentUser!!.imageUrl != ""){
//                Log.d("tes", Helper.currentUser!!.imageUrl.toString())
                Picasso.get()
                    .load(Helper.currentUser!!.imageUrl) // url
                    .placeholder(R.drawable.baseline_person_24) // Optional: Placeholder image while loading
                    .error(R.drawable.default_profile) // Optional: Error image if loading fails
                    .into(binding.ivLeaderboardProfile)
            }else{
                binding.ivLeaderboardProfile.setImageResource(R.drawable.default_profile)
            }

        }
        viewModel.currentUserLeaderboard.observe(viewLifecycleOwner, userPositionLeaderboardObserver)
        viewModel.getCurrentUserLeaderboardDetail()

        //observe user current position in leaderboard
        val userPositionObserver:Observer<Int> = Observer{
            if(it != 0){
                binding.tvLeaderBoardPosition.text = it.toString()
            }else{
                binding.tvLeaderBoardPosition.text = "-"
            }
        }
        viewModel.currentPosition.observe(viewLifecycleOwner, userPositionObserver)

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
        binding.bottomNavigation.menu.findItem(R.id.bottom_leaderboard).isChecked = true
    }
}