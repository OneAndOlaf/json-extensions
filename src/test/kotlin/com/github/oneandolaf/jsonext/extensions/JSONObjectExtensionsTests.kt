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

package com.github.oneandolaf.jsonext.extensions

import com.github.oneandolaf.jsonext.testutils.JSONGenerators
import com.github.oneandolaf.jsonext.testutils.shouldBeSimilarTo
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.map
import org.json.JSONArray
import org.json.JSONObject


class JSONObjectExtensionsTests : FunSpec({

    data class PutIfAbsentData<T>(
        val putValue: T,
        val result: Any? = putValue
    )

    fun <T> runPutIfAbsentTest(data: PutIfAbsentData<T>, func: JSONObject.(key: String, value: T) -> Any) {
        val nullObj = JSONObject()

        nullObj.func("a", data.putValue) shouldBeSameInstanceAs nullObj
        nullObj shouldBeSimilarTo JSONObject().put("a", data.result)

        for (initialValue in (JSONGenerators.values).values) {
            val obj = JSONObject().put("a", initialValue)

            obj.func("a", data.putValue) shouldBeSameInstanceAs obj
            obj shouldBeSimilarTo JSONObject().put("a", initialValue)
        }


    }

    context("putIfAbsent Boolean") {
        checkAll(
            JSONGenerators.bools.map(::PutIfAbsentData)
        ) {
            runPutIfAbsentTest(it, JSONObject::putIfAbsent)
        }
    }


    context("putIfAbsent Collection") {
        checkAll(JSONGenerators.arraysAsLists.map {
            PutIfAbsentData(it, JSONArray(it))
        }) {
            runPutIfAbsentTest(it, JSONObject::putIfAbsent)
        }
    }

    context("putIfAbsent Double") {
        checkAll(JSONGenerators.doubles.map(::PutIfAbsentData)) {
            runPutIfAbsentTest(it, JSONObject::putIfAbsent)
        }
    }

    context("putIfAbsent Float") {
        checkAll(JSONGenerators.floats.map(::PutIfAbsentData)) {
            runPutIfAbsentTest(it, JSONObject::putIfAbsent)
        }
    }

    context("putIfAbsent Int") {
        checkAll(JSONGenerators.ints.map(::PutIfAbsentData)) {
            runPutIfAbsentTest(it, JSONObject::putIfAbsent)
        }
    }

    context("putIfAbsent Long") {
        checkAll(JSONGenerators.longs.map(::PutIfAbsentData)) {
            runPutIfAbsentTest(it, JSONObject::putIfAbsent)
        }
    }

    context("putIfAbsent Map") {
        checkAll(JSONGenerators.objectsAsMaps.map {
            PutIfAbsentData(it, JSONObject(it))
        }) {
            runPutIfAbsentTest(it, JSONObject::putIfAbsent)
        }
    }

    context("putIfAbsent Any") {
        checkAll(
            (JSONGenerators.values).map(::PutIfAbsentData)
        ) {
            runPutIfAbsentTest(it, JSONObject::putIfAbsent)
        }
    }

})