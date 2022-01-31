package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.application.shared.model.ProductModel
import com.wutsi.flutter.sdui.Action
import com.wutsi.flutter.sdui.Column
import com.wutsi.flutter.sdui.Container
import com.wutsi.flutter.sdui.Text
import com.wutsi.flutter.sdui.WidgetAware
import com.wutsi.flutter.sdui.enums.CrossAxisAlignment
import com.wutsi.flutter.sdui.enums.MainAxisAlignment
import com.wutsi.flutter.sdui.enums.TextAlignment
import com.wutsi.flutter.sdui.enums.TextOverflow

class ProductCard(
    private val model: ProductModel,
    private val action: Action? = null
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware = Container(
        border = 1.0,
        borderRadius = 5.0,
        borderColor = Theme.COLOR_DIVIDER,
        action = action,
        child = Column(
            mainAxisAlignment = MainAxisAlignment.start,
            crossAxisAlignment = CrossAxisAlignment.start,
            children = listOf(
                Container(
                    backgroundImageUrl = model.thumbnail?.url,
                    height = 200.0
                ),
                Container(
                    padding = 5.0,
                    child = Column(
                        mainAxisAlignment = MainAxisAlignment.start,
                        crossAxisAlignment = CrossAxisAlignment.start,
                        children = listOfNotNull(
                            Text(
                                caption = model.title,
                                overflow = TextOverflow.Elipsis,
                                alignment = TextAlignment.Left
                            ),
                            if (model.price != null)
                                Text(
                                    caption = model.price.text,
                                    bold = true
                                )
                            else
                                null
                        ),
                    )
                )
            ),
        ),
    )
}
