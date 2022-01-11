package com.mayokunadeniyi.instantweather.ui.forecast

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mayokunadeniyi.instantweather.CoroutineTestRule
import com.mayokunadeniyi.instantweather.MainCoroutineRule
import com.mayokunadeniyi.instantweather.cityId
import com.mayokunadeniyi.instantweather.data.source.repository.WeatherRepository
import com.mayokunadeniyi.instantweather.fakeWeatherForecast
import com.mayokunadeniyi.instantweather.fakeWeatherForecastList
import com.mayokunadeniyi.instantweather.getOrAwaitValue
import com.mayokunadeniyi.instantweather.invalidDataException
import com.mayokunadeniyi.instantweather.utils.Result
import com.shrikanthravi.collapsiblecalendarview.data.Day
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Mayokun Adeniyi on 06/08/2020.
 */
@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class ForecastFragmentViewModelTest {

    //region constants

    //endregion constants

    //region helper fields
    @Mock
    private lateinit var repository: WeatherRepository
    //endregion helper fields

    private lateinit var systemUnderTest: ForecastFragmentViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @Before
    fun setUp() {
        systemUnderTest = ForecastFragmentViewModel(repository, coroutineTestRule.dispatcher)
    }

    @Test
    fun `assert that getWeatherForecast with refresh as false receives successful response from the repository`() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getForecastWeather(cityId, false)).thenReturn(
                Result.Success(
                    listOf(
                        fakeWeatherForecast
                    )
                )
            )

            systemUnderTest.getWeatherForecast(cityId)
            verify(repository, times(1)).getForecastWeather(cityId, false)

            assertThat(
                systemUnderTest.forecast.getOrAwaitValue(),
                `is`(listOf(fakeWeatherForecast))
            )
            assertThat(systemUnderTest.dataFetchState.getOrAwaitValue(), `is`(true))
            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
        }

    @Test
    fun `assert that getWeatherForecast with refresh as false receives a null value as response`() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getForecastWeather(cityId, false)).thenReturn(Result.Success(null))
            `when`(repository.getForecastWeather(cityId, true)).thenReturn(Result.Success(null))

            systemUnderTest.getWeatherForecast(cityId)
            verify(repository, times(1)).getForecastWeather(cityId, false)
            verify(repository, times(1)).getForecastWeather(cityId, true)

            assertThat(systemUnderTest.forecast.getOrAwaitValue(), `is`(nullValue()))
            assertThat(systemUnderTest.dataFetchState.getOrAwaitValue(), `is`(false))
            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
        }

    @Test
    fun `assert that getWeatherForecast with refresh as true receives an error response`() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getForecastWeather(cityId, false)).thenReturn(Result.Success(null))
            `when`(repository.getForecastWeather(cityId, true)).thenReturn(
                Result.Error(
                    invalidDataException
                )
            )

            systemUnderTest.getWeatherForecast(cityId)
            verify(repository, times(1)).getForecastWeather(cityId, false)
            verify(repository, times(1)).getForecastWeather(cityId, true)

            assertThat(systemUnderTest.dataFetchState.getOrAwaitValue(), `is`(false))
            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
        }

    @Test
    fun `assert that refreshForecastData receives successful response from the repository`() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getForecastWeather(cityId, true)).thenReturn(
                Result.Success(
                    listOf(
                        fakeWeatherForecast
                    )
                )
            )

            systemUnderTest.refreshForecastData(cityId)
            verify(repository, times(1)).getForecastWeather(cityId, true)

            assertThat(
                systemUnderTest.forecast.getOrAwaitValue(),
                `is`(listOf(fakeWeatherForecast))
            )
            assertThat(systemUnderTest.dataFetchState.getOrAwaitValue(), `is`(true))
            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
        }

    @Test
    fun `assert that refreshForecastData receives an error response`() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getForecastWeather(cityId, true)).thenReturn(
                Result.Error(
                    invalidDataException
                )
            )

            systemUnderTest.refreshForecastData(cityId)
            verify(repository, times(1)).getForecastWeather(cityId, true)

            assertThat(systemUnderTest.dataFetchState.getOrAwaitValue(), `is`(false))
            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
        }

    @Test
    fun `assert that refreshForecastData receives a null value as response`() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getForecastWeather(cityId, true)).thenReturn(Result.Success(null))

            systemUnderTest.refreshForecastData(cityId)
            verify(repository, times(1)).getForecastWeather(cityId, true)

            assertThat(systemUnderTest.forecast.getOrAwaitValue(), `is`(nullValue()))
            assertThat(systemUnderTest.dataFetchState.getOrAwaitValue(), `is`(false))
            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
        }

    @Test
    fun `assert that updateWeatherForecast returns a correctly filtered list`() = mainCoroutineRule.runBlockingTest {
        val day = Day(2022, 0, 9)

        systemUnderTest.updateWeatherForecast(day, fakeWeatherForecastList)

        assertThat(systemUnderTest.filteredForecast.getOrAwaitValue().size, `is`(3))
    }
}
