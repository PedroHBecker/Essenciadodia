package com.seugrupo.essenciadodia.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ocasioes")
data class Ocasiao(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val intensidadeIdeal: Int   // 1 = leve, 2 = moderada, 3 = forte
)