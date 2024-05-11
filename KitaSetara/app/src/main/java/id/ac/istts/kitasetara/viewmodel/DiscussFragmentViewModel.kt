package id.ac.istts.kitasetara.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ac.istts.kitasetara.model.forum.Comment
import id.ac.istts.kitasetara.model.forum.Post
import id.ac.istts.kitasetara.model.forum.newComment
import id.ac.istts.kitasetara.services.API
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiscussFragmentViewModel : ViewModel() {
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private var _selectedPost = MutableLiveData<Post>()
    private var _comments = MutableLiveData<List<Comment>>()

    val selectedPost: MutableLiveData<Post>
        get() = _selectedPost

    val comments: MutableLiveData<List<Comment>>
        get() = _comments

    fun selectPost(post: Post) {
        _selectedPost.value = post
    }

    fun getPostDetails(){
        try {
            ioScope.launch {
                _selectedPost.value?.id_post?.let {
                    _selectedPost.postValue(API.retrofitService.getPostById(it))
                    _comments.postValue(API.retrofitService.getAllComments(it))
                }
            }
        }
        catch (e: Exception){
            Log.e("DiscussFragmentViewModel", "getPostDetails: ${e.message}")
        }
    }

    fun createComment(comment: newComment) {
        ioScope.launch {
            withContext(Dispatchers.IO) {
                API.retrofitService.createComment(comment)
            }
        }
    }

//    fun getPostDetails(){
//        Log.d("DiscussFragmentViewModel", "getPostDetails: ${_selectedPost.value?.id}")
//        ioScope.launch {
//            _selectedPost.postValue(_selectedPost.value?.id?.let { API.retrofitService.getPostById(it) })
//            _comments.postValue(_selectedPost.value?.id?.let {
//                _selectedPost.value!!.id?.let { it1 ->
//                    API.retrofitService.getAllComments(
//                        it1
//                    )
//                }
//            })
//        }
//    }

}