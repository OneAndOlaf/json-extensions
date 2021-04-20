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
import spock.lang.Specification

class ReadOnlyJsonArraySpec extends Specification {

    def "length"() {
        given:
        def arr = new JSONArray()
        def ro = new ReadOnlyJsonArray(arr)

        expect:
        arr.length() == 0
        ro.length() == 0

        when:
        arr.put(1)

        then:
        arr.length() == 1
        ro.length() == 1
    }

    def "clear"() {
        given:
        def arr = new JSONArray()
        def ro = new ReadOnlyJsonArray(arr)

        when:
        ro.clear()

        then:
        thrown(UnsupportedOperationException)

        when:
        arr.put(1)
        ro.clear()

        then:
        thrown(UnsupportedOperationException)
        ro.length() == 1
    }

    def "join"() {
        given:
        def arr = new JSONArray()
        def ro = new ReadOnlyJsonArray(arr)

        arr.put("a").put("b")

        expect:
        ro.join(",") == '"a","b"'
    }

    def "get"() {
        given:
        def arr = new JSONArray([1, "foo", new JSONObject(), new JSONArray([3])])
        def ro = new ReadOnlyJsonArray(arr)

        when:
        def el0 = ro.get(0)
        def el1 = ro.get(1)
        def el2 = ro.get(2)
        def el3 = ro.get(3)

        then:
        el0 == 1
        el1 == "foo"
        el2 instanceof ReadOnlyJsonObject
        el3 instanceof ReadOnlyJsonArray
        (el3 as ReadOnlyJsonArray).get(0) == 3
    }

    def "getJSONObject"() {
        given:
        def arr = new JSONArray([1, new JSONObject(), new ReadOnlyJsonObject(new JSONObject())])
        def ro = new ReadOnlyJsonArray(arr)

        when:
        ro.getJSONObject(0)

        then:
        thrown(JSONException)

        expect:
        ro.getJSONObject(1) instanceof ReadOnlyJsonObject
        ro.getJSONObject(2) instanceof ReadOnlyJsonObject
    }

    def "getJSONArray"() {
        given:
        def arr = new JSONArray([1, new JSONArray(), new ReadOnlyJsonArray(new JSONArray())])
        def ro = new ReadOnlyJsonArray(arr)

        when:
        ro.getJSONArray(0)
        then:
        thrown(JSONException)

        expect:
        ro.getJSONArray(1) instanceof ReadOnlyJsonArray
        ro.getJSONArray(2) instanceof ReadOnlyJsonArray
    }

    def "opt"() {
        given:
        def arr = new JSONArray([1, "foo", new JSONObject(), new JSONArray([3])])
        def ro = new ReadOnlyJsonArray(arr)

        when:
        def el0 = ro.opt(0)
        def el1 = ro.opt(1)
        def el2 = ro.opt(2)
        def el3 = ro.opt(3)
        def el4 = ro.opt(4)

        then:
        el0 == 1
        el1 == "foo"
        el2 instanceof ReadOnlyJsonObject
        el3 instanceof ReadOnlyJsonArray
        (el3 as ReadOnlyJsonArray).get(0) == 3
        el4 == null
    }

    def "optJSONArray"() {
        given:
        def arr = new JSONArray([1, new JSONArray(), new ReadOnlyJsonArray(new JSONArray())])
        def ro = new ReadOnlyJsonArray(arr)

        expect:
        ro.optJSONArray(0) == null
        ro.optJSONArray(1) instanceof ReadOnlyJsonArray
        ro.optJSONArray(2) instanceof ReadOnlyJsonArray
        ro.optJSONArray(3) == null
    }

    def "optJSONObject"() {
        given:
        def arr = new JSONArray([1, new JSONObject(), new ReadOnlyJsonObject(new JSONObject())])
        def ro = new ReadOnlyJsonArray(arr)

        expect:
        ro.optJSONObject(0) == null
        ro.optJSONObject(1) instanceof ReadOnlyJsonObject
        ro.optJSONObject(2) instanceof ReadOnlyJsonObject
        ro.optJSONObject(3) == null
    }

