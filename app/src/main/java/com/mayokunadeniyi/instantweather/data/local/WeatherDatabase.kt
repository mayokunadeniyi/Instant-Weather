package com.mayokunadeniyi.instantweather.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mayokunadeniyi.instantweather.data.local.dao.WeatherDao
import com.mayokunadeniyi.instantweather.data.local.entity.DBWeather
import com.mayokunadeniyi.instantweather.data.local.entity.DBWeatherForecast
import com.mayokunadeniyi.instantweather.utils.typeconverters.*

/**
 * Created by Mayokun Adeniyi on 2020-01-27.
 */

@Database(
    entities = [DBWeather::class, DBWeatherForecast::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(
    ListNetworkWeatherDescriptionConverter::class,
    NetworkWeatherConditionConverter::class,
    WindConverter::class,
    CityConverter::class,
    SysConverter::class
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract val weatherDao: WeatherDao

    companion object {
        @Volatile
        private var instance: WeatherDatabase? = null

        /**
         * This checks if there is an existing instance of the [WeatherDatabase] in the
         * specified [context] and creates one if there isn't or else, it returns the
         * already existing instance. This function ensures that the [WeatherDatabase] is
         * accessed at any instance by a single thread.
         */
        fun getInstance(context: Context): WeatherDatabase {
            synchronized(this) {
                var _instance = instance
                if (_instance == null) {
                    _instance = Room.databaseBuilder(
                        context.applicationContext,
                        WeatherDatabase::class.java,
                        "weather_database"
                    )
                        .fallbackToDestructiveMigration()
                        .addMigrations(MIGRATION_1_2)
                        .build()
                    instance = _instance
                }
                return _instance
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE weather_table ADD COLUMN sys TEXT DEFAULT ''")
            }
        }
    }

}