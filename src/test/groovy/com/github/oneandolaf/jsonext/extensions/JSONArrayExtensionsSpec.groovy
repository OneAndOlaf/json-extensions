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

package com.github.oneandolaf.jsonext.extensions

import org.json.JSONArray
import spock.lang.Specification

class JSONArrayExtensionsSpec extends Specification {

    def "deepCopy"() {
        given:
        def arr = new JSONArray([
                'a',
                1,
                1.2,
                true,
                ['foo', 'bar'],
                [
                        a: 'b'
                ]
        ])

        when:
        def copy = arr.deepCopy()

        then:
        copy != arr
        copy.similar(arr)

        copy[4] != arr[4]
        copy[5] != arr[5]

    }

    def "toBigDecimalList"() {
        given:
        def arr = new JSONArray([BigDecimal.valueOf(1), BigDecimal.valueOf(2)])

        when:
        def list = arr.toBigDecimalList()

        then:
        list instanceof List
        list.size() == 2
        list[0] == BigDecimal.valueOf(1)
        list[1] == BigDecimal.valueOf(2)

        when:
        arr.put(BigDecimal.valueOf(3))

        then:
        list.size() == 2

        when:
        list.add(BigDecimal.valueOf(4))

        then:
        arr.similar(new JSONArray([BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(3)]))
    }

}
