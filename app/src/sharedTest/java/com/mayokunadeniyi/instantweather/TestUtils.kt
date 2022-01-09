package com.mayokunadeniyi.instantweather

import com.mayokunadeniyi.instantweather.data.model.LocationModel
import com.mayokunadeniyi.instantweather.data.model.NetworkWeather
import com.mayokunadeniyi.instantweather.data.model.NetworkWeatherCondition
import com.mayokunadeniyi.instantweather.data.model.NetworkWeatherDescription
import com.mayokunadeniyi.instantweather.data.model.NetworkWeatherForecast
import com.mayokunadeniyi.instantweather.data.model.Weather
import com.mayokunadeniyi.instantweather.data.model.WeatherForecast
import com.mayokunadeniyi.instantweather.data.model.Wind
import com.mayokunadeniyi.instantweather.data.source.local.entity.DBWeather
import com.mayokunadeniyi.instantweather.data.source.local.entity.DBWeatherForecast

/**
 * Created by Mayokun Adeniyi on 02/08/2020.
 */

val fakeDbWeatherEntity = DBWeather(
    1,
    123,
    "Lagos",
    Wind(32.5, 24),
    listOf(NetworkWeatherDescription(1L, "Main", "Cloudy", "icon")),
    NetworkWeatherCondition(324.43, 1234.32, 32.5)
)

val fakeDbWeatherForecast = DBWeatherForecast(
    1, "Date", Wind(22.2, 21),
    listOf(
        NetworkWeatherDescription(1L, "Main", "Desc", "Icon")
    ),
    NetworkWeatherCondition(22.3, 22.2, 22.2)
)

val dummyLocation = LocationModel(12.2, 23.4)

val fakeNetworkWeather = NetworkWeather(
    1,
    123,
    "Lagos",
    Wind(32.5, 24),
    listOf(NetworkWeatherDescription(1L, "Main", "Cloudy", "icon")),
    NetworkWeatherCondition(324.43, 1234.32, 32.5)
)

val fakeNetworkWeatherForecast = NetworkWeatherForecast(
    1, "Date", Wind(22.2, 21),
    listOf(
        NetworkWeatherDescription(1L, "Main", "Desc", "Icon")
    ),
    NetworkWeatherCondition(22.3, 22.2, 22.2)
)

val fakeWeather = Weather(
    1,
    123,
    "Lagos",
    Wind(32.5, 24),
    listOf(NetworkWeatherDescription(1L, "Main", "Cloudy", "cloud")),
    NetworkWeatherCondition(324.43, 1234.32, 32.5)
)

val fakeWeatherForecast = WeatherForecast(
    1, "2021-07-25 14:22:10", Wind(22.2, 21),
    listOf(
        NetworkWeatherDescription(1L, "Main", "Desc", "Icon")
    ),
    NetworkWeatherCondition(22.3, 22.2, 22.2)
)

fun createNewWeatherForecast(date: String): WeatherForecast {
    return WeatherForecast(
        1, date, Wind(22.2, 21),
        listOf(
            NetworkWeatherDescription(1L, "Main", "Desc", "Icon")
        ),
        NetworkWeatherCondition(22.3, 22.2, 22.2)
    )
}

val fakeWeatherForecastList = listOf(
    createNewWeatherForecast("3 Jan 2022, 2:00pm"),
    createNewWeatherForecast("4 Jan 2022, 12:00am"),
    createNewWeatherForecast("9 Jan 2022, 12:00am"),
    createNewWeatherForecast("9 Jan 2022, 12:00am"),
    createNewWeatherForecast("9 Jan 2022, 12:00am")
)

val invalidDataException = Exception("Invalid Data")
const val queryLocation = "Lagos"
const val cityId = 1234
