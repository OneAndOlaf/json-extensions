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

@file:JvmName("JSONGlobals")

package com.github.oneandolaf.jsonext.extensions

import org.json.JSONArray
import org.json.JSONObject

/**
 * Returns a [JSONArray] containing the passed items.
 *
 * If any items are collections or maps, they will be wrapped into [JSONArray]s or [JSONObject]s.
 *
 * @param items the items to add to the array
 * @return an array containing the items
 */
fun jsonArrayOf(vararg items: Any): JSONArray = JSONArray(listOf(*items))

/**
 * Returns a [JSONObject] containing the passed key-value pairs.
 *
 * If any items are collections or maps, they will be wrapped into [JSONArray]s or [JSONObject]s.
 *
 * @param pairs the key-value pairs to add to the object
 * @return an object containing the items
 */
fun jsonObjectOf(vararg pairs: Pair<String, Any>): JSONObject = JSONObject(mapOf(*pairs))

/**
 * Creates the String representation of a JSON Pointer (see [RFC 6901](https://datatracker.ietf.org/doc/html/rfc6901)) from a list of property names.
 *
 * The property names should _not_ already be JSON-Pointer-escaped.
 */
fun jsonPointerOf(vararg elements: String): String {
    return jsonPointerOf(elements.asList())
}

/**
 * Creates the String representation of a JSON Pointer (see [RFC 6901](https://datatracker.ietf.org/doc/html/rfc6901)) from a list of property names.
 *
 * The property names should _not_ already be JSON-Pointer-escaped.
 */
fun jsonPointerOf(elements: List<String>): String {
    return if (elements.isEmpty()) "#" else "#/${
        elements.joinToString("/") {
            it.replace("~", "~0").replace("/", "~1")
        }
    }"
}