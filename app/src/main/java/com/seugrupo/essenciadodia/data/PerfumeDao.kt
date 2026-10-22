package com.seugrupo.essenciadodia.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PerfumeDao {

    @Insert
    suspend fun inserir(perfume: Perfume)

    @Update
    suspend fun atualizar(perfume: Perfume)

    @Delete
    suspend fun excluir(perfume: Perfume)

    @Query("SELECT * FROM perfumes ORDER BY marca, nome")
    fun listar(): Flow<List<Perfume>>

    @Query("SELECT * FROM perfumes WHERE id = :id")
    suspend fun buscarPorId(id: Int): Perfume?
}