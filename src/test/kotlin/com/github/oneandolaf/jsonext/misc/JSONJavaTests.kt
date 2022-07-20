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

package com.github.oneandolaf.jsonext.misc

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Field

/**
 * Tests to see if JSON-Java actually works as expected.
 */
class JSONJavaTests : FunSpec({

    /**
     * Check whether getInt will actually allow non-integer numbers, Strings of integers, but not Strings of
     * non-integers.
     */
    test("getInt") {

        JSONObject().put("a", 1.0).getInt("a") shouldBe 1
        JSONObject().put("a", "1").getInt("a") shouldBe 1

        shouldThrow<JSONException> {
            JSONObject().put("a", "1.0").getInt("a")
        }
    }

    /**
     * Checks whether the reflective access to [JSONArray]'s internal array succeeds.
     *
     * This is not a perfect indicator, but it should at least detect if the field is renamed or something.
     */
    test("JSONArray field is accessible") {
        shouldNotThrow<Throwable> {
            val field: Field? = JSONArray::class.java.getDeclaredField("myArrayList")

            field?.let {
                it.isAccessible = true
                it.get(JSONArray()).shouldBeInstanceOf<ArrayList<*>>()
            }
        }
    }

})