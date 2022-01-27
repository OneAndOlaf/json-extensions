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

import com.github.oneandolaf.jsonext.extensions.asReadOnly
import com.github.oneandolaf.jsonext.impl.Conversions
import com.github.oneandolaf.jsonext.testutils.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
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

class ReadOnlyJSONValTests : FunSpec({

    test("orNull") {
        checkAll(JSONGenerators.values) {
            val value = ReadOnlyJSONVal(it)

            value.orNull() shouldBeSimilarTo it

            if (it is JSONObject) {
                value.orNull().shouldBeInstanceOf<ReadOnlyJSONObject>()
            } else if (it is JSONArray) {
                value.orNull().shouldBeInstanceOf<ReadOnlyJSONArray>()
            }
        }

        ReadOnlyJSONVal(null).orNull().shouldBeNull()
    }

    test("orDefault") {
        checkAll(JSONGenerators.values * JSONGenerators.values) { data ->
            val value = ReadOnlyJSONVal(data.first)

            value.orDefault(data.second) shouldBeSimilarTo data.first

            ReadOnlyJSONVal(null).orDefault(data.second) shouldBeSimilarTo data.second
        }
    }

    test("orElse") {
        checkAll(JSONGenerators.values * JSONGenerators.values) { data ->
            val value = ReadOnlyJSONVal(data.first)

            value.orElse { data.second } shouldBeSimilarTo data.first

            ReadOnlyJSONVal(null).orElse { data.second } shouldBeSimilarTo data.second
        }
    }

    test("isArray") {
        checkAll(JSONGenerators.arrays) {
            ReadOnlyJSONVal(it).isArray().shouldBeTrue()
        }
        checkAll(JSONGenerators.nonArrays) {
            ReadOnlyJSONVal(it).isArray().shouldBeFalse()
        }
        ReadOnlyJSONVal(null).isArray().shouldBeFalse()
    }

    test("asArrayOrNull") {
        checkAll(JSONGenerators.arrays) {
            ReadOnlyJSONVal(it).asArrayOrNull() shouldBeSimilarTo it
        }
        checkAll(JSONGenerators.nonArrays) {
            ReadOnlyJSONVal(it).asArrayOrNull().shouldBeNull()
        }
        ReadOnlyJSONVal(null).asArrayOrNull().shouldBeNull()
    }

    test("asArrayOrDefault") {
        checkAll(JSONGenerators.arrays * JSONGenerators.arrays) { data ->
            ReadOnlyJSONVal(data.first).asArrayOrDefault(data.second.asReadOnly()) shouldBeSimilarTo data.first
        }
        checkAll(JSONGenerators.nonArrays * JSONGenerators.arrays) { data ->
            ReadOnlyJSONVal(data.first).asArrayOrDefault(data.second.asReadOnly()) shouldBeSimilarTo data.second
        }
        checkAll(JSONGenerators.arrays) {
            ReadOnlyJSONVal(null).asArrayOrDefault(it.asReadOnly()) shouldBeSimilarTo it
        }
    }

    test("asArrayOrEmpty") {
        checkAll(JSONGenerators.arrays) {
            ReadOnlyJSONVal(it).asArrayOrEmpty() shouldBeSimilarTo it
        }
        checkAll(JSONGenerators.nonArrays) {
            ReadOnlyJSONVal(it).asArrayOrEmpty() shouldBeSimilarTo ReadOnlyJSONArray.EMPTY
        }
        ReadOnlyJSONVal(null).asArrayOrEmpty() shouldBeSimilarTo ReadOnlyJSONArray.EMPTY
    }

    test("asArrayOrElse") {
        checkAll(JSONGenerators.arrays * JSONGenerators.arrays) { data ->
            ReadOnlyJSONVal(data.first).asArrayOrElse { data.second.asReadOnly() } shouldBeSimilarTo data.first
        }
        checkAll(JSONGenerators.nonArrays * JSONGenerators.arrays) { data ->
            ReadOnlyJSONVal(data.first).asArrayOrElse { data.second.asReadOnly() } shouldBeSimilarTo data.second
        }
        checkAll(JSONGenerators.arrays) {
            ReadOnlyJSONVal(null).asArrayOrElse { it.asReadOnly() } shouldBeSimilarTo it
        }
    }

    test("isBigDecimal") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).isBigDecimal().shouldBeTrue()
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).isBigDecimal().shouldBeFalse()
        }
        ReadOnlyJSONVal(null).isBigDecimal().shouldBeFalse()
    }

    test("asBigDecimalOrNull") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).asBigDecimalOrNull() shouldBe Conversions.toBigDecimal(it)!!
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).asBigDecimalOrNull().shouldBeNull()
        }
        ReadOnlyJSONVal(null).asBigDecimalOrNull().shouldBeNull()
    }

    test("asBigDecimalOrDefault") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.bigDecimals) { data ->
            ReadOnlyJSONVal(data.first).asBigDecimalOrDefault(data.second) shouldBe Conversions.toBigDecimal(
                data.first
            )!!
        }
        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.bigDecimals) { data ->
            ReadOnlyJSONVal(data.first).asBigDecimalOrDefault(data.second) shouldBe data.second
        }
        checkAll(JSONGenerators.bigDecimals) {
            ReadOnlyJSONVal(null).asBigDecimalOrDefault(it) shouldBe it
        }
    }

    test("asBigDecimalOrZero") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).asBigDecimalOrZero() shouldBe Conversions.toBigDecimal(it)!!
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).asBigDecimalOrZero() shouldBe BigDecimal.ZERO
        }
        ReadOnlyJSONVal(null).asBigDecimalOrZero() shouldBe BigDecimal.ZERO
    }

    test("asBigDecimalOrOne") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).asBigDecimalOrOne() shouldBe Conversions.toBigDecimal(it)!!
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).asBigDecimalOrOne() shouldBe BigDecimal.ONE
        }
        ReadOnlyJSONVal(null).asBigDecimalOrOne() shouldBe BigDecimal.ONE
    }

    test("asBigDecimalOrMinusOne") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).asBigDecimalOrMinusOne() shouldBe Conversions.toBigDecimal(it)!!
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).asBigDecimalOrMinusOne() shouldBe BigDecimal.valueOf(-1L)
        }
        ReadOnlyJSONVal(null).asBigDecimalOrMinusOne() shouldBe BigDecimal.valueOf(-1L)
    }

    test("asBigDecimalOrElse") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.bigDecimals) { data ->
            ReadOnlyJSONVal(data.first).asBigDecimalOrElse { data.second } shouldBe Conversions.toBigDecimal(
                data.first
            )!!
        }
        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.bigDecimals) { data ->
            ReadOnlyJSONVal(data.first).asBigDecimalOrElse { data.second } shouldBe data.second
        }
        checkAll(JSONGenerators.bigDecimals) {
            ReadOnlyJSONVal(null).asBigDecimalOrElse { it } shouldBe it
        }
    }

    test("isBigInteger") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).isBigInteger().shouldBeTrue()
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).isBigInteger().shouldBeFalse()
        }
        ReadOnlyJSONVal(null).isBigInteger().shouldBeFalse()
    }

    test("asBigIntegerOrNull") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).asBigIntegerOrNull() shouldBe Conversions.toBigInteger(it)!!
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).asBigIntegerOrNull().shouldBeNull()
        }
        ReadOnlyJSONVal(null).asBigIntegerOrNull().shouldBeNull()
    }

    test("asBigIntegerOrDefault") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.bigIntegers) { data ->
            ReadOnlyJSONVal(data.first).asBigIntegerOrDefault(data.second) shouldBe Conversions.toBigInteger(
                data.first
            )!!
        }
        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.bigIntegers) { data ->
            ReadOnlyJSONVal(data.first).asBigIntegerOrDefault(data.second) shouldBe data.second
        }
        checkAll(JSONGenerators.bigIntegers) {
            ReadOnlyJSONVal(null).asBigIntegerOrDefault(it) shouldBe it
        }
    }

    test("asBigIntegerOrZero") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).asBigIntegerOrZero() shouldBe Conversions.toBigInteger(it)!!
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).asBigIntegerOrZero() shouldBe BigInteger.ZERO
        }
        ReadOnlyJSONVal(null).asBigIntegerOrZero() shouldBe BigInteger.ZERO
    }

    test("asBigIntegerOrOne") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).asBigIntegerOrOne() shouldBe Conversions.toBigInteger(it)!!
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).asBigIntegerOrOne() shouldBe BigInteger.ONE
        }
        ReadOnlyJSONVal(null).asBigIntegerOrOne() shouldBe BigInteger.ONE
    }

    test("asBigIntegerOrMinusOne") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).asBigIntegerOrMinusOne() shouldBe Conversions.toBigInteger(it)!!
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).asBigIntegerOrMinusOne() shouldBe BigInteger.valueOf(-1L)
        }
        ReadOnlyJSONVal(null).asBigIntegerOrMinusOne() shouldBe BigInteger.valueOf(-1L)
    }

    test("asBigIntegerOrElse") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.bigIntegers) { data ->
            ReadOnlyJSONVal(data.first).asBigIntegerOrElse { data.second } shouldBe Conversions.toBigInteger(
                data.first
            )!!
        }
        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.bigIntegers) { data ->
            ReadOnlyJSONVal(data.first).asBigIntegerOrElse { data.second } shouldBe data.second
        }
        checkAll(JSONGenerators.bigIntegers) {
            ReadOnlyJSONVal(null).asBigIntegerOrElse { it } shouldBe it
        }
    }

    test("isBoolean") {
        checkAll(JSONGenerators.booleanLikes) {
            ReadOnlyJSONVal(it).isBoolean().shouldBeTrue()
        }
        checkAll(JSONGenerators.nonBooleanLikes) {
            ReadOnlyJSONVal(it).isBoolean().shouldBeFalse()
        }
        ReadOnlyJSONVal(null).isBoolean().shouldBeFalse()
    }

    test("asBooleanOrNull") {
        checkAll(JSONGenerators.booleanLikes) {
            ReadOnlyJSONVal(it).asBooleanOrNull() shouldBe Conversions.toBoolean(it)!!
        }
        checkAll(JSONGenerators.nonBooleanLikes) {
            ReadOnlyJSONVal(it).asBooleanOrNull().shouldBeNull()
        }
        ReadOnlyJSONVal(null).asBooleanOrNull().shouldBeNull()
    }

    test("asBooleanOrDefault") {
        checkAll(JSONGenerators.booleanLikes * JSONGenerators.bools) { data ->
            ReadOnlyJSONVal(data.first).asBooleanOrDefault(data.second) shouldBe Conversions.toBoolean(data.first)!!
        }
        checkAll(JSONGenerators.nonBooleanLikes * JSONGenerators.bools) { data ->
            ReadOnlyJSONVal(data.first).asBooleanOrDefault(data.second) shouldBe data.second
        }
        checkAll(JSONGenerators.bools) {
            ReadOnlyJSONVal(null).asBooleanOrDefault(it) shouldBe it
        }
    }

    test("asBooleanOrTrue") {
        checkAll(JSONGenerators.booleanLikes) {
            ReadOnlyJSONVal(it).asBooleanOrTrue() shouldBe Conversions.toBoolean(it)!!
        }
        checkAll(JSONGenerators.nonBooleanLikes) {
            ReadOnlyJSONVal(it).asBooleanOrTrue().shouldBeTrue()
        }
        ReadOnlyJSONVal(null).asBooleanOrTrue().shouldBeTrue()
    }

    test("asBooleanOrFalse") {
        checkAll(JSONGenerators.booleanLikes) {
            ReadOnlyJSONVal(it).asBooleanOrFalse() shouldBe Conversions.toBoolean(it)!!
        }
        checkAll(JSONGenerators.nonBooleanLikes) {
            ReadOnlyJSONVal(it).asBooleanOrFalse().shouldBeFalse()
        }
        ReadOnlyJSONVal(null).asBooleanOrFalse().shouldBeFalse()
    }

    test("asBooleanOrElse") {
        checkAll(JSONGenerators.booleanLikes * JSONGenerators.bools) { data ->
            ReadOnlyJSONVal(data.first).asBooleanOrElse { data.second } shouldBe Conversions.toBoolean(data.first)!!
        }
        checkAll(JSONGenerators.nonBooleanLikes * JSONGenerators.bools) { data ->
            ReadOnlyJSONVal(data.first).asBooleanOrElse { data.second } shouldBe data.second
        }
        checkAll(JSONGenerators.bools) {
            ReadOnlyJSONVal(null).asBooleanOrElse { it } shouldBe it
        }
    }

    test("isDouble") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).isDouble().shouldBeTrue()
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).isDouble().shouldBeFalse()
        }
        ReadOnlyJSONVal(null).isDouble().shouldBeFalse()
    }

    test("asDoubleOrNull") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).asDoubleOrNull() shouldBe Conversions.toDouble(it)!!
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).asDoubleOrNull().shouldBeNull()
        }
        ReadOnlyJSONVal(null).asDoubleOrNull().shouldBeNull()
    }

    test("asDoubleOrDefault") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.doubles) { data ->
            ReadOnlyJSONVal(data.first).asDoubleOrDefault(data.second) shouldBe Conversions.toDouble(data.first)!!
        }
        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.doubles) { data ->
            ReadOnlyJSONVal(data.first).asDoubleOrDefault(data.second) shouldBe data.second
        }
        checkAll(JSONGenerators.doubles) {
            ReadOnlyJSONVal(null).asDoubleOrDefault(it) shouldBe it
        }
    }

    test("asDoubleOrZero") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).asDoubleOrZero() shouldBe Conversions.toDouble(it)!!
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).asDoubleOrZero() shouldBe 0.0
        }
        ReadOnlyJSONVal(null).asDoubleOrZero() shouldBe 0.0
    }

    test("asDoubleOrOne") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).asDoubleOrOne() shouldBe Conversions.toDouble(it)!!
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).asDoubleOrOne() shouldBe 1.0
        }
        ReadOnlyJSONVal(null).asDoubleOrOne() shouldBe 1.0
    }

    test("asDoubleOrMinusOne") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).asDoubleOrMinusOne() shouldBe Conversions.toDouble(it)!!
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).asDoubleOrMinusOne() shouldBe -1.0
        }
        ReadOnlyJSONVal(null).asDoubleOrMinusOne() shouldBe -1.0
    }

    test("asDoubleOrNaN") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).asDoubleOrNaN() shouldBe Conversions.toDouble(it)!!
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).asDoubleOrNaN() shouldBe Double.NaN
        }
        ReadOnlyJSONVal(null).asDoubleOrNaN() shouldBe Double.NaN
    }

    test("asDoubleOrElse") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.doubles) { data ->
            ReadOnlyJSONVal(data.first).asDoubleOrElse { data.second } shouldBe Conversions.toDouble(data.first)!!
        }
        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.doubles) { data ->
            ReadOnlyJSONVal(data.first).asDoubleOrElse { data.second } shouldBe data.second
        }
        checkAll(JSONGenerators.doubles) {
            ReadOnlyJSONVal(null).asDoubleOrElse { it } shouldBe it
        }
    }

    test("isEnum") {
        checkAll(Exhaustive.enum<TestEnum>()) {
            ReadOnlyJSONVal(it).isEnum<TestEnum>().shouldBeTrue()
            ReadOnlyJSONVal(it).isEnum(TestEnum::class.java).shouldBeTrue()
        }
        checkAll(JSONGenerators.values) {
            ReadOnlyJSONVal(it).isEnum<TestEnum>().shouldBeFalse()
            ReadOnlyJSONVal(it).isEnum(TestEnum::class.java).shouldBeFalse()
        }
        ReadOnlyJSONVal(null).isEnum<TestEnum>().shouldBeFalse()
        ReadOnlyJSONVal(null).isEnum(TestEnum::class.java).shouldBeFalse()
    }

    test("asEnumOrNull") {
        checkAll(Exhaustive.enum<TestEnum>()) {
            ReadOnlyJSONVal(it).asEnumOrNull<TestEnum>() shouldBe it
            ReadOnlyJSONVal(it).asEnumOrNull(TestEnum::class.java) shouldBe it
        }
        checkAll(JSONGenerators.values) {
            ReadOnlyJSONVal(it).asEnumOrNull<TestEnum>().shouldBeNull()
            ReadOnlyJSONVal(it).asEnumOrNull(TestEnum::class.java).shouldBeNull()
        }
        ReadOnlyJSONVal(null).asEnumOrNull<TestEnum>().shouldBeNull()
        ReadOnlyJSONVal(null).asEnumOrNull(TestEnum::class.java).shouldBeNull()
    }

    test("asEnumOrDefault") {
        checkAll(Exhaustive.enum<TestEnum>() cross Exhaustive.enum<TestEnum>()) {
            ReadOnlyJSONVal(it.first).asEnumOrDefault(it.second) shouldBe it.first
        }
        checkAll(JSONGenerators.values cross Exhaustive.enum<TestEnum>()) {
            ReadOnlyJSONVal(it.first).asEnumOrDefault(it.second) shouldBe it.second
        }
        checkAll(Exhaustive.enum<TestEnum>()) {
            ReadOnlyJSONVal(null).asEnumOrDefault(it) shouldBe it
        }
    }

    test("asEnumOrElse") {
        checkAll(Exhaustive.enum<TestEnum>() cross Exhaustive.enum<TestEnum>()) {
            ReadOnlyJSONVal(it.first).asEnumOrElse { it.second } shouldBe it.first
            ReadOnlyJSONVal(it.first).asEnumOrElse(TestEnum::class.java) { it.second } shouldBe it.first
        }
        checkAll(JSONGenerators.values cross Exhaustive.enum<TestEnum>()) {
            ReadOnlyJSONVal(it.first).asEnumOrElse { it.second } shouldBe it.second
            ReadOnlyJSONVal(it.first).asEnumOrElse(TestEnum::class.java) { it.second } shouldBe it.second
        }
        checkAll(Exhaustive.enum<TestEnum>()) {
            ReadOnlyJSONVal(null).asEnumOrElse { it } shouldBe it
            ReadOnlyJSONVal(null).asEnumOrElse(TestEnum::class.java) { it } shouldBe it
        }
    }

    test("isFloat") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).isFloat().shouldBeTrue()
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).isFloat().shouldBeFalse()
        }
        ReadOnlyJSONVal(null).isFloat().shouldBeFalse()
    }

    test("asFloatOrNull") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).asFloatOrNull() shouldBe Conversions.toFloat(it)!!
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).asFloatOrNull().shouldBeNull()
        }
        ReadOnlyJSONVal(null).asFloatOrNull().shouldBeNull()
    }

    test("asFloatOrDefault") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.floats) { data ->
            ReadOnlyJSONVal(data.first).asFloatOrDefault(data.second) shouldBe Conversions.toFloat(data.first)!!
        }
        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.floats) { data ->
            ReadOnlyJSONVal(data.first).asFloatOrDefault(data.second) shouldBe data.second
        }
        checkAll(JSONGenerators.floats) {
            ReadOnlyJSONVal(null).asFloatOrDefault(it) shouldBe it
        }
    }

    test("asFloatOrZero") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).asFloatOrZero() shouldBe Conversions.toFloat(it)!!
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).asFloatOrZero() shouldBe 0.0f
        }
        ReadOnlyJSONVal(null).asFloatOrZero() shouldBe 0.0f
    }

    test("asFloatOrOne") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).asFloatOrOne() shouldBe Conversions.toFloat(it)!!
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).asFloatOrOne() shouldBe 1.0f
        }
        ReadOnlyJSONVal(null).asFloatOrOne() shouldBe 1.0f
    }

    test("asFloatOrMinusOne") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).asFloatOrMinusOne() shouldBe Conversions.toFloat(it)!!
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).asFloatOrMinusOne() shouldBe -1.0f
        }
        ReadOnlyJSONVal(null).asFloatOrMinusOne() shouldBe -1.0f
    }

    test("asFloatOrNaN") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).asFloatOrNaN() shouldBe Conversions.toFloat(it)!!
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).asFloatOrNaN() shouldBe Float.NaN
        }
        ReadOnlyJSONVal(null).asFloatOrNaN() shouldBe Float.NaN
    }

    test("asFloatOrElse") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.floats) { data ->
            ReadOnlyJSONVal(data.first).asFloatOrElse { data.second } shouldBe Conversions.toFloat(data.first)!!
        }
        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.floats) { data ->
            ReadOnlyJSONVal(data.first).asFloatOrElse { data.second } shouldBe data.second
        }
        checkAll(JSONGenerators.floats) {
            ReadOnlyJSONVal(null).asFloatOrElse { it } shouldBe it
        }
    }

    test("isInt") {
        checkAll(JSONGenerators.intLikes) {
            ReadOnlyJSONVal(it).isInt().shouldBeTrue()
        }
        checkAll(JSONGenerators.nonIntLikes) {
            ReadOnlyJSONVal(it).isInt().shouldBeFalse()
        }
        ReadOnlyJSONVal(null).isInt().shouldBeFalse()
    }

    test("asIntOrNull") {
        checkAll(JSONGenerators.intLikes) {
            ReadOnlyJSONVal(it).asIntOrNull() shouldBe Conversions.toInt(it)!!
        }
        checkAll(JSONGenerators.nonIntLikes) {
            ReadOnlyJSONVal(it).asIntOrNull().shouldBeNull()
        }
        ReadOnlyJSONVal(null).asIntOrNull().shouldBeNull()
    }

    test("asIntOrDefault") {
        checkAll(JSONGenerators.intLikes * JSONGenerators.ints) { data ->
            ReadOnlyJSONVal(data.first).asIntOrDefault(data.second) shouldBe Conversions.toInt(data.first)!!
        }
        checkAll(JSONGenerators.nonIntLikes * JSONGenerators.ints) { data ->
            ReadOnlyJSONVal(data.first).asIntOrDefault(data.second) shouldBe data.second
        }
        checkAll(JSONGenerators.ints) {
            ReadOnlyJSONVal(null).asIntOrDefault(it) shouldBe it
        }
    }

    test("asIntOrZero") {
        checkAll(JSONGenerators.intLikes) {
            ReadOnlyJSONVal(it).asIntOrZero() shouldBe Conversions.toInt(it)!!
        }
        checkAll(JSONGenerators.nonIntLikes) {
            ReadOnlyJSONVal(it).asIntOrZero() shouldBe 0
        }
        ReadOnlyJSONVal(null).asIntOrZero() shouldBe 0
    }

    test("asIntOrOne") {
        checkAll(JSONGenerators.intLikes) {
            ReadOnlyJSONVal(it).asIntOrOne() shouldBe Conversions.toInt(it)!!
        }
        checkAll(JSONGenerators.nonIntLikes) {
            ReadOnlyJSONVal(it).asIntOrOne() shouldBe 1
        }
        ReadOnlyJSONVal(null).asIntOrOne() shouldBe 1
    }

    test("asIntOrMinusOne") {
        checkAll(JSONGenerators.intLikes) {
            ReadOnlyJSONVal(it).asIntOrMinusOne() shouldBe Conversions.toInt(it)!!
        }
        checkAll(JSONGenerators.nonIntLikes) {
            ReadOnlyJSONVal(it).asIntOrMinusOne() shouldBe -1
        }
        ReadOnlyJSONVal(null).asIntOrMinusOne() shouldBe -1
    }

    test("asIntOrElse") {
        checkAll(JSONGenerators.intLikes * JSONGenerators.ints) { data ->
            ReadOnlyJSONVal(data.first).asIntOrElse { data.second } shouldBe Conversions.toInt(data.first)!!
        }
        checkAll(JSONGenerators.nonIntLikes * JSONGenerators.ints) { data ->
            ReadOnlyJSONVal(data.first).asIntOrElse { data.second } shouldBe data.second
        }
        checkAll(JSONGenerators.ints) {
            ReadOnlyJSONVal(null).asIntOrElse { it } shouldBe it
        }
    }

    test("isLong") {
        checkAll(JSONGenerators.intLikes) {
            ReadOnlyJSONVal(it).isLong().shouldBeTrue()
        }
        checkAll(JSONGenerators.nonIntLikes) {
            ReadOnlyJSONVal(it).isLong().shouldBeFalse()
        }
        ReadOnlyJSONVal(null).isLong().shouldBeFalse()
    }

    test("asLongOrNull") {
        checkAll(JSONGenerators.intLikes) {
            ReadOnlyJSONVal(it).asLongOrNull() shouldBe Conversions.toLong(it)!!
        }
        checkAll(JSONGenerators.nonIntLikes) {
            ReadOnlyJSONVal(it).asLongOrNull().shouldBeNull()
        }
        ReadOnlyJSONVal(null).asLongOrNull().shouldBeNull()
    }

    test("asLongOrDefault") {
        checkAll(JSONGenerators.intLikes * JSONGenerators.longs) { data ->
            ReadOnlyJSONVal(data.first).asLongOrDefault(data.second) shouldBe Conversions.toLong(data.first)!!
        }
        checkAll(JSONGenerators.nonIntLikes * JSONGenerators.longs) { data ->
            ReadOnlyJSONVal(data.first).asLongOrDefault(data.second) shouldBe data.second
        }
        checkAll(JSONGenerators.longs) {
            ReadOnlyJSONVal(null).asLongOrDefault(it) shouldBe it
        }
    }

    test("asLongOrZero") {
        checkAll(JSONGenerators.intLikes) {
            ReadOnlyJSONVal(it).asLongOrZero() shouldBe Conversions.toLong(it)!!
        }
        checkAll(JSONGenerators.nonIntLikes) {
            ReadOnlyJSONVal(it).asLongOrZero() shouldBe 0L
        }
        ReadOnlyJSONVal(null).asLongOrZero() shouldBe 0L
    }

    test("asLongOrOne") {
        checkAll(JSONGenerators.intLikes) {
            ReadOnlyJSONVal(it).asLongOrOne() shouldBe Conversions.toLong(it)!!
        }
        checkAll(JSONGenerators.nonIntLikes) {
            ReadOnlyJSONVal(it).asLongOrOne() shouldBe 1L
        }
        ReadOnlyJSONVal(null).asLongOrOne() shouldBe 1L
    }

    test("asLongOrMinusOne") {
        checkAll(JSONGenerators.intLikes) {
            ReadOnlyJSONVal(it).asLongOrMinusOne() shouldBe Conversions.toLong(it)!!
        }
        checkAll(JSONGenerators.nonIntLikes) {
            ReadOnlyJSONVal(it).asLongOrMinusOne() shouldBe -1L
        }
        ReadOnlyJSONVal(null).asLongOrMinusOne() shouldBe -1L
    }

    test("asLongOrElse") {
        checkAll(JSONGenerators.intLikes * JSONGenerators.longs) { data ->
            ReadOnlyJSONVal(data.first).asLongOrElse { data.second } shouldBe Conversions.toLong(data.first)!!
        }
        checkAll(JSONGenerators.nonIntLikes * JSONGenerators.longs) { data ->
            ReadOnlyJSONVal(data.first).asLongOrElse { data.second } shouldBe data.second
        }
        checkAll(JSONGenerators.longs) {
            ReadOnlyJSONVal(null).asLongOrElse { it } shouldBe it
        }
    }

    test("isNumber") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).isNumber().shouldBeTrue()
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).isNumber().shouldBeFalse()
        }
        ReadOnlyJSONVal(null).isNumber().shouldBeFalse()
    }

    test("asNumberOrNull") {
        checkAll(JSONGenerators.numberLikes) {
            ReadOnlyJSONVal(it).asNumberOrNull() shouldBe Conversions.toNumber(it)!!
        }
        checkAll(JSONGenerators.nonNumberLikes) {
            ReadOnlyJSONVal(it).asNumberOrNull().shouldBeNull()
        }
        ReadOnlyJSONVal(null).asNumberOrNull().shouldBeNull()
    }

    test("asNumberOrDefault") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.floats) { data ->
            ReadOnlyJSONVal(data.first).asNumberOrDefault(data.second) shouldBe Conversions.toNumber(data.first)!!
        }
        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.floats) { data ->
            ReadOnlyJSONVal(data.first).asNumberOrDefault(data.second) shouldBe data.second
        }
        checkAll(JSONGenerators.floats) {
            ReadOnlyJSONVal(null).asNumberOrDefault(it) shouldBe it
        }
    }

    test("asNumberOrElse") {
        checkAll(JSONGenerators.numberLikes * JSONGenerators.floats) { data ->
            ReadOnlyJSONVal(data.first).asNumberOrElse { data.second } shouldBe Conversions.toNumber(data.first)!!
        }
        checkAll(JSONGenerators.nonNumberLikes * JSONGenerators.floats) { data ->
            ReadOnlyJSONVal(data.first).asNumberOrElse { data.second } shouldBe data.second
        }
        checkAll(JSONGenerators.floats) {
            ReadOnlyJSONVal(null).asNumberOrElse { it } shouldBe it
        }
    }

    test("isObject") {
        checkAll(JSONGenerators.objects) {
            ReadOnlyJSONVal(it).isObject().shouldBeTrue()
        }
        checkAll(JSONGenerators.nonObjects) {
            ReadOnlyJSONVal(it).isObject().shouldBeFalse()
        }
        ReadOnlyJSONVal(null).isObject().shouldBeFalse()
    }

    test("asObjectOrNull") {
        checkAll(JSONGenerators.objects) {
            ReadOnlyJSONVal(it).asObjectOrNull() shouldBeSimilarTo it
        }
        checkAll(JSONGenerators.nonObjects) {
            ReadOnlyJSONVal(it).asObjectOrNull().shouldBeNull()
        }
        ReadOnlyJSONVal(null).asObjectOrNull().shouldBeNull()
    }

    test("asObjectOrDefault") {
        checkAll(JSONGenerators.objects * JSONGenerators.objects) { data ->
            ReadOnlyJSONVal(data.first).asObjectOrDefault(data.second.asReadOnly()) shouldBeSimilarTo data.first
        }
        checkAll(JSONGenerators.nonObjects * JSONGenerators.objects) { data ->
            ReadOnlyJSONVal(data.first).asObjectOrDefault(data.second.asReadOnly()) shouldBeSimilarTo data.second
        }
        checkAll(JSONGenerators.objects) {
            ReadOnlyJSONVal(null).asObjectOrDefault(it.asReadOnly()) shouldBeSimilarTo it
        }
    }

    test("asObjectOrEmpty") {
        checkAll(JSONGenerators.objects) {
            ReadOnlyJSONVal(it).asObjectOrEmpty() shouldBeSimilarTo it
        }
        checkAll(JSONGenerators.nonObjects) {
            ReadOnlyJSONVal(it).asObjectOrEmpty() shouldBeSimilarTo ReadOnlyJSONObject.EMPTY
        }
        ReadOnlyJSONVal(null).asObjectOrEmpty() shouldBeSimilarTo ReadOnlyJSONObject.EMPTY
    }

    test("asObjectOrElse") {
        checkAll(JSONGenerators.objects * JSONGenerators.objects) { data ->
            ReadOnlyJSONVal(data.first).asObjectOrElse { data.second.asReadOnly() } shouldBeSimilarTo data.first
        }
        checkAll(JSONGenerators.nonObjects * JSONGenerators.objects) { data ->
            ReadOnlyJSONVal(data.first).asObjectOrElse { data.second.asReadOnly() } shouldBeSimilarTo data.second
        }
        checkAll(JSONGenerators.objects) {
            ReadOnlyJSONVal(null).asObjectOrElse { it.asReadOnly() } shouldBeSimilarTo it
        }
    }

    test("isString") {
        checkAll(JSONGenerators.allStrings) {
            ReadOnlyJSONVal(it).isString().shouldBeTrue()
            ReadOnlyJSONVal(it).isString(false).shouldBeTrue()
            ReadOnlyJSONVal(it).isString(true).shouldBeTrue()
        }

        checkAll(JSONGenerators.nonStrings) {
            ReadOnlyJSONVal(it).isString().shouldBeFalse()
            ReadOnlyJSONVal(it).isString(false).shouldBeFalse()
            ReadOnlyJSONVal(it).isString(true).shouldBeTrue()
        }
        ReadOnlyJSONVal(null).isString().shouldBeFalse()
        ReadOnlyJSONVal(null).isString(false).shouldBeFalse()
        ReadOnlyJSONVal(null).isString(true).shouldBeFalse()
    }

    test("asStringOrNull") {
        checkAll(JSONGenerators.allStrings) {
            ReadOnlyJSONVal(it).asStringOrNull() shouldBe it
            ReadOnlyJSONVal(it).asStringOrNull(false) shouldBe it
            ReadOnlyJSONVal(it).asStringOrNull(true) shouldBe it
        }

        checkAll(JSONGenerators.nonStrings) {
            ReadOnlyJSONVal(it).asStringOrNull().shouldBeNull()
            ReadOnlyJSONVal(it).asStringOrNull(false).shouldBeNull()
            ReadOnlyJSONVal(it).asStringOrNull(true) shouldBeSimilarStringAs it
        }
        ReadOnlyJSONVal(null).asStringOrNull().shouldBeNull()
        ReadOnlyJSONVal(null).asStringOrNull(false).shouldBeNull()
        ReadOnlyJSONVal(null).asStringOrNull(true).shouldBeNull()
    }

    test("asStringOrDefault") {
        checkAll(JSONGenerators.allStrings cross JSONGenerators.basicStrings) {
            ReadOnlyJSONVal(it.first).asStringOrDefault(it.second) shouldBe it.first
            ReadOnlyJSONVal(it.first).asStringOrDefault(it.second, false) shouldBe it.first
            ReadOnlyJSONVal(it.first).asStringOrDefault(it.second, true) shouldBe it.first
        }

        checkAll(JSONGenerators.nonStrings cross JSONGenerators.basicStrings) {
            ReadOnlyJSONVal(it.first).asStringOrDefault(it.second) shouldBe it.second
            ReadOnlyJSONVal(it.first).asStringOrDefault(it.second, false) shouldBe it.second
            ReadOnlyJSONVal(it.first).asStringOrDefault(it.second, true) shouldBe Conversions.toString(it.first, true)
        }

        checkAll(JSONGenerators.basicStrings) {
            ReadOnlyJSONVal(null).asStringOrDefault(it) shouldBe it
            ReadOnlyJSONVal(null).asStringOrDefault(it, false) shouldBe it
            ReadOnlyJSONVal(null).asStringOrDefault(it, true) shouldBe it
        }
    }

    test("asStringOrEmpty") {
        checkAll(JSONGenerators.allStrings) {
            ReadOnlyJSONVal(it).asStringOrEmpty() shouldBe it
            ReadOnlyJSONVal(it).asStringOrEmpty(false) shouldBe it
            ReadOnlyJSONVal(it).asStringOrEmpty(true) shouldBe it
        }

        checkAll(JSONGenerators.nonStrings) {
            ReadOnlyJSONVal(it).asStringOrEmpty().shouldBeEmpty()
            ReadOnlyJSONVal(it).asStringOrEmpty(false).shouldBeEmpty()
            ReadOnlyJSONVal(it).asStringOrEmpty(true) shouldBeSimilarStringAs it
        }
        ReadOnlyJSONVal(null).asStringOrEmpty().shouldBeEmpty()
        ReadOnlyJSONVal(null).asStringOrEmpty().shouldBeEmpty()
        ReadOnlyJSONVal(null).asStringOrEmpty().shouldBeEmpty()
    }

    test("asStringOrElse") {
        checkAll(JSONGenerators.allStrings cross JSONGenerators.basicStrings) {
            ReadOnlyJSONVal(it.first).asStringOrElse { it.second } shouldBe it.first
            ReadOnlyJSONVal(it.first).asStringOrElse({ it.second }, false) shouldBe it.first
            ReadOnlyJSONVal(it.first).asStringOrElse(false) { it.second } shouldBe it.first
            ReadOnlyJSONVal(it.first).asStringOrElse({ it.second }, true) shouldBe Conversions.toString(it.first, true)
            ReadOnlyJSONVal(it.first).asStringOrElse(true) { it.second } shouldBe Conversions.toString(it.first, true)
        }

        checkAll(JSONGenerators.nonStrings cross JSONGenerators.basicStrings) {
            ReadOnlyJSONVal(it.first).asStringOrElse { it.second } shouldBe it.second
            ReadOnlyJSONVal(it.first).asStringOrElse({ it.second }, false) shouldBe it.second
            ReadOnlyJSONVal(it.first).asStringOrElse(false) { it.second } shouldBe it.second
            ReadOnlyJSONVal(it.first).asStringOrElse({ it.second }, true) shouldBe Conversions.toString(it.first, true)
            ReadOnlyJSONVal(it.first).asStringOrElse(true) { it.second } shouldBe Conversions.toString(it.first, true)
        }

        checkAll(JSONGenerators.basicStrings) {
            ReadOnlyJSONVal(null).asStringOrElse { it } shouldBe it
            ReadOnlyJSONVal(null).asStringOrElse({ it }, false) shouldBe it
            ReadOnlyJSONVal(null).asStringOrElse(false) { it } shouldBe it
            ReadOnlyJSONVal(null).asStringOrElse({ it }, true) shouldBe it
            ReadOnlyJSONVal(null).asStringOrElse(true) { it } shouldBe it
        }
    }

})