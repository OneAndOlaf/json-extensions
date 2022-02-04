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

import io.kotest.property.Exhaustive
import io.kotest.property.exhaustive.exhaustive

/**
 * Allows safe creation of an exhaustive generator for data that is mutable.
 */
fun <T> mutableExhaustive(generator: () -> Exhaustive<T>): Exhaustive<T> {
    return object : Exhaustive<T>() {
        override val values: List<T>
            get() = generator().values

    }
}

fun <A, B> Exhaustive<A>.mapMutable(f: (A) -> B): Exhaustive<B> = object : Exhaustive<B>() {
    override val values: List<B>
        get() = this@mapMutable.values.map { f(it) }
}

operator fun <A : B, B> Exhaustive<A>.minus(other: Exhaustive<B>): Exhaustive<A> {
    return (values.filter { it !in other.values }).exhaustive()
}

/**
 * Alternative to * that does not require `other`s type to be related to the type of `this`.
 */
infix fun <A, B> Exhaustive<A>.cross(other: Exhaustive<B>): Exhaustive<Pair<A, B>> {
    val values = this.values.flatMap { a ->
        other.values.map { b ->
            Pair(a, b)
        }
    }
    return values.exhaustive()
}