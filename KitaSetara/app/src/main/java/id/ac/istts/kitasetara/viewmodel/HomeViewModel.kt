package id.ac.istts.kitasetara.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ac.istts.kitasetara.KitaSetaraApplication
import id.ac.istts.kitasetara.model.quotes.Quote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val quotesRepository = KitaSetaraApplication.quotesRepository
    private val _quote = MutableLiveData<Quote>()
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    //getter
    val quote: LiveData<Quote>
        get() = _quote

    fun getQuote() { //berfungsi untuk refresh/isi data di RV
        ioScope.launch {//panggil getallposts dari repository
            _quote.postValue(quotesRepository.getSingleQuote())
        }
    }
}