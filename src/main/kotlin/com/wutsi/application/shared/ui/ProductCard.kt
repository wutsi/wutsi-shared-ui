package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.application.shared.model.ProductModel
import com.wutsi.application.shared.service.StringUtil
import com.wutsi.flutter.sdui.Action
import com.wutsi.flutter.sdui.AspectRatio
import com.wutsi.flutter.sdui.Center
import com.wutsi.flutter.sdui.Column
import com.wutsi.flutter.sdui.Container
import com.wutsi.flutter.sdui.Image
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
    private val action: Action? = null
) : CompositeWidgetAware() {
    companion object {
        const val IMAGE_HEIGHT = 200.0
        const val PADDING = 5.0
    }

    override fun toWidgetAware(): WidgetAware = Container(
        border = 0.5,
        borderColor = Theme.COLOR_DIVIDER,
        action = action,
        padding = PADDING,
        child = Column(
            mainAxisAlignment = MainAxisAlignment.start,
            crossAxisAlignment = CrossAxisAlignment.start,
            children = listOf(
                Container(
                    child = Center(
                        child = AspectRatio(
                            aspectRatio = 4.0 / 3.0,
                            child = Image(
                                url = model.thumbnail?.url ?: "",
                                fit = BoxFit.fitWidth,
                                height = IMAGE_HEIGHT,
                            )
                        )
                    ),
                ),
                Container(padding = PADDING),
                Container(
                    height = 5 * Theme.TEXT_SIZE_SMALL,
                    alignment = Alignment.Center,
                    child = Column(
                        mainAxisAlignment = MainAxisAlignment.start,
                        crossAxisAlignment = CrossAxisAlignment.start,
                        children = listOfNotNull(

                            if (model.price != null)
                                Text(
                                    caption = model.price.text,
                                    bold = true,
                                    color = if (model.comparablePrice != null) Theme.COLOR_PRIMARY else Theme.COLOR_BLACK
                                )
                            else
                                null,

                            if (model.comparablePrice != null)
                                Text(
                                    caption = model.comparablePrice.text,
                                    size = Theme.TEXT_SIZE_SMALL,
                                    decoration = TextDecoration.Strikethrough,
                                )
                            else
                                null,

                            Text(
                                caption = StringUtil.capitalizeFirstLetter(model.title),
                                overflow = TextOverflow.Elipsis,
                                size = Theme.TEXT_SIZE_SMALL,
                                maxLines = 2
                            ),
                        ),
                    )
                ),
            ),
        )
    )
}
