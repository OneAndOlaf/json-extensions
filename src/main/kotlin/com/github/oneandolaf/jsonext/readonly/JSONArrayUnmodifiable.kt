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
import java.lang.reflect.Field

/**
 * Unmodifiable subclass of [JSONArray]. Instances are backed by a modifiable array, so any changes in the original
 * are reflected in the unmodifiable view.
 *
 * This class should be used with caution, especially if using a newer version of JSON-Java than this library was
 * built with. See [ReadOnlyJSONArray] for a safer alternative.
 */
class JSONArrayUnmodifiable(private val src: JSONArray) : JSONArray() {

    init {
        try {
            // set the internal ArrayList because org.json likes to access the private fields of other objects
            // for instance, similar would not be symmetric if we don't do this

            val field: Field? = JSONArray::class.java.getDeclaredField("myArrayList")

            field?.let {
                it.isAccessible = true
                val list = it.get(src)
                it.set(this, list)
            }
        } catch (e: Exception) {
            // do nothing for now, it was worth a try
        }
    }

    private fun unsupported() = UnsupportedOperationException("Attempt to write to a read-only object")

    /**
     * Returns an iterator for this array.
     *
     * While the iterator is typed as mutable, attempting to mutate the array through it will throw an exception.
     */
    override fun iterator(): MutableIterator<*> {
        return IteratorUnmodifiable(this)
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

    override fun getJSONObject(index: Int): JSONObjectUnmodifiable {
        return when (val data = src.getJSONObject(index)) {
            is JSONObjectUnmodifiable -> data
            else -> JSONObjectUnmodifiable(data)
        }
    }

    override fun getJSONArray(index: Int): JSONArrayUnmodifiable {
        return when (val data = src.getJSONArray(index)) {
            is JSONArrayUnmodifiable -> data
            else -> JSONArrayUnmodifiable(data)
        }
    }

    override fun opt(index: Int): Any? {
        return when (val data = src.opt(index)) {
            is JSONObjectUnmodifiable -> data
            is JSONArrayUnmodifiable -> data
            is JSONObject -> JSONObjectUnmodifiable(data)
            is JSONArray -> JSONArrayUnmodifiable(data)
            else -> data
        }
    }

    override fun optJSONArray(index: Int): JSONArrayUnmodifiable? {
        return when (val data = opt(index)) {
            is JSONArrayUnmodifiable -> data
            is JSONArray -> JSONArrayUnmodifiable(data)
            else -> null
        }
    }

    override fun optJSONObject(index: Int): JSONObjectUnmodifiable? {
        return when (val data = opt(index)) {
            is JSONObjectUnmodifiable -> data
            is JSONObject -> JSONObjectUnmodifiable(data)
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
    override fun put(value: Collection<*>?): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(value: Map<*, *>?): JSONArray {
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
    override fun put(index: Int, value: Collection<*>?): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun put(index: Int, value: Map<*, *>?): JSONArray {
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
    override fun putAll(collection: Collection<*>?): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun putAll(iter: Iterable<*>?): JSONArray {
        throw unsupported()
    }

    /**
     * Unsupported for read-only objects.
     */
    override fun remove(index: Int): Any {
        throw unsupported()
    }


    override fun similar(other: Any?): Boolean {
        return if (other is JSONArrayUnmodifiable) {
            // this is necessary because similar just accesses the other object's internal ArrayList, which is empty
            // for read-only objects
            src.similar(other.src)
        } else {
            src.similar(other)
        }
    }

    override fun toString(): String = src.toString()

    override fun toString(indentFactor: Int): String = src.toString(indentFactor)

    override fun write(writer: Writer?): Writer = src.write(writer)

    override fun write(writer: Writer?, indentFactor: Int, indent: Int): Writer =
        src.write(writer, indentFactor, indent)

    override fun toList(): MutableList<Any> = src.toList()

    override fun isEmpty(): Boolean = src.isEmpty

    private class IteratorUnmodifiable(array: JSONArrayUnmodifiable) : MutableIterator<Any> {

        private val srcIt = array.src.iterator()

        override fun hasNext(): Boolean = srcIt.hasNext()

        override fun next(): Any {
            val nextItem = srcIt.next()

            return when {
                nextItem is JSONObject && nextItem !is JSONObjectUnmodifiable -> JSONObjectUnmodifiable(nextItem)
                nextItem is JSONArray && nextItem !is JSONArrayUnmodifiable -> JSONArrayUnmodifiable(nextItem)
                else -> nextItem
            }
        }

        override fun remove() {
            throw UnsupportedOperationException("Attempt to write to a read-only object")
        }

    }
}