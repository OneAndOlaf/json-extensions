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

import com.github.oneandolaf.jsonext.extensions.deepCopy
import com.github.oneandolaf.jsonext.impl.Conversions
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import java.math.BigInteger

class ReadOnlyJSONArray private constructor(private val arr: JSONArray) : Iterable<Any> {

    /**
     * Gets the value at a given index.
     *
     * If the value is an object or an array, it will be wrapped inside a readonly version.
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the value at the given index, or `null` if not found or `index` is negative
     */
    fun getOrNull(index: Int): Any? {
        return ReadOnlyJSONObject.makeReadOnlyNullable(arr.opt(index))
    }

    /**
     * Gets the value at a given index.
     *
     * If the value is an object or an array, it will be wrapped inside a readonly version.
     *
     * @param index the index
     * @param defaultValue a function to calculate a default value if not found
     * @throws NullPointerException if any parameter is `null`
     * @return the value associated with the index, or the result of `defaultValue` if not found
     */
    fun getOrElse(index: Int, defaultValue: (index: Int) -> Any?): Any? {
        return getOrNull(index) ?: defaultValue(index)
    }

    /**
     * Gets the array value at a given index.
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the array value, or `null` if not found
     */
    fun getArrayOrNull(index: Int): ReadOnlyJSONArray? {
        // getOrNull already wraps it, so we don't need to do that here
        return getOrNull(index) as? ReadOnlyJSONArray
    }

    /**
     * Gets the array value at a given index.
     *
     * @param index the index
     * @param defaultValue a default value
     * @throws NullPointerException if any parameter is `null`
     * @return the array value, or `defaultValue` if not found
     */
    fun getArrayOrDefault(index: Int, defaultValue: ReadOnlyJSONArray): ReadOnlyJSONArray {
        return getArrayOrNull(index) ?: defaultValue
    }

    /**
     * Gets the array value at a given index.
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the array value, or an empty object if not found
     */
    fun getArrayOrEmpty(index: Int) = getArrayOrDefault(index, EMPTY)

    /**
     * Gets the array value at a given index.
     *
     * @param index the index
     * @param defaultValue a function to calculate the default value
     * @throws NullPointerException if any parameter is `null`
     * @return the array value, or the result of `defaultValue` if not found
     */
    fun getArrayOrElse(index: Int, defaultValue: (index: Int) -> ReadOnlyJSONArray): ReadOnlyJSONArray {
        return getArrayOrNull(index) ?: defaultValue(index)
    }

    /**
     * Gets the number value at a given index as a [BigDecimal].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `null` if not found
     */
    fun getBigDecimalOrNull(index: Int): BigDecimal? {
        return Conversions.toBigDecimal(getOrNull(index))
    }

    /**
     * Gets the number value at a given index as a [BigDecimal].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `defaultValue` if not found
     */
    fun getBigDecimalOrDefault(index: Int, defaultValue: BigDecimal): BigDecimal {
        return getBigDecimalOrNull(index) ?: defaultValue
    }

    /**
     * Gets the number value at a given index as a [BigDecimal].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `0` if not found
     */
    fun getBigDecimalOrZero(index: Int) = getBigDecimalOrDefault(index, BigDecimal.ZERO)

    /**
     * Gets the number value at a given index as a [BigDecimal].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `1` if not found
     */
    fun getBigDecimalOrOne(index: Int) = getBigDecimalOrDefault(index, BigDecimal.ONE)

    /**
     * Gets the number value at a given index as a [BigDecimal].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `1` if not found
     */
    fun getBigDecimalOrMinusOne(index: Int) = getBigDecimalOrDefault(index, BigDecimal.valueOf(-1L))

    /**
     * Gets the number value at a given index as a [BigDecimal].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or the result of `defaultValue` if not found
     */
    fun getBigDecimalOrElse(index: Int, defaultValue: (index: Int) -> BigDecimal): BigDecimal {
        return getBigDecimalOrNull(index) ?: defaultValue(index)
    }

    /**
     * Gets the number value at a given index as a [BigInteger].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `null` if not found
     */
    fun getBigIntegerOrNull(index: Int): BigInteger? {
        return Conversions.toBigInteger(getOrNull(index))
    }

