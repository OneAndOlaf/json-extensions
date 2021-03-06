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

import com.github.oneandolaf.jsonext.testutils.JSONGenerators
import com.github.oneandolaf.jsonext.testutils.cross
import com.github.oneandolaf.jsonext.testutils.shouldBeSimilarTo
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.checkAll
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JSONObjectUnmodifiableTests : FunSpec({

    test("accumulate") {
        checkAll(JSONGenerators.allStrings cross JSONGenerators.values) {
            val obj = JSONObject()

            val unmodifiable = JSONObjectUnmodifiable(obj)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.accumulate(it.first, it.second)
            }
        }
    }

    test("append") {
        checkAll(JSONGenerators.allStrings cross JSONGenerators.values) {
            val obj = JSONObject()

            val unmodifiable = JSONObjectUnmodifiable(obj)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.append(it.first, it.second)
            }
        }
    }

    test("has") {
        checkAll(JSONGenerators.objects cross JSONGenerators.allStrings) {

            val unmodifiable = JSONObjectUnmodifiable(it.first)

            for (key in it.first.keySet()) {
                unmodifiable.has(key) shouldBe true
            }

            unmodifiable.has(it.second) shouldBe it.first.has(it.second)

        }
    }

    test("increment") {
        checkAll(JSONGenerators.objects cross JSONGenerators.allStrings) {

            val unmodifiable = JSONObjectUnmodifiable(it.first)

            for (key in it.first.keySet() + it.second) {
                shouldThrow<UnsupportedOperationException> {
                    unmodifiable.increment(key)
                }
            }
        }
    }

    test("keys") {
        checkAll(JSONGenerators.objects) {

            val unmodifiable = JSONObjectUnmodifiable(it)

            val unmodifiableKeys = unmodifiable.keys().asSequence().toList()

            val thisKeys = it.keys().asSequence().toList()

            unmodifiableKeys shouldBe thisKeys
        }
    }

    test("keySet") {
        checkAll(JSONGenerators.objects) {
            val unmodifiable = JSONObjectUnmodifiable(it)

            val unmodifiableKeys = unmodifiable.keySet()

            val thisKeys = it.keySet()

            unmodifiableKeys shouldBe thisKeys
        }
    }

    test("length") {
        checkAll(JSONGenerators.objects) {
            JSONObjectUnmodifiable(it).length() shouldBe it.length()
        }
    }

    test("clear") {
        checkAll(JSONGenerators.objects) {
            val unmodifiable = JSONObjectUnmodifiable(it)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.clear()
            }
        }
    }

    test("isEmpty") {
        checkAll(JSONGenerators.objects) {
            val unmodifiable = JSONObjectUnmodifiable(it)

            unmodifiable.isEmpty shouldBe it.isEmpty
        }
    }

    test("names") {
        checkAll(JSONGenerators.objects) {
            val unmodifiable = JSONObjectUnmodifiable(it)

            unmodifiable.names() shouldBeSimilarTo it.names()
        }
    }

    test("opt same as src") {
        checkAll(JSONGenerators.objects) {

            val unmodifiable = JSONObjectUnmodifiable(it)

            for (key in it.keySet()) {
                unmodifiable.opt(key) shouldBeSimilarTo it.opt(key)
            }
        }
    }

    test("opt is unmodifiable") {
        checkAll(JSONGenerators.objects) {

            val unmodifiable = JSONObjectUnmodifiable(it)

            for (key in it.keySet()) {
                if (it.opt(key) is JSONObject) {
                    unmodifiable.opt(key).shouldBeInstanceOf<JSONObjectUnmodifiable>()
                } else if (it.opt(key) is JSONArray) {
                    unmodifiable.opt(key).shouldBeInstanceOf<JSONArrayUnmodifiable>()
                }
            }
        }
    }

    test("optJSONObject is unmodifiable") {
        checkAll(JSONGenerators.objects) {
            val unmodifiable = JSONObjectUnmodifiable(it)

            for (key in it.keySet()) {
                if (it.optJSONObject(key) != null) {
                    unmodifiable.optJSONObject(key).shouldBeInstanceOf<JSONObjectUnmodifiable>()
                } else {
                    unmodifiable.optJSONObject(key).shouldBeNull()
                }
            }
        }
    }

    test("optJSONObject w. default is unmodifiable") {
        checkAll(JSONGenerators.objects) {
            val unmodifiable = JSONObjectUnmodifiable(it)

            for (key in it.keySet()) {
                if (it.optJSONObject(key, null) != null) {
                    unmodifiable.optJSONObject(key, null).shouldBeInstanceOf<JSONObjectUnmodifiable>()
                } else {
                    unmodifiable.optJSONObject(key, null).shouldBeNull()
                }
            }
        }
    }

    test("optJSONArray is unmodifiable") {
        checkAll(JSONGenerators.objects) {
            val unmodifiable = JSONObjectUnmodifiable(it)

            for (key in it.keySet()) {
                if (it.optJSONArray(key) != null) {
                    unmodifiable.optJSONArray(key).shouldBeInstanceOf<JSONArrayUnmodifiable>()
                } else {
                    unmodifiable.optJSONArray(key).shouldBeNull()
                }
            }
        }
    }

    test("get is unmodifiable") {
        checkAll(JSONGenerators.objects) {

            val unmodifiable = JSONObjectUnmodifiable(it)

            for (key in it.keySet()) {
                if (it.get(key) is JSONObject) {
                    unmodifiable.get(key).shouldBeInstanceOf<JSONObjectUnmodifiable>()
                } else if (it.get(key) is JSONArray) {
                    unmodifiable.get(key).shouldBeInstanceOf<JSONArrayUnmodifiable>()
                }
            }
        }
    }

    test("getJSONObject is unmodifiable") {
        checkAll(JSONGenerators.objects) {
            val unmodifiable = JSONObjectUnmodifiable(it)

            for (key in it.keySet()) {
                val expectException = try {
                    it.getJSONObject(key)
                    false
                } catch (ex: JSONException) {
                    true
                }

                if (expectException) {
                    shouldThrow<JSONException> {
                        unmodifiable.getJSONObject(key)
                    }
                } else {
                    unmodifiable.getJSONObject(key).shouldBeInstanceOf<JSONObjectUnmodifiable>()
                }
            }
        }
    }

    test("getJSONArray is unmodifiable") {
        checkAll(JSONGenerators.objects) {
            val unmodifiable = JSONObjectUnmodifiable(it)

            for (key in it.keySet()) {
                val expectException = try {
                    it.getJSONArray(key)
                    false
                } catch (ex: JSONException) {
                    true
                }

                if (expectException) {
                    shouldThrow<JSONException> {
                        unmodifiable.getJSONArray(key)
                    }
                } else {
                    unmodifiable.getJSONArray(key).shouldBeInstanceOf<JSONArrayUnmodifiable>()
                }
            }
        }
    }

    test("put any") {
        checkAll(JSONGenerators.allStrings cross JSONGenerators.values) {
            val obj = JSONObject()

            val unmodifiable = JSONObjectUnmodifiable(obj)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(it.first, it.second)
            }
        }
    }

    test("put boolean") {
        checkAll(JSONGenerators.allStrings cross JSONGenerators.bools) {
            val obj = JSONObject()

            val unmodifiable = JSONObjectUnmodifiable(obj)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(it.first, it.second)
            }
        }
    }

    test("put double") {
        checkAll(JSONGenerators.allStrings cross JSONGenerators.doubles) {
            val obj = JSONObject()

            val unmodifiable = JSONObjectUnmodifiable(obj)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(it.first, it.second)
            }
        }
    }

    test("put floats") {
        checkAll(JSONGenerators.allStrings cross JSONGenerators.floats) {
            val obj = JSONObject()

            val unmodifiable = JSONObjectUnmodifiable(obj)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(it.first, it.second)
            }
        }
    }

    test("put ints") {
        checkAll(JSONGenerators.allStrings cross JSONGenerators.ints) {
            val obj = JSONObject()

            val unmodifiable = JSONObjectUnmodifiable(obj)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(it.first, it.second)
            }
        }
    }

    test("put longs") {
        checkAll(JSONGenerators.allStrings cross JSONGenerators.longs) {
            val obj = JSONObject()

            val unmodifiable = JSONObjectUnmodifiable(obj)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(it.first, it.second)
            }
        }
    }

    test("put collections") {
        checkAll(JSONGenerators.allStrings cross JSONGenerators.arraysAsLists) {
            val obj = JSONObject()

            val unmodifiable = JSONObjectUnmodifiable(obj)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(it.first, it.second)
            }
        }
    }

    test("put maps") {
        checkAll(JSONGenerators.allStrings cross JSONGenerators.objectsAsMaps) {
            val obj = JSONObject()

            val unmodifiable = JSONObjectUnmodifiable(obj)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.put(it.first, it.second)
            }
        }
    }

    test("putOnce") {
        checkAll(JSONGenerators.allStrings cross JSONGenerators.values) {
            val obj = JSONObject()

            val unmodifiable = JSONObjectUnmodifiable(obj)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.putOnce(it.first, it.second)
            }
        }
    }

    test("putOpt") {
        checkAll(JSONGenerators.allStrings cross JSONGenerators.values) {
            val obj = JSONObject()

            val unmodifiable = JSONObjectUnmodifiable(obj)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.putOpt(it.first, it.second)
            }
        }
    }

    test("remove") {
        checkAll(JSONGenerators.objects) {

            val unmodifiable = JSONObjectUnmodifiable(it)

            for (key in it.keySet()) {
                shouldThrow<UnsupportedOperationException> {
                    unmodifiable.remove(key)
                }
            }
        }
    }

    test("similar") {
        checkAll(JSONGenerators.objects cross JSONGenerators.values) {

            val unmodifiable = JSONObjectUnmodifiable(it.first)

            unmodifiable.similar(it.second) shouldBe it.first.similar(it.second)
        }
    }

    test("toString") {
        checkAll(JSONGenerators.objects) {
            JSONObjectUnmodifiable(it).toString() shouldBe it.toString()
        }
    }

    test("toString indent") {
        checkAll(JSONGenerators.objects) {
            for (indent in 0..5) {
                JSONObjectUnmodifiable(it).toString(indent) shouldBe it.toString(indent)
            }
        }
    }

    test("toMap") {
        checkAll(JSONGenerators.objects) {
            JSONObjectUnmodifiable(it).toMap() shouldBe it.toMap()
        }
    }

})