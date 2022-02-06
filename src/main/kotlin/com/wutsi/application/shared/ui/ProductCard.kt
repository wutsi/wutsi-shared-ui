package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.application.shared.model.ProductModel
import com.wutsi.application.shared.service.StringUtil
import com.wutsi.flutter.sdui.Action
import com.wutsi.flutter.sdui.AspectRatio
import com.wutsi.flutter.sdui.Column
import com.wutsi.flutter.sdui.Container
import com.wutsi.flutter.sdui.Image
import com.wutsi.flutter.sdui.Positioned
import com.wutsi.flutter.sdui.Row
import com.wutsi.flutter.sdui.Stack
import com.wutsi.flutter.sdui.Text
import com.wutsi.flutter.sdui.WidgetAware
import com.wutsi.flutter.sdui.enums.Alignment
import com.wutsi.flutter.sdui.enums.BoxFit
import com.wutsi.flutter.sdui.enums.CrossAxisAlignment
import com.wutsi.flutter.sdui.enums.MainAxisAlignment
import com.wutsi.flutter.sdui.enums.TextDecoration
import com.wutsi.flutter.sdui.enums.TextOverflow

class ProductCard(
    private val model: ProductModel,
    private val savingsPercentageThreshold: Int = 1,
    private val action: Action? = null
) : CompositeWidgetAware() {
    companion object {
        const val PADDING = 5.0
        const val SPACING = 2.0
    }

    override fun toWidgetAware(): WidgetAware = Container(
        border = 0.2,
        borderColor = Theme.COLOR_DIVIDER,
        action = action,
        margin = SPACING,
        child = Column(
            mainAxisAlignment = MainAxisAlignment.start,
            crossAxisAlignment = CrossAxisAlignment.start,
            children = listOfNotNull(
                if (model.thumbnail?.url != null)
                    Container(
                        background = Theme.COLOR_GRAY_LIGHT,
                        child = Stack(
                            children = listOfNotNull(
                                Container(
                                    padding = 10.0,
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

                                if (model.savings?.percent != null && model.savings.percent >= savingsPercentageThreshold)
                                    Positioned(
                                        left = 5.0,
                                        bottom = 5.0,
                                        child = Container(
                                            background = Theme.COLOR_SUCCESS,
                                            padding = 4.0,
                                            borderRadius = 4.0,
                                            child = Text(
                                                caption = "${model.savings.percent}% off",
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
                else
                    null,

                Container(
                    height = 6 * Theme.TEXT_SIZE_LARGE,
                    alignment = Alignment.Center,
                    padding = PADDING,
                    child = Column(
                        mainAxisAlignment = MainAxisAlignment.start,
                        crossAxisAlignment = CrossAxisAlignment.start,
                        children = listOfNotNull(

                            if (model.price != null)
                                Row(
                                    mainAxisAlignment = MainAxisAlignment.start,
                                    crossAxisAlignment = CrossAxisAlignment.end,
                                    children = listOfNotNull(
                                        Text(
                                            caption = model.price.text,
                                            bold = true,
                                            color = Theme.COLOR_PRIMARY
                                        ),
                                        if (model.comparablePrice != null)
                                            Text(
                                                caption = model.comparablePrice.text,
                                                color = Theme.COLOR_GRAY,
                                                decoration = TextDecoration.Strikethrough,
                                                size = Theme.TEXT_SIZE_SMALL
                                            )
                                        else
                                            null,
                                    ),
                                )
                            else
                                null,

                            Container(padding = 5.0),
                            Text(
                                caption = StringUtil.capitalizeFirstLetter(model.title),
                                overflow = TextOverflow.Elipsis,
                                maxLines = 3
                            ),
                        ),
                    )
                ),
            )
        )
    )
}
