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

package com.github.oneandolaf.jsonext.impl

import com.github.oneandolaf.jsonext.testutils.JSONGenerators
import com.github.oneandolaf.jsonext.testutils.TestEnum
import com.github.oneandolaf.jsonext.testutils.minus
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.property.Exhaustive
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.enum
import io.kotest.property.exhaustive.map

class ConversionsTests : FunSpec({

    test("toBigDecimal") {
        Conversions.toBigDecimal(null).shouldBeNull()

        checkAll(JSONGenerators.bigDecimals) {
            Conversions.toBigDecimal(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.bigIntegers) {
            Conversions.toBigDecimal(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.numbers) {
            Conversions.toBigDecimal(it).shouldNotBeNull()
        }

        Conversions.toBigDecimal(Double.NaN).shouldBeNull()
        Conversions.toBigDecimal(Double.POSITIVE_INFINITY).shouldBeNull()
        Conversions.toBigDecimal(Double.NEGATIVE_INFINITY).shouldBeNull()
        Conversions.toBigDecimal(Float.NaN).shouldBeNull()
        Conversions.toBigDecimal(Float.POSITIVE_INFINITY).shouldBeNull()
        Conversions.toBigDecimal(Float.NEGATIVE_INFINITY).shouldBeNull()

        checkAll(JSONGenerators.numericStrings) {
            Conversions.toBigDecimal(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.allStrings - JSONGenerators.numericStrings) {
            Conversions.toBigDecimal(it).shouldBeNull()
        }
        checkAll(JSONGenerators.bools) {
            Conversions.toBigDecimal(it).shouldBeNull()
        }
    }

    test("toBigInteger") {
        Conversions.toBigInteger(null).shouldBeNull()

        checkAll(JSONGenerators.bigDecimals) {
            Conversions.toBigInteger(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.bigIntegers) {
            Conversions.toBigInteger(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.numbers) {
            Conversions.toBigInteger(it).shouldNotBeNull()
        }

        Conversions.toBigInteger(Double.NaN).shouldBeNull()
        Conversions.toBigInteger(Double.POSITIVE_INFINITY).shouldBeNull()
        Conversions.toBigInteger(Double.NEGATIVE_INFINITY).shouldBeNull()
        Conversions.toBigInteger(Float.NaN).shouldBeNull()
        Conversions.toBigInteger(Float.POSITIVE_INFINITY).shouldBeNull()
        Conversions.toBigInteger(Float.NEGATIVE_INFINITY).shouldBeNull()

        checkAll(JSONGenerators.numericStrings) {
            Conversions.toBigInteger(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.allStrings - JSONGenerators.numericStrings) {
            Conversions.toBigInteger(it).shouldBeNull()
        }
        checkAll(JSONGenerators.bools) {
            Conversions.toBigInteger(it).shouldBeNull()
        }
    }

    test("toBoolean") {
        Conversions.toBoolean(null).shouldBeNull()

        checkAll(JSONGenerators.booleanLikes) {
            Conversions.toBoolean(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.nonBooleanLikes) {
            Conversions.toBoolean(it).shouldBeNull()
        }
    }

    test("toDouble") {
        Conversions.toDouble(null).shouldBeNull()

        checkAll(JSONGenerators.bigDecimals) {
            Conversions.toDouble(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.bigIntegers) {
            Conversions.toDouble(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.numbers) {
            Conversions.toDouble(it).shouldNotBeNull()
        }

        Conversions.toDouble(Double.NaN).shouldNotBeNull()
        Conversions.toDouble(Double.POSITIVE_INFINITY).shouldNotBeNull()
        Conversions.toDouble(Double.NEGATIVE_INFINITY).shouldNotBeNull()
        Conversions.toDouble(Float.NaN).shouldNotBeNull()
        Conversions.toDouble(Float.POSITIVE_INFINITY).shouldNotBeNull()
        Conversions.toDouble(Float.NEGATIVE_INFINITY).shouldNotBeNull()

        checkAll(JSONGenerators.numericStrings) {
            Conversions.toDouble(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.allStrings - JSONGenerators.numericStrings) {
            Conversions.toDouble(it).shouldBeNull()
        }
        checkAll(JSONGenerators.bools) {
            Conversions.toDouble(it).shouldBeNull()
        }
    }

    test("toEnum") {
        Conversions.toEnum(null, TestEnum::class.java).shouldBeNull()
        Conversions.toEnum<TestEnum>(null).shouldBeNull()

        checkAll(JSONGenerators.values) {
            Conversions.toEnum(it, TestEnum::class.java).shouldBeNull()
            Conversions.toEnum<TestEnum>(it).shouldBeNull()
        }

        checkAll(Exhaustive.enum<TestEnum>()) {
            Conversions.toEnum(it, TestEnum::class.java).shouldNotBeNull()
            Conversions.toEnum<TestEnum>(it).shouldNotBeNull()
        }

        checkAll(Exhaustive.enum<TestEnum>().map { it.name }) {
            Conversions.toEnum(it, TestEnum::class.java).shouldNotBeNull()
            Conversions.toEnum<TestEnum>(it).shouldNotBeNull()
        }

        checkAll(Exhaustive.enum<TestEnum>().map { it.name.lowercase() }) {
            Conversions.toEnum(it, TestEnum::class.java).shouldBeNull()
            Conversions.toEnum<TestEnum>(it).shouldBeNull()
        }
    }

    test("toFloat") {
        Conversions.toFloat(null).shouldBeNull()

        checkAll(JSONGenerators.bigDecimals) {
            Conversions.toFloat(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.bigIntegers) {
            Conversions.toFloat(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.numbers) {
            Conversions.toFloat(it).shouldNotBeNull()
        }

        Conversions.toFloat(Double.NaN).shouldNotBeNull()
        Conversions.toFloat(Double.POSITIVE_INFINITY).shouldNotBeNull()
        Conversions.toFloat(Double.NEGATIVE_INFINITY).shouldNotBeNull()
        Conversions.toFloat(Float.NaN).shouldNotBeNull()
        Conversions.toFloat(Float.POSITIVE_INFINITY).shouldNotBeNull()
        Conversions.toFloat(Float.NEGATIVE_INFINITY).shouldNotBeNull()

        checkAll(JSONGenerators.numericStrings) {
            Conversions.toFloat(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.allStrings - JSONGenerators.numericStrings) {
            Conversions.toFloat(it).shouldBeNull()
        }
        checkAll(JSONGenerators.bools) {
            Conversions.toFloat(it).shouldBeNull()
        }
    }

    test("toInt") {
        Conversions.toInt(null).shouldBeNull()

        checkAll(JSONGenerators.bigDecimals) {
            Conversions.toInt(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.bigIntegers) {
            Conversions.toInt(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.numbers) {
            Conversions.toInt(it).shouldNotBeNull()
        }

        Conversions.toInt(Double.NaN).shouldNotBeNull()
        Conversions.toInt(Double.POSITIVE_INFINITY).shouldNotBeNull()
        Conversions.toInt(Double.NEGATIVE_INFINITY).shouldNotBeNull()
        Conversions.toInt(Float.NaN).shouldNotBeNull()
        Conversions.toInt(Float.POSITIVE_INFINITY).shouldNotBeNull()
        Conversions.toInt(Float.NEGATIVE_INFINITY).shouldNotBeNull()

        checkAll(JSONGenerators.intStrings) {
            Conversions.toInt(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.doubleStrings) {
            Conversions.toInt(it).shouldBeNull()
        }

        checkAll(JSONGenerators.allStrings - JSONGenerators.numericStrings) {
            Conversions.toInt(it).shouldBeNull()
        }
        checkAll(JSONGenerators.bools) {
            Conversions.toInt(it).shouldBeNull()
        }
    }

    test("toLong") {
        Conversions.toLong(null).shouldBeNull()

        checkAll(JSONGenerators.bigDecimals) {
            Conversions.toLong(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.bigIntegers) {
            Conversions.toLong(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.numbers) {
            Conversions.toLong(it).shouldNotBeNull()
        }

        Conversions.toLong(Double.NaN).shouldNotBeNull()
        Conversions.toLong(Double.POSITIVE_INFINITY).shouldNotBeNull()
        Conversions.toLong(Double.NEGATIVE_INFINITY).shouldNotBeNull()
        Conversions.toLong(Float.NaN).shouldNotBeNull()
        Conversions.toLong(Float.POSITIVE_INFINITY).shouldNotBeNull()
        Conversions.toLong(Float.NEGATIVE_INFINITY).shouldNotBeNull()

        checkAll(JSONGenerators.intStrings) {
            Conversions.toLong(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.doubleStrings) {
            Conversions.toLong(it).shouldBeNull()
        }

        checkAll(JSONGenerators.allStrings - JSONGenerators.numericStrings) {
            Conversions.toLong(it).shouldBeNull()
        }
        checkAll(JSONGenerators.bools) {
            Conversions.toLong(it).shouldBeNull()
        }
    }

    test("toNumber") {
        Conversions.toNumber(null).shouldBeNull()

        checkAll(JSONGenerators.bigDecimals) {
            Conversions.toNumber(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.bigIntegers) {
            Conversions.toNumber(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.numbers) {
            Conversions.toNumber(it).shouldNotBeNull()
        }

        Conversions.toNumber(Double.NaN).shouldNotBeNull()
        Conversions.toNumber(Double.POSITIVE_INFINITY).shouldNotBeNull()
        Conversions.toNumber(Double.NEGATIVE_INFINITY).shouldNotBeNull()
        Conversions.toNumber(Float.NaN).shouldNotBeNull()
        Conversions.toNumber(Float.POSITIVE_INFINITY).shouldNotBeNull()
        Conversions.toNumber(Float.NEGATIVE_INFINITY).shouldNotBeNull()

        checkAll(JSONGenerators.numericStrings) {
            Conversions.toNumber(it).shouldNotBeNull()
        }
        checkAll(JSONGenerators.allStrings - JSONGenerators.numericStrings) {
            Conversions.toNumber(it).shouldBeNull()
        }
        checkAll(JSONGenerators.bools) {
            Conversions.toNumber(it).shouldBeNull()
        }
    }

    test("toString") {
        Conversions.toString(null, false).shouldBeNull()
        Conversions.toString(null, true).shouldBeNull()

        checkAll(JSONGenerators.allStrings) {
            Conversions.toString(it, false).shouldNotBeNull()
            Conversions.toString(it, true).shouldNotBeNull()
        }
        checkAll(JSONGenerators.nonStrings) {
            Conversions.toString(it, false).shouldBeNull()
            Conversions.toString(it, true).shouldNotBeNull()
        }
    }

})