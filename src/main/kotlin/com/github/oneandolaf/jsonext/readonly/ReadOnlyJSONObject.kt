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
import java.math.BigDecimal
import java.math.BigInteger

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
     * Gets the array value associated with a key.
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the array value, or `null` if not found
     */
    fun getArrayOrNull(key: String): ReadOnlyJSONArray? {
        // getOrNull already wraps it, so we don't need to do that here
        return getOrNull(key) as? ReadOnlyJSONArray
    }

    /**
     * Gets the array value associated with a key.
     *
     * @param key the key
     * @param defaultValue a default value
     * @throws NullPointerException if any parameter is `null`
     * @return the array value, or `defaultValue` if not found
     */
    fun getArrayOrDefault(key: String, defaultValue: ReadOnlyJSONArray): ReadOnlyJSONArray {
        return getArrayOrNull(key) ?: defaultValue
    }

    /**
     * Gets the array value associated with a key.
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the array value, or an empty object if not found
     */
    fun getArrayOrEmpty(key: String) = getArrayOrDefault(key, ReadOnlyJSONArray.EMPTY)

    /**
     * Gets the array value associated with a key.
     *
     * @param key the key
     * @param defaultValue a function to calculate the default value
     * @throws NullPointerException if any parameter is `null`
     * @return the array value, or the result of `defaultValue` if not found
     */
    fun getArrayOrElse(key: String, defaultValue: (key: String) -> ReadOnlyJSONArray): ReadOnlyJSONArray {
        return getArrayOrNull(key) ?: defaultValue(key)
    }

    /**
     * Gets the number value associated with a key as a [BigDecimal].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `null` if not found
     */
    fun getBigDecimalOrNull(key: String): BigDecimal? {
        return Conversions.toBigDecimal(getOrNull(key))
    }

    /**
     * Gets the number value associated with a key as a [BigDecimal].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `defaultValue` if not found
     */
    fun getBigDecimalOrDefault(key: String, defaultValue: BigDecimal): BigDecimal {
        return getBigDecimalOrNull(key) ?: defaultValue
    }

    /**
     * Gets the number value associated with a key as a [BigDecimal].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `0` if not found
     */
    fun getBigDecimalOrZero(key: String) = getBigDecimalOrDefault(key, BigDecimal.ZERO)

    /**
     * Gets the number value associated with a key as a [BigDecimal].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `1` if not found
     */
    fun getBigDecimalOrOne(key: String) = getBigDecimalOrDefault(key, BigDecimal.ONE)

    /**
     * Gets the number value associated with a key as a [BigDecimal].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `1` if not found
     */
    fun getBigDecimalOrMinusOne(key: String) = getBigDecimalOrDefault(key, BigDecimal.valueOf(-1L))

    /**
     * Gets the number value associated with a key as a [BigDecimal].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or the result of `defaultValue` if not found
     */
    fun getBigDecimalOrElse(key: String, defaultValue: (key: String) -> BigDecimal): BigDecimal {
        return getBigDecimalOrNull(key) ?: defaultValue(key)
    }

    /**
     * Gets the number value associated with a key as a [BigInteger].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `null` if not found
     */
    fun getBigIntegerOrNull(key: String): BigInteger? {
        return Conversions.toBigInteger(getOrNull(key))
    }

    /**
     * Gets the number value associated with a key as a [BigInteger].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `defaultValue` if not found
     */
    fun getBigIntegerOrDefault(key: String, defaultValue: BigInteger): BigInteger {
        return getBigIntegerOrNull(key) ?: defaultValue
    }

    /**
     * Gets the number value associated with a key as a [BigInteger].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `0` if not found
     */
    fun getBigIntegerOrZero(key: String) = getBigIntegerOrDefault(key, BigInteger.ZERO)

    /**
     * Gets the number value associated with a key as a [BigInteger].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `1` if not found
     */
    fun getBigIntegerOrOne(key: String) = getBigIntegerOrDefault(key, BigInteger.ONE)

    /**
     * Gets the number value associated with a key as a [BigInteger].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `-1` if not found
     */
    fun getBigIntegerOrMinusOne(key: String) = getBigIntegerOrDefault(key, BigInteger.valueOf(-1L))

    /**
     * Gets the number value associated with a key as a [BigInteger].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or the result of `defaultValue` if not found
     */
    fun getBigIntegerOrElse(key: String, defaultValue: (key: String) -> BigInteger): BigInteger {
        return getBigIntegerOrNull(key) ?: defaultValue(key)
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


    /**
     * Gets the number value associated with a key as a [Double].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `null` if not found
     */
    fun getDoubleOrNull(key: String): Double? {
        return Conversions.toDouble(getOrNull(key))
    }

    /**
     * Gets the number value associated with a key as a [Double].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `defaultValue` if not found
     */
    fun getDoubleOrDefault(key: String, defaultValue: Double): Double {
        return getDoubleOrNull(key) ?: defaultValue
    }

    /**
     * Gets the number value associated with a key as a [Double].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `0.0` if not found
     */
    fun getDoubleOrZero(key: String) = getDoubleOrDefault(key, 0.0)

    /**
     * Gets the number value associated with a key as a [Double].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `1.0` if not found
     */
    fun getDoubleOrOne(key: String) = getDoubleOrDefault(key, 1.0)

    /**
     * Gets the number value associated with a key as a [Double].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `-1.0` if not found
     */
    fun getDoubleOrMinusOne(key: String) = getDoubleOrDefault(key, -1.0)

    /**
     * Gets the number value associated with a key as a [Double].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `NaN` if not found
     */
    fun getDoubleOrNaN(key: String) = getDoubleOrDefault(key, Double.NaN)

    /**
     * Gets the number value associated with a key as a [Double].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or the result of `defaultValue` if not found
     */
    fun getDoubleOrElse(key: String, defaultValue: (key: String) -> Double): Double {
        return getDoubleOrNull(key) ?: defaultValue(key)
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
    fun <E : Enum<E>> getEnumOrDefault(clazz: Class<E>, key: String, defaultValue: E): E {
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
    fun <E : Enum<E>> getEnumOrElse(clazz: Class<E>, key: String, defaultValue: (key: String) -> E): E {
        return getEnumOrNull(clazz, key) ?: defaultValue(key)
    }

    /**
     * Gets the number value associated with a key as a [Float].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `null` if not found
     */
    fun getFloatOrNull(key: String): Float? {
        return Conversions.toFloat(getOrNull(key))
    }

    /**
     * Gets the number value associated with a key as a [Float].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `defaultValue` if not found
     */
    fun getFloatOrDefault(key: String, defaultValue: Float): Float {
        return getFloatOrNull(key) ?: defaultValue
    }

    /**
     * Gets the number value associated with a key as a [Float].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `0.0` if not found
     */
    fun getFloatOrZero(key: String) = getFloatOrDefault(key, 0.0f)

    /**
     * Gets the number value associated with a key as a [Float].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `1.0` if not found
     */
    fun getFloatOrOne(key: String) = getFloatOrDefault(key, 1.0f)

    /**
     * Gets the number value associated with a key as a [Float].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `-1.0` if not found
     */
    fun getFloatOrMinusOne(key: String) = getFloatOrDefault(key, -1.0f)

    /**
     * Gets the number value associated with a key as a [Float].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `NaN` if not found
     */
    fun getFloatOrNaN(key: String) = getFloatOrDefault(key, Float.NaN)

    /**
     * Gets the number value associated with a key as a [Float].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or the result of `defaultValue` if not found
     */
    fun getFloatOrElse(key: String, defaultValue: (key: String) -> Float): Float {
        return getFloatOrNull(key) ?: defaultValue(key)
    }

    /**
     * Gets the number value associated with a key as a [Int].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `null` if not found
     */
    fun getIntOrNull(key: String): Int? {
        return Conversions.toInt(getOrNull(key))
    }

    /**
     * Gets the number value associated with a key as a [Int].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `defaultValue` if not found
     */
    fun getIntOrDefault(key: String, defaultValue: Int): Int {
        return getIntOrNull(key) ?: defaultValue
    }

    /**
     * Gets the number value associated with a key as a [Int].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `0` if not found
     */
    fun getIntOrZero(key: String) = getIntOrDefault(key, 0)

    /**
     * Gets the number value associated with a key as a [Int].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `1` if not found
     */
    fun getIntOrOne(key: String) = getIntOrDefault(key, 1)

    /**
     * Gets the number value associated with a key as a [Int].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `-1` if not found
     */
    fun getIntOrMinusOne(key: String) = getIntOrDefault(key, -1)

    /**
     * Gets the number value associated with a key as a [Int].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or the result of `defaultValue` if not found
     */
    fun getIntOrElse(key: String, defaultValue: (key: String) -> Int): Int {
        return getIntOrNull(key) ?: defaultValue(key)
    }

    /**
     * Gets the number value associated with a key as a [Long].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `null` if not found
     */
    fun getLongOrNull(key: String): Long? {
        return Conversions.toLong(getOrNull(key))
    }

    /**
     * Gets the number value associated with a key as a [Long].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `defaultValue` if not found
     */
    fun getLongOrDefault(key: String, defaultValue: Long): Long {
        return getLongOrNull(key) ?: defaultValue
    }

    /**
     * Gets the number value associated with a key as a [Long].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `0` if not found
     */
    fun getLongOrZero(key: String) = getLongOrDefault(key, 0L)

    /**
     * Gets the number value associated with a key as a [Long].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `1` if not found
     */
    fun getLongOrOne(key: String) = getLongOrDefault(key, 1L)

    /**
     * Gets the number value associated with a key as a [Long].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `-1` if not found
     */
    fun getLongOrMinusOne(key: String) = getLongOrDefault(key, -1L)

    /**
     * Gets the number value associated with a key as a [Long].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or the result of `defaultValue` if not found
     */
    fun getLongOrElse(key: String, defaultValue: (key: String) -> Long): Long {
        return getLongOrNull(key) ?: defaultValue(key)
    }

    /**
     * Gets the number value associated with a key as a [Number].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `null` if not found
     */
    fun getNumberOrNull(key: String): Number? {
        return Conversions.toNumber(getOrNull(key))
    }

    /**
     * Gets the number value associated with a key as a [Number].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `defaultValue` if not found
     */
    fun getNumberOrDefault(key: String, defaultValue: Number): Number {
        return getNumberOrNull(key) ?: defaultValue
    }

    /**
     * Gets the number value associated with a key as a [Number].
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or the result of `defaultValue` if not found
     */
    fun getNumberOrElse(key: String, defaultValue: (key: String) -> Number): Number {
        return getNumberOrNull(key) ?: defaultValue(key)
    }

    /**
     * Gets the object value associated with a key.
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the object value, or `null` if not found
     */
    fun getObjectOrNull(key: String): ReadOnlyJSONObject? {
        // getOrNull already wraps it, so we don't need to do that here
        return getOrNull(key) as? ReadOnlyJSONObject
    }

    /**
     * Gets the object value associated with a key.
     *
     * @param key the key
     * @param defaultValue a default value
     * @throws NullPointerException if any parameter is `null`
     * @return the object value, or `defaultValue` if not found
     */
    fun getObjectOrDefault(key: String, defaultValue: ReadOnlyJSONObject): ReadOnlyJSONObject {
        return getObjectOrNull(key) ?: defaultValue
    }

    /**
     * Gets the object value associated with a key.
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the object value, or an empty object if not found
     */
    fun getObjectOrEmpty(key: String) = getObjectOrDefault(key, EMPTY)

    /**
     * Gets the object value associated with a key.
     *
     * @param key the key
     * @param defaultValue a function to calculate the default value
     * @throws NullPointerException if any parameter is `null`
     * @return the object value, or the result of `defaultValue` if not found
     */
    fun getObjectOrElse(key: String, defaultValue: (key: String) -> ReadOnlyJSONObject): ReadOnlyJSONObject {
        return getObjectOrNull(key) ?: defaultValue(key)
    }

    /**
     * Gets the string value associated with a key.
     *
     * By default, `null` is returned if the value is present, but not a string. A coercion mechanism can be used to
     * convert it into a String via its `toString` method. When using the coercion, `null` is only returned if the key
     * is missing.
     *
     * @param key the key
     * @param coerce whether to convert non-null, but non-string values to strings
     * @throws NullPointerException if any parameter is `null`
     * @return the string value, or `null` if not found
     */
    @JvmOverloads
    fun getStringOrNull(key: String, coerce: Boolean = false): String? {
        return Conversions.toString(getOrNull(key), coerce)
    }

    /**
     * Gets the string value associated with a key.
     *
     * By default, `defaultValue` is returned if the value is present, but not a string. A coercion mechanism can be
     * used to convert it into a String via its `toString` method. When using the coercion, `defaultValue` is only
     * returned if the key is missing.
     *
     * @param key the key
     * @param defaultValue a default value
     * @throws NullPointerException if any parameter is `null`
     * @return the string value, or `defaultValue` if not found
     */
    @JvmOverloads
    fun getStringOrDefault(key: String, defaultValue: String, coerce: Boolean = false): String {
        return getStringOrNull(key, coerce) ?: defaultValue
    }

    /**
     * Gets the string value associated with a key.
     *
     * By default, an empty string is returned if the value is present, but not a string. A coercion mechanism can be
     * used to convert it into a String via its `toString` method. When using the coercion, an empty string is only
     * returned if the key is missing.
     *
     * @param key the key
     * @throws NullPointerException if any parameter is `null`
     * @return the string value, or an empty string if not found
     */
    @JvmOverloads
    fun getStringOrEmpty(key: String, coerce: Boolean = false) = getStringOrDefault(key, "", coerce)

    /**
     * Gets the string value associated with a key.
     *
     * By default, `defaultValue` is queried if the value is present, but not a string. A coercion mechanism can be
     * used to convert it into a String via its `toString` method. When using the coercion, `defaultValue` is only
     * queried if the key is missing.
     *
     * @param key the key
     * @param defaultValue a function to calculate the default value
     * @throws NullPointerException if any parameter is `null`
     * @return the string value, or `defaultValue` if not found
     */
    @JvmOverloads
    fun getStringOrElse(key: String, defaultValue: (key: String) -> String, coerce: Boolean = false): String {
        return getStringOrNull(key, coerce) ?: defaultValue(key)
    }

    /**
     * Gets the string value associated with a key.
     *
     * By default, `defaultValue` is queried if the value is present, but not a string. A coercion mechanism can be
     * used to convert it into a String via its `toString` method. When using the coercion, `defaultValue` is only
     * queried if the key is missing.
     *
     * This method behaves exactly like its overload and exists so that Kotlin/Groovy users can pass a closure outside
     * of the parentheses.
     *
     * @param key the key
     * @param defaultValue a function to calculate the default value
     * @throws NullPointerException if any parameter is `null`
     * @return the string value, or `defaultValue` if not found
     */
    fun getStringOrElse(key: String, coerce: Boolean, defaultValue: (key: String) -> String) =
        getStringOrElse(key, defaultValue, coerce)

    companion object {

        @JvmStatic
        val EMPTY = ReadOnlyJSONObject(JSONObject())

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