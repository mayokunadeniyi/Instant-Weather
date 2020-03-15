package com.example.instantweather.mapper

/**
 * Created by Mayokun Adeniyi on 10/03/2020.
 */
interface BaseMapperRepository<E,D> {

    fun transform(type: E): D

    fun transformToRepository(type: D): E
}