    def "put end"() {
        given:
        def arr = new JSONArray()
        def ro = new ReadOnlyJsonArray(arr)

        when:
        ro.put(new Object())
        then:
        thrown(UnsupportedOperationException)

        when:
        ro.put(true)
        then:
        thrown(UnsupportedOperationException)

        when:
        ro.put(1.0)
        then:
        thrown(UnsupportedOperationException)

        when:
        ro.put(1.0f)
        then:
        thrown(UnsupportedOperationException)

        when:
        ro.put(1)
        then:
        thrown(UnsupportedOperationException)

        when:
        ro.put(1l)
        then:
        thrown(UnsupportedOperationException)

        when:
        ro.put([])
        then:
        thrown(UnsupportedOperationException)

        when:
        ro.put([:])
        then:
        thrown(UnsupportedOperationException)
    }

    def "put index"() {
        given:
        def arr = new JSONArray([1, 2])
        def ro = new ReadOnlyJsonArray(arr)

        when:
        ro.put(0, new Object())
        then:
        thrown(UnsupportedOperationException)

        when:
        ro.put(0, true)
        then:
        thrown(UnsupportedOperationException)

        when:
        ro.put(0, 1.0)
        then:
        thrown(UnsupportedOperationException)

        when:
        ro.put(0, 1.0f)
        then:
        thrown(UnsupportedOperationException)

        when:
        ro.put(0, 1)
        then:
        thrown(UnsupportedOperationException)

        when:
        ro.put(0, 1l)
        then:
        thrown(UnsupportedOperationException)

        when:
        ro.put(0, [])
        then:
        thrown(UnsupportedOperationException)

        when:
        ro.put(0, [:])
        then:
        thrown(UnsupportedOperationException)
    }

    def "putAll"() {
        given:
        def arr = new JSONArray()
        def ro = new ReadOnlyJsonArray(arr)

        when:
        ro.putAll(new Object())
        then:
        thrown(UnsupportedOperationException)

        when:
        ro.putAll(new JSONArray())
        then:
        thrown(UnsupportedOperationException)

        when:
        ro.putAll([])
        then:
        thrown(UnsupportedOperationException)

        when:
        ro.putAll([] as Iterable<?>)
        then:
        thrown(UnsupportedOperationException)
    }

    def "remove"() {
        given:
        def arr = new JSONArray()
        def ro = new ReadOnlyJsonArray(arr)

        when:
        ro.remove(0)
        then:
        thrown(UnsupportedOperationException)
    }

    def "similar"() {
        given:
        def arr1 = new JSONArray()
        def ro1 = new ReadOnlyJsonArray(arr1)
        def arr2 = new JSONArray()
        def ro2 = new ReadOnlyJsonArray(arr2)

        expect:
        ro1.similar(arr1)
        ro1.similar(arr2)
        ro1.similar(ro2)
        ro2.similar(arr1)
        ro2.similar(arr2)
        ro2.similar(ro1)
        arr1.similar(ro1)
        arr1.similar(arr2)
        arr1.similar(ro2)

        when:
        arr1.put(1)

        then:
        ro1.similar(arr1)
        !ro1.similar(arr2)
        !ro1.similar(ro2)
        !ro2.similar(arr1)
        ro2.similar(arr2)
        !ro2.similar(ro1)
        arr1.similar(ro1)
        !arr1.similar(arr2)
        !arr1.similar(ro2)

        when:
        arr2.put(1)

        then:
        ro1.similar(arr1)
        ro1.similar(arr2)
        ro1.similar(ro2)
        ro2.similar(arr1)
        ro2.similar(arr2)
        ro2.similar(ro1)
        arr1.similar(ro1)
        arr1.similar(arr2)
        arr1.similar(ro2)

    }

}
