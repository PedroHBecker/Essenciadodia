package com.seugrupo.essenciadodia.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OcasiaoDao {

    @Insert
    suspend fun inserir(ocasiao: Ocasiao)

    @Update
    suspend fun atualizar(ocasiao: Ocasiao)

    @Delete
    suspend fun excluir(ocasiao: Ocasiao)

    @Query("SELECT * FROM ocasioes ORDER BY nome")
    fun listar(): Flow<List<Ocasiao>>

    @Query("SELECT * FROM ocasioes WHERE id = :id")
    suspend fun buscarPorId(id: Int): Ocasiao?
}