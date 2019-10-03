package com.cleveroad.blur_tutorial.ext

internal fun Any.getPrivateField(fieldName: String): Any? =
        try {
            javaClass.getDeclaredField(fieldName).apply {
                isAccessible = true
            }.get(this)
        } catch (exc: NoSuchFieldException) {
            null
        }

internal fun Any.getParentPrivateField(fieldName: String): Any? =
        try {
            javaClass.superclass?.getDeclaredField(fieldName)?.apply {
                isAccessible = true
            }?.get(this)
        } catch (exc: NoSuchFieldException) {
            null
        }

internal inline fun <T> T.applyIf(predicate: (T) -> Boolean = { true }, block: T.() -> Unit): T = apply {
    if (predicate(this)) block()
}

internal fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1? = null,
                                          p2: T2? = null,
                                          block: (T1, T2) -> R?): R? =
        p1?.let { param1 ->
            p2.takeUnless { it == null }?.let { block(param1, it) }
        }