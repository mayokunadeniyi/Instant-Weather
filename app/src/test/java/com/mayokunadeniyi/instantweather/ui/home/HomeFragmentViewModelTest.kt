package com.mayokunadeniyi.instantweather.ui.home

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mayokunadeniyi.instantweather.MainCoroutineRule
import com.mayokunadeniyi.instantweather.data.source.repository.WeatherRepository
import com.mayokunadeniyi.instantweather.dummyLocation
import com.mayokunadeniyi.instantweather.fakeWeather
import com.mayokunadeniyi.instantweather.getOrAwaitValue
import com.mayokunadeniyi.instantweather.invalidDataException
import com.mayokunadeniyi.instantweather.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created by Mayokun Adeniyi on 07/08/2020.
 */
@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class HomeFragmentViewModelTest {

    //region constants

    //endregion constants

    //region helper fields
    private var repository: WeatherRepository = mock(WeatherRepository::class.java)
    //endregion helper fields

    private lateinit var systemUnderTest: HomeFragmentViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        systemUnderTest =
            HomeFragmentViewModel(repository)
    }

    @Test
    fun `assert that getWeather receives weather data from the repository successfully `() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getWeather(dummyLocation, false)).thenReturn(
                Result.Success(
                    fakeWeather
                )
            )

            systemUnderTest.getWeather(dummyLocation)
            verify(repository, times(1)).getWeather(dummyLocation, false)

            assertThat(systemUnderTest.weather.getOrAwaitValue(), `is`(fakeWeather))
            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
            assertThat(systemUnderTest.dataFetchState.getOrAwaitValue(), `is`(true))
        }

    @Test
    fun `assert that getWeather receives null data from the repository `() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getWeather(dummyLocation, false)).thenReturn(Result.Success(null))
            `when`(repository.getWeather(dummyLocation, true)).thenReturn(Result.Success(null))

            systemUnderTest.getWeather(dummyLocation)
            verify(repository, times(1)).getWeather(dummyLocation, false)
            verify(repository, times(1)).getWeather(dummyLocation, true)

            assertThat(systemUnderTest.weather.getOrAwaitValue(), `is`(nullValue()))
            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
            assertThat(systemUnderTest.dataFetchState.getOrAwaitValue(), `is`(false))
        }

    @Test
    fun `assert that getWeather receives an error from the repository `() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getWeather(dummyLocation, false)).thenReturn(Result.Success(null))
            `when`(repository.getWeather(dummyLocation, true)).thenReturn(
                Result.Error(
                    invalidDataException
                )
            )

            systemUnderTest.getWeather(dummyLocation)
            verify(repository, times(1)).getWeather(dummyLocation, false)
            verify(repository, times(1)).getWeather(dummyLocation, true)

            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
            assertThat(systemUnderTest.dataFetchState.getOrAwaitValue(), `is`(false))
        }

    @Test
    fun `assert that refreshWeather receives weather data from the repository successfully `() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getWeather(dummyLocation, true)).thenReturn(
                Result.Success(
                    fakeWeather
                )
            )

            systemUnderTest.refreshWeather(dummyLocation)
            verify(repository, times(1)).getWeather(dummyLocation, true)

            assertThat(systemUnderTest.weather.getOrAwaitValue(), `is`(fakeWeather))
            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
            assertThat(systemUnderTest.dataFetchState.getOrAwaitValue(), `is`(true))
        }

    @Test
    fun `assert that refreshWeather receives null data from the repository `() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getWeather(dummyLocation, true)).thenReturn(Result.Success(null))

            systemUnderTest.refreshWeather(dummyLocation)
            verify(repository, times(1)).getWeather(dummyLocation, true)

            assertThat(systemUnderTest.weather.getOrAwaitValue(), `is`(nullValue()))
            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
            assertThat(systemUnderTest.dataFetchState.getOrAwaitValue(), `is`(false))
        }

    @Test
    fun `assert that refreshWeather receives an error from the repository `() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getWeather(dummyLocation, true)).thenReturn(
                Result.Error(
                    invalidDataException
                )
            )

            systemUnderTest.refreshWeather(dummyLocation)
            verify(repository, times(1)).getWeather(dummyLocation, true)

            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
            assertThat(systemUnderTest.dataFetchState.getOrAwaitValue(), `is`(false))
        }

    // region helper methods

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}
