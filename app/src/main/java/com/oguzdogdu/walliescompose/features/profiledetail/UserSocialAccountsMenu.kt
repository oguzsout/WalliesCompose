package com.oguzdogdu.walliescompose.features.profiledetail

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.painter.Painter

data class UserSocialAccountsMenu(
    val title: String,
    val titleIcon: Painter,
    val menuItemType: MenuItemType
) {
    enum class MenuItemType {
        INSTAGRAM,
        TWITTER,
        PORTFOLIO
    }
}
