/*
 * Copyright (c) 2022 OneAndOlaf
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
import java.math.BigDecimal
import java.math.BigInteger

class ReadOnlyJSONVal(
    value: Any?
) {

    private val value = ReadOnlyJSONObject.makeReadOnlyNullable(value)

    /**
     * Returns the value, or `null` if no value is present.
     */
    fun orNull(): Any? {
        return value
    }

    /**
     * Returns the value, or the result of a callback if no value is present.
     *
     * @param defaultValue the callback to generate the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun orElse(defaultValue: () -> Any): Any {
        return value ?: defaultValue()
    }

    /**
     * Returns the value, or a default if no value is present.
     *
     * @param defaultValue the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun orDefault(defaultValue: Any): Any {
        return value ?: defaultValue
    }

    /**
     * Checks whether the value is an array.
     */
    fun isArray(): Boolean {
        return value is ReadOnlyJSONArray
    }

    /**
     * Returns the value if it is an array, or `null` otherwise.
     */
    fun asArrayOrNull(): ReadOnlyJSONArray? {
        return value as? ReadOnlyJSONArray
    }

    /**
     * Returns the value if it is an array, or a default value otherwise.
     *
     * @param defaultValue the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asArrayOrDefault(defaultValue: ReadOnlyJSONArray): ReadOnlyJSONArray {
        return asArrayOrNull() ?: defaultValue
    }

    /**
     * Returns the value if it is an array, or an empty array otherwise.
     */
    fun asArrayOrEmpty(): ReadOnlyJSONArray = asArrayOrDefault(ReadOnlyJSONArray.EMPTY)

    /**
     * Returns the value if it is an array, or the result of a callback otherwise.
     *
     * @param defaultValue the callback to generate the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asArrayOrElse(defaultValue: () -> ReadOnlyJSONArray): ReadOnlyJSONArray {
        return asArrayOrNull() ?: defaultValue()
    }

    /**
     * Checks whether the value is a [BigDecimal].
     */
    fun isBigDecimal(): Boolean {
        return Conversions.toBigDecimal(value) != null
    }

    /**
     * Returns the value if it is a [BigDecimal], or `null` otherwise.
     */
    fun asBigDecimalOrNull(): BigDecimal? {
        return Conversions.toBigDecimal(value)
    }

    /**
     * Returns the value if it is a [BigDecimal], or a default value otherwise.
     *
     * @param defaultValue the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asBigDecimalOrDefault(defaultValue: BigDecimal): BigDecimal {
        return asBigDecimalOrNull() ?: defaultValue
    }

    /**
     * Returns the value if it is a [BigDecimal], or `0` otherwise.
     */
    fun asBigDecimalOrZero(): BigDecimal = asBigDecimalOrDefault(BigDecimal.ZERO)

    /**
     * Returns the value if it is a [BigDecimal], or `1` otherwise.
     */
    fun asBigDecimalOrOne(): BigDecimal = asBigDecimalOrDefault(BigDecimal.ONE)

    /**
     * Returns the value if it is a [BigDecimal], or `-1` otherwise.
     */
    fun asBigDecimalOrMinusOne(): BigDecimal = asBigDecimalOrDefault(BigDecimal.valueOf(-1L))

    /**
     * Returns the value if it is a [BigDecimal], or the result of a callback otherwise.
     *
     * @param defaultValue the callback to generate the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asBigDecimalOrElse(defaultValue: () -> BigDecimal): BigDecimal {
        return asBigDecimalOrNull() ?: defaultValue()
    }

    /**
     * Checks whether the value is a [BigInteger].
     */
    fun isBigInteger(): Boolean {
        return Conversions.toBigInteger(value) != null
    }

    /**
     * Returns the value if it is a [BigInteger], or `null` otherwise.
     */
    fun asBigIntegerOrNull(): BigInteger? {
        return Conversions.toBigInteger(value)
    }

    /**
     * Returns the value if it is a [BigInteger], or a default value otherwise.
     *
     * @param defaultValue the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asBigIntegerOrDefault(defaultValue: BigInteger): BigInteger {
        return asBigIntegerOrNull() ?: defaultValue
    }

    /**
     * Returns the value if it is a [BigInteger], or a `0` otherwise.
     */
    fun asBigIntegerOrZero(): BigInteger = asBigIntegerOrDefault(BigInteger.ZERO)

    /**
     * Returns the value if it is a [BigInteger], or a `1` otherwise.
     */
    fun asBigIntegerOrOne(): BigInteger = asBigIntegerOrDefault(BigInteger.ONE)

    /**
     * Returns the value if it is a [BigInteger], or a `-1` otherwise.
     */
    fun asBigIntegerOrMinusOne(): BigInteger = asBigIntegerOrDefault(BigInteger.valueOf(-1L))

    /**
     * Returns the value if it is a [BigInteger], or the result of a callback otherwise.
     *
     * @param defaultValue the callback to generate the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asBigIntegerOrElse(defaultValue: () -> BigInteger): BigInteger {
        return asBigIntegerOrNull() ?: defaultValue()
    }

    /**
     * Checks whether the value is a boolean.
     */
    fun isBoolean(): Boolean {
        return Conversions.toBoolean(value) != null
    }

    /**
     * Returns the value if it is a boolean, or `null` otherwise.
     */
    fun asBooleanOrNull(): Boolean? {
        return Conversions.toBoolean(value)
    }

    /**
     * Returns the value if it is a boolean, or a default value otherwise.
     *
     * @param defaultValue the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asBooleanOrDefault(defaultValue: Boolean): Boolean {
        return asBooleanOrNull() ?: defaultValue
    }

    /**
     * Returns the value if it is a boolean, or `true` otherwise.
     */
    fun asBooleanOrTrue(): Boolean = asBooleanOrDefault(true)

    /**
     * Returns the value if it is a boolean, or `false` otherwise.
     */
    fun asBooleanOrFalse(): Boolean = asBooleanOrDefault(false)

    /**
     * Returns the value if it is a boolean, or the result of a callback otherwise.
     *
     * @param defaultValue the callback to generate the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asBooleanOrElse(defaultValue: () -> Boolean): Boolean {
        return asBooleanOrNull() ?: defaultValue()
    }

    /**
     * Checks whether the value is a double.
     */
    fun isDouble(): Boolean {
        return Conversions.toDouble(value) != null
    }

    /**
     * Returns the value if it is a double, or `null` otherwise.
     */
    fun asDoubleOrNull(): Double? {
        return Conversions.toDouble(value)
    }

    /**
     * Returns the value if it is a double, or a default value otherwise.
     *
     * @param defaultValue the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asDoubleOrDefault(defaultValue: Double): Double {
        return asDoubleOrNull() ?: defaultValue
    }

    /**
     * Returns the value if it is a double, or `0` otherwise.
     */
    fun asDoubleOrZero(): Double = asDoubleOrDefault(0.0)

    /**
     * Returns the value if it is a double, or `1` otherwise.
     */
    fun asDoubleOrOne(): Double = asDoubleOrDefault(1.0)

    /**
     * Returns the value if it is a double, or `-1` otherwise.
     */
    fun asDoubleOrMinusOne(): Double = asDoubleOrDefault(-1.0)

    /**
     * Returns the value if it is a double, or `NaN` otherwise.
     */
    fun asDoubleOrNaN(): Double = asDoubleOrDefault(Double.NaN)

    /**
     * Returns the value if it is a double, or the result of a callback otherwise.
     *
     * @param defaultValue the callback to generate the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asDoubleOrElse(defaultValue: () -> Double): Double {
        return asDoubleOrNull() ?: defaultValue()
    }

    /**
     * Checks whether the value is an enum.
     *
     * @param enumClass the enum class
     * @throws NullPointerException if any parameter is `null`
     */
    fun <E : Enum<E>> isEnum(enumClass: Class<E>): Boolean {
        return Conversions.toEnum(value, enumClass) != null
    }

    /**
     * Checks whether the value is an enum.
     *
     * This function is only callable from Kotlin.
     * Use its overload when calling from other languages.
     */
    inline fun <reified E : Enum<E>> isEnum(): Boolean = isEnum(E::class.java)

    /**
     * Returns the value if it is an enum, or `null` otherwise.
     *
     * @throws NullPointerException if any parameter is `null`
     */
    fun <E : Enum<E>> asEnumOrNull(enumClass: Class<E>): E? {
        return Conversions.toEnum(value, enumClass)
    }

    /**
     * Returns the value if it is an enum, or `null` otherwise.
     *
     * This function is only callable from Kotlin.
     * Use its overload when calling from other languages.
     */
    inline fun <reified E : Enum<E>> asEnumOrNull(): E? = asEnumOrNull(E::class.java)

    /**
     * Returns the value if it is an enum, or a default value otherwise.
     *
     * @param defaultValue the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun <E : Enum<E>> asEnumOrDefault(defaultValue: E): E {
        return asEnumOrNull(defaultValue::class.java) ?: defaultValue
    }

    /**
     * Returns the value if it is an enum, or the result of a callback otherwise.
     *
     * @param defaultValue the callback to generate the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun <E : Enum<E>> asEnumOrElse(enumClass: Class<E>, defaultValue: () -> E): E {
        return asEnumOrNull(enumClass) ?: defaultValue()
    }

    /**
     * Returns the value if it is an enum, or the result of a callback otherwise.
     *
     * This function is only callable from Kotlin.
     * Use its overload when calling from other languages.
     *
     * @param defaultValue the callback to generate the default value
     * @throws NullPointerException if any parameter is `null`
     */
    inline fun <reified E : Enum<E>> asEnumOrElse(defaultValue: () -> E): E = asEnumOrNull<E>() ?: defaultValue()

    /**
     * Checks whether the value is a float.
     */
    fun isFloat(): Boolean {
        return Conversions.toFloat(value) != null
    }

    /**
     * Returns the value if it is a float, or `null` otherwise.
     */
    fun asFloatOrNull(): Float? {
        return Conversions.toFloat(value)
    }

    /**
     * Returns the value if it is a float, or a default value otherwise.
     *
     * @param defaultValue the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asFloatOrDefault(defaultValue: Float): Float {
        return asFloatOrNull() ?: defaultValue
    }

    /**
     * Returns the value if it is a float, or `0` otherwise.
     */
    fun asFloatOrZero(): Float = asFloatOrDefault(0F)

    /**
     * Returns the value if it is a float, or `1` otherwise.
     */
    fun asFloatOrOne(): Float = asFloatOrDefault(1F)

    /**
     * Returns the value if it is a float, or `-1` otherwise.
     */
    fun asFloatOrMinusOne(): Float = asFloatOrDefault(-1F)

    /**
     * Returns the value if it is a float, or `NaN` otherwise.
     */
    fun asFloatOrNaN(): Float = asFloatOrDefault(Float.NaN)

    /**
     * Returns the value if it is a float, or the result of a callback otherwise.
     *
     * @param defaultValue the callback to generate the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asFloatOrElse(defaultValue: () -> Float): Float {
        return asFloatOrNull() ?: defaultValue()
    }

    /**
     * Checks whether the value is an integer.
     */
    fun isInt(): Boolean {
        return Conversions.toInt(value) != null
    }

    /**
     * Returns the value if it is an integer, or `null` otherwise.
     */
    fun asIntOrNull(): Int? {
        return Conversions.toInt(value)
    }

    /**
     * Returns the value if it is an integer, or a default value otherwise.
     *
     * @param defaultValue the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asIntOrDefault(defaultValue: Int): Int {
        return asIntOrNull() ?: defaultValue
    }

    /**
     * Returns the value if it is an integer, or `0` otherwise.
     */
    fun asIntOrZero(): Int = asIntOrDefault(0)

    /**
     * Returns the value if it is an integer, or `1` otherwise.
     */
    fun asIntOrOne(): Int = asIntOrDefault(1)

    /**
     * Returns the value if it is an integer, or `-1` otherwise.
     */
    fun asIntOrMinusOne(): Int = asIntOrDefault(-1)

    /**
     * Returns the value if it is an integer, or the result of a callback otherwise.
     *
     * @param defaultValue the callback to generate the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asIntOrElse(defaultValue: () -> Int): Int {
        return asIntOrNull() ?: defaultValue()
    }

    /**
     * Checks whether the value is a long.
     */
    fun isLong(): Boolean {
        return Conversions.toLong(value) != null
    }

    /**
     * Returns the value if it is a long, or `null` otherwise.
     */
    fun asLongOrNull(): Long? {
        return Conversions.toLong(value)
    }

    /**
     * Returns the value if it is a long, or a default value otherwise.
     *
     * @param defaultValue the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asLongOrDefault(defaultValue: Long): Long {
        return asLongOrNull() ?: defaultValue
    }

    /**
     * Returns the value if it is a long, or `0` otherwise.
     */
    fun asLongOrZero(): Long = asLongOrDefault(0L)

    /**
     * Returns the value if it is a long, or `1` otherwise.
     */
    fun asLongOrOne(): Long = asLongOrDefault(1L)

    /**
     * Returns the value if it is a long, or `-1` otherwise.
     */
    fun asLongOrMinusOne(): Long = asLongOrDefault(-1L)

    /**
     * Returns the value if it is a long, or the result of a callback otherwise.
     *
     * @param defaultValue the callback to generate the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asLongOrElse(defaultValue: () -> Long): Long {
        return asLongOrNull() ?: defaultValue()
    }

    /**
     * Checks whether the value is a [Number].
     */
    fun isNumber(): Boolean {
        return Conversions.toNumber(value) != null
    }

    /**
     * Returns the value if it is a [Number], or `null` otherwise.
     */
    fun asNumberOrNull(): Number? {
        return Conversions.toNumber(value)
    }

    /**
     * Returns the value if it is a [Number], or a default value otherwise.
     *
     * @param defaultValue the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asNumberOrDefault(defaultValue: Number): Number {
        return asNumberOrNull() ?: defaultValue
    }

    /**
     * Returns the value if it is a [Number], or the result of a callback otherwise.
     *
     * @param defaultValue the callback to generate the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asNumberOrElse(defaultValue: () -> Number): Number {
        return asNumberOrNull() ?: defaultValue()
    }

    /**
     * Checks whether the value is an object.
     */
    fun isObject(): Boolean {
        return value is ReadOnlyJSONObject
    }

    /**
     * Returns the value if it is an object, or `null` otherwise.
     */
    fun asObjectOrNull(): ReadOnlyJSONObject? {
        return value as? ReadOnlyJSONObject
    }

    /**
     * Returns the value if it is an object, or a default value otherwise.
     *
     * @param defaultValue the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asObjectOrDefault(defaultValue: ReadOnlyJSONObject): ReadOnlyJSONObject {
        return asObjectOrNull() ?: defaultValue
    }

    /**
     * Returns the value if it is an object, or an empty object otherwise.
     */
    fun asObjectOrEmpty(): ReadOnlyJSONObject = asObjectOrDefault(ReadOnlyJSONObject.EMPTY)

    /**
     * Returns the value if it is an object, or the result of a callback otherwise.
     *
     * @param defaultValue the callback to generate the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asObjectOrElse(defaultValue: () -> ReadOnlyJSONObject): ReadOnlyJSONObject {
        return asObjectOrNull() ?: defaultValue()
    }

    /**
     * Checks whether the value is a [String].
     *
     * @param coerce whether to convert non-null, but non-string values to strings
     */
    fun isString(coerce: Boolean = false): Boolean {
        return Conversions.toString(value, coerce) != null
    }

    /**
     * Returns the value if it is a [String], or `null` otherwise.
     *
     * @param coerce whether to convert non-null, but non-string values to strings
     */
    @JvmOverloads
    fun asStringOrNull(coerce: Boolean = false): String? {
        return Conversions.toString(value, coerce)
    }

    /**
     * Returns the value if it is a [String], or a default value otherwise.
     *
     * @param defaultValue the default value
     * @param coerce whether to convert non-null, but non-string values to strings
     * @throws NullPointerException if any parameter is `null`
     */
    @JvmOverloads
    fun asStringOrDefault(defaultValue: String, coerce: Boolean = false): String {
        return asStringOrNull(coerce) ?: defaultValue
    }

    /**
     * Returns the value if it is a [String], or an empty [String] otherwise.
     *
     * @param coerce whether to convert non-null, but non-string values to strings
     * @throws NullPointerException if any parameter is `null`
     */
    @JvmOverloads
    fun asStringOrEmpty(coerce: Boolean = false): String = asStringOrDefault("", coerce)

    /**
     * Returns the value if it is a [String], or the result of a callback otherwise.
     *
     * @param defaultValue the callback to generate the default value
     * @param coerce whether to convert non-null, but non-string values to strings
     * @throws NullPointerException if any parameter is `null`
     */
    fun asStringOrElse(defaultValue: () -> String, coerce: Boolean): String {
        return asStringOrNull(coerce) ?: defaultValue()
    }

    /**
     * Returns the value if it is a [String], or the result of a callback otherwise.
     *
     * @param coerce whether to convert non-null, but non-string values to strings
     * @param defaultValue the callback to generate the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asStringOrElse(coerce: Boolean, defaultValue: () -> String): String {
        return asStringOrNull(coerce) ?: defaultValue()
    }

    /**
     * Returns the value if it is a [String], or the result of a callback otherwise.
     *
     * This overload does not coerce non-strings to string values.
     *
     * @param defaultValue the callback to generate the default value
     * @throws NullPointerException if any parameter is `null`
     */
    fun asStringOrElse(defaultValue: () -> String): String = asStringOrElse(defaultValue, false)

}