package com.seugrupo.essenciadodia.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.seugrupo.essenciadodia.data.AppDatabase
import com.seugrupo.essenciadodia.data.ClimaService
import com.seugrupo.essenciadodia.data.Ocasiao
import com.seugrupo.essenciadodia.data.Perfume
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.abs

data class Sugestao(val perfume: Perfume, val compatibilidade: Int)

// Lógica de sugestão: quanto mais perto da temperatura e da intensidade ideal, maior a nota
fun calcularSugestoes(
    perfumes: List<Perfume>,
    ocasiao: Ocasiao,
    temperatura: Double
): List<Sugestao> {
    return perfumes.map { p ->
        val distanciaTemp = when {
            temperatura < p.tempMin -> p.tempMin - temperatura
            temperatura > p.tempMax -> temperatura - p.tempMax
            else -> 0.0
        }
        val diferencaIntensidade = abs(p.intensidade - ocasiao.intensidadeIdeal)
        val pontos = (100 - distanciaTemp * 5 - diferencaIntensidade * 25).coerceIn(0.0, 100.0)
        Sugestao(p, pontos.toInt())
    }.sortedByDescending { it.compatibilidade }
}

class SugestaoViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)

    val perfumes: StateFlow<List<Perfume>> = db.perfumeDao().listar()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val ocasioes: StateFlow<List<Ocasiao>> = db.ocasiaoDao().listar()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _temperatura = MutableStateFlow<Double?>(null)
    val temperatura: StateFlow<Double?> = _temperatura.asStateFlow()

    private val _carregando = MutableStateFlow(false)
    val carregando: StateFlow<Boolean> = _carregando.asStateFlow()

    private val _erro = MutableStateFlow<String?>(null)
    val erro: StateFlow<String?> = _erro.asStateFlow()

    private val _ocasiaoSelecionadaId = MutableStateFlow<Int?>(null)
    val ocasiaoSelecionadaId: StateFlow<Int?> = _ocasiaoSelecionadaId.asStateFlow()

    init {
        carregarClima()
    }

    fun selecionarOcasiao(id: Int) {
        _ocasiaoSelecionadaId.value = id
    }

    // Coordenadas de Curitiba
    fun carregarClima() {
        viewModelScope.launch {
            _carregando.value = true
            _erro.value = null
            try {
                val resposta = ClimaService.api.temperaturaAtual(
                    latitude = -25.4284,
                    longitude = -49.2733,
                    campos = "temperature_2m"
                )
                _temperatura.value = resposta.atual.temperatura
            } catch (e: Exception) {
                _erro.value = "Não foi possível obter a temperatura. Verifique a conexão."
            } finally {
                _carregando.value = false
            }
        }
    }
}