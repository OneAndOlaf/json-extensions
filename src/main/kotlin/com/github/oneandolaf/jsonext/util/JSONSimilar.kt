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
import org.json.JSONArray
import org.json.JSONObject

/**
 * Utility to compare various implementations of JSON structures.
 */
object JSONSimilar {

    /**
     * Checks whether two objects are similar structurally.
     *
     * This method will correctly compare any combination of [ReadOnlyJSONObject]s, [ReadOnlyJSONArray]s, plain
     * [JSONObject]s, and plain [JSONArray]s with each other.
     *
     * If none of the parameters are JSON structures, they will be compared using standard equality.
     */
    @JvmStatic
    fun similar(a: Any?, b: Any?): Boolean {
        return when {
            a === b -> true
            a == null || b == null -> false
            a is ReadOnlyJSONObject -> similarToReadOnlyObject(a, b)
            b is ReadOnlyJSONObject -> similarToReadOnlyObject(b, a)
            a is ReadOnlyJSONArray -> similarToReadOnlyArray(a, b)
            b is ReadOnlyJSONArray -> similarToReadOnlyArray(b, a)
            a is JSONObject -> a.similar(b)
            a is JSONArray -> a.similar(b)
            // no need to check whether b is a JSONObject or JSONArray because if so,
            // some method above would have returned true already
            else -> a == b
        }
    }

    private fun similarToReadOnlyObject(a: ReadOnlyJSONObject, b: Any): Boolean {
        return when (b) {
            is JSONObject -> a.similarToPlainObject(b)
            else -> a.similar(b)
        }
    }

    private fun similarToReadOnlyArray(a: ReadOnlyJSONArray, b: Any): Boolean {
        return when (b) {
            is JSONArray -> a.similarToPlainArray(b)
            else -> a.similar(b)
        }
    }

}