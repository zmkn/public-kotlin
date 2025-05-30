package com.zmkn.extension

import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

/**
 * 查找类的属性
 * @param propertyName 属性名称
 * @return 属性，未找到时返回 null
 */
fun <T : Any> KClass<out T>.findProperty(propertyName: String): KProperty1<out T, *>? = memberProperties
    .firstOrNull {
        it.name == propertyName
    }?.apply {
        if (!isAccessible) {
            isAccessible = true
        }
    }

/**
 * 查找类的属性的类型
 * @param propertyName 属性名称
 * @return 属性类型，未找到时返回 null
 */
fun <T : Any> KClass<out T>.findPropertyType(propertyName: String): KClassifier? = findProperty(propertyName)?.returnType?.classifier

/**
 * 查找类的属性的类型（带类型转换）
 * @param propertyName 属性名称
 * @return 属性类型，未找到或类型不匹配时返回 null
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any, R : Any> KClass<out T>.findPropertyTypeAs(propertyName: String): R? = findPropertyType(propertyName) as? R
