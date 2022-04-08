package com.wutsi.application.shared.ui

import com.wutsi.application.shared.model.ProductModel
import com.wutsi.flutter.sdui.Action
import com.wutsi.flutter.sdui.Column
import com.wutsi.flutter.sdui.Container
import com.wutsi.flutter.sdui.Flexible
import com.wutsi.flutter.sdui.Row
import com.wutsi.flutter.sdui.WidgetAware
import com.wutsi.flutter.sdui.enums.CrossAxisAlignment
import com.wutsi.flutter.sdui.enums.MainAxisAlignment
import com.wutsi.flutter.sdui.enums.MainAxisSize

class ProductGridView(
    private val models: List<ProductModel>,
    private val savingsPercentageThreshold: Int = 1,
    private val productsPerRow: Int = 2,
    private val spacing: Double = 5.0,
    private val actionProvider: ProductActionProvider
) : CompositeWidgetAware() {
    override fun toWidgetAware(): WidgetAware {
        val productRows = toProductRows(models, productsPerRow)
        return Column(
            crossAxisAlignment = CrossAxisAlignment.start,
            mainAxisAlignment = MainAxisAlignment.start,
            children = productRows.map {
                toRowWidget(it)
            },
        )
    }

    private fun toRowWidget(items: List<ProductModel>): WidgetAware {
        val widgets = mutableListOf<WidgetAware>()
        widgets.addAll(items.map { toProductWidget(it) })
        while (widgets.size < productsPerRow) // Padding
            widgets.add(Container())

        return Row(
            mainAxisAlignment = MainAxisAlignment.start,
            crossAxisAlignment = CrossAxisAlignment.start,
            mainAxisSize = MainAxisSize.min,
            children = widgets.map {
                Flexible(
                    child = it
                )
            },
        )
    }

    private fun toProductRows(products: List<ProductModel>, size: Int): List<List<ProductModel>> {
        val rows = mutableListOf<List<ProductModel>>()
        var cur = mutableListOf<ProductModel>()
        products.forEach {
            cur.add(it)
            if (cur.size == size) {
                rows.add(cur)
                cur = mutableListOf()
            }
        }
        if (cur.isNotEmpty())
            rows.add(cur)
        return rows
    }

    private fun toProductWidget(model: ProductModel) = ProductCard(
        margin = spacing,
        savingsPercentageThreshold = savingsPercentageThreshold,
        model = model,
        action = actionProvider.getAction(model),
    )
}

interface ProductActionProvider {
    fun getAction(model: ProductModel): Action
}
