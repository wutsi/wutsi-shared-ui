package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.application.shared.model.ProductModel
import com.wutsi.application.shared.service.StringUtil
import com.wutsi.flutter.sdui.Action
import com.wutsi.flutter.sdui.AspectRatio
import com.wutsi.flutter.sdui.Column
import com.wutsi.flutter.sdui.Container
import com.wutsi.flutter.sdui.Image
import com.wutsi.flutter.sdui.MoneyText
import com.wutsi.flutter.sdui.Positioned
import com.wutsi.flutter.sdui.Row
import com.wutsi.flutter.sdui.Stack
import com.wutsi.flutter.sdui.Text
import com.wutsi.flutter.sdui.WidgetAware
import com.wutsi.flutter.sdui.enums.BoxFit
import com.wutsi.flutter.sdui.enums.CrossAxisAlignment
import com.wutsi.flutter.sdui.enums.MainAxisAlignment
import com.wutsi.flutter.sdui.enums.TextAlignment
import com.wutsi.flutter.sdui.enums.TextOverflow

class ProductCard(
    private val model: ProductModel,
    private val savingsPercentageThreshold: Int = 1,
    private val action: Action? = null,
    private val margin: Double? = null,
) : CompositeWidgetAware() {
    companion object {
        const val PADDING = 5.0
    }

    override fun toWidgetAware(): WidgetAware = Container(
        border = 0.2,
        borderColor = Theme.COLOR_DIVIDER,
        action = action,
        margin = margin,
        child = Column(
            mainAxisAlignment = MainAxisAlignment.start,
            crossAxisAlignment = CrossAxisAlignment.start,
            children = listOfNotNull(
                toThumbnailWidget(),
                toInfoWidget(),
                toMerchantWidget()
            )
        ),
    )

    private fun toThumbnailWidget(): WidgetAware? {
        if (model.thumbnail?.url == null)
            return null

        return Container(
            background = Theme.COLOR_GRAY_LIGHT,
            child = Stack(
                children = listOfNotNull(
                    // Thumbnail
                    Container(
                        padding = PADDING,
                        child = AspectRatio(
                            aspectRatio = 4.0 / 3.0,
                            child = Container(
                                child = Image(
                                    url = model.thumbnail.url,
                                    fit = BoxFit.fitHeight,
                                )
                            )
                        )
                    ),

                    // Savings Pills
                    if (hasSavings())
                        Positioned(
                            left = 0.0,
                            top = PADDING,
                            child = Container(
                                background = Theme.COLOR_SUCCESS,
                                padding = 4.0,
                                child = Text(
                                    caption = "${model.savings!!.percent}%",
                                    size = Theme.TEXT_SIZE_SMALL,
                                    color = Theme.COLOR_WHITE
                                ),
                            )
                        )
                    else
                        null,
                )
            ),
        )
    }

    private fun toInfoWidget(): WidgetAware = Container(
        height = (PADDING /* top */ + PADDING /* bottom-margin */ + Theme.TEXT_SIZE_LARGE /* text */) * getInfoLines(),
        child = Column(
            mainAxisAlignment = MainAxisAlignment.spaceBetween,
            crossAxisAlignment = CrossAxisAlignment.start,
            children = listOfNotNull(
                Container(
                    padding = PADDING,
                    child = Text(
                        caption = StringUtil.capitalizeFirstLetter(model.title),
                        overflow = TextOverflow.Elipsis,
                        maxLines = 2,
                    ),
                ),
                if (model.price != null)
                    Container(
                        padding = PADDING,
                        child = MoneyText(
                            color = Theme.COLOR_PRIMARY,
                            value = model.price.amount,
                            currency = model.price.currency,
                            numberFormat = model.price.numberFormat,
                            valueFontSize = Theme.TEXT_SIZE_LARGE,
                            currencyFontSize = Theme.TEXT_SIZE_SMALL
                        )
                    )
                else
                    null,
            ),
        )
    )

    private fun getInfoLines(): Int = 3

    private fun toMerchantWidget(): WidgetAware? =
        model.merchant?.let {
            Row(
                mainAxisAlignment = MainAxisAlignment.end,
                crossAxisAlignment = CrossAxisAlignment.end,
                children = listOfNotNull(
                    Container(
                        padding = PADDING,
                        child = Text(
                            caption = it.displayName ?: "",
                            size = Theme.TEXT_SIZE_SMALL,
                            alignment = TextAlignment.Right
                        )
                    )
                )
            )
        }

    private fun hasSavings(): Boolean =
        model.savings?.percent != null && model.savings.percent >= savingsPercentageThreshold
}