    /**
     * Gets the number value at a given index as a [BigInteger].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `defaultValue` if not found
     */
    fun getBigIntegerOrDefault(index: Int, defaultValue: BigInteger): BigInteger {
        return getBigIntegerOrNull(index) ?: defaultValue
    }

    /**
     * Gets the number value at a given index as a [BigInteger].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `0` if not found
     */
    fun getBigIntegerOrZero(index: Int) = getBigIntegerOrDefault(index, BigInteger.ZERO)

    /**
     * Gets the number value at a given index as a [BigInteger].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `1` if not found
     */
    fun getBigIntegerOrOne(index: Int) = getBigIntegerOrDefault(index, BigInteger.ONE)

    /**
     * Gets the number value at a given index as a [BigInteger].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `-1` if not found
     */
    fun getBigIntegerOrMinusOne(index: Int) = getBigIntegerOrDefault(index, BigInteger.valueOf(-1L))

    /**
     * Gets the number value at a given index as a [BigInteger].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or the result of `defaultValue` if not found
     */
    fun getBigIntegerOrElse(index: Int, defaultValue: (index: Int) -> BigInteger): BigInteger {
        return getBigIntegerOrNull(index) ?: defaultValue(index)
    }

    /**
     * Gets the boolean value at a given index.
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the boolean value, or `null` if not found
     */
    fun getBooleanOrNull(index: Int): Boolean? {
        return Conversions.toBoolean(getOrNull(index))
    }

    /**
     * Gets the boolean value at a given index.
     *
     * @param index the index
     * @param defaultValue a function to calculate the default value
     * @throws NullPointerException if any parameter is `null`
     * @return the boolean value, or `defaultValue` if not found
     */
    fun getBooleanOrDefault(index: Int, defaultValue: Boolean): Boolean {
        return getBooleanOrNull(index) ?: defaultValue
    }

    /**
     * Gets the boolean value at a given index.
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the boolean value, or `true` if not found
     */
    fun getBooleanOrTrue(index: Int) = getBooleanOrDefault(index, true)

    /**
     * Gets the boolean value at a given index.
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the boolean value, or `false` if not found
     */
    fun getBooleanOrFalse(index: Int) = getBooleanOrDefault(index, false)

    /**
     * Gets the boolean value at a given index.
     *
     * @param index the index
     * @param defaultValue a function to calculate the default value
     * @throws NullPointerException if any parameter is `null`
     * @return the boolean value, or the result of `defaultValue` if not found
     */
    fun getBooleanOrElse(index: Int, defaultValue: (index: Int) -> Boolean): Boolean {
        return getBooleanOrNull(index) ?: defaultValue(index)
    }


    /**
     * Gets the number value at a given index as a [Double].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `null` if not found
     */
    fun getDoubleOrNull(index: Int): Double? {
        return Conversions.toDouble(getOrNull(index))
    }

    /**
     * Gets the number value at a given index as a [Double].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `defaultValue` if not found
     */
    fun getDoubleOrDefault(index: Int, defaultValue: Double): Double {
        return getDoubleOrNull(index) ?: defaultValue
    }

    /**
     * Gets the number value at a given index as a [Double].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `0.0` if not found
     */
    fun getDoubleOrZero(index: Int) = getDoubleOrDefault(index, 0.0)

    /**
     * Gets the number value at a given index as a [Double].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `1.0` if not found
     */
    fun getDoubleOrOne(index: Int) = getDoubleOrDefault(index, 1.0)

    /**
     * Gets the number value at a given index as a [Double].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `-1.0` if not found
     */
    fun getDoubleOrMinusOne(index: Int) = getDoubleOrDefault(index, -1.0)

    /**
     * Gets the number value at a given index as a [Double].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `NaN` if not found
     */
    fun getDoubleOrNaN(index: Int) = getDoubleOrDefault(index, Double.NaN)

    /**
     * Gets the number value at a given index as a [Double].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or the result of `defaultValue` if not found
     */
    fun getDoubleOrElse(index: Int, defaultValue: (index: Int) -> Double): Double {
        return getDoubleOrNull(index) ?: defaultValue(index)
    }

