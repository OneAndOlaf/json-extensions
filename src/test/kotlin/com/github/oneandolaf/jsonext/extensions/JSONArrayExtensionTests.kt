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

package com.github.oneandolaf.jsonext.extensions

import com.github.oneandolaf.jsonext.testutils.JSONGenerators
import com.github.oneandolaf.jsonext.testutils.shouldBeSimilarTo
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.kotest.property.checkAll
import org.json.JSONArray
import org.json.JSONObject

class JSONArrayExtensionTests : FunSpec({

    test("deepCopy") {
        checkAll(JSONGenerators.arrays) {
            val copy = it.deepCopy()

            copy shouldBeSimilarTo it
            copy shouldNotBeSameInstanceAs it

            for (i in 0 until copy.length()) {
                if (copy[i] is JSONObject || copy[i] is JSONArray) {
                    copy[i] shouldNotBeSameInstanceAs it[i]
                }
            }
        }
    }

    test("asUnmodifiable") {
        checkAll(JSONGenerators.arrays) {
            val readOnly = it.asUnmodifiable()

            readOnly shouldBeSimilarTo it

            it.put("newValue")

            readOnly.opt(readOnly.length() - 1) shouldBe "newValue"
        }
    }

    test("asReadOnly") {
        checkAll(JSONGenerators.arrays) {
            val readOnly = it.asReadOnly()

            readOnly shouldBeSimilarTo it

            it.put("newValue")

            readOnly.getOrNull(readOnly.size - 1) shouldBe "newValue"
        }
    }

    test("readOnlySnapshot") {
        checkAll(JSONGenerators.arrays) {
            val readOnly = it.readOnlySnapshot()

            readOnly shouldBeSimilarTo it

            it.put("newValue")

            readOnly.getOrNull(readOnly.size - 1) shouldNotBe "newValue"
        }
    }

})