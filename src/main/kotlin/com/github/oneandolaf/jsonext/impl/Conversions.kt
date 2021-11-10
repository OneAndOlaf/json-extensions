/*
 * Copyright (c) 2021 OneAndOlaf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.oneandolaf.jsonext.impl

import org.json.JSONObject
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Utility to convert arbitrary objects to JSON types. These methods usually employ the same rules as used by JSON-Java.
 */
object Conversions {

    private val jsonObjHelper = object : JSONObject() {
        fun checkIsDecimalNotation(value: String): Boolean {
            return isDecimalNotation(value)
        }

        fun convStringToNumber(value: String): Number {
            return stringToNumber(value)
        }
    }

    /**
     * Attempts to convert an object to a [BigDecimal]. This will return `true` in the following cases:
     *
     * - `obj` is a finite [Number]
     * - `obj` is the string representation of a number
     *
     * @param obj the object to convert
     * @return the converted [BigDecimal], or `null` if conversion failed
     */
    @JvmStatic
    fun toBigDecimal(obj: Any?): BigDecimal? {
        return when (obj) {
            null -> null
            is BigDecimal -> obj
            is BigInteger -> obj.toBigDecimal()
            is Double -> if (obj.isFinite()) obj.toBigDecimal() else null
            is Float -> if (obj.isFinite()) obj.toBigDecimal() else null
            is Number -> obj.toLong().toBigDecimal()
            else -> obj.toString().toBigDecimalOrNull()
        }
    }

    /**
     * Attempts to convert an object to a [BigInteger]. This will return `true` in the following cases:
     *
     * - `obj` is a finite [Number]
     * - `obj` is the string representation of a number
     *
     * @param obj the object to convert
     * @return the converted [BigInteger], or `null` if conversion failed
     */
    @JvmStatic
    fun toBigInteger(obj: Any?): BigInteger? {
        return when (obj) {
            null -> null
            is BigInteger -> obj
            is BigDecimal -> obj.toBigInteger()
            is Double -> if (obj.isFinite()) obj.toBigDecimal().toBigInteger() else null
            is Float -> if (obj.isFinite()) obj.toBigDecimal().toBigInteger() else null
            is Number -> obj.toLong().toBigInteger()
            else -> {
                val strObj = obj.toString()
                if (jsonObjHelper.checkIsDecimalNotation(strObj)) {
                    strObj.toBigDecimalOrNull()?.toBigInteger()
                } else {
                    strObj.toBigIntegerOrNull()
                }
            }
        }
    }

    /**
     * Attempts to convert an object to a [Boolean]. This will return `true` in the following cases:
     *
     * - `obj` is a [Boolean] already
     * - `obj` is the [String] representation of a boolean (case-insensitive)
     *
     * @param obj the object to convert
     * @return the converted [Boolean], or `null` if conversion failed
     */
    @JvmStatic
    fun toBoolean(obj: Any?): Boolean? {
        return when {
            obj == null -> null
            obj is Boolean -> obj
            "false".equals(obj.toString(), ignoreCase = true) -> false
            "true".equals(obj.toString(), ignoreCase = true) -> true
            else -> null
        }
    }

    /**
     * Attempts to convert an object to a [Double]. This will return `true` in the following cases:
     *
     * - `obj` is a [Number]
     * - `obj` is the string representation of a number
     *
     * @param obj the object to convert
     * @return the converted [Double], or `null` if conversion failed
     */
    @JvmStatic
    fun toDouble(obj: Any?): Double? {
        return when (obj) {
            null -> null
            is Number -> obj.toDouble()
            else -> obj.toString().toDoubleOrNull()
        }
    }

