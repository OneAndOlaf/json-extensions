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
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.kotest.property.checkAll
import org.json.JSONArray
import org.json.JSONObject

class JSONObjectExtensionsTests : FunSpec({

    test("deepCopy") {
        checkAll(JSONGenerators.objects) {
            val copy = it.deepCopy()

            copy shouldNotBeSameInstanceAs it
            copy shouldBeSimilarTo it

            for (key in copy.keySet()) {
                val item = copy.opt(key)

                if (item is JSONObject || item is JSONArray) {
                    copy.opt(key) shouldNotBeSameInstanceAs it.opt(key)
                }
            }
        }
    }

    test("asUnmodifiable") {
        checkAll(JSONGenerators.objects) {
            val unmod = it.asUnmodifiable()

            unmod shouldBeSimilarTo it

            it.put("newKey", "newValue")

            unmod.opt("newKey") shouldBe "newValue"
        }
    }

    test("asReadOnly") {
        checkAll(JSONGenerators.objects) {
            val readOnly = it.asReadOnly()

            readOnly shouldBeSimilarTo it

            it.put("newKey", "newValue")

            readOnly.getOrNull("newKey") shouldBe "newValue"
        }
    }

    test("readOnlySnapshot") {
        checkAll(JSONGenerators.objects) {
            val readOnly = it.readOnlySnapshot()

            readOnly shouldBeSimilarTo it

            it.put("newKey", "newValue")

            readOnly.getOrNull("newKey").shouldBeNull()
        }
    }
})