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

@file:JvmName("QueryExtensions")

package com.github.oneandolaf.jsonext.extensions

import com.github.oneandolaf.jsonext.impl.Conversions
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONPointer
import java.math.BigDecimal
import java.math.BigInteger

fun JSONObject.queryForBigDecimal(jsonPointer: JSONPointer): BigDecimal {
    val rawResult = query(jsonPointer)
    return Conversions.toBigDecimal(rawResult) ?: throw queryWrongTypeException(jsonPointer, "BigDecimal", rawResult)
}

fun JSONObject.queryForBigDecimal(jsonPointer: String) = queryForBigDecimal(JSONPointer(jsonPointer))

fun JSONObject.queryForBigInteger(jsonPointer: JSONPointer): BigInteger {
    val rawResult = query(jsonPointer)
    return Conversions.toBigInteger(rawResult) ?: throw queryWrongTypeException(jsonPointer, "BigInteger", rawResult)
}

fun JSONObject.queryForBigInteger(jsonPointer: String) = queryForBigInteger(JSONPointer(jsonPointer))

fun JSONObject.queryForBoolean(jsonPointer: JSONPointer): Boolean {
    val rawResult = query(jsonPointer)
    return Conversions.toBoolean(rawResult) ?: throw queryWrongTypeException(jsonPointer, "boolean", rawResult)
}

fun JSONObject.queryForBoolean(jsonPointer: String) = queryForBoolean(JSONPointer(jsonPointer))

fun JSONObject.queryForDouble(jsonPointer: JSONPointer): Double {
    val rawResult = query(jsonPointer)
    return Conversions.toDouble(rawResult) ?: throw queryWrongTypeException(jsonPointer, "double", rawResult)
}

fun JSONObject.queryForDouble(jsonPointer: String) = queryForDouble(JSONPointer(jsonPointer))

fun <E : Enum<E>> JSONObject.queryForEnum(jsonPointer: JSONPointer, enumClass: Class<E>): E {
    val rawResult = query(jsonPointer)
    return Conversions.toEnum(rawResult, enumClass) ?: throw queryWrongTypeException(
        jsonPointer, enumClass.canonicalName,
        rawResult
    )
}

fun <E : Enum<E>> JSONObject.queryForEnum(jsonPointer: String, enumClass: Class<E>) = queryForEnum(
    JSONPointer
        (jsonPointer), enumClass
)

fun JSONObject.queryForFloat(jsonPointer: JSONPointer): Float {
    val rawResult = query(jsonPointer)
    return Conversions.toFloat(rawResult) ?: throw queryWrongTypeException(jsonPointer, "float", rawResult)
}

fun JSONObject.queryForFloat(jsonPointer: String) = queryForFloat(JSONPointer(jsonPointer))

fun JSONObject.queryForInt(jsonPointer: JSONPointer): Int {
    val rawResult = query(jsonPointer)
    return Conversions.toInt(rawResult) ?: throw queryWrongTypeException(jsonPointer, "int", rawResult)
}

fun JSONObject.queryForInt(jsonPointer: String) = queryForInt(JSONPointer(jsonPointer))

fun JSONObject.queryForJSONArray(jsonPointer: JSONPointer): JSONArray {
    val rawResult = query(jsonPointer)
    return rawResult as? JSONArray ?: throw queryWrongTypeException(jsonPointer, "JSONArray", rawResult)
}

fun JSONObject.queryForJSONArray(jsonPointer: String) = queryForJSONArray(JSONPointer(jsonPointer))

fun JSONObject.queryForJSONObject(jsonPointer: JSONPointer): JSONObject {
    val rawResult = query(jsonPointer)
    return rawResult as? JSONObject ?: throw queryWrongTypeException(jsonPointer, "JSONObject", rawResult)
}

fun JSONObject.queryForJSONObject(jsonPointer: String) = queryForJSONObject(JSONPointer(jsonPointer))

fun JSONObject.queryForLong(jsonPointer: JSONPointer): Long {
    val rawResult = query(jsonPointer)
    return Conversions.toLong(rawResult) ?: throw queryWrongTypeException(jsonPointer, "long", rawResult)
}

fun JSONObject.queryForLong(jsonPointer: String) = queryForLong(JSONPointer(jsonPointer))

fun JSONObject.queryForNumber(jsonPointer: JSONPointer): Number {
    val rawResult = query(jsonPointer)
    return Conversions.toNumber(rawResult) ?: throw queryWrongTypeException(jsonPointer, "Number", rawResult)
}

