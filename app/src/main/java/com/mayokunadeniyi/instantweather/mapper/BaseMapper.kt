package com.mayokunadeniyi.instantweather.mapper

/**
 * Created by Mayokun Adeniyi on 10/03/2020.
 */

interface BaseMapper<E, D> {

    fun transformToDomain(type: E): D

    fun transformToDto(type: D): E
}
