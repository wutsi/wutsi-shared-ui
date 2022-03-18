package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.application.shared.model.ActionModel
import com.wutsi.application.shared.model.PriceSummaryModel
import com.wutsi.application.shared.service.TranslationUtil.getText
import com.wutsi.flutter.sdui.Button
import com.wutsi.flutter.sdui.Column
import com.wutsi.flutter.sdui.Container
import com.wutsi.flutter.sdui.Row
import com.wutsi.flutter.sdui.Text
import com.wutsi.flutter.sdui.WidgetAware
import com.wutsi.flutter.sdui.enums.MainAxisAlignment
import com.wutsi.flutter.sdui.enums.MainAxisSize
import com.wutsi.flutter.sdui.enums.TextAlignment

class PriceSummaryCard(
    private val model: PriceSummaryModel,
    private val action: ActionModel? = null,
    private val showPaymentStatus: Boolean = false,
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware {
        val children = mutableListOf<WidgetAware?>(
            toRow(
                getText(
                    if (model.itemCount > 1)
                        "shared-ui.price-summary.sub-total-n-products"
                    else
                        "shared-ui.price-summary.sub-total",
                    arrayOf(model.itemCount.toString())
                ),
                model.subTotal.text
            ),
            model.deliveryFees?.let {
                toRow(getText("shared-ui.price-summary.delivery-fees"), it.text)
            },
            model.savings?.text?.let {
                toRow(
                    getText("shared-ui.price-summary.savings"),
                    "-$it",
                    false,
                    Theme.COLOR_SUCCESS
                )
            },
            Container(
                background = Theme.COLOR_PRIMARY_LIGHT,
                child = toRow(
                    getText("shared-ui.price-summary.total"),
                    model.total.text,
                    true,
                    Theme.COLOR_PRIMARY
                ),
            ),
        )

        if (showPaymentStatus)
            children.addAll(
                listOf(
                    Container(
                        background = Theme.COLOR_PRIMARY_LIGHT,
                        child = toRow(
                            getText("shared-ui.price-summary.total-paid"),
                            model.totalPaid.text,
                            true,
                        ),
                    ),
                    Container(
                        background = Theme.COLOR_PRIMARY_LIGHT,
                        child = toRow(
                            getText("shared-ui.price-summary.balance"),
                            model.balance.text,
                            true,
                            if (model.paid) Theme.COLOR_SUCCESS else null
                        ),
                    )
                )
            )

        if (action != null)
            children.addAll(
                listOf(
                    Container(padding = 10.0),
                    Button(
                        caption = action.caption,
                        action = action.action
                    )
                )
            )

        return Container(
            padding = 10.0,
            border = 1.0,
            borderColor = Theme.COLOR_DIVIDER,
            borderRadius = 2.0,
            margin = 10.0,
            child = Column(
                mainAxisAlignment = MainAxisAlignment.start,
                mainAxisSize = MainAxisSize.min,
                children = children.filterNotNull()
            ),
        )
    }

    private fun toRow(
        name: String,
        value: String,
        bold: Boolean = false,
        color: String? = null
    ) = Row(
        children = listOf(
            Container(
                padding = 10.0,
                child = Text(name, bold = bold),
            ),
            Container(
                padding = 10.0,
                child = Text(
                    value,
                    bold = bold,
                    color = color,
                    alignment = TextAlignment.Right,
                )
            )
        ),
        mainAxisAlignment = MainAxisAlignment.spaceBetween
    )
}
