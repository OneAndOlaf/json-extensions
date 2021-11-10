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

import com.github.oneandolaf.jsonext.TestEnum
import com.github.oneandolaf.jsonext.extensions.deepCopy
import com.github.oneandolaf.jsonext.impl.Conversions
import com.github.oneandolaf.jsonext.util.JSONGenerators
import com.github.oneandolaf.jsonext.util.shouldBeSimilarTo
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


class ReadOnlyJSONObjectGetterTests : FunSpec({


    context("getOrNull") {
        checkAll(JSONGenerators.values) {
            val obj = JSONObject().put("val", it)
            val orig = obj.deepCopy()

            val ro = ReadOnlyJSONObject(obj)

            ro.getOrNull("val") shouldBeSimilarTo it

            if (it is JSONObject) {
                ro.getOrNull("val").shouldBeInstanceOf<ReadOnlyJSONObject>()
            } else if (it is JSONArray) {
                ro.getOrNull("val").shouldBeInstanceOf<ReadOnlyJSONArray>()
            }

            ro.getOrNull("missing").shouldBeNull()

            obj shouldBeSimilarTo orig
        }
    }

    context("getOrElse") {
        checkAll(JSONGenerators.values * JSONGenerators.values) { data ->
            val obj = JSONObject().put("val", data.first)
            val orig = obj.deepCopy()

            val ro = ReadOnlyJSONObject(obj)

            ro.getOrElse("val") { data.second } shouldBeSimilarTo data.first
            ro.getOrElse("missing") { data.second } shouldBeSimilarTo data.second

            obj shouldBeSimilarTo orig
        }
    }

    context("getArrayOrNull") {
        checkAll(JSONGenerators.arrays) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getArrayOrNull("val") shouldBeSimilarTo it
            }
        }

        checkAll(JSONGenerators.nonArrays) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getArrayOrNull("val").shouldBeNull()
            }
        }
    }

    context("getArrayOrDefault") {
        checkAll(JSONGenerators.arrays * JSONGenerators.arrays) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getArrayOrDefault("val", ReadOnlyJSONArray(it.second)) shouldBeSimilarTo it.first
            }
        }

        checkAll(JSONGenerators.nonArrays * JSONGenerators.arrays) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getArrayOrDefault("val", ReadOnlyJSONArray(it.second)) shouldBeSimilarTo it.second
            }
        }
    }

    context("getArrayOrEmpty") {
        checkAll(JSONGenerators.arrays) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getArrayOrEmpty("val") shouldBeSimilarTo it
            }
        }

        checkAll(JSONGenerators.nonArrays) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getArrayOrEmpty("val") shouldBeSimilarTo JSONArray()
            }
        }
    }

    context("getArrayOrElse") {
        checkAll(JSONGenerators.arrays * JSONGenerators.arrays) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getArrayOrElse("val") {
                    ReadOnlyJSONArray(data.second)
                } shouldBeSimilarTo data.first
            }
        }

        checkAll(JSONGenerators.nonArrays * JSONGenerators.arrays) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getArrayOrElse("val") {
                    ReadOnlyJSONArray(data.second)
                } shouldBeSimilarTo data.second
            }
        }
    }

    context("getBigDecimalOrNull") {
        checkAll(JSONGenerators.numberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBigDecimalOrNull("val") shouldBe BigDecimal(it.toString())
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBigDecimalOrNull("val").shouldBeNull()
            }
        }
    }

    context("getBigDecimalOrDefault") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.bigDecimals) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getBigDecimalOrDefault("val", it.second) shouldBe BigDecimal(it.first.toString())
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.bigDecimals) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getBigDecimalOrDefault("val", it.second) shouldBe it.second
            }
        }
    }

    context("getBigDecimalOrZero") {
        checkAll(JSONGenerators.numberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBigDecimalOrZero("val") shouldBe BigDecimal(it.toString())
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBigDecimalOrZero("val") shouldBe BigDecimal.ZERO
            }
        }
    }

    context("getBigDecimalOrOne") {
        checkAll(JSONGenerators.numberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBigDecimalOrOne("val") shouldBe BigDecimal(it.toString())
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBigDecimalOrOne("val") shouldBe BigDecimal.ONE
            }
        }
    }

    context("getBigDecimalOrMinusOne") {
        checkAll(JSONGenerators.numberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBigDecimalOrMinusOne("val") shouldBe BigDecimal(it.toString())
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBigDecimalOrMinusOne("val") shouldBe BigDecimal.valueOf(-1L)
            }
        }
    }

    context("getBigDecimalOrElse") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.bigDecimals) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getBigDecimalOrElse("val") { data.second } shouldBe BigDecimal(data.first.toString())
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.bigDecimals) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getBigDecimalOrElse("val") { data.second } shouldBe data.second
            }
        }
    }

    context("getBigIntegerOrNull") {
        checkAll(JSONGenerators.numberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBigIntegerOrNull("val") shouldBe BigDecimal(it.toString()).toBigInteger()
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBigIntegerOrNull("val").shouldBeNull()
            }
        }
    }

    context("getBigIntegerOrDefault") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.bigIntegers) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getBigIntegerOrDefault("val", it.second) shouldBe BigDecimal(it.first.toString()).toBigInteger()
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.bigIntegers) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getBigIntegerOrDefault("val", it.second) shouldBe it.second
            }
        }
    }

    context("getBigIntegerOrZero") {
        checkAll(JSONGenerators.numberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBigIntegerOrZero("val") shouldBe BigDecimal(it.toString()).toBigInteger()
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBigIntegerOrZero("val") shouldBe BigInteger.ZERO
            }
        }
    }

    context("getBigIntegerOrOne") {
        checkAll(JSONGenerators.numberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBigIntegerOrOne("val") shouldBe BigDecimal(it.toString()).toBigInteger()
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBigIntegerOrOne("val") shouldBe BigInteger.ONE
            }
        }
    }

    context("getBigIntegerOrMinusOne") {
        checkAll(JSONGenerators.numberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBigIntegerOrMinusOne("val") shouldBe BigDecimal(it.toString()).toBigInteger()
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBigIntegerOrMinusOne("val") shouldBe BigInteger.valueOf(-1L)
            }
        }
    }

    context("getBigIntegerOrElse") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.bigIntegers) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getBigIntegerOrElse("val") { data.second } shouldBe BigDecimal(data.first.toString()).toBigInteger()
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.bigIntegers) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getBigIntegerOrElse("val") { data.second } shouldBe data.second
            }
        }
    }

    context("getBooleanOrNull") {
        checkAll(JSONGenerators.booleanLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBooleanOrNull("val") shouldBe Conversions.toBoolean(it)!!
            }
        }

        checkAll(JSONGenerators.nonBooleanLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBooleanOrNull("val").shouldBeNull()
            }
        }
    }

    context("getBooleanOrDefault") {
        checkAll(JSONGenerators.booleanLikes * JSONGenerators.bools) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getBooleanOrDefault("val", it.second) shouldBe Conversions.toBoolean(it.first)!!
            }
        }

        checkAll(JSONGenerators.nonBooleanLikes * JSONGenerators.bools) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getBooleanOrDefault("val", it.second) shouldBe it.second
            }
        }
    }

    context("getBooleanOrTrue") {
        checkAll(JSONGenerators.booleanLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBooleanOrTrue("val") shouldBe Conversions.toBoolean(it)!!
            }
        }

        checkAll(JSONGenerators.nonBooleanLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBooleanOrTrue("val") shouldBe true
            }
        }
    }

    context("getBooleanOrFalse") {
        checkAll(JSONGenerators.booleanLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBooleanOrFalse("val") shouldBe Conversions.toBoolean(it)!!
            }
        }

        checkAll(JSONGenerators.nonBooleanLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getBooleanOrFalse("val") shouldBe false
            }
        }
    }

    context("getBooleanOrElse") {
        checkAll(JSONGenerators.booleanLikes * JSONGenerators.bools) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getBooleanOrElse("val") { data.second } shouldBe Conversions.toBoolean(data.first)!!
            }
        }

        checkAll(JSONGenerators.nonBooleanLikes * JSONGenerators.bools) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getBooleanOrElse("val") { data.second } shouldBe data.second
            }
        }
    }

    context("getDoubleOrNull") {
        checkAll(JSONGenerators.numberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getDoubleOrNull("val") shouldBe Conversions.toDouble(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getDoubleOrNull("val").shouldBeNull()
            }
        }
    }

    context("getDoubleOrDefault") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.doubles) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getDoubleOrDefault("val", it.second) shouldBe Conversions.toDouble(it.first)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.doubles) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getDoubleOrDefault("val", it.second) shouldBe it.second
            }
        }
    }

    context("getDoubleOrZero") {
        checkAll(JSONGenerators.numberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getDoubleOrZero("val") shouldBe Conversions.toDouble(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getDoubleOrZero("val") shouldBe 0.0
            }
        }
    }

    context("getDoubleOrOne") {
        checkAll(JSONGenerators.numberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getDoubleOrOne("val") shouldBe Conversions.toDouble(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getDoubleOrOne("val") shouldBe 1.0
            }
        }
    }

    context("getDoubleOrMinusOne") {
        checkAll(JSONGenerators.numberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getDoubleOrMinusOne("val") shouldBe Conversions.toDouble(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getDoubleOrMinusOne("val") shouldBe -1.0
            }
        }
    }

    context("getDoubleOrNaN") {
        checkAll(JSONGenerators.numberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getDoubleOrNaN("val") shouldBe Conversions.toDouble(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getDoubleOrNaN("val") shouldBe Double.NaN
            }
        }
    }

    context("getDoubleOrElse") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.doubles) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getDoubleOrElse("val") { data.second } shouldBe Conversions.toDouble(data.first)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.doubles) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getDoubleOrElse("val") { data.second } shouldBe data.second
            }
        }
    }

    context("getEnumOrNull") {
        checkAll(Exhaustive.enum<TestEnum>()) {
            val obj = JSONObject().put("val", it.name)

            checkWithReadOnly(obj) {
                getEnumOrNull(TestEnum::class.java, "val") shouldBe it
                getEnumOrNull<TestEnum>("val") shouldBe it
            }
        }

        checkAll(JSONGenerators.values) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getEnumOrNull(TestEnum::class.java, "val").shouldBeNull()
                getEnumOrNull<TestEnum>("val").shouldBeNull()
            }
        }
    }

    context("getEnumOrDefault") {
        checkAll(Exhaustive.enum<TestEnum>() * Exhaustive.enum()) {
            val obj = JSONObject().put("val", it.first.name)

            checkWithReadOnly(obj) {
                getEnumOrDefault(TestEnum::class.java, "val", it.second) shouldBe it.first
                getEnumOrDefault("val", it.second) shouldBe it.first
            }
        }

        checkAll(JSONGenerators.values * Exhaustive.enum<TestEnum>()) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getEnumOrDefault(TestEnum::class.java, "val", it.second) shouldBe it.second
                getEnumOrDefault("val", it.second) shouldBe it.second
            }
        }
    }

    context("getEnumOrElse") {
        checkAll(Exhaustive.enum<TestEnum>() * Exhaustive.enum()) { data ->
            val obj = JSONObject().put("val", data.first.name)

            checkWithReadOnly(obj) {
                getEnumOrElse(TestEnum::class.java, "val") { data.second } shouldBe data.first
                getEnumOrElse("val") { data.second } shouldBe data.first
            }
        }

        checkAll(JSONGenerators.values * Exhaustive.enum<TestEnum>()) { data ->
            val obj = JSONObject().put("val", data)

            checkWithReadOnly(obj) {
                getEnumOrElse(TestEnum::class.java, "val") { data.second } shouldBe data.second
                getEnumOrElse("val") { data.second } shouldBe data.second
            }
        }
    }

    context("getFloatOrNull") {
        checkAll(JSONGenerators.numberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getFloatOrNull("val") shouldBe Conversions.toFloat(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getFloatOrNull("val").shouldBeNull()
            }
        }
    }

    context("getFloatOrDefault") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.floats) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getFloatOrDefault("val", it.second) shouldBe Conversions.toFloat(it.first)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.floats) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getFloatOrDefault("val", it.second) shouldBe it.second
            }
        }
    }

    context("getFloatOrZero") {
        checkAll(JSONGenerators.numberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getFloatOrZero("val") shouldBe Conversions.toFloat(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getFloatOrZero("val") shouldBe 0.0f
            }
        }
    }

    context("getFloatOrOne") {
        checkAll(JSONGenerators.numberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getFloatOrOne("val") shouldBe Conversions.toFloat(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getFloatOrOne("val") shouldBe 1.0f
            }
        }
    }

    context("getFloatOrMinusOne") {
        checkAll(JSONGenerators.numberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getFloatOrMinusOne("val") shouldBe Conversions.toFloat(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getFloatOrMinusOne("val") shouldBe -1.0f
            }
        }
    }

    context("getFloatOrNaN") {
        checkAll(JSONGenerators.numberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getFloatOrNaN("val") shouldBe Conversions.toFloat(it)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getFloatOrNaN("val") shouldBe Float.NaN
            }
        }
    }

    context("getFloatOrElse") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.floats) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getFloatOrElse("val") { data.second } shouldBe Conversions.toFloat(data.first)
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.floats) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getFloatOrElse("val") { data.second } shouldBe data.second
            }
        }
    }

    context("getIntOrNull") {
        checkAll(JSONGenerators.intLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getIntOrNull("val") shouldBe Conversions.toInt(it)!!
            }
        }

        checkAll(JSONGenerators.nonIntLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getIntOrNull("val").shouldBeNull()
            }
        }
    }

    context("getIntOrDefault") {
        checkAll(JSONGenerators.intLikes * JSONGenerators.ints) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getIntOrDefault("val", it.second) shouldBe Conversions.toInt(it.first)
            }
        }

        checkAll(JSONGenerators.nonIntLikes * JSONGenerators.ints) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getIntOrDefault("val", it.second) shouldBe it.second
            }
        }
    }

    context("getIntOrZero") {
        checkAll(JSONGenerators.intLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getIntOrZero("val") shouldBe Conversions.toInt(it)
            }
        }

        checkAll(JSONGenerators.nonIntLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getIntOrZero("val") shouldBe 0
            }
        }
    }

    context("getIntOrOne") {
        checkAll(JSONGenerators.intLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getIntOrOne("val") shouldBe Conversions.toInt(it)
            }
        }

        checkAll(JSONGenerators.nonIntLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getIntOrOne("val") shouldBe 1
            }
        }
    }

    context("getIntOrMinusOne") {
        checkAll(JSONGenerators.intLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getIntOrMinusOne("val") shouldBe Conversions.toInt(it)
            }
        }

        checkAll(JSONGenerators.nonIntLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getIntOrMinusOne("val") shouldBe -1
            }
        }
    }

    context("getIntOrElse") {
        checkAll(JSONGenerators.intLikes * JSONGenerators.ints) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getIntOrElse("val") { data.second } shouldBe Conversions.toInt(data.first)
            }
        }

        checkAll(JSONGenerators.nonIntLikes * JSONGenerators.ints) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getIntOrElse("val") { data.second } shouldBe data.second
            }
        }
    }

    context("getLongOrNull") {
        checkAll(JSONGenerators.intLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getLongOrNull("val") shouldBe Conversions.toLong(it)
            }
        }

        checkAll(JSONGenerators.nonIntLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getLongOrNull("val").shouldBeNull()
            }
        }
    }

    context("getLongOrDefault") {
        checkAll(JSONGenerators.intLikes * JSONGenerators.longs) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getLongOrDefault("val", it.second) shouldBe Conversions.toLong(it.first)
            }
        }

        checkAll(JSONGenerators.nonIntLikes * JSONGenerators.longs) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getLongOrDefault("val", it.second) shouldBe it.second
            }
        }
    }

    context("getLongOrZero") {
        checkAll(JSONGenerators.intLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getLongOrZero("val") shouldBe Conversions.toLong(it)
            }
        }

        checkAll(JSONGenerators.nonIntLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getLongOrZero("val") shouldBe 0L
            }
        }
    }

    context("getLongOrOne") {
        checkAll(JSONGenerators.intLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getLongOrOne("val") shouldBe Conversions.toLong(it)
            }
        }

        checkAll(JSONGenerators.nonIntLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getLongOrOne("val") shouldBe 1L
            }
        }
    }

    context("getLongOrMinusOne") {
        checkAll(JSONGenerators.intLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getLongOrMinusOne("val") shouldBe Conversions.toLong(it)
            }
        }

        checkAll(JSONGenerators.nonIntLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getLongOrMinusOne("val") shouldBe -1L
            }
        }
    }

    context("getLongOrElse") {
        checkAll(JSONGenerators.intLikes * JSONGenerators.longs) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getLongOrElse("val") { data.second } shouldBe Conversions.toLong(data.first)
            }
        }

        checkAll(JSONGenerators.nonIntLikes * JSONGenerators.longs) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getLongOrElse("val") { data.second } shouldBe data.second
            }
        }
    }

    context("getNumberOrNull") {
        checkAll(JSONGenerators.numberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getNumberOrNull("val")!!.toString() shouldBe it.toString()
            }
        }

        checkAll(JSONGenerators.nonNumberLikes) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getNumberOrNull("val").shouldBeNull()
            }
        }
    }

    context("getNumberOrDefault") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.numbers) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getNumberOrDefault("val", it.second).toString() shouldBe it.first.toString()
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.numbers) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getNumberOrDefault("val", it.second) shouldBe it.second
            }
        }
    }

    context("getNumberOrElse") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.numbers) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getNumberOrElse("val") { data.second }.toString() shouldBe data.first.toString()
            }
        }

        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.numbers) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getNumberOrElse("val") { data.second } shouldBe data.second
            }
        }
    }

    context("getObjectOrNull") {
        checkAll(JSONGenerators.objects) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getObjectOrNull("val") shouldBeSimilarTo it
            }
        }

        checkAll(JSONGenerators.nonObjects) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getObjectOrNull("val").shouldBeNull()
            }
        }
    }

    context("getObjectOrDefault") {
        checkAll(JSONGenerators.objects * JSONGenerators.objects) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getObjectOrDefault("val", ReadOnlyJSONObject(it.second)) shouldBeSimilarTo it.first
            }
        }

        checkAll(JSONGenerators.nonObjects * JSONGenerators.objects) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getObjectOrDefault("val", ReadOnlyJSONObject(it.second)) shouldBeSimilarTo it.second
            }
        }
    }

    context("getObjectOrEmpty") {
        checkAll(JSONGenerators.objects) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getObjectOrEmpty("val") shouldBeSimilarTo it
            }
        }

        checkAll(JSONGenerators.nonObjects) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getObjectOrEmpty("val") shouldBeSimilarTo JSONObject()
            }
        }
    }

    context("getObjectOrElse") {
        checkAll(JSONGenerators.objects * JSONGenerators.objects) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getObjectOrElse("val") {
                    ReadOnlyJSONObject(data.second)
                } shouldBeSimilarTo data.first
            }
        }

        checkAll(JSONGenerators.nonObjects * JSONGenerators.objects) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getObjectOrElse("val") {
                    ReadOnlyJSONObject(data.second)
                } shouldBeSimilarTo data.second
            }
        }
    }

    context("getStringOrNull") {
        checkAll(JSONGenerators.allStrings) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getStringOrNull("val", false) shouldBe it
                getStringOrNull("val", true) shouldBe it
            }
        }

        checkAll(JSONGenerators.nonStrings) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getStringOrNull("val", false).shouldBeNull()
                getStringOrNull("val", true) shouldBe it.toString()
            }
        }
    }

    context("getStringOrDefault") {
        checkAll(JSONGenerators.allStrings * JSONGenerators.allStrings) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getStringOrDefault("val", it.second, false) shouldBe it.first
                getStringOrDefault("val", it.second, true) shouldBe it.first
            }
        }

        checkAll(JSONGenerators.nonStrings * JSONGenerators.allStrings) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getStringOrDefault("val", it.second, false) shouldBe it.second
                getStringOrDefault("val", it.second, true) shouldBe it.first.toString()
            }
        }
    }

    context("getStringOrEmpty") {
        checkAll(JSONGenerators.allStrings) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getStringOrEmpty("val", false) shouldBe it
                getStringOrEmpty("val", true) shouldBe it
            }
        }

        checkAll(JSONGenerators.nonStrings) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getStringOrEmpty("val", false).shouldBeEmpty()
                getStringOrEmpty("val", true) shouldBe it.toString()
            }
        }
    }

    context("getStringOrElse") {
        checkAll(JSONGenerators.allStrings * JSONGenerators.allStrings) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getStringOrElse("val", false) { data.second } shouldBe data.first
                getStringOrElse("val", true) { data.second } shouldBe data.first
                getStringOrElse("val", { data.second }, false) shouldBe data.first
                getStringOrElse("val", { data.second }, true) shouldBe data.first
            }
        }

        checkAll(JSONGenerators.nonStrings * JSONGenerators.allStrings) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getStringOrElse("val", false) { data.second } shouldBe data.second
                getStringOrElse("val", true) { data.second } shouldBe data.first.toString()
                getStringOrElse("val", { data.second }, false) shouldBe data.second
                getStringOrElse("val", { data.second }, true) shouldBe data.first.toString()
            }
        }
    }

})

private fun checkWithReadOnly(orig: JSONObject, func: ReadOnlyJSONObject.() -> Unit) {
    val ro = ReadOnlyJSONObject(orig.deepCopy())

    ro.func()

    ro shouldBeSimilarTo orig
}

