package com.wutsi.application.shared.ui

import com.wutsi.application.shared.model.AddressModel
import com.wutsi.ecommerce.order.entity.AddressType
import com.wutsi.flutter.sdui.Column
import com.wutsi.flutter.sdui.Text
import com.wutsi.flutter.sdui.WidgetAware
import com.wutsi.flutter.sdui.enums.CrossAxisAlignment
import com.wutsi.flutter.sdui.enums.MainAxisAlignment

class AddressCard(
    private val model: AddressModel,
    private val showPostalAddress: Boolean = true,
    private val showEmailAddress: Boolean = true,
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware = Column(
        mainAxisAlignment = MainAxisAlignment.start,
        crossAxisAlignment = CrossAxisAlignment.start,
        children = listOfNotNull(
            Text(model.fullName.trim(), bold = true),

            if (model.type == AddressType.POSTAL && showPostalAddress)
                postalAddressLabel()?.let { Text(it, maxLines = 5) }
            else if (model.type == AddressType.EMAIL && showEmailAddress)
                model.email?.let { Text(it) }
            else
                null,
        )
    )

    private fun postalAddressLabel(): String? {
        val address = listOfNotNull(
            model.street?.trim(),
            model.location.trim(),
            model.zipCode?.trim()
        ).joinToString("\n")
        return if (address.isEmpty()) null else address
    }
}
