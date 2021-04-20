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

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal
import java.math.BigInteger


/**
 * Puts a key/boolean pair into the object if the key does not exist yet.
 *
 * @param key the key
 * @param value the value to put
 * @return this
 * @throws JSONException if the value is a non-finite number
 * @throws NullPointerException if the key is `null`
 */
@Throws(JSONException::class)
fun JSONObject.putIfAbsent(key: String, value: Boolean): JSONObject {
    if (!this.has(key)) {
        put(key, value)
    }
    return this
}

/**
 * Puts a key/collection pair into the object if the key does not exist yet.
 *
 * @param key the key
 * @param value the value to put, which will be converted into a [JSONArray]
 * @return this
 * @throws JSONException if the value is a non-finite number
 * @throws NullPointerException if the key is `null`
 */
@Throws(JSONException::class)
fun JSONObject.putIfAbsent(key: String, value: Collection<*>): JSONObject {
    if (!this.has(key)) {
        put(key, value)
    }
    return this
}

/**
 * Puts a key/double pair into the object if the key does not exist yet.
 *
 * @param key the key
 * @param value the value to put
 * @return this
 * @throws JSONException if the value is a non-finite number
 * @throws NullPointerException if the key is `null`
 */
@Throws(JSONException::class)
fun JSONObject.putIfAbsent(key: String, value: Double): JSONObject {
    if (!this.has(key)) {
        put(key, value)
    }
    return this
}

/**
 * Puts a key/float pair into the object if the key does not exist yet.
 *
 * @param key the key
 * @param value the value to put
 * @return this
 * @throws JSONException if the value is a non-finite number
 * @throws NullPointerException if the key is `null`
 */
@Throws(JSONException::class)
fun JSONObject.putIfAbsent(key: String, value: Float): JSONObject {
    if (!this.has(key)) {
        put(key, value)
    }
    return this
}

/**
 * Puts a key/int pair into the object if the key does not exist yet.
 *
 * @param key the key
 * @param value the value to put
 * @return this
 * @throws JSONException if the value is a non-finite number
 * @throws NullPointerException if the key is `null`
 */
@Throws(JSONException::class)
fun JSONObject.putIfAbsent(key: String, value: Int): JSONObject {
    if (!this.has(key)) {
        put(key, value)
    }
    return this
}

/**
 * Puts a key/long pair into the object if the key does not exist yet.
 *
 * @param key the key
 * @param value the value to put
 * @return this
 * @throws JSONException if the value is a non-finite number
 * @throws NullPointerException if the key is `null`
 */
@Throws(JSONException::class)
fun JSONObject.putIfAbsent(key: String, value: Long): JSONObject {
    if (!this.has(key)) {
        put(key, value)
    }
    return this
}

/**
 * Puts a key/map pair into the object if the key does not exist yet.
 *
 * @param key the key
 * @param value the value to put, which will be converted into a [JSONObject]
 * @return this
 * @throws JSONException if the value is a non-finite number
 * @throws NullPointerException if the key is `null`
 */
@Throws(JSONException::class)
fun JSONObject.putIfAbsent(key: String, value: Map<*,*>): JSONObject {
    if (!this.has(key)) {
        put(key, value)
    }
    return this
}

/**
 * Puts a key/map pair into the object if the key does not exist yet.
 *
 * @param key the key
 * @param value the value to put
 * @return this
 * @throws JSONException if the value is a non-finite number
 * @throws NullPointerException if the key is `null`
 */
@Throws(JSONException::class)
fun JSONObject.putIfAbsent(key: String, value: Any): JSONObject {
    if (!this.has(key)) {
        put(key, value)
    }
    return this
}

/**
 * Puts a key/long pair into the object if the key does not exist yet and both the key and the object are non-`null`.
 *
 * @param key the key
 * @param value the value to put
 * @return this
 * @throws JSONException if the value is a non-finite number
 */
@Throws(JSONException::class)
fun JSONObject.putOptIfAbsent(key: String?, value: Any?): JSONObject {
    if (key != null && value != null && !this.has(key)) {
        put(key, value)
    }
    return this
}

/**
 * Gets a value from the object if it exists and is a number, or puts a default value and returns it otherwise.
 *
 * @param key the key
 * @param value the default value to put
 * @return the value
 */
fun JSONObject.getOrPut(key: String, value: BigDecimal): BigDecimal {
    val data = optBigDecimal(key, null)

    if (data == null) {
        put(key, value)
    }
    return getBigDecimal(key)
}

