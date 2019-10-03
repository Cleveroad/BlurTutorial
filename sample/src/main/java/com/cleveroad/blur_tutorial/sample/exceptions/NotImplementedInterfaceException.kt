package com.cleveroad.blur_tutorial.sample.exceptions

/**
 * Exception for cases when someone trying to use instances of class without interface implementation
 */
class NotImplementedInterfaceException(clazz: Class<*>) :
        ClassCastException(String.format(MESSAGE_TEMPLATE, clazz)) {

    companion object {
        private const val MESSAGE_TEMPLATE = "You need to implement %s"
    }
}