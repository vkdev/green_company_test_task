package ru.vkdev.greentest.ui_common.dimen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import ru.vkdev.greentest.ui_common.R

object DimensList {
    val contentPadding @Composable get() = dimensionResource(R.dimen.list__content_padding)
    val verticalSpacing @Composable get() = dimensionResource(R.dimen.list__vertical_spacing)
}