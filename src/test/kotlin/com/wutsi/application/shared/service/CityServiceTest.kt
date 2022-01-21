package com.wutsi.application.shared.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CityServiceTest {
    private val service: CityService = CityService()

    @Test
    fun all() {
        val cities = service.all()
        assertEquals(122, cities.size)
    }

    @Test
    fun get() {
        val city = service.get(2224863)
        assertEquals(2224863, city?.id)
        assertEquals("Ngambé", city?.name)
        assertEquals("Ngambe", city?.asciiName)
    }

    @Test
    fun searchIgnoreCase() {
        val cities = service.search("YaOundé")
        assertEquals(1, cities.size)
        assertEquals(2220957, cities[0].id)
    }

    @Test
    fun searchMultiple() {
        val cities = service.search("ng")
        assertEquals(6, cities.size)
    }

    @Test
    fun searchNotFound() {
        val cities = service.search("xxxxxx")
        assertEquals(0, cities.size)
    }
}
