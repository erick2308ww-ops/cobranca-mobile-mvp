package com.alves.cestabasica.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alves.cestabasica.data.local.entity.Cesta
import kotlinx.coroutines.flow.Flow

@Dao
interface CestaDao {
    @Query("SELECT * FROM cestas ORDER BY nome ASC")
    fun observarTodas(): Flow<List<Cesta>>

    @Query("SELECT * FROM cestas WHERE id = :id")
    suspend fun buscarPorId(id: Long): Cesta?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvar(cesta: Cesta): Long

    @Update
    suspend fun atualizar(cesta: Cesta)

    @Delete
    suspend fun excluir(cesta: Cesta)
}
