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

import com.github.oneandolaf.jsonext.extensions.asReadOnly
import com.github.oneandolaf.jsonext.extensions.jsonArrayOf
import com.github.oneandolaf.jsonext.extensions.jsonObjectOf
import com.github.oneandolaf.jsonext.extensions.jsonPointerOf
import com.github.oneandolaf.jsonext.impl.Conversions
import com.github.oneandolaf.jsonext.testutils.JSONGenerators
import com.github.oneandolaf.jsonext.testutils.shouldBeSimilarTo
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.exhaustive
import io.kotest.property.forAll
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONPointer

class ReadOnlyJSONArrayTests : FunSpec({

    test("size") {
        forAll(JSONGenerators.arrays) {
            ReadOnlyJSONArray.create(it).size == it.length()
        }
    }

    test("isEmpty") {
        forAll(JSONGenerators.arrays) {
            ReadOnlyJSONArray.create(it).isEmpty == it.isEmpty
        }
    }

    test("isNotEmpty") {
        forAll(JSONGenerators.arrays) {
            ReadOnlyJSONArray.create(it).isNotEmpty != it.isEmpty
        }
    }

    test("itemsAsArrays") {
        checkAll(JSONGenerators.arrays) {
            ReadOnlyJSONArray.create(it).itemsAsArrays() shouldHaveSize it.filterIsInstance<JSONArray>().size
        }
    }

    test("itemsAsBooleans") {
        checkAll(JSONGenerators.arrays) {
            ReadOnlyJSONArray.create(it)
                .itemsAsBooleans() shouldHaveSize it.mapNotNull { Conversions.toBoolean(it) }.size
        }
    }

    test("itemsAsDoubles") {
        checkAll(JSONGenerators.arrays) {
            ReadOnlyJSONArray.create(it).itemsAsDoubles() shouldHaveSize it.mapNotNull { Conversions.toDouble(it) }.size
        }
    }

    test("itemsAsInts") {
        checkAll(JSONGenerators.arrays) {
            ReadOnlyJSONArray.create(it).itemsAsInts() shouldHaveSize it.mapNotNull { Conversions.toInt(it) }.size
        }
    }

    test("itemsAsLongs") {
        checkAll(JSONGenerators.arrays) {
            ReadOnlyJSONArray.create(it).itemsAsLongs() shouldHaveSize it.mapNotNull { Conversions.toLong(it) }.size
        }
    }

    test("itemsAsObjects") {
        checkAll(JSONGenerators.arrays) {
            ReadOnlyJSONArray.create(it).itemsAsObjects() shouldHaveSize it.filterIsInstance<JSONObject>().size
        }
    }

    test("itemsAsStrings") {
        checkAll(JSONGenerators.arrays) {
            ReadOnlyJSONArray.create(it).itemsAsStrings() shouldHaveSize it.mapNotNull {
                Conversions.toString(
                    it,
                    false
                )
            }
                .size
            ReadOnlyJSONArray.create(it).itemsAsStrings(false) shouldHaveSize it.mapNotNull {
                Conversions.toString(it, false)
            }.size
            ReadOnlyJSONArray.create(it).itemsAsStrings(true) shouldHaveSize it.mapNotNull {
                Conversions.toString(it, true)
            }.size
        }
    }

    test("query") {
        val arr = ReadOnlyJSONArray.create(
            jsonArrayOf(
                "bar",
                42,
                true,
                mapOf(
                    "aSubStr" to "value",
                    "aSubDouble" to 3.14
                ),
                listOf(
                    "a",
                    4,
                    false,
                    mapOf(
                        "nested" to "value"
                    )
                )
            )
        )

        fun ReadOnlyJSONArray.checkForPointerFormats(rawPointer: List<String>, func: (ReadOnlyJSONVal) -> Unit) {
            func(query(rawPointer))
            func(query(jsonPointerOf(rawPointer)))
            func(query(JSONPointer(jsonPointerOf(rawPointer))))
        }

        checkAll(
            listOf(
                emptyList<String>() to arr,
                listOf("0") to "bar",
                listOf("1") to 42,
                listOf("2") to true,
                listOf("3") to jsonObjectOf(
                    "aSubStr" to "value",
                    "aSubDouble" to 3.14
                ),
                listOf("3", "aSubStr") to "value",
                listOf("3", "aSubDouble") to 3.14,
                listOf("4") to jsonArrayOf(
                    "a",
                    4,
                    false,
                    mapOf(
                        "nested" to "value"
                    )
                ),
                listOf("4", "0") to "a",
                listOf("4", "1") to 4,
                listOf("4", "2") to false,
                listOf("4", "3") to jsonObjectOf(
                    "nested" to "value"
                ),
                listOf("4", "3", "nested") to "value"
            ).exhaustive()
        ) { data ->
            arr.checkForPointerFormats(data.first) {
                it.orNull() shouldBeSimilarTo data.second
            }
        }

        checkAll(
            listOf(
                listOf("-1"),
                listOf("27"),
                listOf("3", "foo"),
                listOf(""),
                listOf("3", ""),
                listOf("4", "foo"),
                listOf("0", "0"),
                listOf("0", "foo")
            ).exhaustive()
        ) { data ->
            arr.checkForPointerFormats(data) {
                it.orNull().shouldBeNull()
            }
        }
    }

    test("copyToPlain") {
        checkAll(JSONGenerators.arrays) {
            val ro = it.asReadOnly()

            val newCopy = ro.copyToPlain()

            newCopy shouldBeSimilarTo it
            newCopy shouldNotBeSameInstanceAs it
        }
    }

    test("snapshot") {
        checkAll(JSONGenerators.arrays) {
            val ro = it.asReadOnly()

            val newCopy = ro.snapshot()

            newCopy shouldBeSimilarTo it
            newCopy shouldNotBeSameInstanceAs it
        }

        ReadOnlyJSONArray.EMPTY.snapshot() shouldBeSameInstanceAs ReadOnlyJSONArray.EMPTY
    }

})