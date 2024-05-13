package id.ac.istts.kitasetara.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.databinding.FragmentQuizBinding
import id.ac.istts.kitasetara.model.quiz.Question
import id.ac.istts.kitasetara.viewmodel.QuizViewModel

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private val viewModel: QuizViewModel by viewModels<QuizViewModel>()
    private var score = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun resetOptions(opt: TextView) {
            opt.setBackgroundColor(Color.WHITE)
            opt.setBackgroundResource(R.drawable.rounded_corner)
            opt.setTextColor(Color.BLACK)
            opt.tag = "false"
        }


        binding.ivQuizBack.setOnClickListener {
            //go back to course page
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.tvQuizScore.text = "Score : $score"

        viewModel.questionNumber.observe(viewLifecycleOwner) {
            binding.tvQuizNumber.text = "$it of 10"
        }
        viewModel.startIndex.observe(viewLifecycleOwner) {
//            viewModel.fetchQuestions() //fetch data from ROOM
            val questions = viewModel.getQuestions()
            if (questions.isNotEmpty()) {
                binding.tvQuizQuestion.text = questions[it].question
                binding.tvFirstAns.text = questions[it].option1
                binding.tvSecondAns.text = questions[it].option2
                binding.tvThirdAns.text = questions[it].option3
                binding.tvFourthAns.text = questions[it].option4

                //reset previously selected textview's bgcolor
                if (binding.tvFirstAns.tag == "true") {//selected
                    resetOptions(binding.tvFirstAns)
                } else if (binding.tvSecondAns.tag == "true") {
                    resetOptions(binding.tvSecondAns)
                } else if (binding.tvThirdAns.tag == "true") {
                    resetOptions(binding.tvThirdAns)
                } else if (binding.tvFourthAns.tag == "true") {
                    resetOptions(binding.tvFourthAns)
                }
            }
        }
        binding.btnQuizNext.setOnClickListener {
            val startIndex = viewModel.getIndex()
            if (binding.btnQuizNext.text == "Finish"){
                //check last answer before finishing
                binding.root.forEach { view ->
                    if (view is TextView && view.tag == "true") {
                        //cek jawaban benar
                        if (view.text == viewModel.getQuestions().get(startIndex ).answer) {
                            score += 5
                            //update score UI
                            binding.tvQuizScore.text = "Score : $score"
                        }
                    }
                }
            }

            if (startIndex < viewModel.getQuestions().size - 1) {
                //check answer
                binding.root.forEach { view ->
                    if (view is TextView && view.tag == "true") {
                        //cek jawaban benar
                        if (view.text == viewModel.getQuestions().get(startIndex ).answer) {
                            score += 5
                            //update score UI
                            binding.tvQuizScore.text = "Score : $score"
                        }
                    }
                }
                viewModel.setIndex() //update question index
                viewModel.updateQNumber() //update question number
                if (viewModel.getIndex() == viewModel.getQuestions().size - 1){
                    binding.btnQuizNext.text = "Finish"
                    binding.btnQuizNext.setBackgroundColor(Color.RED)
                }

            } else {
                // Handle case when there are no more questions
                //show alert dialog
                val alertDialog = AlertDialog.Builder(requireActivity())
                alertDialog.apply {
                    setTitle("Congratulations")
                    setMessage("Your score is $score")
                    setCancelable(false)
                    setNeutralButton("OK"){dialog,_->
                        dialog.dismiss()
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                }
                alertDialog.show()
            }
        }
        
        binding.tvFirstAns.setOnClickListener {
            if (binding.tvFirstAns.tag == "false") {
                //this option is yet to be selected
                binding.tvFirstAns.setBackgroundColor(resources.getColor(R.color.orange_base))
                binding.tvFirstAns.setTextColor(Color.WHITE)
                binding.tvFirstAns.tag = "true"
                //reset other answers to default setting
                resetOptions(binding.tvSecondAns)
                resetOptions(binding.tvThirdAns)
                resetOptions(binding.tvFourthAns)
            } else {
                //reset to default color
                resetOptions(binding.tvFirstAns)
            }
        }

        binding.tvSecondAns.setOnClickListener {
            if (binding.tvSecondAns.tag == "false") {
                //this option is yet to be selected
                binding.tvSecondAns.setBackgroundColor(resources.getColor(R.color.orange_base))
                binding.tvSecondAns.setTextColor(Color.WHITE)
                binding.tvSecondAns.tag = "true"

                //reset other answers to default setting
                resetOptions(binding.tvFirstAns)
                resetOptions(binding.tvThirdAns)
                resetOptions(binding.tvFourthAns)
            } else {
                //reset to default color
                resetOptions(binding.tvSecondAns)
            }
        }

        binding.tvThirdAns.setOnClickListener {
            if (binding.tvThirdAns.tag == "false") {
                //this option is yet to be selected
                binding.tvThirdAns.setBackgroundColor(resources.getColor(R.color.orange_base))
                binding.tvThirdAns.setTextColor(Color.WHITE)
                binding.tvThirdAns.tag = "true"
                //reset other answers to default setting
                resetOptions(binding.tvSecondAns)
                resetOptions(binding.tvFirstAns)
                resetOptions(binding.tvFourthAns)
            } else {
                //reset to default color
                resetOptions(binding.tvThirdAns)
            }
        }

        binding.tvFourthAns.setOnClickListener {
            if (binding.tvFourthAns.tag == "false") {
                //this option is yet to be selected
                binding.tvFourthAns.setBackgroundColor(resources.getColor(R.color.orange_base))
                binding.tvFourthAns.setTextColor(Color.WHITE)
                binding.tvFourthAns.tag = "true"
                //reset other answers to default setting
                resetOptions(binding.tvSecondAns)
                resetOptions(binding.tvThirdAns)
                resetOptions(binding.tvFirstAns)
            } else {
                //reset to default color
                resetOptions(binding.tvFourthAns)
            }
        }
    }


}