    /**
     * Attempts to convert an object to an [Enum]. This will return `true` in the following cases:
     *
     * - `obj` is an instance of `enumClass`
     * - `obj` is the [String] representation of an instance of `enumClass` (case sensitive)
     *
     * @param obj the object to convert
     * @return the converted [Boolean], or `null` if conversion failed
     */
    @JvmStatic
    fun <E : Enum<E>> toEnum(obj: Any?, enumClass: Class<E>): E? {
        return when {
            obj == null -> null
            enumClass.isInstance(obj) -> enumClass.cast(obj)
            else -> {
                try {
                    java.lang.Enum.valueOf(enumClass, obj.toString())
                } catch (e: java.lang.Exception) {
                    null
                }

            }
        }
    }

    /**
     * Attempts to convert an object to an [Enum]. This will return `true` in the following cases:
     *
     * - `obj` is an instance of `E`
     * - `obj` is the [String] representation of an instance of `E` (case sensitive)
     *
     * This method is only callable from Kotlin.
     *
     * @param obj the object to convert
     * @return the converted [Boolean], or `null` if conversion failed
     */
    inline fun <reified E : Enum<E>> toEnum(obj: Any?): E? {
        return toEnum(obj, E::class.java)
    }

    /**
     * Attempts to convert an object to a [Float]. This will return `true` in the following cases:
     *
     * - `obj` is a [Number]
     * - `obj` is the string representation of a number
     *
     * @param obj the object to convert
     * @return the converted [Float], or `null` if conversion failed
     */
    @JvmStatic
    fun toFloat(obj: Any?): Float? {
        return when (obj) {
            null -> null
            is Number -> obj.toFloat()
            else -> obj.toString().toFloatOrNull()
        }
    }

    /**
     * Attempts to convert an object to an [Int]. This will return `true` in the following cases:
     *
     * - `obj` is a [Number]
     * - `obj` is the string representation of an integer
     *
     * This means that `1.0` (a floating-point number) and `"1"` (the string representation of an integer) will
     * successfully convert to `1`, but `"1.0"` (the string representation of a floating-point number) will not.
     *
     * @param obj the object to convert
     * @return the converted [Int], or `null` if conversion failed
     */
    @JvmStatic
    fun toInt(obj: Any?): Int? {
        return when (obj) {
            null -> null
            is Number -> obj.toInt()
            else -> obj.toString().toIntOrNull()
        }
    }

    /**
     * Attempts to convert an object to a [Long]. This will return `true` in the following cases:
     *
     * - `obj` is a [Number]
     * - `obj` is the string representation of a long
     *
     * This means that `1.0` (a floating-point number) and `"1"` (the string representation of an integer) will
     * successfully convert to `1`, but `"1.0"` (the string representation of a floating-point number) will not.
     *
     * @param obj the object to convert
     * @return the converted [Long], or `null` if conversion failed
     */
    @JvmStatic
    fun toLong(obj: Any?): Long? {
        return when (obj) {
            null -> null
            is Number -> obj.toLong()
            else -> obj.toString().toLongOrNull()
        }
    }

    /**
     * Attempts to convert an object to a [Number]. This will return `true` in the following cases:
     *
     * - `obj` is a [Number]
     * - `obj` is the string representation of a number
     *
     * This means that `1.0` (a floating-point number) and `"1"` (the string representation of an integer) will
     * successfully convert to `1`, but `"1.0"` (the string representation of a floating-point number) will not.
     *
     * @param obj the object to convert
     * @return the converted [Number], or `null` if conversion failed
     */
    @JvmStatic
    fun toNumber(obj: Any?): Number? {
        return when (obj) {
            null -> null
            is Number -> obj
            else -> try {
                jsonObjHelper.convStringToNumber(obj.toString())
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Attempts to convert an object to a [String]. This operation has two modes: a coercing mode and a non-coercing
     * mode. If coercing, any non-null object will be converted to a [String] using its [Any.toString] method. If
     * non-coercing, only actual [String]s will be returned, and any non-string will result in `null`.
     *
     * @param obj the object to convert
     * @param coerce whether to use string coercion
     * @return the converted [String], or `null` if conversion failed
     */
    @JvmStatic
    fun toString(obj: Any?, coerce: Boolean): String? {
        return when {
            !coerce -> obj as? String
            null != obj -> obj.toString()
            else -> null
        }
    }


}