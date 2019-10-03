package com.cleveroad.blur_tutorial.explanator.extract.reflectors

import android.view.View
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.recyclerview.widget.RecyclerView
import com.cleveroad.blur_tutorial.ext.getPrivateField
import com.google.android.material.internal.NavigationMenuPresenter
import com.google.android.material.navigation.NavigationView

/**
 * Realization of [MenuReflector] for [NavigationView].
 */
internal class NavigationViewReflector: MenuReflector {

    companion object {
        private const val PRESENTER_FIELD_NAME = "presenter"
        private const val ADAPTER_FIELD_NAME = "adapter"
        private const val ITEMS_FIELD_NAME = "items"
        private const val MENU_ITEM_FIELD_NAME = "menuItem"
        private const val MENU_VIEW_FIELD_NAME = "menuView"
    }

    override fun extractMenuItemView(view: View, itemId: Int, onViewExtracted: (View?) -> Unit) {
        (view.getPrivateField(PRESENTER_FIELD_NAME) as? NavigationMenuPresenter)?.let { menuPresenter ->
            (menuPresenter.getPrivateField(ADAPTER_FIELD_NAME)
                    ?.getPrivateField(ITEMS_FIELD_NAME) as? ArrayList<*>)
                    ?.indexOfFirst {
                        (it.getPrivateField(MENU_ITEM_FIELD_NAME) as? MenuItemImpl)?.itemId == itemId
                    }?.let { itemIndex ->
                        (menuPresenter.getPrivateField(MENU_VIEW_FIELD_NAME) as? RecyclerView)?.run {
                            scrollToPosition(itemIndex)
                            post {
                                onViewExtracted(findViewHolderForAdapterPosition(itemIndex)?.itemView)
                            }
                        }
                    }
        }
    }
}