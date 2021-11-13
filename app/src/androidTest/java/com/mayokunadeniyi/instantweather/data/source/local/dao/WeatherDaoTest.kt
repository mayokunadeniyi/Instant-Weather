package com.mayokunadeniyi.instantweather.data.source.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mayokunadeniyi.instantweather.MainCoroutineRule
import com.mayokunadeniyi.instantweather.data.model.NetworkWeatherCondition
import com.mayokunadeniyi.instantweather.data.model.NetworkWeatherDescription
import com.mayokunadeniyi.instantweather.data.model.Wind
import com.mayokunadeniyi.instantweather.data.source.local.WeatherDatabase
import com.mayokunadeniyi.instantweather.data.source.local.entity.DBWeather
import com.mayokunadeniyi.instantweather.data.source.local.entity.DBWeatherForecast
import com.mayokunadeniyi.instantweather.fakeDbWeatherEntity
import com.mayokunadeniyi.instantweather.fakeDbWeatherForecast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.hamcrest.Matchers.nullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Mayokun Adeniyi on 04/08/2020.
 */

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class WeatherDaoTest {

    // region helper fields
    private lateinit var database: WeatherDatabase
    // endregion helper fields

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).allowMainThreadQueries().build()

        systemUnderTest = database.weatherDao
    }

    @After
    fun cleanUp() {
        database.close()
    }

    private lateinit var systemUnderTest: WeatherDao

    @Test
    fun insertWeather_verifyWeatherDbIsNotEmpty() = mainCoroutineRule.runBlockingTest {
        systemUnderTest.insertWeather(fakeDbWeatherEntity)

        val weather = systemUnderTest.getWeather()

        assertThat<DBWeather>(weather, `is`(notNullValue()))
        assertThat(weather.uId, `is`(fakeDbWeatherEntity.uId))
        assertThat(weather.cityName, `is`(fakeDbWeatherEntity.cityName))
        assertThat(weather.cityId, `is`(fakeDbWeatherEntity.cityId))
        assertThat(
            weather.networkWeatherCondition,
            `is`(fakeDbWeatherEntity.networkWeatherCondition)
        )
        assertThat(
            weather.networkWeatherDescription,
            `is`(fakeDbWeatherEntity.networkWeatherDescription)
        )
    }

    @Test
    fun insertWeatherWithSameId_ReplaceOnConflict_returnNewlyInsertedWeather() =
        mainCoroutineRule.runBlockingTest {
            val newWeatherEntity = DBWeather(
                1,
                453,
                "Boston",
                Wind(34.5, 43),
                listOf(NetworkWeatherDescription(2L, "Mains", "Clouds", "icons")),
                NetworkWeatherCondition(424.43, 3434.32, 23.5)
            )
            // Insert first weather
            systemUnderTest.insertWeather(fakeDbWeatherEntity)

            // Insert new weather with same id
            systemUnderTest.insertWeather(newWeatherEntity)

            val weather = systemUnderTest.getWeather()

            assertThat<DBWeather>(weather, `is`(notNullValue()))
            assertThat(weather, `is`(newWeatherEntity))
            assertThat(weather.cityName, `is`(newWeatherEntity.cityName))
            assertThat(weather.cityId, `is`(newWeatherEntity.cityId))
            assertThat(
                weather.networkWeatherDescription,
                `is`(newWeatherEntity.networkWeatherDescription)
            )
            assertThat(
                weather.networkWeatherCondition,
                `is`(newWeatherEntity.networkWeatherCondition)
            )
        }

    @Test
    fun insertForecastWeather_verifyForecastWeatherDbIsNotEmpty() =
        mainCoroutineRule.runBlockingTest {
            systemUnderTest.insertForecastWeather(fakeDbWeatherForecast)

            val weather = systemUnderTest.getAllWeatherForecast().first()

            assertThat<DBWeatherForecast>(weather, `is`(notNullValue()))
            assertThat(weather.id, `is`(fakeDbWeatherForecast.id))
            assertThat(weather.date, `is`(fakeDbWeatherForecast.date))
            assertThat(weather.wind, `is`(fakeDbWeatherForecast.wind))
            assertThat(
                weather.networkWeatherCondition,
                `is`(fakeDbWeatherForecast.networkWeatherCondition)
            )
            assertThat(
                weather.networkWeatherDescriptions,
                `is`(fakeDbWeatherForecast.networkWeatherDescriptions)
            )
        }

    @Test
    fun insertForecastWeatherWithSameId_ReplaceOnConflict_returnNewlyInsertedForecastWeather() =
        mainCoroutineRule.runBlockingTest {
            val newDbWeatherForecast = DBWeatherForecast(
                1, "Dated", Wind(42.2, 21),
                listOf(
                    NetworkWeatherDescription(2L, "Mained", "Desces", "Icons")
                ),
                NetworkWeatherCondition(32.3, 52.2, 12.2)
            )
            // Insert first weather forecast
            systemUnderTest.insertWeather(fakeDbWeatherEntity)

            // Insert new weather with same id
            systemUnderTest.insertForecastWeather(newDbWeatherForecast)

            val weather = systemUnderTest.getAllWeatherForecast().first()

            assertThat<DBWeatherForecast>(weather, `is`(notNullValue()))
            assertThat(weather.id, `is`(newDbWeatherForecast.id))
            assertThat(weather.date, `is`(newDbWeatherForecast.date))
            assertThat(weather.wind, `is`(newDbWeatherForecast.wind))
            assertThat(
                weather.networkWeatherCondition,
                `is`(newDbWeatherForecast.networkWeatherCondition)
            )
            assertThat(
                weather.networkWeatherDescriptions,
                `is`(newDbWeatherForecast.networkWeatherDescriptions)
            )
        }

    @Test
    fun deleteWeather_returnNullValue() = mainCoroutineRule.runBlockingTest {
        systemUnderTest.insertWeather(fakeDbWeatherEntity)
        systemUnderTest.deleteAllWeather()

        val weather = systemUnderTest.getWeather()

        assertThat(weather, `is`(nullValue()))
    }

    fun deleteForecastWeather_returnNullValue() = mainCoroutineRule.runBlockingTest {
        systemUnderTest.insertForecastWeather(fakeDbWeatherForecast)
        systemUnderTest.deleteAllWeatherForecast()

        val weather = systemUnderTest.getAllWeatherForecast()

        assertThat(weather, `is`(nullValue()))
    }
}
