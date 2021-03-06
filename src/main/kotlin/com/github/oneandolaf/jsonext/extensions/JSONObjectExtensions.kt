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
@file:JvmName("JSONObjectExtensions")

package com.github.oneandolaf.jsonext.extensions

import com.github.oneandolaf.jsonext.readonly.JSONObjectUnmodifiable
import com.github.oneandolaf.jsonext.readonly.ReadOnlyJSONObject
import org.json.JSONArray
import org.json.JSONObject

/**
 * Creates a deep copy of this object.
 */
fun JSONObject.deepCopy(): JSONObject {
    val copy = JSONObject()

    for (key in keySet()) {
        when (val value = opt(key)) {
            is JSONObject -> {
                copy.put(key, value.deepCopy())
            }
            is JSONArray -> {
                copy.put(key, value.deepCopy())
            }
            else -> {
                copy.put(key, value)
            }
        }
    }

    return copy
}

/**
 * Wraps this [JSONObject] into an unmodifiable subclass.
 *
 * Consider using [ReadOnlyJSONObject] for a more typesafe alternative.
 *
 * The object returned is backed by this one: future changes to this object will
 * be reflected in the one returned.
 */
fun JSONObject.asUnmodifiable(): JSONObjectUnmodifiable {
    return this as? JSONObjectUnmodifiable ?: JSONObjectUnmodifiable(this)
}

/**
 * Wraps this [JSONObject] into a [ReadOnlyJSONObject].
 *
 * The object returned is backed by this one: future changes to this object will
 * be reflected in the one returned.
 */
fun JSONObject.asReadOnly(): ReadOnlyJSONObject {
    return ReadOnlyJSONObject.create(this)
}

/**
 * Creates a [ReadOnlyJSONObject] from the current state of this object.
 *
 * The object returned is _not_ backed by this one: the object returned will
 * never change after creation.
 */
fun JSONObject.readOnlySnapshot(): ReadOnlyJSONObject {
    return ReadOnlyJSONObject.snapshot(this)
}
