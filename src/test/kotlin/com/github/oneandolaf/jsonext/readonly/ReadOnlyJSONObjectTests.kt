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
import com.github.oneandolaf.jsonext.testutils.JSONGenerators
import com.github.oneandolaf.jsonext.testutils.cross
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import io.kotest.property.forAll
import org.json.JSONObject

class ReadOnlyJSONObjectTests : FunSpec({

    context("contains") {
        checkAll(JSONGenerators.objects) {
            val ro = ReadOnlyJSONObject(it)

            for (key in it.keySet()) {
                ro.contains(key).shouldBeTrue()
            }

            for (s in JSONGenerators.allStrings.values) {
                ro.contains(s) shouldBe it.has(s)
            }
        }
    }

    context("containsNonNull") {
        checkAll(JSONGenerators.objects) {
            val ro = ReadOnlyJSONObject(it)

            for (key in it.keySet()) {
                ro.containsNonNull(key) shouldBe (ro.getOrNull(key) !== JSONObject.NULL)
            }

            for (s in JSONGenerators.allStrings.values) {
                ro.contains(s) shouldBe (it.has(s) && it.opt(s) !== JSONObject.NULL)
            }
        }
    }

    context("size") {
        forAll(JSONGenerators.objects) {
            ReadOnlyJSONObject(it).size == it.length()
        }
    }

    context("isEmpty") {
        forAll(JSONGenerators.objects) {
            ReadOnlyJSONObject(it).isEmpty == it.isEmpty
        }
    }

    context("isNotEmpty") {
        forAll(JSONGenerators.objects) {
            ReadOnlyJSONObject(it).isNotEmpty != it.isEmpty
        }
    }

    context("keySet") {
        forAll(JSONGenerators.objects) {
            ReadOnlyJSONObject(it).keySet == it.keySet()
        }
    }

    context("similar") {
        checkAll(JSONGenerators.objects cross JSONGenerators.objects) {
            val (obj1, obj2) = it

            val ro1 = ReadOnlyJSONObject(obj1.deepCopy())
            val ro2 = ReadOnlyJSONObject(obj2.deepCopy())

            ro1.similar(ro1).shouldBeTrue()
            ro2.similar(ro2).shouldBeTrue()

            ro1.similar(ro2) shouldBe (obj1.similar(obj2))
            ro2.similar(ro1) shouldBe (obj2.similar(obj1))

            ro1.similar(obj1).shouldBeFalse()
            ro2.similar(obj2).shouldBeFalse()
        }
    }

    context("similarToPlainObject") {
        checkAll(JSONGenerators.objects cross JSONGenerators.objects) {
            val (obj1, obj2) = it

            val ro1 = ReadOnlyJSONObject(obj1.deepCopy())
            val ro2 = ReadOnlyJSONObject(obj2.deepCopy())

            ro1.similarToPlainObject(null).shouldBeFalse()
            ro2.similarToPlainObject(null).shouldBeFalse()

            ro1.similarToPlainObject(obj1).shouldBeTrue()
            ro2.similarToPlainObject(obj2).shouldBeTrue()

            ro1.similarToPlainObject(obj2) shouldBe (obj1.similar(obj2))
            ro2.similarToPlainObject(obj1) shouldBe (obj2.similar(obj1))

        }
    }

    context("toString") {
        forAll(JSONGenerators.objects) {
            ReadOnlyJSONObject(it).toString() == it.toString()
        }
    }

})