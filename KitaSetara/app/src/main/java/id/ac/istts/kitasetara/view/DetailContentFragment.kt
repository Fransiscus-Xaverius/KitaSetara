package id.ac.istts.kitasetara.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
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

        val contentObserver:Observer<Content> = Observer{
            binding.tvContentTitle.text = it.name
            binding.tvContent.text = it.content
            binding.tvContentTitle2.text = it.name
        }
        viewModel.content.observe(viewLifecycleOwner, contentObserver)

        viewModel.getContent(idContent)

        binding.btnContentPrev.setOnClickListener {
//            if(currentContentNumber > 1){//cek jika bukan content page yang pertama
//                val action = DetailContentFragmentDirections.actionDetailContentFragmentSelf(idContent.toString(), currentContentNumber, maxContentNumber)
//                findNavController().navigate(action)
//            }
        }

        binding.btnContentPrev.setOnClickListener {
//            if(currentContentNumber < maxContentNumber){//cek jika bukan content page yang terakhir
//                val action = DetailContentFragmentDirections.actionDetailContentFragmentSelf(idContent.toString(), currentContentNumber, maxContentNumber)
//                findNavController().navigate(action)
//            }else{
//
//            }
        }

        binding.ivContentExit.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }


}