fun JSONObject.queryForNumber(jsonPointer: String) = queryForNumber(JSONPointer(jsonPointer))

fun JSONObject.queryForString(jsonPointer: JSONPointer): String {
    val rawResult = query(jsonPointer)
    return rawResult as? String ?: throw queryWrongTypeException(jsonPointer, "String", rawResult)
}

fun JSONObject.queryForString(jsonPointer: String): String = queryForString(JSONPointer(jsonPointer))

fun JSONObject.optQueryForBigDecimal(jsonPointer: JSONPointer): BigDecimal? {
    return Conversions.toBigDecimal(optQuery(jsonPointer))
}

fun JSONObject.optQueryForBigDecimal(jsonPointer: String) = optQueryForBigDecimal(JSONPointer(jsonPointer))

fun JSONObject.optQueryForBigInteger(jsonPointer: JSONPointer): BigInteger? {
    return Conversions.toBigInteger(optQuery(jsonPointer))
}

fun JSONObject.optQueryForBigInteger(jsonPointer: String) = optQueryForBigInteger(JSONPointer(jsonPointer))

fun JSONObject.optQueryForBoolean(jsonPointer: JSONPointer): Boolean? {
    return Conversions.toBoolean(optQuery(jsonPointer))
}

fun JSONObject.optQueryForBoolean(jsonPointer: String) = optQueryForBoolean(JSONPointer(jsonPointer))

fun JSONObject.optQueryForDouble(jsonPointer: JSONPointer): Double? {
    return Conversions.toNumber(optQuery(jsonPointer))?.toDouble()
}

fun JSONObject.optQueryForDouble(jsonPointer: String) = optQueryForDouble(JSONPointer(jsonPointer))

fun <E : Enum<E>> JSONObject.optQueryForEnum(jsonPointer: JSONPointer, enumClass: Class<E>): E? {
    return Conversions.toEnum(optQuery(jsonPointer), enumClass)
}

fun <E : Enum<E>> JSONObject.optQueryForEnum(jsonPointer: String, enumClass: Class<E>) = optQueryForEnum(
    JSONPointer
        (jsonPointer), enumClass
)

fun JSONObject.optQueryForFloat(jsonPointer: JSONPointer): Float? {
    return Conversions.toNumber(optQuery(jsonPointer))?.toFloat()
}

fun JSONObject.optQueryForFloat(jsonPointer: String) = optQueryForFloat(JSONPointer(jsonPointer))

fun JSONObject.optQueryForInt(jsonPointer: JSONPointer): Int? {
    return Conversions.toNumber(optQuery(jsonPointer))?.toInt()
}

fun JSONObject.optQueryForInt(jsonPointer: String) = optQueryForInt(JSONPointer(jsonPointer))

fun JSONObject.optQueryForJSONArray(jsonPointer: JSONPointer): JSONArray? {
    return optQuery(jsonPointer) as? JSONArray
}

fun JSONObject.optQueryForJSONArray(jsonPointer: String) = optQueryForJSONArray(JSONPointer(jsonPointer))

fun JSONObject.optQueryForJSONObject(jsonPointer: JSONPointer): JSONObject? {
    return optQuery(jsonPointer) as? JSONObject
}

fun JSONObject.optQueryForJSONObject(jsonPointer: String) = optQueryForJSONObject(JSONPointer(jsonPointer))

fun JSONObject.optQueryForLong(jsonPointer: JSONPointer): Long? {
    return Conversions.toNumber(optQuery(jsonPointer))?.toLong()
}

fun JSONObject.optQueryForLong(jsonPointer: String) = optQueryForLong(JSONPointer(jsonPointer))

fun JSONObject.optQueryForNumber(jsonPointer: JSONPointer): Number? {
    return Conversions.toNumber(optQuery(jsonPointer))
}

fun JSONObject.optQueryForNumber(jsonPointer: String) = optQueryForNumber(JSONPointer(jsonPointer))

fun JSONObject.optQueryForString(jsonPointer: JSONPointer): String? {
    return optQuery(jsonPointer)?.toString()
}

fun JSONObject.optQueryForString(jsonPointer: String) = optQueryForString(JSONPointer(jsonPointer))

private fun queryWrongTypeException(pointer: JSONPointer, expectedType: String, value: Any?): Exception {
    return JSONException(
        """Expected type $expectedType when resolving "$pointer", but received ${value?.toString()}"""
    )
}