    /**
     * Gets the enum value at a given index.
     *
     * @param clazz the enum class
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the enum value, or `null` if not found
     */
    fun <E : Enum<E>> getEnumOrNull(clazz: Class<E>, index: Int): E? {
        return Conversions.toEnum(getOrNull(index), clazz)
    }

    /**
     * Gets the enum value at a given index.
     *
     * This function is only callable from Kotlin.
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the enum value, or `null` if not found
     */
    inline fun <reified E : Enum<E>> getEnumOrNull(index: Int): E? {
        return Conversions.toEnum<E>(getOrNull(index))
    }

    /**
     * Gets the enum value at a given index.
     *
     * @param clazz the enum class
     * @param index the index
     * @param defaultValue the default value
     * @throws NullPointerException if any parameter is `null`
     * @return the enum value, or `defaultValue` if not found
     */
    fun <E : Enum<E>> getEnumOrDefault(clazz: Class<E>, index: Int, defaultValue: E): E {
        return getEnumOrNull(clazz, index) ?: defaultValue
    }

    /**
     * Gets the enum value at a given index.
     *
     * This function is only callable from Kotlin.
     *
     * @param index the index
     * @param defaultValue the default value
     * @throws NullPointerException if any parameter is `null`
     * @return the enum value, or `defaultValue` if not found
     */
    inline fun <reified E : Enum<E>> getEnumOrDefault(index: Int, defaultValue: E): E {
        return getEnumOrNull<E>(index) ?: defaultValue
    }

    /**
     * Gets the enum value at a given index.
     *
     * @param clazz the enum class
     * @param index the index
     * @param defaultValue a function to calculate the default value
     * @throws NullPointerException if any parameter is `null`
     * @return the enum value, or the result of `defaultValue` if not found
     */
    fun <E : Enum<E>> getEnumOrElse(clazz: Class<E>, index: Int, defaultValue: (index: Int) -> E): E {
        return getEnumOrNull(clazz, index) ?: defaultValue(index)
    }

    /**
     * Gets the enum value at a given index.
     *
     * This function is only callable from Kotlin.
     *
     * @param E the enum class
     * @param index the index
     * @param defaultValue a function to calculate the default value
     * @throws NullPointerException if any parameter is `null`
     * @return the enum value, or the result of `defaultValue` if not found
     */
    inline fun <reified E : Enum<E>> getEnumOrElse(index: Int, defaultValue: (index: Int) -> E): E {
        return getEnumOrNull<E>(index) ?: defaultValue(index)
    }

    /**
     * Gets the number value at a given index as a [Float].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `null` if not found
     */
    fun getFloatOrNull(index: Int): Float? {
        return Conversions.toFloat(getOrNull(index))
    }

    /**
     * Gets the number value at a given index as a [Float].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `defaultValue` if not found
     */
    fun getFloatOrDefault(index: Int, defaultValue: Float): Float {
        return getFloatOrNull(index) ?: defaultValue
    }

    /**
     * Gets the number value at a given index as a [Float].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `0.0` if not found
     */
    fun getFloatOrZero(index: Int) = getFloatOrDefault(index, 0.0f)

    /**
     * Gets the number value at a given index as a [Float].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `1.0` if not found
     */
    fun getFloatOrOne(index: Int) = getFloatOrDefault(index, 1.0f)

    /**
     * Gets the number value at a given index as a [Float].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `-1.0` if not found
     */
    fun getFloatOrMinusOne(index: Int) = getFloatOrDefault(index, -1.0f)

    /**
     * Gets the number value at a given index as a [Float].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `NaN` if not found
     */
    fun getFloatOrNaN(index: Int) = getFloatOrDefault(index, Float.NaN)

    /**
     * Gets the number value at a given index as a [Float].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or the result of `defaultValue` if not found
     */
    fun getFloatOrElse(index: Int, defaultValue: (index: Int) -> Float): Float {
        return getFloatOrNull(index) ?: defaultValue(index)
    }

    /**
     * Gets the number value at a given index as a [Int].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `null` if not found
     */
    fun getIntOrNull(index: Int): Int? {
        return Conversions.toInt(getOrNull(index))
    }

