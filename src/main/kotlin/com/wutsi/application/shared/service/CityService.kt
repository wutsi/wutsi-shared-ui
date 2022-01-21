package com.wutsi.application.shared.service

import com.wutsi.application.shared.entity.CityEntity
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader

class CityService {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(CityService::class.java)
    }

    private var cities: List<CityEntity>? = null

    fun search(query: String?, countries: List<String>? = null): List<CityEntity> {
        val ascii = StringUtil.unaccent(query)
        return all()
            .filter { accept(it, ascii, countries) }
            .sortedBy { it.asciiName }
    }

    private fun accept(city: CityEntity, query: String, countries: List<String>?): Boolean =
        (query == null || city.asciiName.startsWith(query, ignoreCase = true)) &&
            (countries == null || countries.contains(city.country))

    fun all(): List<CityEntity> {
        if (cities == null)
            load()
        return cities ?: emptyList()
    }

    fun get(id: Long?): CityEntity? =
        if (id == null)
            null
        else
            all().find { it.id == id }

    private fun load() {
        var count: Int = 0
        count += load("CM")

        LOGGER.info("$count cities loaded")
    }

    private fun load(country: String): Int {
        LOGGER.info("Loading cou from CSV from $country")

        val inputStream = CityService::class.java.getResourceAsStream("/geonames/$country.csv")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))

        val csvParser = CSVParser(
            bufferedReader,
            CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withDelimiter(';')
                .withTrim()
        )

        var count = 0
        val tmp = mutableListOf<CityEntity>()
        for (csvRecord in csvParser) {
            tmp.add(
                CityEntity(
                    id = csvRecord.get("Geoname ID").toLong(),
                    name = csvRecord.get("Name"),
                    asciiName = csvRecord.get("ASCII Name"),
                    country = csvRecord.get("Country Code")
                )
            )
            count++
        }
        cities = tmp

        LOGGER.info("$count cities loaded from $country")
        return count
    }
}
