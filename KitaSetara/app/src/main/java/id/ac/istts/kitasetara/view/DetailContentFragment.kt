package id.ac.istts.kitasetara.view

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.databinding.FragmentDetailContentBinding
import id.ac.istts.kitasetara.model.course.Content
import id.ac.istts.kitasetara.viewmodel.DetailContentViewModel

class DetailContentFragment : Fragment() {
    private var _binding :FragmentDetailContentBinding?=null
    private val binding get() = _binding!!

    val viewModel: DetailContentViewModel by viewModels<DetailContentViewModel>()
    val navArgs:DetailContentFragmentArgs by navArgs<DetailContentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idContent = navArgs.idContent.toInt()
        val currentContentNumber = navArgs.currentContentNumber;
        val maxContentNumber = navArgs.maxContentNumber
        val contentList = navArgs.contentsList

        val contentObserver:Observer<Content> = Observer{
            binding.tvContentTitle.text = it.name
            binding.tvContent.text = it.content
            binding.tvContentTitle2.text = it.name
        }
        viewModel.content.observe(viewLifecycleOwner, contentObserver)

        viewModel.getContent(idContent)

        if(currentContentNumber == 1){//first content
            binding.btnContentPrev.visibility = View.INVISIBLE
        }else if(currentContentNumber == maxContentNumber){
            binding.btnContentNext.text = "Finish"
            binding.btnContentNext.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.circle_play_icon))
        }

        binding.btnContentPrev.setOnClickListener {
//            Log.d("TEST", "Hello")
//            Log.d("current", currentContentNumber.toString())
            if(currentContentNumber > 1){//cek jika bukan content page yang pertama
                val prevNum = currentContentNumber-1
                val targetId = contentList[prevNum-1].id //-1 untuk menyesuaikan index
//                Log.d("Target", targetId.toString())
                val action = DetailContentFragmentDirections.actionDetailContentFragmentSelf(targetId.toString(), currentContentNumber-1, maxContentNumber, contentList)
                findNavController().navigate(action)
            }
        }

        binding.btnContentNext.setOnClickListener {
            if(currentContentNumber < maxContentNumber){//cek jika bukan content page yang terakhir
                val nextNum = currentContentNumber+1
                val targetId = contentList[nextNum-1].id
                val action = DetailContentFragmentDirections.actionDetailContentFragmentSelf(targetId.toString(), currentContentNumber+1, maxContentNumber, contentList)
                findNavController().navigate(action)
            }else{

            }
        }

        binding.ivContentExit.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }


}