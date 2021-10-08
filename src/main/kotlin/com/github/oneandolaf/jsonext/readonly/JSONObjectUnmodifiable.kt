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
import org.json.JSONException
import org.json.JSONObject
import java.io.Writer
import java.util.*

/**
 * Unmodifiable subclass of [JSONObject]. Instances are backed by a modifiable object, so any changes in the original
 * are reflected in the unmodifiable view.
 */
class JSONObjectUnmodifiable(private val src: JSONObject) : JSONObject() {

    private fun unsupported() = UnsupportedOperationException("Attempt to write to a read-only object")

    /**
     * Unsupported for read-only objects.
     */
    override fun accumulate(key: String?, value: Any?): JSONObject {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun append(key: String?, value: Any?): JSONObject {
        throw unsupported()
    }

    override fun has(key: String?): Boolean {
        return src.has(key)
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun increment(key: String?): JSONObject {
        throw unsupported()
    }

    /**
     * Gets an iterator over the keys of the object. Attempts to change the object will result in an
     * [UnsupportedOperationException].
     *
     * @return an iterator over the object's keys
     */
    override fun keys(): Iterator<String> {
        return keySet().iterator()
    }

    /**
     * Gets the set of keys of the object. Changes in the object are reflected in the set, but attempts to change the
     * object through the set will result in an [UnsupportedOperationException].
     *
     * @return the keys of the object
     */
    override fun keySet(): Set<String> {
        return Collections.unmodifiableSet(src.keySet())
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

    override fun isEmpty(): Boolean {
        return src.isEmpty
    }

    /**
     * Produces an array containing the names of the elements of this object. The array returned is mutable, but
     * changing it does not affect this object.
     *
     * @return an array containing the keys, or `null` if the object is empty
     */
    override fun names(): JSONArray? {
        return src.names()
    }

    override fun opt(key: String?): Any? {
        return when (val value = src.opt(key)) {
            is JSONObjectUnmodifiable -> value
            is JSONArrayUnmodifiable -> value
            is JSONObject -> JSONObjectUnmodifiable(value)
            is JSONArray -> JSONArrayUnmodifiable(value)
            else -> value
        }
    }

    override fun optJSONObject(key: String?): JSONObjectUnmodifiable? {
        return when (val obj = opt(key)) {
            is JSONObjectUnmodifiable -> obj
            is JSONObject -> JSONObjectUnmodifiable(obj)
            else -> null
        }
    }

    override fun optJSONArray(key: String?): JSONArrayUnmodifiable? {
        return when (val obj = opt(key)) {
            is JSONArrayUnmodifiable -> obj
            is JSONArray -> JSONArrayUnmodifiable(obj)
            else -> null
        }
    }

    override fun get(key: String?): Any {
        when (key) {
            null -> throw JSONException("Null key.")
            else -> return opt(key) ?: throw JSONException("JSONObject[" + quote(key) + "] not found.")
        }
    }

    override fun getJSONObject(key: String?): JSONObjectUnmodifiable {
        return when (val obj = src.getJSONObject(key)) {
            is JSONObjectUnmodifiable -> obj
            else -> JSONObjectUnmodifiable(obj)
        }
    }

    override fun getJSONArray(key: String?): JSONArrayUnmodifiable {
        return when (val obj = src.getJSONArray(key)) {
            is JSONArrayUnmodifiable -> obj
            else -> JSONArrayUnmodifiable(obj)
        }
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(key: String?, value: Any?): JSONObject {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(key: String?, value: Boolean): JSONObject {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(key: String?, value: Double): JSONObject {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(key: String?, value: Float): JSONObject {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(key: String?, value: Int): JSONObject {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(key: String?, value: Long): JSONObject {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(key: String?, value: MutableCollection<*>?): JSONObject {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(key: String?, value: MutableMap<*, *>?): JSONObject {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun putOnce(key: String?, value: Any?): JSONObject {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun putOpt(key: String?, value: Any?): JSONObject {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun remove(key: String?): Any {
        throw unsupported()
    }

    override fun similar(other: Any?): Boolean {
        return src.similar(other)
    }


    override fun toString(): String {
        return src.toString()
    }

    override fun toString(indentFactor: Int): String {
        return src.toString(indentFactor)
    }

    override fun write(writer: Writer?): Writer {
        return src.write(writer)
    }

    override fun write(writer: Writer?, indentFactor: Int, indent: Int): Writer {
        return src.write(writer, indentFactor, indent)
    }

    override fun toMap(): MutableMap<String, Any> {
        return src.toMap()
    }


}