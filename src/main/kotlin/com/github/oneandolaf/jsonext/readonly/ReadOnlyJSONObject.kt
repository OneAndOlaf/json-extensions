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

package com.github.oneandolaf.jsonext.readonly

import com.github.oneandolaf.jsonext.impl.Conversions
import org.json.JSONArray
import org.json.JSONObject

/**
 * Unmodifiable variant of JSONObject. This wraps a [JSONObject], but it does not extend from it.
 */
class ReadOnlyJSONObject(private val obj: JSONObject) {

    /**
     * Gets the value associated with a key.
     *
     * If the value is an object or an array, it will be wrapped inside a readonly version.
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the value associated with the key, or `null` if not found
     */
    fun getOrNull(key: String): Any? {
        return makeReadOnly(obj.opt(key))
    }

    /**
     * Gets the value associated with a key.
     *
     * If the value is an object or an array, it will be wrapped inside a readonly version.
     *
     * @param key the key
     * @param defaultValue a function to calculate a default value if not found
     * @throws NullPointerException if any parameter is `null`
     * @return the value associated with the key, or the result of `defaultValue` if not found
     */
    fun getOrElse(key: String, defaultValue: (key: String) -> Any?): Any? {
        return getOrNull(key) ?: defaultValue(key)
    }

    /**
     * Gets the enum value associated with a key.
     *
     * @param clazz the enum class
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the enum value, or `null` if not found
     */
    fun <E : Enum<E>> getEnumOrNull(clazz: Class<E>, key: String): E? {
        return Conversions.toEnum(getOrNull(key), clazz)
    }

    /**
     * Gets the enum value associated with a key.
     *
     * @param clazz the enum class
     * @param key the key
     * @param defaultValue the default value
     * @throws NullPointerException if any parameter is `null`
     * @return the enum value, or `defaultValue` if not found
     */
    fun <E: Enum<E>> getEnumOrDefault(clazz: Class<E>, key: String, defaultValue: E): E {
        return getEnumOrNull(clazz, key) ?: defaultValue
    }

    /**
     * Gets the enum value associated with a key.
     *
     * @param clazz the enum class
     * @param key the key
     * @param defaultValue a function to calculate the default value
     * @throws NullPointerException if any parameter is `null`
     * @return the enum value, or the result of `defaultValue` if not found
     */
    fun <E: Enum<E>> getEnumOrElse(clazz: Class<E>, key: String, defaultValue: (key: String) -> E): E {
        return getEnumOrNull(clazz, key) ?: defaultValue(key)
    }

    /**
     * Gets the boolean value associated with a key.
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the boolean value, or `null` if not found
     */
    fun getBooleanOrNull(key: String): Boolean? {
        return Conversions.toBoolean(getOrNull(key))
    }

    /**
     * Gets the boolean value associated with a key.
     *
     * @param key the key
     * @param defaultValue a function to calculate the default value
     * @throws NullPointerException if any parameter is `null`
     * @return the boolean value, or `defaultValue` if not found
     */
    fun getBooleanOrDefault(key: String, defaultValue: Boolean): Boolean {
        return getBooleanOrNull(key) ?: defaultValue
    }

    /**
     * Gets the boolean value associated with a key.
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the boolean value, or `true` if not found
     */
    fun getBooleanOrTrue(key: String) = getBooleanOrDefault(key, true)

    /**
     * Gets the boolean value associated with a key.
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the boolean value, or `false` if not found
     */
    fun getBooleanOrFalse(key: String) = getBooleanOrDefault(key, false)

    /**
     * Gets the boolean value associated with a key.
     *
     * @param key the key
     * @param defaultValue a function to calculate the default value
     * @throws NullPointerException if any parameter is `null`
     * @return the boolean value, or the result of `defaultValue` if not found
     */
    fun getBooleanOrElse(key: String, defaultValue: (key: String) -> Boolean): Boolean {
        return getBooleanOrNull(key) ?: defaultValue(key)
    }

    companion object {

        internal fun makeReadOnly(o: Any?): Any? {
            return when (o) {
                null -> o
                is JSONObject -> ReadOnlyJSONObject(o)
                is JSONArray -> ReadOnlyJSONArray(o)
                else -> o
            }
        }

    }

}