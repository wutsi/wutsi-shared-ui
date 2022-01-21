package com.wutsi.application.shared.service

import com.wutsi.application.shared.model.AccountModel
import com.wutsi.platform.account.dto.Account
import com.wutsi.platform.account.dto.AccountSummary
import org.springframework.context.i18n.LocaleContextHolder
import java.util.Locale

class SharedUIMapper(
    private val categoryService: CategoryService,
) {
    fun toAccountModel(obj: AccountSummary) = AccountModel(
        displayName = obj.displayName,
        pictureUrl = obj.pictureUrl,
        business = obj.business,
        retail = obj.retail,
        location = locationText(obj.language, obj.country),
    )

    fun toAccountModel(obj: Account) = AccountModel(
        displayName = obj.displayName,
        pictureUrl = obj.pictureUrl,
        website = obj.website,
        business = obj.business,
        retail = obj.retail,
        location = locationText(obj.language, obj.country),
        category = obj.categoryId?.let { categoryText(it) },
        phoneNumber = PhoneUtil.format(obj.phone?.number, obj.phone?.country),
        biography = obj.biography
    )

    private fun locationText(language: String, country: String): String {
        val locale = LocaleContextHolder.getLocale()
        val location = Locale(language, country).getDisplayCountry(locale)
        return StringUtil.capitalizeFirstLetter(location)
    }

    private fun categoryText(categoryId: Long): String? {
        val category = categoryService.get(categoryId)
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
