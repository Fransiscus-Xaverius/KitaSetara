package id.ac.istts.kitasetara.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.adapters.postAdapter
import id.ac.istts.kitasetara.databinding.FragmentDiscussBinding
import id.ac.istts.kitasetara.model.forum.Post
import id.ac.istts.kitasetara.services.API
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class DiscussFragment : Fragment() {
    private var _binding : FragmentDiscussBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchBar:SearchView
    private lateinit var sortBtn:ImageButton
    private lateinit var discussRV: RecyclerView
    private lateinit var composeBtn: LinearLayout
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val originalPosts = ArrayList<Post>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDiscussBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //handle discussion onclick
        searchBar = binding.searchViewDiscuss
        sortBtn = binding.sortDiscussBtn
        discussRV = binding.discussRV
        composeBtn = binding.ComposeBtn
        val moshi = Moshi.Builder()
            .add(Date::class.java, object : JsonAdapter<Date>() {
                @FromJson
                override fun fromJson(reader: JsonReader): Date? {
                    return if (reader.peek() != JsonReader.Token.NULL) {
                        Date(reader.nextLong())
                    } else {
                        reader.nextNull<Any>()
                        null
                    }
                }

                @ToJson
                override fun toJson(writer: JsonWriter, value: Date?) {
                    value?.let { writer.value(it.time) }
                }
            })
            .build()

        val postJsonAdapter: JsonAdapter<Post> = moshi.adapter(Post::class.java)

        discussRV.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)

        val posts = ArrayList<Post>()
        val postAdapter = postAdapter(posts, requireContext())

        ioScope.launch { //access ke API
            val tempPosts = API.retrofitService.getAllPosts()
            mainScope.launch { //untuk update tampilan
                posts.clear()
                posts.addAll(tempPosts)
                originalPosts.clear()
                originalPosts.addAll(tempPosts)
                postAdapter.notifyDataSetChanged()
            }
        }



        discussRV.adapter = postAdapter

        composeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_global_newPostFragment)
        }

        sortBtn.setOnClickListener{

        }

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    val filteredPosts = originalPosts.filter { post ->
                        post.title?.contains(query, ignoreCase = true) ?: false
                    }
                    mainScope.launch { // Update UI
                        posts.clear()
                        posts.addAll(filteredPosts)
                        postAdapter.notifyDataSetChanged()
                    }
                } else {
                    mainScope.launch { // Update UI
                        posts.clear()
                        posts.addAll(originalPosts)
                        postAdapter.notifyDataSetChanged()
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // You can implement live search here if you want
                return false
            }
        })

        //handle bottom menu onclick
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
        binding.bottomNavigation.menu.findItem(R.id.bottom_discuss).isChecked = true
    }
}