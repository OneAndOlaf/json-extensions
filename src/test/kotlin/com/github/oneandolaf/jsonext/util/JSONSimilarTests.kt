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

package com.github.oneandolaf.jsonext.util

import com.github.oneandolaf.jsonext.readonly.ReadOnlyJSONArray
import com.github.oneandolaf.jsonext.readonly.ReadOnlyJSONObject
import com.github.oneandolaf.jsonext.testutils.JSONGenerators
import com.github.oneandolaf.jsonext.testutils.cross
import com.github.oneandolaf.jsonext.testutils.shouldBeSimilarTo
import com.github.oneandolaf.jsonext.testutils.shouldNotBeSimilarTo
import io.kotest.core.spec.style.FunSpec
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.times
import org.json.JSONObject

class JSONSimilarTests : FunSpec({

    test("similar") {

        checkAll(JSONGenerators.values) {
            it shouldBeSimilarTo it
        }

        checkAll(JSONGenerators.objects) {
            it shouldBeSimilarTo ReadOnlyJSONObject.create(it)
            ReadOnlyJSONObject.create(it) shouldBeSimilarTo it
        }

        checkAll(JSONGenerators.arrays) {
            it shouldBeSimilarTo ReadOnlyJSONArray.create(it)
            ReadOnlyJSONArray.create(it) shouldBeSimilarTo it
        }

        null shouldBeSimilarTo null
    }

    test("not similar") {

        null shouldNotBeSimilarTo JSONObject.NULL
        JSONObject.NULL shouldNotBeSimilarTo null

        checkAll(JSONGenerators.objects * JSONGenerators.objects) {
            if (!it.first.similar(it.second)) {
                it.first shouldNotBeSimilarTo it.second
                ReadOnlyJSONObject.create(it.first) shouldNotBeSimilarTo it.second
                it.first shouldNotBeSimilarTo ReadOnlyJSONObject.create(it.second)
                ReadOnlyJSONObject.create(it.first) shouldNotBeSimilarTo ReadOnlyJSONObject.create(it.second)

                it.second shouldNotBeSimilarTo it.first
                ReadOnlyJSONObject.create(it.second) shouldNotBeSimilarTo it.first
                it.second shouldNotBeSimilarTo ReadOnlyJSONObject.create(it.first)
                ReadOnlyJSONObject.create(it.second) shouldNotBeSimilarTo ReadOnlyJSONObject.create(it.first)
            }
        }


        checkAll(JSONGenerators.objects cross JSONGenerators.nonObjects) {
            it.first shouldNotBeSimilarTo it.second
            it.second shouldNotBeSimilarTo it.first
            ReadOnlyJSONObject.create(it.first) shouldNotBeSimilarTo it.second
            it.second shouldNotBeSimilarTo ReadOnlyJSONObject.create(it.first)
        }

        checkAll(JSONGenerators.arrays * JSONGenerators.arrays) {
            if (!it.first.similar(it.second)) {
                it.first shouldNotBeSimilarTo it.second
                ReadOnlyJSONArray.create(it.first) shouldNotBeSimilarTo it.second
                it.first shouldNotBeSimilarTo ReadOnlyJSONArray.create(it.second)
                ReadOnlyJSONArray.create(it.first) shouldNotBeSimilarTo ReadOnlyJSONArray.create(it.second)

                it.second shouldNotBeSimilarTo it.first
                ReadOnlyJSONArray.create(it.second) shouldNotBeSimilarTo it.first
                it.second shouldNotBeSimilarTo ReadOnlyJSONArray.create(it.first)
                ReadOnlyJSONArray.create(it.second) shouldNotBeSimilarTo ReadOnlyJSONArray.create(it.first)
            }
        }

        checkAll(JSONGenerators.arrays cross JSONGenerators.nonArrays) {
            it.first shouldNotBeSimilarTo it.second
            it.second shouldNotBeSimilarTo it.first
            ReadOnlyJSONArray.create(it.first) shouldNotBeSimilarTo it.second
            it.second shouldNotBeSimilarTo ReadOnlyJSONArray.create(it.first)
        }
    }

})