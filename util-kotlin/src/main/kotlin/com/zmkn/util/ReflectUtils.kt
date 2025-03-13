package com.zmkn.util

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

object ReflectUtils {
    fun getKClassOrNull(schema: KClass<*>, fieldName: String): KClass<*>? {
        return schema.memberProperties.find {
            it.name == fieldName
        }?.let {
            it.returnType.classifier as? KClass<*>
        }
    }

    fun getKClass(schema: KClass<*>, fieldName: String): KClass<*> {
        return getKClassOrNull(schema, fieldName) ?: throw IllegalArgumentException("The field name '$fieldName' does not match any property in the class ${schema.simpleName}.")
    }
}
