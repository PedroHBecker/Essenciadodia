package com.seugrupo.essenciadodia.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "perfumes")
data class Perfume(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val marca: String,
    val familiaOlfativa: String,
    val intensidade: Int,   // 1 = leve, 2 = moderada, 3 = forte
    val tempMin: Int,       // temperatura mínima ideal (°C)
    val tempMax: Int        // temperatura máxima ideal (°C)
)