    /**
     * Gets the number value at a given index as a [Int].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `defaultValue` if not found
     */
    fun getIntOrDefault(index: Int, defaultValue: Int): Int {
        return getIntOrNull(index) ?: defaultValue
    }

    /**
     * Gets the number value at a given index as a [Int].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `0` if not found
     */
    fun getIntOrZero(index: Int) = getIntOrDefault(index, 0)

    /**
     * Gets the number value at a given index as a [Int].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `1` if not found
     */
    fun getIntOrOne(index: Int) = getIntOrDefault(index, 1)

    /**
     * Gets the number value at a given index as a [Int].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `-1` if not found
     */
    fun getIntOrMinusOne(index: Int) = getIntOrDefault(index, -1)

    /**
     * Gets the number value at a given index as a [Int].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or the result of `defaultValue` if not found
     */
    fun getIntOrElse(index: Int, defaultValue: (index: Int) -> Int): Int {
        return getIntOrNull(index) ?: defaultValue(index)
    }

    /**
     * Gets the number value at a given index as a [Long].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `null` if not found
     */
    fun getLongOrNull(index: Int): Long? {
        return Conversions.toLong(getOrNull(index))
    }

    /**
     * Gets the number value at a given index as a [Long].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `defaultValue` if not found
     */
    fun getLongOrDefault(index: Int, defaultValue: Long): Long {
        return getLongOrNull(index) ?: defaultValue
    }

    /**
     * Gets the number value at a given index as a [Long].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `0` if not found
     */
    fun getLongOrZero(index: Int) = getLongOrDefault(index, 0L)

    /**
     * Gets the number value at a given index as a [Long].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `1` if not found
     */
    fun getLongOrOne(index: Int) = getLongOrDefault(index, 1L)

    /**
     * Gets the number value at a given index as a [Long].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `-1` if not found
     */
    fun getLongOrMinusOne(index: Int) = getLongOrDefault(index, -1L)

    /**
     * Gets the number value at a given index as a [Long].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or the result of `defaultValue` if not found
     */
    fun getLongOrElse(index: Int, defaultValue: (index: Int) -> Long): Long {
        return getLongOrNull(index) ?: defaultValue(index)
    }

    /**
     * Gets the number value at a given index as a [Number].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `null` if not found
     */
    fun getNumberOrNull(index: Int): Number? {
        return Conversions.toNumber(getOrNull(index))
    }

    /**
     * Gets the number value at a given index as a [Number].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or `defaultValue` if not found
     */
    fun getNumberOrDefault(index: Int, defaultValue: Number): Number {
        return getNumberOrNull(index) ?: defaultValue
    }

    /**
     * Gets the number value at a given index as a [Number].
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the number value, or the result of `defaultValue` if not found
     */
    fun getNumberOrElse(index: Int, defaultValue: (index: Int) -> Number): Number {
        return getNumberOrNull(index) ?: defaultValue(index)
    }

    /**
     * Gets the object value at a given index.
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the object value, or `null` if not found
     */
    fun getObjectOrNull(index: Int): ReadOnlyJSONObject? {
        // getOrNull already wraps it, so we don't need to do that here
        return getOrNull(index) as? ReadOnlyJSONObject
    }

    /**
     * Gets the object value at a given index.
     *
     * @param index the index
     * @param defaultValue a default value
     * @throws NullPointerException if any parameter is `null`
     * @return the object value, or `defaultValue` if not found
     */
    fun getObjectOrDefault(index: Int, defaultValue: ReadOnlyJSONObject): ReadOnlyJSONObject {
        return getObjectOrNull(index) ?: defaultValue
    }

    /**
     * Gets the object value at a given index.
     *
     * @param index the index
     * @throws NullPointerException if any parameter is `null`
     * @return the object value, or an empty object if not found
     */
    fun getObjectOrEmpty(index: Int) = getObjectOrDefault(index, ReadOnlyJSONObject.EMPTY)

