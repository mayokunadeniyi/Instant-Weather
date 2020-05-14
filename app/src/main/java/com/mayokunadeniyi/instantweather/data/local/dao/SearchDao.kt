package com.mayokunadeniyi.instantweather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mayokunadeniyi.instantweather.data.local.entity.DBSearch

/**
 * Created by haydar on 2020-05-13.
 */
@Dao
interface SearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(vararg searchDB: DBSearch)

    @Query("SELECT * FROM search_table ORDER BY unique_id DESC")
    suspend fun selectSearch(): List<DBSearch>


}



