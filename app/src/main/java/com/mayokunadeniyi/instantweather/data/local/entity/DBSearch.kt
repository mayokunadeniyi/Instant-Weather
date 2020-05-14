package com.mayokunadeniyi.instantweather.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by haydar on 2020-05-13.
 */
@Entity(tableName = "search_table", indices = [Index(value = ["name"], unique = true)])
data class DBSearch(
    @ColumnInfo(name = "unique_id")
    @PrimaryKey(autoGenerate = true)
    var uId: Int = 0,

    @ColumnInfo
    val name: String,
    val country: String
)