package com.cleveroad.blur_tutorial.explanator.extract.reflectors

import android.view.View
import androidx.appcompat.view.menu.MenuItemImpl
import com.cleveroad.blur_tutorial.ext.children
import com.cleveroad.blur_tutorial.ext.getPrivateField
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Realization of [MenuReflector] for [BottomNavigationView].
 */
internal class BottomNavigationReflector: MenuReflector {

    companion object {
        private const val MENU_VIEW_FIELD_NAME = "menuView"
        private const val ITEM_FIELD_NAME = "itemData"
    }

    override fun extractMenuItemView(view: View, itemId: Int, onViewExtracted: (View?) -> Unit) {
        (view.getPrivateField(MENU_VIEW_FIELD_NAME) as? BottomNavigationMenuView)?.run {
            children.mapNotNull { it as? BottomNavigationItemView }.find {
                (it.getPrivateField(ITEM_FIELD_NAME) as? MenuItemImpl)?.itemId == itemId
            }.let {
                onViewExtracted(it)
            }
        }
    }
}