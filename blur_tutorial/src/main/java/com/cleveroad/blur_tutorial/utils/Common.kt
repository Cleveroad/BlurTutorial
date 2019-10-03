package com.cleveroad.blur_tutorial.utils

/**
 * Helper for miscellaneous operations
 */
class Common {

    companion object {
        private const val EXTRA = "EXTRA"
        var defaultPackageName: String = ""

        /**
         * Get String with name for EXTRA parameter.
         * pattern packageName + class.simpleName + EXTRA + extraName
         */
        @JvmOverloads
        fun <T> getExtra(extraName: String,
                         clazz: Class<T>? = null,
                         packageName: String = defaultPackageName): String {
            return (if (defaultPackageName.isNotBlank()) defaultPackageName else packageName)
                    .let { "${it}_${clazz?.simpleName ?: ""}_${EXTRA}_$extraName" }
        }
    }
}