package id.ac.istts.kitasetara.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.KitaSetaraApplication
import id.ac.istts.kitasetara.data.LeaderboardsRepository
import id.ac.istts.kitasetara.model.highscore.Highscore
import id.ac.istts.kitasetara.model.leaderboard.Leaderboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LeaderboardViewModel: ViewModel() {
    private val leaderboardsRepository:LeaderboardsRepository = KitaSetaraApplication.leaderboardsRepository
    private val _leaderboards = MutableLiveData<List<Leaderboard>>()
    private val _currentUserLeaderboard = MutableLiveData<Leaderboard>(Leaderboard("-", 0, "-", "-", "-"))
    private val _currentUserPosition = MutableLiveData<Int>(0)

    private val ioScope:CoroutineScope = CoroutineScope(Dispatchers.IO)

    val leaderboards:LiveData<List<Leaderboard>>
        get() = _leaderboards

    val currentUserLeaderboard:LiveData<Leaderboard>
        get() = _currentUserLeaderboard

    val currentPosition:LiveData<Int>
        get() = _currentUserPosition

    fun getLeaderboards(){
        ioScope.launch {
            _leaderboards.postValue(leaderboardsRepository.getLeaderboards())
        }
    }

    fun loadLeaderboards(){
       ioScope.launch {
           fetchLeaderboardsData()
       }
    }

    private suspend fun fetchLeaderboardsData(){
        val newData = withContext(Dispatchers.IO) {
            leaderboardsRepository.fetchLeaderboardScoreFromFirebaseAndInsertToRoom()
        }
    }

    fun getCurrentUserLeaderboardDetail(){
        ioScope.launch {
            var temp = leaderboardsRepository.getLeaderboardByUserId(Helper.currentUser!!.id!!)
            if (temp != null){
                _currentUserLeaderboard.postValue(temp)
            }
        }
    }

    fun getPlaceInLeaderboard(leaderboard: List<Leaderboard>){
        for ((idx, item) in leaderboard.withIndex()){
            if(item.userid == Helper.currentUser!!.id){
                _currentUserPosition.postValue(idx+1)
                break
            }
        }
    }

}