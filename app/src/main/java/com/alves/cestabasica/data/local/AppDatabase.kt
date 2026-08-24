package com.alves.cestabasica.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alves.cestabasica.data.local.dao.CestaDao
import com.alves.cestabasica.data.local.dao.ClienteDao
import com.alves.cestabasica.data.local.dao.ParcelaDao
import com.alves.cestabasica.data.local.dao.VendaDao
import com.alves.cestabasica.data.local.entity.Cesta
import com.alves.cestabasica.data.local.entity.Cliente
import com.alves.cestabasica.data.local.entity.Parcela
import com.alves.cestabasica.data.local.entity.Venda

@Database(
    entities = [Cliente::class, Cesta::class, Venda::class, Parcela::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun clienteDao(): ClienteDao
    abstract fun cestaDao(): CestaDao
    abstract fun vendaDao(): VendaDao
    abstract fun parcelaDao(): ParcelaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cesta_basica_alves.db"
                ).build().also { INSTANCE = it }
            }
    }
}
