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
@file:JvmName("JSONArrayExtensions")

package com.github.oneandolaf.jsonext.extensions

import com.github.oneandolaf.jsonext.readonly.JSONArrayUnmodifiable
import com.github.oneandolaf.jsonext.readonly.ReadOnlyJSONArray
import org.json.JSONArray
import org.json.JSONObject

/**
 * Creates a deep copy of this array.
 */
fun JSONArray.deepCopy(): JSONArray {
    val copy = JSONArray()

    for (item in this) {
        when (item) {
            is JSONObject -> {
                copy.put(item.deepCopy())
            }
            is JSONArray -> {
                copy.put(item.deepCopy())
            }
            else -> {
                copy.put(item)
            }
        }
    }

    return copy
}

/**
 * Wraps this [JSONArray] into an unmodifiable subclass.
 *
 * Consider using [ReadOnlyJSONArray] for a more typesafe alternative.
 *
 * The object returned is backed by this one: future changes to this object will
 * be reflected in the one returned.
 */
fun JSONArray.asUnmodifiable(): JSONArrayUnmodifiable {
    return this as? JSONArrayUnmodifiable ?: JSONArrayUnmodifiable(this)
}

/**
 * Wraps this [JSONArray] into a [ReadOnlyJSONArray].
 *
 * The array returned is backed by this one: future changes to this array will
 * be reflected in the one returned.
 */
fun JSONArray.asReadOnly(): ReadOnlyJSONArray = ReadOnlyJSONArray.create(this)

/**
 * Creates a [ReadOnlyJSONArray] from the current state of this array.
 *
 * The array returned is _not_ backed by this one: the array returned will
 * never change after creation.
 */
fun JSONArray.readOnlySnapshot(): ReadOnlyJSONArray {
    return ReadOnlyJSONArray.snapshot(this)
}