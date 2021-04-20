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
import org.json.JSONObject
import spock.lang.Specification

import javax.swing.GroupLayout

import static com.github.oneandolaf.jsonext.extensions.JSONObjectExtensionsKt.*

class JSONObjectExtensionsSpec extends Specification {

    def "test putIfAbsent Boolean"() {
        given:
        def obj = new JSONObject([
                present: true,
                different: 'foo'
        ])

        List<JSONObject> returned = []

        when:
        returned += putIfAbsent(obj, 'absent', true)
        returned += putIfAbsent(obj, 'present', false)
        returned += putIfAbsent(obj, 'different', true)

        then:
        obj.getBoolean('absent')
        obj.getBoolean('present')
        obj.get('different') == 'foo'

        returned.every {it === obj }
    }

    def "test putIfAbsent Collection"() {
        given:
        def obj = new JSONObject([
                present: ['a'],
                different: 'foo'
        ])

        List<JSONObject> returned = []

        when:
        returned += putIfAbsent(obj, 'absent', ['b'])
        returned += putIfAbsent(obj, 'present', ['b'])
        returned += putIfAbsent(obj, 'different', ['b'])

        then:
        obj.getJSONArray('absent').similar(new JSONArray(['b']))
        obj.getJSONArray('present').similar(new JSONArray(['a']))
        obj.get('different') == 'foo'

        returned.every {it === obj }
    }

    def "test putIfAbsent Double"() {
        given:
        def obj = new JSONObject([
                present: 4.2,
                different: 'foo'
        ])

        List<JSONObject> returned = []

        when:
        returned += putIfAbsent(obj, 'absent', 1.2d)
        returned += putIfAbsent(obj, 'present', 1.2d)
        returned += putIfAbsent(obj, 'different', 1.2d)

        then:
        obj.getDouble('absent') == 1.2d
        obj.getDouble('present') == 4.2d
        obj.get('different') == 'foo'

        returned.every {it === obj }
    }

    def "test putIfAbsent Float"() {
        given:
        def obj = new JSONObject([
                present: 4.2f,
                different: 'foo'
        ])

        List<JSONObject> returned = []

        when:
        returned += putIfAbsent(obj, 'absent', 1.2f)
        returned += putIfAbsent(obj, 'present', 1.2f)
        returned += putIfAbsent(obj, 'different', 1.2f)

        then:
        obj.getFloat('absent') == 1.2f
        obj.getFloat('present') == 4.2f
        obj.get('different') == 'foo'

        returned.every {it === obj }
    }

    def "test putIfAbsent Int"() {
        given:
        def obj = new JSONObject([
                present: 42,
                different: 'foo'
        ])

        List<JSONObject> returned = []

        when:
        returned += putIfAbsent(obj, 'absent', 12)
        returned += putIfAbsent(obj, 'present', 12)
        returned += putIfAbsent(obj, 'different', 12)

        then:
        obj.getInt('absent') == 12
        obj.getInt('present') == 42
        obj.get('different') == 'foo'

        returned.every {it === obj }
    }

    def "test putIfAbsent Long"() {
        given:
        def obj = new JSONObject([
                present: 42l,
                different: 'foo'
        ])

        List<JSONObject> returned = []

        when:
        returned += putIfAbsent(obj, 'absent', 12l)
        returned += putIfAbsent(obj, 'present', 12l)
        returned += putIfAbsent(obj, 'different', 12l)

        then:
        obj.getLong('absent') == 12l
        obj.getLong('present') == 42l
        obj.get('different') == 'foo'

        returned.every {it === obj }
    }

    def "test putIfAbsent Map"() {
        given:
        def obj = new JSONObject([
                present: [
                    a: true
                ],
                different: 'foo'
        ])

        List<JSONObject> returned = []

        when:
        returned += putIfAbsent(obj, 'absent', [b: true])
        returned += putIfAbsent(obj, 'present', [b: true])
        returned += putIfAbsent(obj, 'different', [b: true])

        then:
        obj.getJSONObject('absent').similar(new JSONObject([b: true]))
        obj.getJSONObject('present').similar(new JSONObject([a: true]))
        obj.get('different') == 'foo'

        returned.every {it === obj }
    }