    /**
     * Gets the object value at a given index.
     *
     * @param index the index
     * @param defaultValue a function to calculate the default value
     * @throws NullPointerException if any parameter is `null`
     * @return the object value, or the result of `defaultValue` if not found
     */
    fun getObjectOrElse(index: Int, defaultValue: (index: Int) -> ReadOnlyJSONObject): ReadOnlyJSONObject {
        return getObjectOrNull(index) ?: defaultValue(index)
    }

    /**
     * Gets the string value at a given index.
     *
     * By default, `null` is returned if the value is present, but not a string. A coercion mechanism can be used to
     * convert it into a String via its `toString` method. When using the coercion, `null` is only returned if the index
     * is missing.
     *
     * @param index the index
     * @param coerce whether to convert non-null, but non-string values to strings
     * @throws NullPointerException if any parameter is `null`
     * @return the string value, or `null` if not found
     */
    @JvmOverloads
    fun getStringOrNull(index: Int, coerce: Boolean = false): String? {
        return Conversions.toString(getOrNull(index), coerce)
    }

    /**
     * Gets the string value at a given index.
     *
     * By default, `defaultValue` is returned if the value is present, but not a string. A coercion mechanism can be
     * used to convert it into a String via its `toString` method. When using the coercion, `defaultValue` is only
     * returned if the index is missing.
     *
     * @param index the index
     * @param defaultValue a default value
     * @param coerce whether to convert non-null, but non-string values to strings
     * @throws NullPointerException if any parameter is `null`
     * @return the string value, or `defaultValue` if not found
     */
    @JvmOverloads
    fun getStringOrDefault(index: Int, defaultValue: String, coerce: Boolean = false): String {
        return getStringOrNull(index, coerce) ?: defaultValue
    }

    /**
     * Gets the string value at a given index.
     *
     * By default, an empty string is returned if the value is present, but not a string. A coercion mechanism can be
     * used to convert it into a String via its `toString` method. When using the coercion, an empty string is only
     * returned if the index is missing.
     *
     * @param index the index
     * @param coerce whether to convert non-null, but non-string values to strings
     * @throws NullPointerException if any parameter is `null`
     * @return the string value, or an empty string if not found
     */
    @JvmOverloads
    fun getStringOrEmpty(index: Int, coerce: Boolean = false) = getStringOrDefault(index, "", coerce)

    /**
     * Gets the string value at a given index.
     *
     * By default, `defaultValue` is queried if the value is present, but not a string. A coercion mechanism can be
     * used to convert it into a String via its `toString` method. When using the coercion, `defaultValue` is only
     * queried if the index is missing.
     *
     * @param index the index
     * @param defaultValue a function to calculate the default value
     * @param coerce whether to convert non-null, but non-string values to strings
     * @throws NullPointerException if any parameter is `null`
     * @return the string value, or `defaultValue` if not found
     */
    fun getStringOrElse(index: Int, defaultValue: (index: Int) -> String, coerce: Boolean): String {
        return getStringOrNull(index, coerce) ?: defaultValue(index)
    }

    /**
     * Gets the string value at a given index.
     *
     * By default, `defaultValue` is queried if the value is present, but not a string. A coercion mechanism can be
     * used to convert it into a String via its `toString` method. When using the coercion, `defaultValue` is only
     * queried if the index is missing.
     *
     * This method behaves exactly like its overloads and exists so that Kotlin/Groovy users can pass a closure outside
     * of the parentheses.
     *
     * @param index the index
     * @param coerce whether to convert non-null, but non-string values to strings
     * @param defaultValue a function to calculate the default value
     * @throws NullPointerException if any parameter is `null`
     * @return the string value, or `defaultValue` if not found
     */
    fun getStringOrElse(index: Int, coerce: Boolean, defaultValue: (index: Int) -> String) =
        getStringOrElse(index, defaultValue, coerce)

    /**
     * Gets the string value at a given index.
     *
     * By default, `defaultValue` is queried if the value is present, but not a string. A coercion mechanism can be
     * used to convert it into a String via its `toString` method. When using the coercion, `defaultValue` is only
     * queried if the index is missing.
     *
     * This method behaves exactly like its overloads and exists so that Kotlin/Groovy users can pass a closure outside
     * of the parentheses.
     *
     * @param index the index
     * @param defaultValue a function to calculate the default value
     * @throws NullPointerException if any parameter is `null`
     * @return the string value, or `defaultValue` if not found
     */
    fun getStringOrElse(index: Int, defaultValue: (index: Int) -> String) =
        getStringOrElse(index, defaultValue, false)

