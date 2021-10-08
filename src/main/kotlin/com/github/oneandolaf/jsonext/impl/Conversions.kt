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

object Conversions {

    private val jsonObjHelper = object : JSONObject() {
        fun checkIsDecimalNotation(value: String): Boolean {
            return isDecimalNotation(value)
        }

        fun convStringToNumber(value: String): Number {
            return stringToNumber(value)
        }
    }

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
                }
                strObj.toBigIntegerOrNull()
            }
        }
    }

    fun toBoolean(obj: Any?): Boolean? {
        return when {
            obj == null -> null
            obj is Boolean -> obj
            "false".equals(obj.toString(), ignoreCase = true) -> false
            "true".equals(obj.toString(), ignoreCase = true) -> true
            else -> null
        }
    }

    fun toDouble(obj: Any?): Double? {
        return when (obj) {
            null -> null
            is Number -> obj.toDouble()
            else -> obj.toString().toDoubleOrNull()
        }
    }

    fun <E : Enum<E>> toEnum(obj: Any?, enumClass: Class<E>): E? {
        return when (obj) {
            null -> null
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

    fun toFloat(obj: Any?): Float? {
        return when (obj) {
            null -> null
            is Number -> obj.toFloat()
            else -> obj.toString().toFloatOrNull()
        }
    }

    fun toInt(obj: Any?): Int? {
        return when (obj) {
            null -> null
            is Number -> obj.toInt()
            else -> obj.toString().toIntOrNull()
        }
    }

    fun toLong(obj: Any?): Long? {
        return when (obj) {
            null -> null
            is Number -> obj.toLong()
            else -> obj.toString().toLongOrNull()
        }
    }

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

    fun toString(obj: Any?, coerce: Boolean): String? {
        @Suppress("IfThenToSafeAccess") // we need to handle JSONObject.NULL, which is == null
        return when {
            coerce -> {
                obj as? String
            }
            null != obj -> {
                obj.toString()
            }
            else -> {
                null
            }
        }
    }


}