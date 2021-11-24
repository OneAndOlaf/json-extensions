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
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.checkAll
import org.json.JSONArray
import org.json.JSONObject

class JSONObjectUnmodifiableTests : FunSpec({

    context("accumulate") {
        checkAll(JSONGenerators.allStrings cross JSONGenerators.values) {
            val obj = JSONObject()

            val unmodifiable = JSONObjectUnmodifiable(obj)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.accumulate(it.first, it.second)
            }
        }
    }

    context("append") {
        checkAll(JSONGenerators.allStrings cross JSONGenerators.values) {
            val obj = JSONObject()

            val unmodifiable = JSONObjectUnmodifiable(obj)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.append(it.first, it.second)
            }
        }
    }

    context("has") {
        checkAll(JSONGenerators.objects cross JSONGenerators.allStrings) {

            val unmodifiable = JSONObjectUnmodifiable(it.first)

            for (key in it.first.keySet()) {
                unmodifiable.has(key) shouldBe true
            }

            unmodifiable.has(it.second) shouldBe it.first.has(it.second)

        }
    }

    context("increment") {
        checkAll(JSONGenerators.objects cross JSONGenerators.allStrings) {

            val unmodifiable = JSONObjectUnmodifiable(it.first)

            for (key in it.first.keySet() + it.second) {
                shouldThrow<UnsupportedOperationException> {
                    unmodifiable.increment(key)
                }
            }
        }
    }

    context("length") {
        checkAll(JSONGenerators.objects) {
            JSONObjectUnmodifiable(it).length() shouldBe it.length()
        }
    }

    context("clear") {
        checkAll(JSONGenerators.objects) {
            val unmodifiable = JSONObjectUnmodifiable(it)

            shouldThrow<UnsupportedOperationException> {
                unmodifiable.clear()
            }
        }
    }

    context("isEmpty") {
        checkAll(JSONGenerators.objects) {
            val unmodifiable = JSONObjectUnmodifiable(it)

            unmodifiable.isEmpty shouldBe it.isEmpty
        }
    }

    context("names") {
        checkAll(JSONGenerators.objects) {
            val unmodifiable = JSONObjectUnmodifiable(it)

            unmodifiable.names() shouldBeSimilarTo it.names()
        }
    }

    context("opt same as src") {
        checkAll(JSONGenerators.objects) {

            val unmodifiable = JSONObjectUnmodifiable(it)

            for (key in it.keySet()) {
                unmodifiable.opt(key) shouldBeSimilarTo it.opt(key)
            }
        }
    }

    context("opt is unmodifiable") {
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


})