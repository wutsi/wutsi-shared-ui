package com.wutsi.application.shared.ui

import com.wutsi.application.shared.model.AddressModel
import com.wutsi.flutter.sdui.Column
import com.wutsi.flutter.sdui.Text
import com.wutsi.flutter.sdui.WidgetAware
import com.wutsi.flutter.sdui.enums.CrossAxisAlignment
import com.wutsi.flutter.sdui.enums.MainAxisAlignment

class AddressCard(
    private val model: AddressModel,
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware = Column(
        mainAxisAlignment = MainAxisAlignment.start,
        crossAxisAlignment = CrossAxisAlignment.start,
        children = listOfNotNull(
            Text(model.fullName.trim()),
            model.street?.let { Text(it, maxLines = 3) },
            Text(model.location),
            model.zipCode?.let { Text(it) }
        )
    )
}
