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

package com.github.oneandolaf.jsonext.testutils

import com.github.oneandolaf.jsonext.util.JSONSimilar
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

fun beSimilarTo(other: Any?) = object : Matcher<Any?> {

    override fun test(value: Any?): MatcherResult {
        return MatcherResult(
            JSONSimilar.similar(value, other),
            "$value should be similar to $other",
            "$value should not be similar to $other"
        )

    }
}

infix fun Any?.shouldBeSimilarTo(other: Any?) = this should beSimilarTo(other)
infix fun Any?.shouldNotBeSimilarTo(other: Any?) = this shouldNot beSimilarTo(other)

fun beSimilarStringAs(other: Any?) = object : Matcher<String?> {

    override fun test(value: String?): MatcherResult {
        return MatcherResult(
            when (other) {
                null -> value == null
                is JSONObject -> try {
                    JSONObject(value).similar(other)
                } catch (ex: JSONException) {
                    false
                }
                is JSONArray -> try {
                    JSONArray(value).similar(other)
                } catch (ex: JSONException) {
                    false
                }
                else -> value == other.toString()
            },
            "$value should be similar to the String of $other",
            "$value should not be similar to the String of $other"
        )
    }
}

infix fun String?.shouldBeSimilarStringAs(other: Any?) = this should beSimilarStringAs(other)
infix fun String?.shouldNotBeSimliarStringAs(other: Any?) = this shouldNot beSimilarStringAs(other)