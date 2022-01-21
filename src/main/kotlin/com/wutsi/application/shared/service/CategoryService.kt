package com.wutsi.application.shared.service

import com.wutsi.application.shared.entity.CategoryEntity
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader

class CategoryService {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(CategoryService::class.java)
    }

    private var categories: List<CategoryEntity>? = null

    fun all(): List<CategoryEntity> {
        if (categories == null)
            load()

        return categories ?: emptyList()
    }

    fun get(id: Long?): CategoryEntity? =
        if (id == null)
            null
        else
            all().find { it.id == id }

    private fun load() {
        LOGGER.info("Loading categories from CSV")

        val inputStream = CategoryService::class.java.getResourceAsStream("/categories.csv")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))

        val csvParser = CSVParser(
            bufferedReader,
            CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
        )

        var count = 0
        val tmp = mutableListOf<CategoryEntity>()
        for (csvRecord in csvParser) {
            tmp.add(
                CategoryEntity(
                    id = csvRecord.get("id").toLong(),
                    title = csvRecord.get("title"),
                    titleFrench = csvRecord.get("title_fr")
                )
            )
            count++
        }
        categories = tmp

        LOGGER.info("$count categories loaded")
    }
}
