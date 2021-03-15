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

import org.json.JSONArray
import org.json.JSONObject
import java.io.Writer
import java.lang.UnsupportedOperationException
import org.json.JSONException




class ReadOnlyJsonArray(private val src: JSONArray) : JSONArray() {

    private fun unsupported() = UnsupportedOperationException("Attempt to write to a read-only object")

    override fun iterator(): MutableIterator<Any> {
        TODO()
    }

    override fun length(): Int {
        return src.length()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun clear() {
        throw unsupported()
    }

    override fun join(separator: String?): String {
        return src.join(separator)
    }

    override fun get(index: Int): Any {
        return opt(index) ?: throw JSONException("JSONArray[$index] not found.")
    }

    override fun getJSONObject(index: Int): JSONObject {
        return when (val data = src.getJSONObject(index)) {
            is ReadOnlyJsonObject -> data
            else -> ReadOnlyJsonObject(data)
        }
    }

    override fun getJSONArray(index: Int): JSONArray {
        return when (val data = src.getJSONArray(index)) {
            is ReadOnlyJsonArray -> data
            else -> ReadOnlyJsonArray(data)
        }
    }

    override fun opt(index: Int): Any? {
        return when (val data = src.opt(index)) {
            is ReadOnlyJsonObject -> data
            is ReadOnlyJsonArray -> data
            is JSONObject -> ReadOnlyJsonObject(data)
            is JSONArray -> ReadOnlyJsonArray(data)
            else -> data
        }
    }

    override fun optJSONArray(index: Int): ReadOnlyJsonArray? {
        return when (val data = opt(index)) {
            is ReadOnlyJsonArray -> data
            is JSONArray -> ReadOnlyJsonArray(data)
            else -> null
        }
    }

    override fun optJSONObject(index: Int): ReadOnlyJsonObject? {
        return when (val data = opt(index)) {
            is ReadOnlyJsonObject -> data
            is JSONObject -> ReadOnlyJsonObject(data)
            else -> null
        }
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(value: Any?): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(value: Boolean): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(value: Double): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(value: Float): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(value: Int): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(value: Long): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(value: MutableCollection<*>?): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(value: MutableMap<*, *>?): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(index: Int, value: Any?): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(index: Int, value: Boolean): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(index: Int, value: Double): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(index: Int, value: Float): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(index: Int, value: Int): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(index: Int, value: Long): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(index: Int, value: MutableCollection<*>?): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(index: Int, value: MutableMap<*, *>?): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun putAll(array: Any?): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun putAll(array: JSONArray?): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun putAll(collection: MutableCollection<*>?): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun putAll(iter: MutableIterable<*>?): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun remove(index: Int): Any {
        throw unsupported()
    }



    override fun similar(other: Any?): Boolean {
        return src.similar(other)
    }

    override fun toString(): String = src.toString()

    override fun toString(indentFactor: Int): String = src.toString(indentFactor)

    override fun write(writer: Writer?): Writer = src.write(writer)

    override fun write(writer: Writer?, indentFactor: Int, indent: Int): Writer =
        src.write(writer, indentFactor, indent)

    override fun toList(): MutableList<Any> = src.toList()

    override fun isEmpty(): Boolean = src.isEmpty


}