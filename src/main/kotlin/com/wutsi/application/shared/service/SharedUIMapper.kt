package com.wutsi.application.shared.service

import com.wutsi.application.shared.entity.CategoryEntity
import com.wutsi.application.shared.entity.CityEntity
import com.wutsi.application.shared.model.AccountModel
import com.wutsi.platform.account.dto.Account
import com.wutsi.platform.account.dto.AccountSummary
import org.springframework.context.i18n.LocaleContextHolder
import java.util.Locale

open class SharedUIMapper(
    private val categoryService: CategoryService,
    private val cityService: CityService,
) {
    open fun toAccountModel(obj: AccountSummary) = AccountModel(
        displayName = obj.displayName,
        pictureUrl = obj.pictureUrl,
        business = obj.business,
        retail = obj.retail,
        location = toLocationText(null, obj.country),
        category = obj.categoryId?.let { toCategoryText(it) }
    )

    open fun toAccountModel(obj: Account) = AccountModel(
        displayName = obj.displayName,
        pictureUrl = obj.pictureUrl,
        website = obj.website,
        business = obj.business,
        retail = obj.retail,
        location = toLocationText(cityService.get(obj.cityId), obj.country),
        category = obj.categoryId?.let { toCategoryText(it) },
        phoneNumber = PhoneUtil.format(obj.phone?.number, obj.phone?.country),
        biography = obj.biography
    )

    open fun toLocationText(city: CityEntity?, country: String): String {
        val locale = LocaleContextHolder.getLocale()
        if (city != null) {
            val country = StringUtil.capitalizeFirstLetter(
                Locale(locale.language, city.country).getDisplayCountry(locale)
            )
            return "${city.name}, $country"
        }
        return StringUtil.capitalizeFirstLetter(Locale(locale.language, country).getDisplayCountry(locale))
    }

    open fun toCategoryText(categoryId: Long): String? {
        val category = categoryService.get(categoryId)
        return toCategoryText(category)
    }

    open fun toCategoryText(category: CategoryEntity?): String? {
        if (category != null) {
            val locale = LocaleContextHolder.getLocale()
            if (locale.language == "fr")
                return category.titleFrench
            else
                return category.title
        } else
            return null
    }
}
