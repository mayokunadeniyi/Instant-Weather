package com.mayokunadeniyi.instantweather.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.mayokunadeniyi.instantweather.MainCoroutineRule
import com.mayokunadeniyi.instantweather.data.source.local.entity.DBWeather
import com.mayokunadeniyi.instantweather.data.source.local.entity.DBWeatherForecast
import com.mayokunadeniyi.instantweather.fakeDbWeatherEntity
import com.mayokunadeniyi.instantweather.fakeDbWeatherForecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Optional.empty

/**
 * Created by Mayokun Adeniyi on 03/08/2020.
 */
@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class WeatherLocalDataSourceTest {

//region constants

    //endregion constants

    //region helper fields
    private lateinit var database: WeatherDatabase
    //endregion helper fields

    private lateinit var systemUnderTest: WeatherLocalDataSourceImpl

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).allowMainThreadQueries().build()

        systemUnderTest = WeatherLocalDataSourceImpl(database.weatherDao, Dispatchers.Main)
    }

    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun saveWeather_getWeather_returnWeatherDbEntity() = mainCoroutineRule.runBlockingTest {
        systemUnderTest.saveWeather(fakeDbWeatherEntity)

        val returnedWeather = systemUnderTest.getWeather()

        assertThat<DBWeather>(
            returnedWeather as DBWeather,
            `is`(notNullValue(DBWeather::class.java))
        )

        assertThat(returnedWeather, `is`(fakeDbWeatherEntity))
        assertThat(returnedWeather.cityId, `is`(fakeDbWeatherEntity.cityId))
        assertThat(returnedWeather.cityName, `is`(fakeDbWeatherEntity.cityName))
        assertThat(returnedWeather.uId, `is`(fakeDbWeatherEntity.uId))
    }

    @Test
    fun saveForecastWeather_getForecastWeather_returnForecastWeatherDbEntity() =
        mainCoroutineRule.runBlockingTest {
            systemUnderTest.saveForecastWeather(fakeDbWeatherForecast)

            val returnedForecastWeather = systemUnderTest.getForecastWeather()?.first()

            assertThat<DBWeatherForecast>(
                returnedForecastWeather as DBWeatherForecast,
                `is`(notNullValue(DBWeatherForecast::class.java))
            )

            assertThat(returnedForecastWeather.date, `is`(fakeDbWeatherForecast.date))
            assertThat(returnedForecastWeather.id, `is`(fakeDbWeatherForecast.id))
            assertThat(returnedForecastWeather.wind, `is`(fakeDbWeatherForecast.wind))
        }

    @Test
    fun deleteWeather_getWeatherReturnsNull() = mainCoroutineRule.runBlockingTest {
        systemUnderTest.saveWeather(fakeDbWeatherEntity)
        systemUnderTest.deleteWeather()

        val result = systemUnderTest.getWeather()
        assertThat(result, `is`(nullValue()))
    }

    @Test
    fun deleteForecastWeather_getForecastWeatherReturnsNull() = mainCoroutineRule.runBlockingTest {
        systemUnderTest.saveForecastWeather(fakeDbWeatherForecast)
        systemUnderTest.deleteForecastWeather()

        val result = systemUnderTest.getForecastWeather()
        assertThat(result, `is`(empty<DBWeatherForecast>()))
    }

    // region helper methods

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}
