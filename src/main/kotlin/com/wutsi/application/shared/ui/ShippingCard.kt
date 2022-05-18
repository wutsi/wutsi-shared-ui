package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.application.shared.model.ShippingModel
import com.wutsi.application.shared.service.TranslationUtil.getText
import com.wutsi.flutter.sdui.Column
import com.wutsi.flutter.sdui.Text
import com.wutsi.flutter.sdui.WidgetAware
import com.wutsi.flutter.sdui.enums.CrossAxisAlignment
import com.wutsi.flutter.sdui.enums.MainAxisAlignment

class ShippingCard(
    private val model: ShippingModel,
    private val textSize: Double = Theme.TEXT_SIZE_DEFAULT,
    private val showShoppingInstructions: Boolean = true,
    private val showExpectedDeliveryDate: Boolean = true,
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware = Column(
        mainAxisAlignment = MainAxisAlignment.start,
        crossAxisAlignment = CrossAxisAlignment.start,
        children = listOfNotNull(
            Text(
                model.type +
                    if (showExpectedDeliveryDate && model.expectedDelivered != null)
                        " - " + getText(
                            key = "shared-ui.shipping-cart.expected-delivery-date",
                            args = arrayOf(model.expectedDelivered),
                        )
                    else
                        "",
                size = textSize
            ),

            if (showShoppingInstructions && model.message != null)
                Text(model.message, maxLines = 5, size = textSize)
            else
                null,
        )
    )
}