    def "test putIfAbsent Any Obj"() {
        given:
        def obj = new JSONObject([
                present: [
                        a: true
                ],
                different: 'foo'
        ])

        List<JSONObject> returned = []

        when:
        returned += putIfAbsent(obj, 'absent', new JSONObject([b: true]))
        returned += putIfAbsent(obj, 'present', new JSONObject([b: true]))
        returned += putIfAbsent(obj, 'different', new JSONObject([b: true]))

        then:
        obj.getJSONObject('absent').similar(new JSONObject([b: true]))
        obj.getJSONObject('present').similar(new JSONObject([a: true]))
        obj.get('different') == 'foo'

        returned.every {it === obj }
    }

    def "test putIfAbsent Any Array"() {
        given:
        def obj = new JSONObject([
                present: [ 'a' ],
                different: 'foo'
        ])

        List<JSONObject> returned = []

        when:
        returned += putIfAbsent(obj, 'absent', new JSONArray(['b']))
        returned += putIfAbsent(obj, 'present', new JSONArray(['b']))
        returned += putIfAbsent(obj, 'different', new JSONArray(['b']))

        then:
        obj.getJSONArray('absent').similar(new JSONArray(['b']))
        obj.getJSONArray('present').similar(new JSONArray(['a']))
        obj.get('different') == 'foo'

        returned.every {it === obj }
    }

    def "test putIfAbsent Any String"() {
        given:
        def obj = new JSONObject([
                present: 'a',
                different: false
        ])

        List<JSONObject> returned = []

        when:
        returned += putIfAbsent(obj, 'absent', 'b')
        returned += putIfAbsent(obj, 'present', 'b')
        returned += putIfAbsent(obj, 'different', 'b')

        then:
        obj.getString('absent') == 'b'
        obj.getString('present') == 'a'
        obj.get('different') == false

        returned.every {it === obj }
    }

    def "test putIfAbsent Any Null"() {
        given:
        def obj = new JSONObject([
                present: [ 'a' ]
        ])

        List<JSONObject> returned = []

        when:
        returned += putIfAbsent(obj, 'absent', JSONObject.NULL)
        returned += putIfAbsent(obj, 'present', JSONObject.NULL)

        then:
        obj.get('absent') === JSONObject.NULL
        obj.getJSONArray('present').similar(new JSONArray(['a']))

        returned.every {it === obj }
    }

    def "test putOptIfAbsent"() {
        given:
        def obj = new JSONObject([
                present: 'a'
        ])

        List<JSONObject> returned = []

        when:
        returned += putOptIfAbsent(obj, 'absent1', 'b')
        returned += putOptIfAbsent(obj, 'present', 'b')
        returned += putOptIfAbsent(obj, 'absent2', null)
        returned += putOptIfAbsent(obj, null, 'b')

        then:
        obj.getString('absent1') == 'b'
        obj.getString('present') == 'a'
        !obj.has("absent2")

        returned.every {it === obj }
    }

    def "test getOrPut BigDecimal"() {
        given:
        def obj = new JSONObject([
                present: 4.0g,
                different: 'foo'
        ])

        when:

        def val1 = getOrPut(obj, 'absent', 2.0g)
        def val2 = getOrPut(obj, 'present', 2.0g)
        def val3 = getOrPut(obj, 'different', 2.0g)

        then:
        val1 == 2.0g
        val2 == 4.0g
        val3 == 2.0g
    }

    def "test getOrPut BigInteger"() {
        given:
        def obj = new JSONObject([
                present: 4g,
                different: 'foo'
        ])

        when:

        def val1 = getOrPut(obj, 'absent', 2g)
        def val2 = getOrPut(obj, 'present', 2g)
        def val3 = getOrPut(obj, 'different', 2g)

        then:
        val1 == 2g
        val2 == 4g
        val3 == 2g
    }

    def "test getOrPut boolean"() {
        given:
        def obj = new JSONObject([
                present: false,
                different: 'foo'
        ])

        when:

        def val1 = getOrPut(obj, 'absent', true)
        def val2 = getOrPut(obj, 'present', true)
        def val3 = getOrPut(obj, 'different', true)

        then:
        val1 === true
        val2 === false
        val3 === true
    }

    def "test getOrPut double"() {
        given:
        def obj = new JSONObject([
                present: 4.0d,
                different: 'foo'
        ])

        when:

        def val1 = getOrPut(obj, 'absent', 2.0d)
        def val2 = getOrPut(obj, 'present', 2.0d)
        def val3 = getOrPut(obj, 'different', 2.0d)

        then:
        val1 == 2.0d
        val2 == 4.0d
        val3 == 2.0d
    }

