package com.seugrupo.essenciadodia.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.seugrupo.essenciadodia.data.AppDatabase
import com.seugrupo.essenciadodia.data.Ocasiao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OcasiaoViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).ocasiaoDao()

    val ocasioes: StateFlow<List<Ocasiao>> = dao.listar()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // id == 0 significa ocasião nova (Create); senão é edição (Update)
    fun salvar(ocasiao: Ocasiao) {
        viewModelScope.launch {
            if (ocasiao.id == 0) dao.inserir(ocasiao) else dao.atualizar(ocasiao)
        }
    }

    fun excluir(ocasiao: Ocasiao) {
        viewModelScope.launch { dao.excluir(ocasiao) }
    }

    suspend fun buscar(id: Int): Ocasiao? = dao.buscarPorId(id)
}