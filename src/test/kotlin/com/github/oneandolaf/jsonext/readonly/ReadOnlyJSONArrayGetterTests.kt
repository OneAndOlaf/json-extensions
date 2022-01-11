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
import com.github.oneandolaf.jsonext.testutils.JSONGenerators
import com.github.oneandolaf.jsonext.testutils.TestEnum
import com.github.oneandolaf.jsonext.testutils.shouldBeSimilarStringAs
import com.github.oneandolaf.jsonext.testutils.shouldBeSimilarTo
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Exhaustive
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.enum
import io.kotest.property.exhaustive.times
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import java.math.BigInteger

class ReadOnlyJSONArrayGetterTests : FunSpec({

    context("getOrNull") {
        checkAll(JSONGenerators.values) {
            val arr = JSONArray().put(it)
            val orig = arr.deepCopy()

            val ro = ReadOnlyJSONArray.create(arr)

            ro.getOrNull(0) shouldBeSimilarTo it

            if (it is JSONObject) {
                ro.getOrNull(0).shouldBeInstanceOf<ReadOnlyJSONObject>()
            } else if (it is JSONArray) {
                ro.getOrNull(0).shouldBeInstanceOf<ReadOnlyJSONArray>()
            }

            ro.getOrNull(1).shouldBeNull()
            ro.getOrNull(2).shouldBeNull()
            ro.getOrNull(42).shouldBeNull()
            ro.getOrNull(-1).shouldBeNull()
            ro.getOrNull(-10).shouldBeNull()

            arr shouldBeSimilarTo orig
        }
    }

    context("getOrElse") {
        checkAll(JSONGenerators.values * JSONGenerators.values) { data ->
            val arr = JSONArray().put(data.first)
            val orig = arr.deepCopy()

            val ro = ReadOnlyJSONArray.create(arr)

            ro.getOrElse(0) { data.second } shouldBeSimilarTo data.first
            ro.getOrElse(1) { data.second } shouldBeSimilarTo data.second

            arr shouldBeSimilarTo orig
        }
    }

    context("getArrayOrNull") {
        checkAll(JSONGenerators.arrays) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getArrayOrNull(0) shouldBeSimilarTo it
            }
        }

        checkAll(JSONGenerators.nonArrays) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getArrayOrNull(0).shouldBeNull()
            }
        }
    }

    context("getArrayOrDefault") {
        checkAll(JSONGenerators.arrays * JSONGenerators.arrays) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getArrayOrDefault(0, ReadOnlyJSONArray.create(it.second)) shouldBeSimilarTo it.first
            }
        }

        checkAll(JSONGenerators.nonArrays * JSONGenerators.arrays) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getArrayOrDefault(0, ReadOnlyJSONArray.create(it.second)) shouldBeSimilarTo it.second
            }
        }
    }

    context("getArrayOrEmpty") {
        checkAll(JSONGenerators.arrays) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getArrayOrEmpty(0) shouldBeSimilarTo it
            }
        }

        checkAll(JSONGenerators.nonArrays) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getArrayOrEmpty(0) shouldBeSimilarTo JSONArray()
            }
        }
    }

    context("getArrayOrElse") {
        checkAll(JSONGenerators.arrays * JSONGenerators.arrays) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getArrayOrElse(0) {
                    ReadOnlyJSONArray.create(data.second)
                } shouldBeSimilarTo data.first
            }
        }

        checkAll(JSONGenerators.nonArrays * JSONGenerators.arrays) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getArrayOrElse(0) {
                    ReadOnlyJSONArray.create(data.second)
                } shouldBeSimilarTo data.second
            }
        }
    }

    context("getBigDecimalOrNull") {
        checkAll(JSONGenerators.numberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBigDecimalOrNull(0) shouldBe BigDecimal(it.toString())
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBigDecimalOrNull(0).shouldBeNull()
            }
        }
    }

    context("getBigDecimalOrDefault") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.bigDecimals) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getBigDecimalOrDefault(0, it.second) shouldBe BigDecimal(it.first.toString())
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.bigDecimals) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getBigDecimalOrDefault(0, it.second) shouldBe it.second
            }
        }
    }

    context("getBigDecimalOrZero") {
        checkAll(JSONGenerators.numberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBigDecimalOrZero(0) shouldBe BigDecimal(it.toString())
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBigDecimalOrZero(0) shouldBe BigDecimal.ZERO
            }
        }
    }

    context("getBigDecimalOrOne") {
        checkAll(JSONGenerators.numberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBigDecimalOrOne(0) shouldBe BigDecimal(it.toString())
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBigDecimalOrOne(0) shouldBe BigDecimal.ONE
            }
        }
    }

    context("getBigDecimalOrMinusOne") {
        checkAll(JSONGenerators.numberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBigDecimalOrMinusOne(0) shouldBe BigDecimal(it.toString())
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBigDecimalOrMinusOne(0) shouldBe BigDecimal.valueOf(-1L)
            }
        }
    }

    context("getBigDecimalOrElse") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.bigDecimals) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getBigDecimalOrElse(0) { data.second } shouldBe BigDecimal(data.first.toString())
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.bigDecimals) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getBigDecimalOrElse(0) { data.second } shouldBe data.second
            }
        }
    }

    context("getBigIntegerOrNull") {
        checkAll(JSONGenerators.numberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBigIntegerOrNull(0) shouldBe BigDecimal(it.toString()).toBigInteger()
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBigIntegerOrNull(0).shouldBeNull()
            }
        }
    }

    context("getBigIntegerOrDefault") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.bigIntegers) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getBigIntegerOrDefault(0, it.second) shouldBe BigDecimal(it.first.toString()).toBigInteger()
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.bigIntegers) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getBigIntegerOrDefault(0, it.second) shouldBe it.second
            }
        }
    }

    context("getBigIntegerOrZero") {
        checkAll(JSONGenerators.numberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBigIntegerOrZero(0) shouldBe BigDecimal(it.toString()).toBigInteger()
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBigIntegerOrZero(0) shouldBe BigInteger.ZERO
            }
        }
    }

    context("getBigIntegerOrOne") {
        checkAll(JSONGenerators.numberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBigIntegerOrOne(0) shouldBe BigDecimal(it.toString()).toBigInteger()
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBigIntegerOrOne(0) shouldBe BigInteger.ONE
            }
        }
    }

    context("getBigIntegerOrMinusOne") {
        checkAll(JSONGenerators.numberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBigIntegerOrMinusOne(0) shouldBe BigDecimal(it.toString()).toBigInteger()
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBigIntegerOrMinusOne(0) shouldBe BigInteger.valueOf(-1L)
            }
        }
    }

    context("getBigIntegerOrElse") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.bigIntegers) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getBigIntegerOrElse(0) { data.second } shouldBe BigDecimal(data.first.toString()).toBigInteger()
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.bigIntegers) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getBigIntegerOrElse(0) { data.second } shouldBe data.second
            }
        }
    }

    context("getBooleanOrNull") {
        checkAll(JSONGenerators.booleanLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBooleanOrNull(0) shouldBe Conversions.toBoolean(it)!!
            }
        }

        checkAll(JSONGenerators.nonBooleanLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBooleanOrNull(0).shouldBeNull()
            }
        }
    }

    context("getBooleanOrDefault") {
        checkAll(JSONGenerators.booleanLikes * JSONGenerators.bools) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getBooleanOrDefault(0, it.second) shouldBe Conversions.toBoolean(it.first)!!
            }
        }

        checkAll(JSONGenerators.nonBooleanLikes * JSONGenerators.bools) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getBooleanOrDefault(0, it.second) shouldBe it.second
            }
        }
    }

    context("getBooleanOrTrue") {
        checkAll(JSONGenerators.booleanLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBooleanOrTrue(0) shouldBe Conversions.toBoolean(it)!!
            }
        }

        checkAll(JSONGenerators.nonBooleanLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBooleanOrTrue(0) shouldBe true
            }
        }
    }

    context("getBooleanOrFalse") {
        checkAll(JSONGenerators.booleanLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBooleanOrFalse(0) shouldBe Conversions.toBoolean(it)!!
            }
        }

        checkAll(JSONGenerators.nonBooleanLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getBooleanOrFalse(0) shouldBe false
            }
        }
    }

    context("getBooleanOrElse") {
        checkAll(JSONGenerators.booleanLikes * JSONGenerators.bools) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getBooleanOrElse(0) { data.second } shouldBe Conversions.toBoolean(data.first)!!
            }
        }

        checkAll(JSONGenerators.nonBooleanLikes * JSONGenerators.bools) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getBooleanOrElse(0) { data.second } shouldBe data.second
            }
        }
    }

    context("getDoubleOrNull") {
        checkAll(JSONGenerators.numberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getDoubleOrNull(0) shouldBe Conversions.toDouble(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getDoubleOrNull(0).shouldBeNull()
            }
        }
    }

    context("getDoubleOrDefault") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.doubles) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getDoubleOrDefault(0, it.second) shouldBe Conversions.toDouble(it.first)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.doubles) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getDoubleOrDefault(0, it.second) shouldBe it.second
            }
        }
    }

    context("getDoubleOrZero") {
        checkAll(JSONGenerators.numberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getDoubleOrZero(0) shouldBe Conversions.toDouble(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getDoubleOrZero(0) shouldBe 0.0
            }
        }
    }

    context("getDoubleOrOne") {
        checkAll(JSONGenerators.numberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getDoubleOrOne(0) shouldBe Conversions.toDouble(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getDoubleOrOne(0) shouldBe 1.0
            }
        }
    }

    context("getDoubleOrMinusOne") {
        checkAll(JSONGenerators.numberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getDoubleOrMinusOne(0) shouldBe Conversions.toDouble(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getDoubleOrMinusOne(0) shouldBe -1.0
            }
        }
    }

    context("getDoubleOrNaN") {
        checkAll(JSONGenerators.numberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getDoubleOrNaN(0) shouldBe Conversions.toDouble(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getDoubleOrNaN(0) shouldBe Double.NaN
            }
        }
    }

    context("getDoubleOrElse") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.doubles) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getDoubleOrElse(0) { data.second } shouldBe Conversions.toDouble(data.first)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.doubles) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getDoubleOrElse(0) { data.second } shouldBe data.second
            }
        }
    }

    context("getEnumOrNull") {
        checkAll(Exhaustive.enum<TestEnum>()) {
            val arr = JSONArray().put(it.name)

            checkWithReadOnly(arr) {
                getEnumOrNull(TestEnum::class.java, 0) shouldBe it
                getEnumOrNull<TestEnum>(0) shouldBe it
            }
        }

        checkAll(JSONGenerators.values) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getEnumOrNull(TestEnum::class.java, 0).shouldBeNull()
                getEnumOrNull<TestEnum>(0).shouldBeNull()
            }
        }
    }

    context("getEnumOrDefault") {
        checkAll(Exhaustive.enum<TestEnum>() * Exhaustive.enum()) {
            val arr = JSONArray().put(it.first.name)

            checkWithReadOnly(arr) {
                getEnumOrDefault(TestEnum::class.java, 0, it.second) shouldBe it.first
                getEnumOrDefault(0, it.second) shouldBe it.first
            }
        }

        checkAll(JSONGenerators.values * Exhaustive.enum<TestEnum>()) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getEnumOrDefault(TestEnum::class.java, 0, it.second) shouldBe it.second
                getEnumOrDefault(0, it.second) shouldBe it.second

            }
        }
    }

    context("getEnumOrElse") {
        checkAll(Exhaustive.enum<TestEnum>() * Exhaustive.enum()) { data ->
            val arr = JSONArray().put(data.first.name)

            checkWithReadOnly(arr) {
                getEnumOrElse(TestEnum::class.java, 0) { data.second } shouldBe data.first
                getEnumOrElse(0) { data.second } shouldBe data.first
            }
        }

        checkAll(JSONGenerators.values * Exhaustive.enum<TestEnum>()) { data ->
            val arr = JSONArray().put(data)

            checkWithReadOnly(arr) {
                getEnumOrElse(TestEnum::class.java, 0) { data.second } shouldBe data.second
                getEnumOrElse(0) { data.second } shouldBe data.second
            }
        }
    }

    context("getFloatOrNull") {
        checkAll(JSONGenerators.numberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getFloatOrNull(0) shouldBe Conversions.toFloat(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getFloatOrNull(0).shouldBeNull()
            }
        }
    }

    context("getFloatOrDefault") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.floats) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getFloatOrDefault(0, it.second) shouldBe Conversions.toFloat(it.first)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.floats) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getFloatOrDefault(0, it.second) shouldBe it.second
            }
        }
    }

    context("getFloatOrZero") {
        checkAll(JSONGenerators.numberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getFloatOrZero(0) shouldBe Conversions.toFloat(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getFloatOrZero(0) shouldBe 0.0f
            }
        }
    }

    context("getFloatOrOne") {
        checkAll(JSONGenerators.numberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getFloatOrOne(0) shouldBe Conversions.toFloat(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getFloatOrOne(0) shouldBe 1.0f
            }
        }
    }

    context("getFloatOrMinusOne") {
        checkAll(JSONGenerators.numberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getFloatOrMinusOne(0) shouldBe Conversions.toFloat(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getFloatOrMinusOne(0) shouldBe -1.0f
            }
        }
    }

    context("getFloatOrNaN") {
        checkAll(JSONGenerators.numberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getFloatOrNaN(0) shouldBe Conversions.toFloat(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getFloatOrNaN(0) shouldBe Float.NaN
            }
        }
    }

    context("getFloatOrElse") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.floats) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getFloatOrElse(0) { data.second } shouldBe Conversions.toFloat(data.first)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.floats) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getFloatOrElse(0) { data.second } shouldBe data.second
            }
        }
    }

    context("getIntOrNull") {
        checkAll(JSONGenerators.intLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getIntOrNull(0) shouldBe Conversions.toInt(it)!!
            }
        }

        checkAll(JSONGenerators.nonIntLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getIntOrNull(0).shouldBeNull()
            }
        }
    }

    context("getIntOrDefault") {
        checkAll(JSONGenerators.intLikes * JSONGenerators.ints) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getIntOrDefault(0, it.second) shouldBe Conversions.toInt(it.first)
            }
        }

        checkAll(JSONGenerators.nonIntLikes * JSONGenerators.ints) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getIntOrDefault(0, it.second) shouldBe it.second
            }
        }
    }

    context("getIntOrZero") {
        checkAll(JSONGenerators.intLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getIntOrZero(0) shouldBe Conversions.toInt(it)
            }
        }

        checkAll(JSONGenerators.nonIntLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getIntOrZero(0) shouldBe 0
            }
        }
    }

    context("getIntOrOne") {
        checkAll(JSONGenerators.intLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getIntOrOne(0) shouldBe Conversions.toInt(it)
            }
        }

        checkAll(JSONGenerators.nonIntLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getIntOrOne(0) shouldBe 1
            }
        }
    }

    context("getIntOrMinusOne") {
        checkAll(JSONGenerators.intLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getIntOrMinusOne(0) shouldBe Conversions.toInt(it)
            }
        }

        checkAll(JSONGenerators.nonIntLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getIntOrMinusOne(0) shouldBe -1
            }
        }
    }

    context("getIntOrElse") {
        checkAll(JSONGenerators.intLikes * JSONGenerators.ints) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getIntOrElse(0) { data.second } shouldBe Conversions.toInt(data.first)
            }
        }

        checkAll(JSONGenerators.nonIntLikes * JSONGenerators.ints) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getIntOrElse(0) { data.second } shouldBe data.second
            }
        }
    }

    context("getLongOrNull") {
        checkAll(JSONGenerators.intLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getLongOrNull(0) shouldBe Conversions.toLong(it)
            }
        }

        checkAll(JSONGenerators.nonIntLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getLongOrNull(0).shouldBeNull()
            }
        }
    }

    context("getLongOrDefault") {
        checkAll(JSONGenerators.intLikes * JSONGenerators.longs) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getLongOrDefault(0, it.second) shouldBe Conversions.toLong(it.first)
            }
        }

        checkAll(JSONGenerators.nonIntLikes * JSONGenerators.longs) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getLongOrDefault(0, it.second) shouldBe it.second
            }
        }
    }

    context("getLongOrZero") {
        checkAll(JSONGenerators.intLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getLongOrZero(0) shouldBe Conversions.toLong(it)
            }
        }

        checkAll(JSONGenerators.nonIntLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getLongOrZero(0) shouldBe 0L
            }
        }
    }

    context("getLongOrOne") {
        checkAll(JSONGenerators.intLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getLongOrOne(0) shouldBe Conversions.toLong(it)
            }
        }

        checkAll(JSONGenerators.nonIntLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getLongOrOne(0) shouldBe 1L
            }
        }
    }

    context("getLongOrMinusOne") {
        checkAll(JSONGenerators.intLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getLongOrMinusOne(0) shouldBe Conversions.toLong(it)
            }
        }

        checkAll(JSONGenerators.nonIntLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getLongOrMinusOne(0) shouldBe -1L
            }
        }
    }

    context("getLongOrElse") {
        checkAll(JSONGenerators.intLikes * JSONGenerators.longs) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getLongOrElse(0) { data.second } shouldBe Conversions.toLong(data.first)
            }
        }

        checkAll(JSONGenerators.nonIntLikes * JSONGenerators.longs) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getLongOrElse(0) { data.second } shouldBe data.second
            }
        }
    }

    context("getNumberOrNull") {
        checkAll(JSONGenerators.numberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getNumberOrNull(0)!!.toString() shouldBe it.toString()
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getNumberOrNull(0).shouldBeNull()
            }
        }
    }

    context("getNumberOrDefault") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.numbers) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getNumberOrDefault(0, it.second).toString() shouldBe it.first.toString()
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.numbers) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getNumberOrDefault(0, it.second) shouldBe it.second
            }
        }
    }

    context("getNumberOrElse") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.numbers) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getNumberOrElse(0) { data.second }.toString() shouldBe data.first.toString()
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.numbers) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getNumberOrElse(0) { data.second } shouldBe data.second
            }
        }
    }

    context("getObjectOrNull") {
        checkAll(JSONGenerators.objects) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getObjectOrNull(0) shouldBeSimilarTo it
            }
        }

        checkAll(JSONGenerators.nonObjects) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getObjectOrNull(0).shouldBeNull()
            }
        }
    }

    context("getObjectOrDefault") {
        checkAll(JSONGenerators.objects * JSONGenerators.objects) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getObjectOrDefault(0, ReadOnlyJSONObject.create(it.second)) shouldBeSimilarTo it.first
            }
        }

        checkAll(JSONGenerators.nonObjects * JSONGenerators.objects) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getObjectOrDefault(0, ReadOnlyJSONObject.create(it.second)) shouldBeSimilarTo it.second
            }
        }
    }

    context("getObjectOrEmpty") {
        checkAll(JSONGenerators.objects) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getObjectOrEmpty(0) shouldBeSimilarTo it
            }
        }

        checkAll(JSONGenerators.nonObjects) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getObjectOrEmpty(0) shouldBeSimilarTo JSONObject()
            }
        }
    }

    context("getObjectOrElse") {
        checkAll(JSONGenerators.objects * JSONGenerators.objects) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getObjectOrElse(0) {
                    ReadOnlyJSONObject.create(data.second)
                } shouldBeSimilarTo data.first
            }
        }

        checkAll(JSONGenerators.nonObjects * JSONGenerators.objects) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getObjectOrElse(0) {
                    ReadOnlyJSONObject.create(data.second)
                } shouldBeSimilarTo data.second
            }
        }
    }

    context("getStringOrNull") {
        checkAll(JSONGenerators.allStrings) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getStringOrNull(0) shouldBe it
                getStringOrNull(0, false) shouldBe it
                getStringOrNull(0, true) shouldBe it
            }
        }

        checkAll(JSONGenerators.nonStrings) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getStringOrNull(0).shouldBeNull()
                getStringOrNull(0, false).shouldBeNull()
                getStringOrNull(0, true) shouldBeSimilarStringAs it

            }
        }
    }

    context("getStringOrDefault") {
        checkAll(JSONGenerators.allStrings * JSONGenerators.allStrings) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getStringOrDefault(0, it.second) shouldBe it.first
                getStringOrDefault(0, it.second, false) shouldBe it.first
                getStringOrDefault(0, it.second, true) shouldBe it.first
            }
        }

        checkAll(JSONGenerators.nonStrings * JSONGenerators.allStrings) {
            val arr = JSONArray().put(it.first)

            checkWithReadOnly(arr) {
                getStringOrDefault(0, it.second) shouldBe it.second
                getStringOrDefault(0, it.second, false) shouldBe it.second
                getStringOrDefault(0, it.second, true) shouldBeSimilarStringAs it.first
            }
        }
    }

    context("getStringOrEmpty") {
        checkAll(JSONGenerators.allStrings) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getStringOrEmpty(0) shouldBe it
                getStringOrEmpty(0, false) shouldBe it
                getStringOrEmpty(0, true) shouldBe it
            }
        }

        checkAll(JSONGenerators.nonStrings) {
            val arr = JSONArray().put(it)

            checkWithReadOnly(arr) {
                getStringOrEmpty(0).shouldBeEmpty()
                getStringOrEmpty(0, false).shouldBeEmpty()
                getStringOrEmpty(0, true) shouldBeSimilarStringAs it
            }
        }
    }

    context("getStringOrElse") {
        checkAll(JSONGenerators.allStrings * JSONGenerators.allStrings) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getStringOrElse(0) { data.second } shouldBe data.first
                getStringOrElse(0, false) { data.second } shouldBe data.first
                getStringOrElse(0, true) { data.second } shouldBe data.first
                getStringOrElse(0, { data.second }, false) shouldBe data.first
                getStringOrElse(0, { data.second }, true) shouldBe data.first
            }
        }

        checkAll(JSONGenerators.nonStrings * JSONGenerators.allStrings) { data ->
            val arr = JSONArray().put(data.first)

            checkWithReadOnly(arr) {
                getStringOrElse(0) { data.second } shouldBe data.second
                getStringOrElse(0, false) { data.second } shouldBe data.second
                getStringOrElse(0, true) { data.second } shouldBeSimilarStringAs data.first
                getStringOrElse(0, { data.second }, false) shouldBe data.second
                getStringOrElse(0, { data.second }, true) shouldBeSimilarStringAs data.first
            }
        }
    }
})

private fun checkWithReadOnly(orig: JSONArray, func: ReadOnlyJSONArray.() -> Unit) {
    val ro = ReadOnlyJSONArray.snapshot(orig)

    ro.func()

    ro shouldBeSimilarTo orig
}