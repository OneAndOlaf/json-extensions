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

import com.github.oneandolaf.jsonext.testutils.JSONGenerators
import com.github.oneandolaf.jsonext.testutils.cross
import com.github.oneandolaf.jsonext.testutils.shouldBeSimilarTo
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.checkAll
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JSONArrayUnmodifiableTests : FunSpec({

    test("iterator") {
        checkAll(JSONGenerators.arrays) {
            val unmodifiable = JSONArrayUnmodifiable(it)

            val unmodItItems = unmodifiable.iterator()

            val origItems = it.iterator()

            for (el in unmodItItems) {
                val actual = origItems.next()

                el shouldBeSimilarTo actual
            }

            unmodItItems.hasNext().shouldBeFalse()
            origItems.hasNext().shouldBeFalse()
        }
    }

    test("length") {
        checkAll(JSONGenerators.arrays) {
            JSONArrayUnmodifiable(it).length() shouldBe it.length()
        }
    }

    test("clear") {
        checkAll(JSONGenerators.arrays) {
            shouldThrow<UnsupportedOperationException> {
                JSONArrayUnmodifiable(it).clear()
            }
        }
    }

    test("join") {
        checkAll(JSONGenerators.arrays) {
            val unmodifiable = JSONArrayUnmodifiable(it)

            unmodifiable.join("") shouldBe it.join("")
            unmodifiable.join(",") shouldBe it.join(",")
        }
    }

    test("get is unmodifiable") {
        checkAll(JSONGenerators.arrays) {
            val unmodifiable = JSONArrayUnmodifiable(it)

            for (i in 0 until it.length()) {
                val actual = it.get(i)

                if (actual is JSONObject) {
                    unmodifiable.get(i).shouldBeInstanceOf<JSONObjectUnmodifiable>()
                } else if (actual is JSONArray) {
                    unmodifiable.get(i).shouldBeInstanceOf<JSONArrayUnmodifiable>()
                }
            }
        }
    }

    test("getJSONObject is unmodifiable") {
        checkAll(JSONGenerators.arrays) {
            val unmodifiable = JSONArrayUnmodifiable(it)

            for (i in 0 until it.length()) {
                val exceptionThrown = try {
                    it.getJSONObject(i)
                    false
                } catch (_: JSONException) {
                    true
                }

                if (exceptionThrown) {
                    shouldThrow<JSONException> {
                        unmodifiable.getJSONObject(i)
                    }
                } else {
                    unmodifiable.getJSONObject(i).shouldBeInstanceOf<JSONObjectUnmodifiable>()
                }
            }
        }
    }

    test("getJSONArray is unmodifiable") {
        checkAll(JSONGenerators.arrays) {
            val unmodifiable = JSONArrayUnmodifiable(it)

            for (i in 0 until it.length()) {
                val exceptionThrown = try {
                    it.getJSONArray(i)
                    false
                } catch (_: JSONException) {
                    true
                }

                if (exceptionThrown) {
                    shouldThrow<JSONException> {
                        unmodifiable.getJSONArray(i)
                    }
                } else {
                    unmodifiable.getJSONArray(i).shouldBeInstanceOf<JSONArrayUnmodifiable>()
                }
            }
        }
    }

    test("opt is unmodifiable") {
        checkAll(JSONGenerators.arrays) {
            val unmodifiable = JSONArrayUnmodifiable(it)

            for (i in 0 until it.length()) {
                val actual = it.opt(i)

                if (actual is JSONObject) {
                    unmodifiable.opt(i).shouldBeInstanceOf<JSONObjectUnmodifiable>()
                } else if (actual is JSONArray) {
                    unmodifiable.opt(i).shouldBeInstanceOf<JSONArrayUnmodifiable>()
                }
            }
        }
    }

    test("optJSONObject is unmodifiable") {
        checkAll(JSONGenerators.arrays) {
            val unmodifiable = JSONArrayUnmodifiable(it)

            for (i in 0 until it.length()) {
                val actual = it.optJSONObject(i)

                if (actual == null) {
                    unmodifiable.optJSONObject(i).shouldBeNull()
                } else {
                    unmodifiable.optJSONObject(i).shouldBeInstanceOf<JSONObjectUnmodifiable>()
                }
            }
        }
    }

    test("optJSONArray is unmodifiable") {
        checkAll(JSONGenerators.arrays) {
            val unmodifiable = JSONArrayUnmodifiable(it)

            for (i in 0 until it.length()) {
                val actual = it.optJSONArray(i)

                if (actual == null) {
                    unmodifiable.optJSONArray(i).shouldBeNull()
                } else {
                    unmodifiable.optJSONArray(i).shouldBeInstanceOf<JSONArrayUnmodifiable>()
                }
            }
        }
    }

    test("put any") {
        checkAll(JSONGenerators.arrays cross JSONGenerators.values) {
            val unmodifiable = JSONArrayUnmodifiable(it.first)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(0, it.second)
            }
            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(it.second)
            }
        }
    }

    test("put booleans") {
        checkAll(JSONGenerators.arrays cross JSONGenerators.bools) {
            val unmodifiable = JSONArrayUnmodifiable(it.first)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(0, it.second)
            }
            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(it.second)
            }
        }
    }

    test("put doubles") {
        checkAll(JSONGenerators.arrays cross JSONGenerators.doubles) {
            val unmodifiable = JSONArrayUnmodifiable(it.first)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(0, it.second)
            }
            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(it.second)
            }
        }
    }

    test("put floats") {
        checkAll(JSONGenerators.arrays cross JSONGenerators.floats) {
            val unmodifiable = JSONArrayUnmodifiable(it.first)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(0, it.second)
            }
            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(it.second)
            }
        }
    }

    test("put ints") {
        checkAll(JSONGenerators.arrays cross JSONGenerators.ints) {
            val unmodifiable = JSONArrayUnmodifiable(it.first)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(0, it.second)
            }
            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(it.second)
            }
        }
    }

    test("put longs") {
        checkAll(JSONGenerators.arrays cross JSONGenerators.longs) {
            val unmodifiable = JSONArrayUnmodifiable(it.first)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(0, it.second)
            }
            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(it.second)
            }
        }
    }

    test("put collections") {
        checkAll(JSONGenerators.arrays cross JSONGenerators.arraysAsLists) {
            val unmodifiable = JSONArrayUnmodifiable(it.first)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(0, it.second)
            }
            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(it.second)
            }
        }
    }

    test("put maps") {
        checkAll(JSONGenerators.arrays cross JSONGenerators.objectsAsMaps) {
            val unmodifiable = JSONArrayUnmodifiable(it.first)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(0, it.second)
            }
            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(it.second)
            }
        }
    }

    test("putAll any") {
        checkAll(JSONGenerators.arrays cross JSONGenerators.objectsAsMaps) {
            val unmodifiable = JSONArrayUnmodifiable(it.first)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.putAll(it.second)
            }
        }
    }

    test("putAll array") {
        checkAll(JSONGenerators.arrays cross JSONGenerators.arrays) {
            val unmodifiable = JSONArrayUnmodifiable(it.first)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.putAll(it.second)
            }
        }
    }

    test("putAll collection") {
        checkAll(JSONGenerators.arrays cross JSONGenerators.arraysAsLists) {
            val unmodifiable = JSONArrayUnmodifiable(it.first)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.putAll(it.second)
            }
        }
    }

    test("putAll iterable") {
        checkAll(JSONGenerators.arrays cross JSONGenerators.arraysAsLists) {
            val unmodifiable = JSONArrayUnmodifiable(it.first)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.putAll(it.second as Iterable<*>)
            }
        }
    }

    test("remove") {
        checkAll(JSONGenerators.arrays) {
            val unmodifiable = JSONArrayUnmodifiable(it)

            for (i in 0 until it.length()) {
                shouldThrow<UnsupportedOperationException> {
                    unmodifiable.remove(i)
                }
            }
        }
    }

    test("similar") {
        checkAll(JSONGenerators.arrays cross JSONGenerators.values) {
            val unmodifiable = JSONArrayUnmodifiable(it.first)

            unmodifiable.similar(it.second) shouldBe it.first.similar(it.second)
        }
    }

    test("toString") {
        checkAll(JSONGenerators.arrays) {
            JSONArrayUnmodifiable(it).toString() shouldBe it.toString()
        }
    }

    test("toString indent") {
        checkAll(JSONGenerators.arrays) {
            for (i in 0..6) {
                JSONArrayUnmodifiable(it).toString(i) shouldBe it.toString(i)
            }
        }
    }

    test("toList") {
        checkAll(JSONGenerators.arrays) {
            JSONArrayUnmodifiable(it).toList() shouldBe it.toList()
        }
    }

    test("isEmpty") {
        checkAll(JSONGenerators.arrays) {
            JSONArrayUnmodifiable(it).isEmpty shouldBe it.isEmpty
        }
    }


})