    def "test getOrPut enum"() {
        given:
        def obj = new JSONObject([
                present: GroupLayout.Alignment.BASELINE,
                different: 42
        ])

        when:

        def val1 = getOrPut(obj, 'absent', GroupLayout.Alignment.LEADING)
        def val2 = getOrPut(obj, 'present', GroupLayout.Alignment.LEADING)
        def val3 = getOrPut(obj, 'different', GroupLayout.Alignment.LEADING)

        then:
        val1 == GroupLayout.Alignment.LEADING
        val2 == GroupLayout.Alignment.BASELINE
        val3 == GroupLayout.Alignment.LEADING
    }

    def "test getOrPut float"() {
        given:
        def obj = new JSONObject([
                present: 4.0f,
                different: 'foo'
        ])

        when:

        def val1 = getOrPut(obj, 'absent', 2.0f)
        def val2 = getOrPut(obj, 'present', 2.0f)
        def val3 = getOrPut(obj, 'different', 2.0f)

        then:
        val1 == 2.0f
        val2 == 4.0f
        val3 == 2.0f
    }

    def "test getOrPut int"() {
        given:
        def obj = new JSONObject([
                present: 4,
                different: 'foo'
        ])

        when:

        def val1 = getOrPut(obj, 'absent', 2)
        def val2 = getOrPut(obj, 'present', 2)
        def val3 = getOrPut(obj, 'different', 2)

        then:
        val1 == 2
        val2 == 4
        val3 == 2
    }

    def "test getOrPut JSONArray"() {
        given:
        def obj = new JSONObject([
                present: ['a'],
                different: 'foo'
        ])

        when:

        def val1 = getOrPut(obj, 'absent', new JSONArray(['b']))
        def val2 = getOrPut(obj, 'present', new JSONArray(['b']))
        def val3 = getOrPut(obj, 'different', new JSONArray(['b']))

        then:
        val1.similar(new JSONArray(['b']))
        val2.similar(new JSONArray(['a']))
        val3.similar(new JSONArray(['b']))
    }

    def "test getOrPut JSONObject"() {
        given:
        def obj = new JSONObject([
                present: [a: true],
                different: 'foo'
        ])

        when:

        def val1 = getOrPut(obj, 'absent', new JSONObject([b: true]))
        def val2 = getOrPut(obj, 'present', new JSONObject([b: true]))
        def val3 = getOrPut(obj, 'different', new JSONObject([b: true]))

        then:
        val1.similar(new JSONObject([b: true]))
        val2.similar(new JSONObject([a: true]))
        val3.similar(new JSONObject([b: true]))
    }

    def "test getOrPut long"() {
        given:
        def obj = new JSONObject([
                present: 4l,
                different: 'foo'
        ])

        when:

        def val1 = getOrPut(obj, 'absent', 2l)
        def val2 = getOrPut(obj, 'present', 2l)
        def val3 = getOrPut(obj, 'different', 2l)

        then:
        val1 == 2l
        val2 == 4l
        val3 == 2l
    }

    def "test getOrPut Number"() {
        given:
        def obj = new JSONObject([
                present: 4l,
                different: 'foo'
        ])

        when:

        def val1 = getOrPut(obj, 'absent', BigInteger.valueOf(2) as Number)
        def val2 = getOrPut(obj, 'present', BigInteger.valueOf(2) as Number)
        def val3 = getOrPut(obj, 'different', BigInteger.valueOf(2) as Number)

        then:
        val1 == BigInteger.valueOf(2) as Number
        val2 == BigInteger.valueOf(4) as Number
        val3 == BigInteger.valueOf(2) as Number
    }

    def "test getOrPut String"() {
        given:
        def obj = new JSONObject([
                present: 'foo',
                different: 42
        ])

        when:

        def val1 = getOrPut(obj, 'absent', 'bar')
        def val2 = getOrPut(obj, 'present', 'bar')
        def val3 = getOrPut(obj, 'different', 'bar')

        then:
        val1 == 'bar'
        val2 == 'foo'
        val3 == 'bar'
    }

    def "deepCopy"() {
        given:
        def obj = new JSONObject([
                subObj: [
                        a: 42,
                        b: 'foo',
                        c: false,
                        subArr: [
                                'a',
                                1
                        ]
                ],
                str: 'bar',
                integer: 2,
                num: 42.3,
                bool: true
        ])

        when:
        def copy = deepCopy(obj)

        then:
        copy != obj
        copy.similar(obj)

        copy.getJSONObject("subObj") != obj.getJSONObject("subObj")

    }
}
