package com.mayokunadeniyi.instantweather.data.source.repository

import com.mayokunadeniyi.instantweather.MainCoroutineRule
import com.mayokunadeniyi.instantweather.data.model.Weather
import com.mayokunadeniyi.instantweather.data.model.WeatherForecast
import com.mayokunadeniyi.instantweather.data.source.local.WeatherLocalDataSource
import com.mayokunadeniyi.instantweather.data.source.remote.WeatherRemoteDataSource
import com.mayokunadeniyi.instantweather.dummyLocation
import com.mayokunadeniyi.instantweather.fakeDbWeatherEntity
import com.mayokunadeniyi.instantweather.fakeDbWeatherForecast
import com.mayokunadeniyi.instantweather.fakeNetworkWeather
import com.mayokunadeniyi.instantweather.fakeNetworkWeatherForecast
import com.mayokunadeniyi.instantweather.invalidDataException
import com.mayokunadeniyi.instantweather.queryLocation
import com.mayokunadeniyi.instantweather.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Mayokun Adeniyi on 04/08/2020.
 */
@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class WeatherRepositoryTest {

    //region helper fields
    @Mock
    private lateinit var remoteDataSource: WeatherRemoteDataSource

    @Mock
    private lateinit var localDataSource: WeatherLocalDataSource
    //endregion helper fields

    private lateinit var systemUnderTest: WeatherRepositoryImpl

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        systemUnderTest = WeatherRepositoryImpl(remoteDataSource, localDataSource, Dispatchers.Main)
    }

    @Test
    fun `assert that getWeather with refresh as true fetches successfully from the remote source`() =
        mainCoroutineRule.runBlockingTest {
            `when`(remoteDataSource.getWeather(dummyLocation)).thenReturn(
                Result.Success(fakeNetworkWeather)
            )

            val response = systemUnderTest.getWeather(dummyLocation, true)

            verify(remoteDataSource, times(1)).getWeather(dummyLocation)
            verifyNoMoreInteractions(localDataSource)

            when (response) {
                is Result.Success -> {
                    val weather = response.data
                    assertThat<Weather>(weather as Weather, `is`(notNullValue()))
                    assertThat(weather.name, `is`(fakeNetworkWeather.name))
                    assertThat(weather.cityId, `is`(fakeNetworkWeather.cityId))
                    assertThat(weather.wind, `is`(fakeNetworkWeather.wind))
                    assertThat(
                        weather.networkWeatherCondition,
                        `is`(fakeNetworkWeather.networkWeatherCondition)
                    )
                    assertThat(
                        weather.networkWeatherDescription,
                        `is`(fakeNetworkWeather.networkWeatherDescriptions)
                    )
                }
            }
        }

    @Test
    fun `assert that getWeather with refresh as false fetches successfully from the local source`() =
        mainCoroutineRule.runBlockingTest {
            `when`(localDataSource.getWeather()).thenReturn(
                fakeDbWeatherEntity
            )

            val response = systemUnderTest.getWeather(dummyLocation, false)

            verify(localDataSource, times(1)).getWeather()
            verifyNoMoreInteractions(remoteDataSource)

            when (response) {
                is Result.Success -> {
                    val weather = response.data
                    assertThat<Weather>(weather as Weather, `is`(notNullValue()))
                    assertThat(weather.name, `is`(fakeDbWeatherEntity.cityName))
                    assertThat(weather.cityId, `is`(fakeDbWeatherEntity.cityId))
                    assertThat(weather.wind, `is`(fakeDbWeatherEntity.wind))
                    assertThat(
                        weather.networkWeatherCondition,
                        `is`(fakeDbWeatherEntity.networkWeatherCondition)
                    )
                    assertThat(
                        weather.networkWeatherDescription,
                        `is`(fakeDbWeatherEntity.networkWeatherDescription)
                    )
                }
            }
        }

    @Test
    fun `assert that getWeather with refresh as true returns fetches from the remote source but returns an Error`() =
        mainCoroutineRule.runBlockingTest {
            `when`(remoteDataSource.getWeather(dummyLocation)).thenReturn(
                Result.Error(
                    invalidDataException
                )
            )

            val response = systemUnderTest.getWeather(dummyLocation, true)

            verify(remoteDataSource, times(1)).getWeather(dummyLocation)
            verifyNoMoreInteractions(localDataSource)

            when (response) {
                is Result.Error -> {
                    assertThat(response.exception, `is`(invalidDataException))
                }
            }
        }

    @Test
    fun `assert that getWeather with refresh as false fetches null data from the local source`() =
        mainCoroutineRule.runBlockingTest {
            `when`(localDataSource.getWeather()).thenReturn(
                null
            )

            val response = systemUnderTest.getWeather(dummyLocation, false)

            verify(localDataSource, times(1)).getWeather()
            verifyNoMoreInteractions(remoteDataSource)

            when (response) {
                is Result.Success -> {
                    assertThat(response.data, `is`(nullValue()))
                }
            }
        }

    @Test
    fun `assert that getForecastWeather with refresh as true fetches successfully from the remote source`() =
        mainCoroutineRule.runBlockingTest {
            `when`(remoteDataSource.getWeatherForecast(fakeNetworkWeatherForecast.id)).thenReturn(
                Result.Success(listOf(fakeNetworkWeatherForecast))
            )

            val response = systemUnderTest.getForecastWeather(fakeNetworkWeatherForecast.id, true)

            verify(remoteDataSource, times(1)).getWeatherForecast(fakeNetworkWeatherForecast.id)
            verifyNoMoreInteractions(localDataSource)

            when (response) {
                is Result.Success -> {
                    val forecast = response.data?.first()
                    assertThat<WeatherForecast>(forecast as WeatherForecast, `is`(notNullValue()))
                    assertThat(forecast.date, `is`(fakeNetworkWeatherForecast.date))
                    assertThat(forecast.uID, `is`(fakeNetworkWeatherForecast.id))
                    assertThat(forecast.wind, `is`(fakeNetworkWeatherForecast.wind))
                    assertThat(
                        forecast.networkWeatherCondition,
                        `is`(fakeNetworkWeatherForecast.networkWeatherCondition)
                    )
                    assertThat(
                        forecast.networkWeatherDescription,
                        `is`(fakeNetworkWeatherForecast.networkWeatherDescription)
                    )
                }
            }
        }

    @Test
    fun `assert that getForecastWeather with refresh as false fetches successfully from the local source`() =
        mainCoroutineRule.runBlockingTest {
            `when`(localDataSource.getForecastWeather()).thenReturn(
                listOf(fakeDbWeatherForecast)
            )

            val response = systemUnderTest.getForecastWeather(fakeNetworkWeatherForecast.id, false)

            verify(localDataSource, times(1)).getForecastWeather()
            verifyNoMoreInteractions(remoteDataSource)

            when (response) {
                is Result.Success -> {
                    val forecast = response.data?.first()
                    assertThat<WeatherForecast>(forecast as WeatherForecast, `is`(notNullValue()))
                    assertThat(forecast.date, `is`(fakeDbWeatherForecast.date))
                    assertThat(forecast.uID, `is`(fakeDbWeatherForecast.id))
                    assertThat(forecast.wind, `is`(fakeDbWeatherForecast.wind))
                    assertThat(
                        forecast.networkWeatherCondition,
                        `is`(fakeDbWeatherForecast.networkWeatherCondition)
                    )
                    assertThat(
                        forecast.networkWeatherDescription,
                        `is`(fakeDbWeatherForecast.networkWeatherDescriptions)
                    )
                }
            }
        }

    @Test
    fun `assert that getForecastWeather with refresh as false fetches null data from the local source`() =
        mainCoroutineRule.runBlockingTest {
            `when`(localDataSource.getForecastWeather()).thenReturn(
                null
            )

            val response = systemUnderTest.getForecastWeather(fakeNetworkWeatherForecast.id, false)

            verify(localDataSource, times(1)).getForecastWeather()
            verifyNoMoreInteractions(remoteDataSource)

            when (response) {
                is Result.Success -> {
                    assertThat(response.data, `is`(nullValue()))
                }
            }
        }

    @Test
    fun `assert that getSearchWeather fetches successfully from the remote source`() =
        mainCoroutineRule.runBlockingTest {
            `when`(remoteDataSource.getSearchWeather(queryLocation)).thenReturn(Result.Success(fakeNetworkWeather))

            val response = systemUnderTest.getSearchWeather(queryLocation)

            verify(remoteDataSource, times(1)).getSearchWeather(queryLocation)

            when (response) {
                is Result.Success -> {
                    val weather = response.data
                    assertThat<Weather>(weather as Weather, `is`(notNullValue()))
                    assertThat(weather.name, `is`(fakeNetworkWeather.name))
                    assertThat(weather.cityId, `is`(fakeNetworkWeather.cityId))
                    assertThat(weather.wind, `is`(fakeNetworkWeather.wind))
                    assertThat(
                        weather.networkWeatherCondition,
                        `is`(fakeNetworkWeather.networkWeatherCondition)
                    )
                    assertThat(
                        weather.networkWeatherDescription,
                        `is`(fakeNetworkWeather.networkWeatherDescriptions)
                    )
                }
            }
        }

    // region helper methods

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}
