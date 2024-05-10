package id.ac.istts.kitasetara.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.istts.kitasetara.KitaSetaraApplication
import id.ac.istts.kitasetara.data.TermsRepository
import id.ac.istts.kitasetara.model.quotes.Quote
import id.ac.istts.kitasetara.model.quotes.QuoteEntity
import id.ac.istts.kitasetara.model.term.Term
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val quotesRepository = KitaSetaraApplication.quotesRepository
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val repository: TermsRepository = KitaSetaraApplication.termsRepository

    val terms: LiveData<List<Term>> = repository.getAllTerms()
    private val _quotes = MutableLiveData<List<QuoteEntity>>()
    val quotes : LiveData<List<QuoteEntity>> get() = _quotes
    init { //fetch automatically from firebase when this class is created
        viewModelScope.launch {
            repository.fetchTermsAndSaveToLocalDatabase()
        }
    }
    fun getQuotes() { //berfungsi untuk refresh/isi data di RV
        ioScope.launch {//panggil getallposts dari repository
            _quotes.postValue(quotesRepository.getQuotesFromLocal())
        }
    }
}