package com.mayokunadeniyi.instantweather.mapper

/**
 * Created by Mayokun Adeniyi on 10/03/2020.
 */
interface BaseMapper<E, D> {

    // Transforms a type to a Domain
    fun transformToDomain(type: E): D

    // Transforms a type to a DTO
    fun transformToDto(type: D): E
}
