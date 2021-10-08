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

import com.github.oneandolaf.jsonext.TestEnum
import org.json.JSONObject
import spock.lang.Specification

class ReadOnlyJSONObjectSpec extends Specification {

    def "getOrNull"() {
        given:
        def src = new JSONObject("""
{
    "int": 1,
    "str": "foo",
    "obj": {"sub": 2},
    "arr": [3],
    "null": null
}
""")
        def readOnly = new ReadOnlyJSONObject(src)

        when:
        def intVal = readOnly.getOrNull("int")
        def strVal = readOnly.getOrNull("str")
        def objVal = readOnly.getOrNull("obj")
        def arrVal = readOnly.getOrNull("arr")
        def nullVal = readOnly.getOrNull("null")
        def missingVal = readOnly.getOrNull("missing")

        then:
        intVal == 1
        strVal == "foo"
        assert objVal instanceof ReadOnlyJSONObject
        objVal.getOrNull("sub") == 2
        assert arrVal instanceof ReadOnlyJSONArray
        arrVal.getOrNull(0) == 3
        nullVal === JSONObject.NULL
        missingVal === null
    }

    def "getOrElse"() {
        given:
        def src = new JSONObject("""
{
    "int": 1,
    "str": "foo",
    "obj": {"sub": 2},
    "arr": [3],
    "null": null
}
""")
        def readOnly = new ReadOnlyJSONObject(src)

        when:
        def intVal = readOnly.getOrElse("int") { 2 }
        def strVal = readOnly.getOrElse("str") { "bar" }
        def objVal = readOnly.getOrElse("obj") { "error" }
        def arrVal = readOnly.getOrElse("arr") { "error" }
        def nullVal = readOnly.getOrElse("null") { "error" }
        def missingVal = readOnly.getOrElse("missing") { "a string" }

        then:
        intVal == 1
        strVal == "foo"
        assert objVal instanceof ReadOnlyJSONObject
        objVal.getOrNull("sub") == 2
        assert arrVal instanceof ReadOnlyJSONArray
        arrVal.getOrNull(0) == 3
        nullVal === JSONObject.NULL
        missingVal == "a string"

        when:
        readOnly.getOrElse("missing") { throw new IllegalArgumentException() }

        then:
        thrown IllegalArgumentException
    }

    def "getEnumOrNull"() {
        given:
        def src = new JSONObject("""
{
    "a": "VAL2",
    "b": "invalid"
}
""")
        def readOnly = new ReadOnlyJSONObject(src)

        when:
        def a = readOnly.getEnumOrNull(TestEnum, "a")
        def b = readOnly.getEnumOrNull(TestEnum, "b")
        def c = readOnly.getEnumOrNull(TestEnum, "c")

        then:
        a == TestEnum.VAL2
        b === null
        c === null
    }

    def "getEnumOrDefault"() {
        given:
        def src = new JSONObject("""
{
    "a": "VAL2",
    "b": "invalid"
}
""")
        def readOnly = new ReadOnlyJSONObject(src)

        when:
        def a = readOnly.getEnumOrDefault(TestEnum, "a", TestEnum.VAL3)
        def b = readOnly.getEnumOrDefault(TestEnum, "b", TestEnum.VAL1)
        def c = readOnly.getEnumOrDefault(TestEnum, "c", TestEnum.VAL2)

        then:
        a == TestEnum.VAL2
        b == TestEnum.VAL1
        c == TestEnum.VAL2
    }

    def "getEnumOrElse"() {
        given:
        def src = new JSONObject("""
{
    "a": "VAL2",
    "b": "invalid"
}
""")
        def readOnly = new ReadOnlyJSONObject(src)

        when:
        def a = readOnly.getEnumOrElse(TestEnum, "a") { TestEnum.VAL3}
        def b = readOnly.getEnumOrElse(TestEnum, "b") { TestEnum.VAL1 }
        def c = readOnly.getEnumOrElse(TestEnum, "c") { TestEnum.VAL2 }

        then:
        a == TestEnum.VAL2
        b == TestEnum.VAL1
        c == TestEnum.VAL2
    }

    def "getBooleanOrNull"() {
        given:
        def src = new JSONObject("""
{
    "a": true,
    "b": false,
    "c": "noBoolean"
}
""")
        def readOnly = new ReadOnlyJSONObject(src)

        when:
        def a = readOnly.getBooleanOrNull("a")
        def b = readOnly.getBooleanOrNull("b")
        def c = readOnly.getBooleanOrNull("c")
        def missing = readOnly.getBooleanOrNull("missing")

        then:
        a === true
        b === false
        c === null
        missing === null
    }

    def "getBooleanOrDefault"() {
        given:
        def src = new JSONObject("""
{
    "a": true,
    "b": false,
    "c": "noBoolean"
}
""")
        def readOnly = new ReadOnlyJSONObject(src)

        when:
        def a = readOnly.getBooleanOrDefault("a", false)
        def b = readOnly.getBooleanOrDefault("b", true)
        def c = readOnly.getBooleanOrDefault("c", true)
        def missing = readOnly.getBooleanOrDefault("missing", false)

        then:
        a === true
        b === false
        c === true
        missing === false
    }

    def "getBooleanOrTrue"() {
        given:
        def src = new JSONObject("""
{
    "a": true,
    "b": false,
    "c": "noBoolean"
}
""")
        def readOnly = new ReadOnlyJSONObject(src)

        when:
        def a = readOnly.getBooleanOrTrue("a")
        def b = readOnly.getBooleanOrTrue("b")
        def c = readOnly.getBooleanOrTrue("c")
        def missing = readOnly.getBooleanOrTrue("missing")

        then:
        a === true
        b === false
        c === true
        missing === true
    }

    def "getBooleanOrFalse"() {
        given:
        def src = new JSONObject("""
{
    "a": true,
    "b": false,
    "c": "noBoolean"
}
""")
        def readOnly = new ReadOnlyJSONObject(src)

        when:
        def a = readOnly.getBooleanOrFalse("a")
        def b = readOnly.getBooleanOrFalse("b")
        def c = readOnly.getBooleanOrFalse("c")
        def missing = readOnly.getBooleanOrFalse("missing")

        then:
        a === true
        b === false
        c === false
        missing === false
    }

    def "getBooleanOrElse"() {
        given:
        def src = new JSONObject("""
{
    "a": true,
    "b": false,
    "c": "noBoolean"
}
""")
        def readOnly = new ReadOnlyJSONObject(src)

        when:
        boolean a = readOnly.getBooleanOrElse("a") { false }
        boolean b = readOnly.getBooleanOrElse("b") { true }
        boolean c = readOnly.getBooleanOrElse("c") { true }
        boolean missing = readOnly.getBooleanOrElse("missing") { false }

        then:
        a === true
        b === false
        c === true
        missing === false
    }



}
