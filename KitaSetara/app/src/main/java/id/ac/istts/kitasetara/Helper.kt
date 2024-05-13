package id.ac.istts.kitasetara

import id.ac.istts.kitasetara.model.forum.User
import id.ac.istts.kitasetara.model.quiz.Question

class Helper {
    companion object{
        var currentUser : User?= null
        val questions = arrayListOf<Question>()
    }
}