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

package com.github.oneandolaf.jsonext.extensions

import com.github.oneandolaf.jsonext.readonly.JSONArrayUnmodifiable
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Creates a deep copy of this array.
 */
fun JSONArray.deepCopy(): JSONArray {
    val copy = JSONArray()

    for (item in this) {
        when (item) {
            is JSONObject -> {
                copy.put(item.deepCopy())
            }
            is JSONArray -> {
                copy.put(item.deepCopy())
            }
            else -> {
                copy.put(item)
            }
        }
    }

    return copy
}

/**
 * Returns a list of all `boolean` items in the array. The list returned represents a snapshot of the JSONArray
 * and is not kept synchronized.
 */
fun JSONArray.toBooleanList(): List<Boolean> {
    return filterIsInstance<Boolean>()
}

/**
 * Returns a list of all `double` items in the array. The list returned represents a snapshot of the JSONArray
 * and is not kept synchronized.
 */
fun JSONArray.toDoubleList(): List<Double> {
    return filterIsInstance<Double>()
}

/**
 * Returns a list of all `float` items in the array. The list returned represents a snapshot of the JSONArray
 * and is not kept synchronized.
 */
fun JSONArray.toFloatList(): List<Float> {
    return filterIsInstance<Float>()
}

/**
 * Returns a list of all [Number] items in the array. The list returned represents a snapshot of the JSONArray
 * and is not kept synchronized.
 */
fun JSONArray.toNumberList(): List<Number> {
    return filterIsInstance<Number>()
}

/**
 * Returns a list of all enum items in the array. The list returned represents a snapshot of the JSONArray
 * and is not kept synchronized.
 */
fun <E: Enum<E>> JSONArray.toEnumList(enumClass: Class<E>): List<E> {
    return filterIsInstance(enumClass)
}

/**
 * Returns a list of all [BigDecimal] items in the array. The list returned represents a snapshot of the JSONArray
 * and is not kept synchronized.
 */
fun JSONArray.toBigDecimalList(): List<BigDecimal> {
    return filterIsInstance<BigDecimal>()
}

/**
 * Returns a list of all [BigInteger] items in the array. The list returned represents a snapshot of the JSONArray
 * and is not kept synchronized.
 */
fun JSONArray.toBigIntegerList(): List<BigInteger> {
    return filterIsInstance<BigInteger>()
}

/**
 * Returns a list of all `int` items in the array. The list returned represents a snapshot of the JSONArray
 * and is not kept synchronized.
 */
fun JSONArray.toIntList(): List<Int> {
    return filterIsInstance<Int>()
}

/**
 * Returns a list of all [JSONArray] items in the array. The list returned represents a snapshot of the JSONArray
 * and is not kept synchronized.
 */
fun JSONArray.toJSONArrayList(): List<JSONArray> {
    return filterIsInstance<JSONArray>()
}

/**
 * Returns a list of all [JSONObject] items in the array. The list returned represents a snapshot of the JSONArray
 * and is not kept synchronized.
 */
fun JSONArray.toJSONObjectList(): List<JSONObject> {
    return filterIsInstance<JSONObject>()
}

/**
 * Returns a list of all `long` items in the array. The list returned represents a snapshot of the JSONArray
 * and is not kept synchronized.
 */
fun JSONArray.toLongList(): List<Long> {
    return filterIsInstance<Long>()
}

/**
 * Returns a list of all [String] items in the array. The list returned represents a snapshot of the JSONArray
 * and is not kept synchronized.
 */
fun JSONArray.toStringList(): List<String> {
    return filterIsInstance<String>()
}



/**
 * Creates a read-only array backed by this one.
 */
fun JSONArray.asUnmodifiable(): JSONArrayUnmodifiable {
    return JSONArrayUnmodifiable(this)
}