package id.ac.istts.kitasetara.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ac.istts.kitasetara.BuildConfig
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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