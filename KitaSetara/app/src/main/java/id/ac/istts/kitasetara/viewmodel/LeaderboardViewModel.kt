package id.ac.istts.kitasetara.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ac.istts.kitasetara.KitaSetaraApplication
import id.ac.istts.kitasetara.data.LeaderboardsRepository
import id.ac.istts.kitasetara.model.leaderboard.Leaderboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LeaderboardViewModel: ViewModel() {
    private val leaderboardsRepository:LeaderboardsRepository = KitaSetaraApplication.leaderboardsRepository
    private val _leaderboards = MutableLiveData<List<Leaderboard>>()
    private val ioScope:CoroutineScope = CoroutineScope(Dispatchers.IO)

    val leaderboards:LiveData<List<Leaderboard>>
        get() = _leaderboards

    fun getLeaderboards(){
        ioScope.launch {
            _leaderboards.postValue(leaderboardsRepository.getLeaderboards())
        }
    }

    fun loadLeaderboards(){
        ioScope.launch {
            leaderboardsRepository.fetchLeaderboardScoreFromFirebaseAndInsertToRoom()
        }
    }

}