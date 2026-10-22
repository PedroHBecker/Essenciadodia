package com.seugrupo.essenciadodia.ui



import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.seugrupo.essenciadodia.data.AppDatabase
import com.seugrupo.essenciadodia.data.Perfume
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PerfumeViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).perfumeDao()

    val perfumes: StateFlow<List<Perfume>> = dao.listar()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // id == 0 significa perfume novo (Create); senão é edição (Update)
    fun salvar(perfume: Perfume) {
        viewModelScope.launch {
            if (perfume.id == 0) dao.inserir(perfume) else dao.atualizar(perfume)
        }
    }

    fun excluir(perfume: Perfume) {
        viewModelScope.launch { dao.excluir(perfume) }
    }

    suspend fun buscar(id: Int): Perfume? = dao.buscarPorId(id)
}