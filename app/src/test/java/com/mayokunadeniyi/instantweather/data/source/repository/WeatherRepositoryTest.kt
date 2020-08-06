package com.mayokunadeniyi.instantweather.data.source.repository

import com.mayokunadeniyi.instantweather.MainCoroutineRule
import com.mayokunadeniyi.instantweather.data.source.local.WeatherLocalDataSource
import com.mayokunadeniyi.instantweather.data.source.remote.WeatherRemoteDataSource
import com.mayokunadeniyi.instantweather.dummyLocation
import com.mayokunadeniyi.instantweather.fakeDbWeatherForecast
import com.mayokunadeniyi.instantweather.fakeNetworkWeather
import com.mayokunadeniyi.instantweather.fakeNetworkWeatherForecast
import com.mayokunadeniyi.instantweather.invalidDataException
import com.mayokunadeniyi.instantweather.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Mayokun Adeniyi on 04/08/2020.
 */
@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class WeatherRepositoryTest {

    //region constants

    //endregion constants

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

//    @Test
//    fun `assert that fetchRemoteWeatherData returns a NetworkResult successfully`() =
//        mainCoroutineRule.runBlockingTest {
//            `when`(remoteDataSource.getWeather(dummyLocation)).thenReturn(
//                Result.Success(
//                    fakeNetworkWeather
//                )
//            )
//
//            val response = systemUnderTest.fetchRemoteWeatherData(dummyLocation)
//
//            verify(remoteDataSource, times(1)).getWeather(dummyLocation)
//
//            when (response) {
//                is Result.Success -> {
//                    assertThat(response.data, `is`(fakeNetworkWeather))
//                }
//            }
//        }
//
//    @Test
//    fun `assert that fetchRemoteWeatherData returns an Error`() =
//        mainCoroutineRule.runBlockingTest {
//            `when`(remoteDataSource.getWeather(dummyLocation)).thenReturn(
//                Result.Error(invalidDataException)
//            )
//
//            val response = systemUnderTest.fetchRemoteWeatherData(dummyLocation)
//
//            verify(remoteDataSource, times(1)).getWeather(dummyLocation)
//
//            when (response) {
//                is Result.Error -> {
//                    assertThat(response.exception, `is`(invalidDataException))
//                }
//            }
//        }
//
//    @Test
//    fun `assert that fetchRemoteWeatherForecast returns a NetworkWeatherForecast successfully`() =
//        mainCoroutineRule.runBlockingTest {
//            `when`(remoteDataSource.getWeatherForecast(fakeNetworkWeatherForecast.id)).thenReturn(
//                Result.Success(
//                    listOf(fakeNetworkWeatherForecast)
//                )
//            )
//
//            val response = systemUnderTest.fetchRemoteWeatherForecast(fakeNetworkWeatherForecast.id)
//
//            verify(remoteDataSource, times(1)).getWeatherForecast(fakeNetworkWeatherForecast.id)
//
//            when (response) {
//                is Result.Success -> {
//                    assertThat(response.data, `is`(listOf(fakeNetworkWeatherForecast)))
//                }
//            }
//        }
//
//    @Test
//    fun `assert that fetchRemoteWeatherForecast returns an Error`() =
//        mainCoroutineRule.runBlockingTest {
//            `when`(remoteDataSource.getWeatherForecast(fakeNetworkWeatherForecast.id)).thenReturn(
//                Result.Error(invalidDataException)
//            )
//
//            val response = systemUnderTest.fetchRemoteWeatherForecast(fakeNetworkWeatherForecast.id)
//
//            verify(remoteDataSource, times(1)).getWeatherForecast(fakeNetworkWeatherForecast.id)
//
//            when (response) {
//                is Result.Error -> {
//                    assertThat(response.exception, `is`(invalidDataException))
//                }
//            }
//        }

    @Test
    fun `assert that getLocalWeatherData`() {

    }


    // region helper methods

    // endregion helper methods

    // region helper classes

    // endregion helper classes

}
