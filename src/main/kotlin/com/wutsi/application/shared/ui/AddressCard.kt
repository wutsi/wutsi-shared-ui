package com.wutsi.application.shared.ui

import com.wutsi.application.shared.Theme
import com.wutsi.application.shared.model.AddressModel
import com.wutsi.flutter.sdui.Column
import com.wutsi.flutter.sdui.Text
import com.wutsi.flutter.sdui.WidgetAware
import com.wutsi.flutter.sdui.enums.CrossAxisAlignment
import com.wutsi.flutter.sdui.enums.MainAxisAlignment

class AddressCard(
    private val model: AddressModel,
    private val textSize: Double = Theme.TEXT_SIZE_DEFAULT,
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware = Column(
        mainAxisAlignment = MainAxisAlignment.start,
        crossAxisAlignment = CrossAxisAlignment.start,
        children = listOfNotNull(
            toText(model.fullName.trim(), bold = true),
            model.street?.let { toText(it, maxLines = 3) },
            toText(model.location),
            model.zipCode?.let { toText(it) }
        )
    )

    private fun toText(caption: String, maxLines: Int? = null, bold: Boolean? = null): WidgetAware =
        Text(caption, size = textSize, maxLines = maxLines, bold = bold)
}
