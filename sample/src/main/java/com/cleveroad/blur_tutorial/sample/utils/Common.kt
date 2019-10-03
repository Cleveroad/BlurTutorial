package com.cleveroad.blur_tutorial.sample.utils

import com.cleveroad.blur_tutorial.sample.exceptions.NotImplementedInterfaceException


inline fun <reified T> bindInterfaceOrThrow(vararg objects: Any?):
        T = objects.find { it is T } as T
        ?: throw NotImplementedInterfaceException(T::class.java)