/**
 * Gets a value from the object if it exists and is a number, or puts a default value and returns it otherwise.
 *
 * @param key the key
 * @param value the default value to put
 * @return the value
 */
fun JSONObject.getOrPut(key: String, value: BigInteger): BigInteger {
    val data = optBigInteger(key, null)

    if (data == null) {
        put(key, value)
    }
    return getBigInteger(key)
}

/**
 * Gets a value from the object if it exists and is a boolean, or puts a default value and returns it otherwise.
 *
 * @param key the key
 * @param value the default value to put
 * @return the value
 */
fun JSONObject.getOrPut(key: String, value: Boolean): Boolean {
    val data: Any? = opt(key)

    if (data !is Boolean) {
        put(key, value)
    }

    return getBoolean(key)
}

/**
 * Gets a value from the object if it exists and is a number, or puts a default value and returns it otherwise.
 *
 * @param key the key
 * @param value the default value to put
 * @return the value
 */
fun JSONObject.getOrPut(key: String, value: Double): Double {
    val data = optNumber(key)

    if (data == null) {
        put(key, value)
    }
    return getDouble(key)
}

/**
 * Gets a value from the object if it exists and is an enum, or puts a default value and returns it otherwise.
 *
 * @param key the key
 * @param value the default value to put
 * @return the value
 */
fun <E: Enum<E>> JSONObject.getOrPut(key: String, value: E): E {
    val data = optEnum(value.javaClass, key)

    if (data == null) {
        put(key, value)
    }
    return getEnum(value.javaClass, key)
}

/**
 * Gets a value from the object if it exists and is a number, or puts a default value and returns it otherwise.
 *
 * @param key the key
 * @param value the default value to put
 * @return the value
 */
fun JSONObject.getOrPut(key: String, value: Float): Float {
    val data = optNumber(key)

    if (data == null) {
        put(key, value)
    }
    return getFloat(key)
}

/**
 * Gets a value from the object if it exists and is a number, or puts a default value and returns it otherwise.
 *
 * @param key the key
 * @param value the default value to put
 * @return the value
 */
fun JSONObject.getOrPut(key: String, value: Int): Int {
    val data = optNumber(key)

    if (data == null) {
        put(key, value)
    }
    return getInt(key)
}

/**
 * Gets a value from the object if it exists and is a [JSONArray], or puts a default value and returns it otherwise.
 *
 * @param key the key
 * @param value the default value to put
 * @return the value
 */
fun JSONObject.getOrPut(key: String, value: JSONArray): JSONArray {
    val data = optJSONArray(key)

    if (data == null) {
        put(key, value)
    }
    return getJSONArray(key)
}

/**
 * Gets a value from the object if it exists and is a [JSONObject], or puts a default value and returns it otherwise.
 *
 * @param key the key
 * @param value the default value to put
 * @return the value
 */
fun JSONObject.getOrPut(key: String, value: JSONObject): JSONObject {
    val data = optJSONObject(key)

    if (data == null) {
        put(key, value)
    }
    return optJSONObject(key)
}

/**
 * Gets a value from the object if it exists and is a number, or puts a default value and returns it otherwise.
 *
 * @param key the key
 * @param value the default value to put
 * @return the value
 */
fun JSONObject.getOrPut(key: String, value: Long): Long {
    val data = optNumber(key)

    if (data == null) {
        put(key, value)
    }
    return getLong(key)
}

/**
 * Gets a value from the object if it exists and is a number, or puts a default value and returns it otherwise.
 *
 * @param key the key
 * @param value the default value to put
 * @return the value
 */
fun JSONObject.getOrPut(key: String, value: Number): Number {
    val data = optNumber(key)

    if (data == null) {
        put(key, value)
    }
    return getNumber(key)
}

/**
 * Gets a value from the object if it exists and is a String, or puts a default value and returns it otherwise.
 *
 * @param key the key
 * @param value the default value to put
 * @return the value
 */
fun JSONObject.getOrPut(key: String, value: String): String {
    val data: Any? = opt(key)

    if (data !is String) {
        put(key, value)
    }
    return getString(key)
}

/**
 * Creates a deep copy of this object.
 */
fun JSONObject.deepCopy(): JSONObject {
    val copy = JSONObject()

    for (key in keySet()) {
        when (val value = opt(key)) {
            is JSONObject -> {
                copy.put(key, value.deepCopy())
            }
            is JSONArray -> {
                copy.put(key, value.deepCopy())
            }
            else -> {
                copy.put(key, value)
            }
        }
    }

    return copy
}