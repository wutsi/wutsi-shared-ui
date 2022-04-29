package com.wutsi.application.shared.service

import com.wutsi.application.shared.entity.CityEntity
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader

open class CityService {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(CityService::class.java)
    }

    private var cities: List<CityEntity>? = null

    open fun search(query: String?, countries: List<String>? = null): List<CityEntity> {
        val ascii = StringUtil.unaccent(query)
        return all()
            .filter { accept(it, ascii, countries) }
            .sortedBy { it.asciiName }
    }

    private fun accept(city: CityEntity, query: String, countries: List<String>?): Boolean =
        (city.asciiName.startsWith(query, ignoreCase = true)) &&
            (countries == null || countries.contains(city.country))

    open fun all(): List<CityEntity> {
        if (cities == null)
            load()
        return cities ?: emptyList()
    }

    open fun get(id: Long?): CityEntity? =
        if (id == null)
            null
        else
            all().find { it.id == id }

    private fun load() {
        val tmp = mutableListOf<CityEntity>()
        tmp.addAll(load("CM", 1000))
//        tmp.addAll(load("FR", 100000))
//        tmp.addAll(load("UK", 100000))
//        tmp.addAll(load("CA", 100000))
//        tmp.addAll(load("US", 400000))

        LOGGER.info("${tmp.size} cities loaded")
        cities = tmp
    }

    private fun load(country: String, populationThreshold: Long): List<CityEntity> {
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

        val items = mutableListOf<CityEntity>()
        for (csvRecord in csvParser) {
            val population = csvRecord.get("Population").toLong()
            if (population >= populationThreshold)
                items.add(
                    CityEntity(
                        id = csvRecord.get("Geoname ID").toLong(),
                        name = csvRecord.get("Name"),
                        asciiName = csvRecord.get("ASCII Name"),
                        country = csvRecord.get("Country Code")
                    )
                )
        }

        LOGGER.info("$${items.size} cities loaded from $country")
        return items
    }
}
