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

import com.github.oneandolaf.jsonext.impl.Conversions
import com.github.oneandolaf.jsonext.testutils.JSONGenerators
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.property.checkAll
import io.kotest.property.forAll
import org.json.JSONArray
import org.json.JSONObject

class ReadOnlyJSONArrayTests : FunSpec({

    context("size") {
        forAll(JSONGenerators.arrays) {
            ReadOnlyJSONArray.create(it).size == it.length()
        }
    }

    context("isEmpty") {
        forAll(JSONGenerators.arrays) {
            ReadOnlyJSONArray.create(it).isEmpty == it.isEmpty
        }
    }

    context("isNotEmpty") {
        forAll(JSONGenerators.arrays) {
            ReadOnlyJSONArray.create(it).isNotEmpty != it.isEmpty
        }
    }

    context("itemsAsArrays") {
        checkAll(JSONGenerators.arrays) {
            ReadOnlyJSONArray.create(it).itemsAsArrays() shouldHaveSize it.filterIsInstance<JSONArray>().size
        }
    }

    context("itemsAsBooleans") {
        checkAll(JSONGenerators.arrays) {
            ReadOnlyJSONArray.create(it)
                .itemsAsBooleans() shouldHaveSize it.mapNotNull { Conversions.toBoolean(it) }.size
        }
    }

    context("itemsAsDoubles") {
        checkAll(JSONGenerators.arrays) {
            ReadOnlyJSONArray.create(it).itemsAsDoubles() shouldHaveSize it.mapNotNull { Conversions.toDouble(it) }.size
        }
    }

    context("itemsAsInts") {
        checkAll(JSONGenerators.arrays) {
            ReadOnlyJSONArray.create(it).itemsAsInts() shouldHaveSize it.mapNotNull { Conversions.toInt(it) }.size
        }
    }

    context("itemsAsLongs") {
        checkAll(JSONGenerators.arrays) {
            ReadOnlyJSONArray.create(it).itemsAsLongs() shouldHaveSize it.mapNotNull { Conversions.toLong(it) }.size
        }
    }

    context("itemsAsObjects") {
        checkAll(JSONGenerators.arrays) {
            ReadOnlyJSONArray.create(it).itemsAsObjects() shouldHaveSize it.filterIsInstance<JSONObject>().size
        }
    }

    context("itemsAsStrings") {
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

})