    /**
     * Checks whether the array is empty.
     */
    val isEmpty: Boolean
        get() = arr.isEmpty

    /**
     * Checks whether the array is not empty.
     */
    val isNotEmpty: Boolean
        get() = !isEmpty

    /**
     * Gets the size of the array.
     */
    val size: Int
        get() = arr.length()

    override fun iterator(): Iterator<Any> = ArrayIterator(this)

    /**
     * Returns a list of all array items in the array.
     */
    fun itemsAsArrays(): List<ReadOnlyJSONArray> {
        return filterIsInstance<ReadOnlyJSONArray>()
    }

    /**
     * Returns a list of all boolean items in the array.
     */
    fun itemsAsBooleans(): List<Boolean> {
        return mapNotNull { Conversions.toBoolean(it) }
    }

    /**
     * Returns a list of all double items in the array.
     */
    fun itemsAsDoubles(): List<Double> {
        return mapNotNull { Conversions.toDouble(it) }
    }

    /**
     * Returns a list of all int items in the array.
     */
    fun itemsAsInts(): List<Int> {
        return mapNotNull { Conversions.toInt(it) }
    }

    /**
     * Returns a list of all long items in the array.
     */
    fun itemsAsLongs(): List<Long> {
        return mapNotNull { Conversions.toLong(it) }
    }

    /**
     * Returns a list of all string items in the array.
     *
     * @param coerce whether to coerce all items to Strings
     */
    @JvmOverloads
    fun itemsAsStrings(coerce: Boolean = false): List<String> {
        return mapNotNull { Conversions.toString(it, coerce) }
    }

    /**
     * Returns a list of all object items in the array.
     */
    fun itemsAsObjects(): List<ReadOnlyJSONObject> {
        return filterIsInstance<ReadOnlyJSONObject>()
    }


    /**
     * Checks if this object is similar to another. `other` must be an instance of `ReadOnlyJSONObject`.
     * If `other` is a plain [JSONObject], this method will return `false` to maintain symmetry.
     *
     * Two objects are similar if the [JSONObject]s they wrap are similar.
     *
     * @param other the object to compare this to
     * @see JSONObject.similar
     * @see similarToPlainArray
     * @return whether the objects are similar
     */
    fun similar(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is ReadOnlyJSONArray) {
            return false
        }
        return similarToPlainArray(other.arr)
    }

    override fun toString(): String {
        return arr.toString()
    }

    /**
     * Checks if this object is similar to a plain [JSONObject].
     *
     * @param other the object to compare this to
     * @return whether the objects are similar
     */
    fun similarToPlainArray(other: JSONArray?): Boolean {
        return other != null && arr.similar(other)
    }

    private class ArrayIterator(array: ReadOnlyJSONArray) : Iterator<Any> {

        private val srcIt = array.arr.iterator()

        override fun hasNext(): Boolean {
            return srcIt.hasNext()
        }

        override fun next(): Any {
            return ReadOnlyJSONObject.makeReadOnly(srcIt.next())
        }


    }

    companion object {

        @JvmStatic
        val EMPTY = ReadOnlyJSONArray(JSONArray())

        /**
         * Creates a read-only array from `arr`.
         *
         * The object returned is backed by `arr`. Any changes made to `arr` will be reflected in the array returned.
         */
        @JvmStatic
        fun create(arr: JSONArray): ReadOnlyJSONArray {
            return ReadOnlyJSONArray(arr)
        }

        /**
         * Creates a read-only array from the current state of `arr`.
         *
         * The array returned is _not_ backed by `arr`, so changes to `arr` will not be reflected in the array
         * returned.
         */
        @JvmStatic
        fun snapshot(arr: JSONArray): ReadOnlyJSONArray {
            if (arr.isEmpty) {
                return EMPTY
            }
            return ReadOnlyJSONArray(arr.deepCopy())
        }

    }


}