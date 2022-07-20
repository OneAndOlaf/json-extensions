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
import com.github.oneandolaf.jsonext.testutils.JSONGenerators
import com.github.oneandolaf.jsonext.testutils.cross
import com.github.oneandolaf.jsonext.testutils.shouldBeSimilarTo
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.exhaustive
import io.kotest.property.forAll
import org.json.JSONObject
import org.json.JSONPointer

class ReadOnlyJSONObjectTests : FunSpec({

    test("contains") {
        checkAll(JSONGenerators.objects) {
            val ro = ReadOnlyJSONObject.create(it)

            for (key in it.keySet()) {
                ro.contains(key).shouldBeTrue()
            }

            for (s in JSONGenerators.allStrings.values) {
                ro.contains(s) shouldBe it.has(s)
            }
        }
    }

    test("containsNonNull") {
        checkAll(JSONGenerators.objects) {
            val ro = ReadOnlyJSONObject.create(it)

            for (key in it.keySet()) {
                ro.containsNonNull(key) shouldBe (ro.getOrNull(key) !== JSONObject.NULL)
            }

            for (s in JSONGenerators.allStrings.values) {
                ro.contains(s) shouldBe (it.has(s) && it.opt(s) !== JSONObject.NULL)
            }
        }
    }

    test("size") {
        forAll(JSONGenerators.objects) {
            ReadOnlyJSONObject.create(it).size == it.length()
        }
    }

    test("isEmpty") {
        forAll(JSONGenerators.objects) {
            ReadOnlyJSONObject.create(it).isEmpty == it.isEmpty
        }
    }

    test("isNotEmpty") {
        forAll(JSONGenerators.objects) {
            ReadOnlyJSONObject.create(it).isNotEmpty != it.isEmpty
        }
    }

    test("keySet") {
        forAll(JSONGenerators.objects) {
            ReadOnlyJSONObject.create(it).keySet == it.keySet()
        }
    }

    test("query") {
        val obj = ReadOnlyJSONObject.create(
            jsonObjectOf(
                "foo" to "bar",
                "anInt" to 42,
                "aBool" to true,
                "aSubObj" to mapOf(
                    "aSubStr" to "value",
                    "aSubDouble" to 3.14
                ),
                "aSubArr" to listOf(
                    "a",
                    4,
                    false,
                    mapOf(
                        "nested" to "value"
                    )
                )
            )
        )

        fun ReadOnlyJSONObject.checkForPointerFormats(rawPointer: List<String>, func: (ReadOnlyJSONVal) -> Unit) {
            func(query(rawPointer))
            func(query(jsonPointerOf(rawPointer)))
            func(query(JSONPointer(jsonPointerOf(rawPointer))))
        }

        checkAll(
            listOf(
                emptyList<String>() to obj,
                listOf("foo") to "bar",
                listOf("anInt") to 42,
                listOf("aBool") to true,
                listOf("aSubObj") to jsonObjectOf(
                    "aSubStr" to "value",
                    "aSubDouble" to 3.14
                ),
                listOf("aSubObj", "aSubStr") to "value",
                listOf("aSubObj", "aSubDouble") to 3.14,
                listOf("aSubArr") to jsonArrayOf(
                    "a",
                    4,
                    false,
                    mapOf(
                        "nested" to "value"
                    )
                ),
                listOf("aSubArr", "0") to "a",
                listOf("aSubArr", "1") to 4,
                listOf("aSubArr", "2") to false,
                listOf("aSubArr", "3") to jsonObjectOf(
                    "nested" to "value"
                ),
                listOf("aSubArr", "3", "nested") to "value"
            ).exhaustive()
        ) { data ->
            obj.checkForPointerFormats(data.first) {
                it.orNull() shouldBeSimilarTo data.second
            }
        }

        checkAll(
            listOf(
                listOf("Foo"),
                listOf("something missing"),
                listOf("aSubObj", "foo"),
                listOf(""),
                listOf("aSubObj", ""),
                listOf("aSubArr", "foo"),
                listOf("foo", "0"),
                listOf("foo", "foo")
            ).exhaustive()
        ) { data ->
            obj.checkForPointerFormats(data) {
                it.orNull().shouldBeNull()
            }
        }
    }

    test("similar") {
        checkAll(JSONGenerators.objects cross JSONGenerators.objects) {
            val (obj1, obj2) = it

            val ro1 = ReadOnlyJSONObject.snapshot(obj1)
            val ro2 = ReadOnlyJSONObject.snapshot(obj2)

            ro1.similar(ro1).shouldBeTrue()
            ro2.similar(ro2).shouldBeTrue()

            ro1.similar(ro2) shouldBe (obj1.similar(obj2))
            ro2.similar(ro1) shouldBe (obj2.similar(obj1))

            ro1.similar(obj1).shouldBeFalse()
            ro2.similar(obj2).shouldBeFalse()
        }
    }

    test("similarToPlainObject") {
        checkAll(JSONGenerators.objects cross JSONGenerators.objects) {
            val (obj1, obj2) = it

            val ro1 = ReadOnlyJSONObject.snapshot(obj1)
            val ro2 = ReadOnlyJSONObject.snapshot(obj2)

            ro1.similarToPlainObject(null).shouldBeFalse()
            ro2.similarToPlainObject(null).shouldBeFalse()

            ro1.similarToPlainObject(obj1).shouldBeTrue()
            ro2.similarToPlainObject(obj2).shouldBeTrue()

            ro1.similarToPlainObject(obj2) shouldBe (obj1.similar(obj2))
            ro2.similarToPlainObject(obj1) shouldBe (obj2.similar(obj1))

        }
    }

    test("copyToPlain") {
        checkAll(JSONGenerators.objects) {
            val ro = it.asReadOnly()

            val newCopy = ro.copyToPlain()

            newCopy shouldBeSimilarTo it
            newCopy shouldNotBeSameInstanceAs it
        }
    }

    test("snapshot") {
        checkAll(JSONGenerators.objects) {
            val ro = it.asReadOnly()

            val newCopy = ro.snapshot()

            newCopy shouldBeSimilarTo it
            newCopy shouldNotBeSameInstanceAs it
        }

        ReadOnlyJSONObject.EMPTY.snapshot() shouldBeSameInstanceAs ReadOnlyJSONObject.EMPTY
    }

    test("toString") {
        forAll(JSONGenerators.objects) {
            ReadOnlyJSONObject.create(it).toString() == it.toString()
        }
    }

})