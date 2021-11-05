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

package com.github.oneandolaf.jsonext.util

import io.kotest.property.Exhaustive
import io.kotest.property.exhaustive.exhaustive
import io.kotest.property.exhaustive.map
import io.kotest.property.exhaustive.plus
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import java.math.BigInteger

object JSONGenerators {

    val bools = listOf(true, false).exhaustive()

    val explicitNull = listOf(JSONObject.NULL).exhaustive()

    val doubles = listOf(
        0.0,
        1.0,
        -1.0,
        5.0,
        Math.PI,
        42.4156
    ).exhaustive()

    val doubleStrings = doubles.map { it.toString() }

    val floats = listOf(
        0.0f,
        1.0f,
        -1.0f,
        5.0f,
        42.41561f
    ).exhaustive()

    val bigDecimals = listOf(
        BigDecimal.ZERO,
        BigDecimal.ONE,
        BigDecimal.TEN,
        BigDecimal("-1.0"),
        BigDecimal("-42.5"),
        BigDecimal("0.5"),
        BigDecimal("42.4541")
    ).exhaustive()

    val ints = listOf(
        0,
        1,
        -1,
        5,
        42
    ).exhaustive()

    val intStrings = ints.map { it.toString() }

    val bigIntegers = listOf(
        BigInteger.ZERO,
        BigInteger.ONE,
        BigInteger.TEN,
        BigInteger.valueOf(-1L),
        BigInteger.valueOf(5L),
        BigInteger.valueOf(42L)
    ).exhaustive()

    val longs = listOf(
        0L,
        1L,
        -1L,
        5L,
        42L
    ).exhaustive()

    /**
     * Strings that are not representations of booleans or numbers.
     */
    val basicStrings = listOf(
        "",
        "a",
        "foo",
        "long long string"
    ).exhaustive()

    /**
     * Strings that are representations of booleans.
     */
    val booleanStrings = listOf(
        "true",
        "false"
    ).exhaustive()

    /**
     * Strings that are representations of numbers.
     */
    val numericStrings = intStrings + doubleStrings

    /**
     * All strings.
     */
    val allStrings = basicStrings + booleanStrings + numericStrings

    val arraysAsLists = listOf(
        emptyList(),
        listOf(""),
        listOf("foo"),
        listOf(true),
        listOf(false),
        listOf(42),
        listOf(5.0),
        listOf(emptyList<Any>()),
        listOf(listOf("foo"))
    ).exhaustive()

    val arrays = arraysAsLists.map { JSONArray(it) }

    val objectsAsMaps = listOf(
        emptyMap(),
        mapOf("foo" to true),
        mapOf("a" to "a string", "b" to 2)
    ).exhaustive()

    val objects = objectsAsMaps.map { JSONObject(it) }


    val nonNullValues = bools + doubles + ints + arrays + objects + longs + allStrings

    val values = nonNullValues + explicitNull

    /**
     * Primitive numbers that can be JSON values (ints, longs, doubles, and floats).
     */
    val numbers: Exhaustive<Number> = ints + doubles + floats + longs

    /**
     * Combination of numbers and the string representations of ints.
     */
    val intLikes = numbers + intStrings

    /**
     * Combination of numbers that can be JSON values, and String representations of numbers.
     */
    val numberLikes = numbers + numericStrings


    /**
     * Combinations of booleans and String representations thereof.
     */
    val booleanLikes = bools + booleanStrings

    /**
     * Values that are not Strings.
     */
    val nonStrings = values - allStrings

    /**
     * Values that are not arrays.
     */
    val nonArrays = values - arrays

    /**
     * Values that are not objects.
     */
    val nonObjects = values - objects

    /**
     * Values that are not int-likes.
     */
    val nonIntLikes = values - intLikes

    /**
     * Values that are not number-likes.
     */
    val nonNumberLikes = values - numberLikes

    /**
     * Values that are not boolean-likes.
     */
    val nonBooleanLikes = values - booleanLikes
}