package id.ac.istts.kitasetara.viewModel

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
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
import java.io.IOException

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

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getPostDetails() {
        ioScope.launch {
            try {
                _selectedPost.value?.id_post?.let { postId ->
                    val post = API.retrofitService.getPostById(postId)
                    val comments = API.retrofitService.getAllComments(postId)
                    _selectedPost.postValue(post)
                    _comments.postValue(comments)
                }
            } catch (e: IOException) {
                // Handle IO exceptions (e.g., network issues)
                Log.e("DiscussFragmentViewModel", "IOException: ${e.message}")
            } catch (e: HttpException) {
                // Handle HTTP exceptions (e.g., 404 Not Found, 500 Internal Server Error)
                Log.e("DiscussFragmentViewModel", "HttpException: ${e.message}")
            } catch (e: Exception) {
                // Handle other exceptions
                Log.e("DiscussFragmentViewModel", "Exception: ${e.message}")
            }
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