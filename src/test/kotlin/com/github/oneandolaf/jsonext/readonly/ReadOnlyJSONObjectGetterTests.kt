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


class ReadOnlyJSONObjectGetterTests : FunSpec({


    test("getOrNull") {
        checkAll(JSONGenerators.values) {
            val obj = JSONObject().put("val", it)
            val orig = obj.deepCopy()

            val ro = ReadOnlyJSONObject.create(obj)

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

    test("get") {
        checkAll(JSONGenerators.values) {
            val obj = JSONObject().put("val", it)
            val orig = obj.deepCopy()

            val ro = ReadOnlyJSONObject.create(obj)

            ro["val"].orNull() shouldBeSimilarTo it

            if (it is JSONObject) {
                ro["val"].orNull().shouldBeInstanceOf<ReadOnlyJSONObject>()
            } else if (it is JSONArray) {
                ro["val"].orNull().shouldBeInstanceOf<ReadOnlyJSONArray>()
            }

            ro["missing"].orNull().shouldBeNull()

            obj shouldBeSimilarTo orig
        }
    }

    test("getOrElse") {
        checkAll(JSONGenerators.values * JSONGenerators.values) { data ->
            val obj = JSONObject().put("val", data.first)
            val orig = obj.deepCopy()

            val ro = ReadOnlyJSONObject.create(obj)

            ro.getOrElse("val") { data.second } shouldBeSimilarTo data.first
            ro.getOrElse("missing") { data.second } shouldBeSimilarTo data.second

            obj shouldBeSimilarTo orig
        }
    }

    test("getArrayOrNull") {
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

    test("getArrayOrDefault") {
        checkAll(JSONGenerators.arrays * JSONGenerators.arrays) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getArrayOrDefault("val", ReadOnlyJSONArray.create(it.second)) shouldBeSimilarTo it.first
            }
        }

        checkAll(JSONGenerators.nonArrays * JSONGenerators.arrays) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getArrayOrDefault("val", ReadOnlyJSONArray.create(it.second)) shouldBeSimilarTo it.second
            }
        }
    }

    test("getArrayOrEmpty") {
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

    test("getArrayOrElse") {
        checkAll(JSONGenerators.arrays * JSONGenerators.arrays) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getArrayOrElse("val") {
                    ReadOnlyJSONArray.create(data.second)
                } shouldBeSimilarTo data.first
            }
        }

        checkAll(JSONGenerators.nonArrays * JSONGenerators.arrays) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getArrayOrElse("val") {
                    ReadOnlyJSONArray.create(data.second)
                } shouldBeSimilarTo data.second
            }
        }
    }

    test("getBigDecimalOrNull") {
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

    test("getBigDecimalOrDefault") {
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

    test("getBigDecimalOrZero") {
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

    test("getBigDecimalOrOne") {
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

    test("getBigDecimalOrMinusOne") {
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

    test("getBigDecimalOrElse") {
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

    test("getBigIntegerOrNull") {
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

    test("getBigIntegerOrDefault") {
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

    test("getBigIntegerOrZero") {
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

    test("getBigIntegerOrOne") {
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

    test("getBigIntegerOrMinusOne") {
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

    test("getBigIntegerOrElse") {
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

    test("getBooleanOrNull") {
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

    test("getBooleanOrDefault") {
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

    test("getBooleanOrTrue") {
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

    test("getBooleanOrFalse") {
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

    test("getBooleanOrElse") {
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

    test("getDoubleOrNull") {
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

    test("getDoubleOrDefault") {
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

    test("getDoubleOrZero") {
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

    test("getDoubleOrOne") {
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

    test("getDoubleOrMinusOne") {
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

    test("getDoubleOrNaN") {
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

    test("getDoubleOrElse") {
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

    test("getEnumOrNull") {
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

    test("getEnumOrDefault") {
        checkAll(Exhaustive.enum<TestEnum>() * Exhaustive.enum()) {
            val obj = JSONObject().put("val", it.first.name)

            checkWithReadOnly(obj) {
                getEnumOrDefault("val", it.second) shouldBe it.first
            }
        }

        checkAll(JSONGenerators.values * Exhaustive.enum<TestEnum>()) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getEnumOrDefault("val", it.second) shouldBe it.second
            }
        }
    }

    test("getEnumOrElse") {
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

    test("getFloatOrNull") {
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

    test("getFloatOrDefault") {
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

    test("getFloatOrZero") {
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

    test("getFloatOrOne") {
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

    test("getFloatOrMinusOne") {
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

    test("getFloatOrNaN") {
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

    test("getFloatOrElse") {
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

    test("getIntOrNull") {
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

    test("getIntOrDefault") {
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

    test("getIntOrZero") {
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

    test("getIntOrOne") {
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

    test("getIntOrMinusOne") {
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

    test("getIntOrElse") {
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

    test("getLongOrNull") {
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

    test("getLongOrDefault") {
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

    test("getLongOrZero") {
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

    test("getLongOrOne") {
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

    test("getLongOrMinusOne") {
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

    test("getLongOrElse") {
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

    test("getNumberOrNull") {
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

    test("getNumberOrDefault") {
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

    test("getNumberOrElse") {
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

    test("getObjectOrNull") {
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

    test("getObjectOrDefault") {
        checkAll(JSONGenerators.objects * JSONGenerators.objects) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getObjectOrDefault("val", ReadOnlyJSONObject.create(it.second)) shouldBeSimilarTo it.first
            }
        }

        checkAll(JSONGenerators.nonObjects * JSONGenerators.objects) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getObjectOrDefault("val", ReadOnlyJSONObject.create(it.second)) shouldBeSimilarTo it.second
            }
        }
    }

    test("getObjectOrEmpty") {
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

    test("getObjectOrElse") {
        checkAll(JSONGenerators.objects * JSONGenerators.objects) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getObjectOrElse("val") {
                    ReadOnlyJSONObject.create(data.second)
                } shouldBeSimilarTo data.first
            }
        }

        checkAll(JSONGenerators.nonObjects * JSONGenerators.objects) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getObjectOrElse("val") {
                    ReadOnlyJSONObject.create(data.second)
                } shouldBeSimilarTo data.second
            }
        }
    }

    test("getStringOrNull") {
        checkAll(JSONGenerators.allStrings) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getStringOrNull("val") shouldBe it
                getStringOrNull("val", false) shouldBe it
                getStringOrNull("val", true) shouldBe it
            }
        }

        checkAll(JSONGenerators.nonStrings) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getStringOrNull("val").shouldBeNull()
                getStringOrNull("val", false).shouldBeNull()
                getStringOrNull("val", true) shouldBeSimilarStringAs it
            }
        }
    }

    test("getStringOrDefault") {
        checkAll(JSONGenerators.allStrings * JSONGenerators.allStrings) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getStringOrDefault("val", it.second) shouldBe it.first
                getStringOrDefault("val", it.second, false) shouldBe it.first
                getStringOrDefault("val", it.second, true) shouldBe it.first
            }
        }

        checkAll(JSONGenerators.nonStrings * JSONGenerators.allStrings) {
            val obj = JSONObject().put("val", it.first)

            checkWithReadOnly(obj) {
                getStringOrDefault("val", it.second) shouldBe it.second
                getStringOrDefault("val", it.second, false) shouldBe it.second
                getStringOrDefault("val", it.second, true) shouldBeSimilarStringAs it.first
            }
        }
    }

    test("getStringOrEmpty") {
        checkAll(JSONGenerators.allStrings) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getStringOrEmpty("val") shouldBe it
                getStringOrEmpty("val", false) shouldBe it
                getStringOrEmpty("val", true) shouldBe it
            }
        }

        checkAll(JSONGenerators.nonStrings) {
            val obj = JSONObject().put("val", it)

            checkWithReadOnly(obj) {
                getStringOrEmpty("val").shouldBeEmpty()
                getStringOrEmpty("val", false).shouldBeEmpty()
                getStringOrEmpty("val", true) shouldBeSimilarStringAs it
            }
        }
    }

    test("getStringOrElse") {
        checkAll(JSONGenerators.allStrings * JSONGenerators.allStrings) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getStringOrElse("val") { data.second } shouldBe data.first
                getStringOrElse("val", false) { data.second } shouldBe data.first
                getStringOrElse("val", true) { data.second } shouldBe data.first
                getStringOrElse("val", { data.second }, false) shouldBe data.first
                getStringOrElse("val", { data.second }, true) shouldBe data.first
            }
        }

        checkAll(JSONGenerators.nonStrings * JSONGenerators.allStrings) { data ->
            val obj = JSONObject().put("val", data.first)

            checkWithReadOnly(obj) {
                getStringOrElse("val") { data.second } shouldBe data.second
                getStringOrElse("val", false) { data.second } shouldBe data.second
                getStringOrElse("val", true) { data.second } shouldBeSimilarStringAs data.first
                getStringOrElse("val", { data.second }, false) shouldBe data.second
                getStringOrElse("val", { data.second }, true) shouldBeSimilarStringAs data.first
            }
        }
    }

})

private fun checkWithReadOnly(orig: JSONObject, func: ReadOnlyJSONObject.() -> Unit) {
    val ro = ReadOnlyJSONObject.snapshot(orig)

    ro.func()

    ro shouldBeSimilarTo orig
}

