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

import com.github.oneandolaf.jsonext.readonly.ReadOnlyJsonObject
import org.json.JSONArray
import org.json.JSONObject
import spock.lang.Specification

import javax.swing.*

class JSONObjectExtensionsSpec extends Specification {

    def "test putIfAbsent Boolean"() {
        given:
        def obj = new JSONObject([
                present: true,
                different: 'foo'
        ])

        List<JSONObject> returned = []

        when:
        returned += obj.putIfAbsent('absent', true)
        returned += obj.putIfAbsent('present', false)
        returned += obj.putIfAbsent('different', true)

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
        returned += obj.putIfAbsent('absent', ['b'])
        returned += obj.putIfAbsent('present', ['b'])
        returned += obj.putIfAbsent('different', ['b'])

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
        returned += obj.putIfAbsent('absent', 1.2d)
        returned += obj.putIfAbsent('present', 1.2d)
        returned += obj.putIfAbsent('different', 1.2d)

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
        returned += obj.putIfAbsent('absent', 1.2f)
        returned += obj.putIfAbsent('present', 1.2f)
        returned += obj.putIfAbsent('different', 1.2f)

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
        returned += obj.putIfAbsent('absent', 12)
        returned += obj.putIfAbsent('present', 12)
        returned += obj.putIfAbsent('different', 12)

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
        returned += obj.putIfAbsent('absent', 12l)
        returned += obj.putIfAbsent('present', 12l)
        returned += obj.putIfAbsent('different', 12l)

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
        returned += obj.putIfAbsent('absent', [b: true])
        returned += obj.putIfAbsent('present', [b: true])
        returned += obj.putIfAbsent('different', [b: true])

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
        returned += obj.putIfAbsent('absent', new JSONObject([b: true]))
        returned += obj.putIfAbsent('present', new JSONObject([b: true]))
        returned += obj.putIfAbsent('different', new JSONObject([b: true]))

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
        returned += obj.putIfAbsent('absent', new JSONArray(['b']))
        returned += obj.putIfAbsent('present', new JSONArray(['b']))
        returned += obj.putIfAbsent('different', new JSONArray(['b']))

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
        returned += obj.putIfAbsent('absent', 'b')
        returned += obj.putIfAbsent('present', 'b')
        returned += obj.putIfAbsent('different', 'b')

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
        returned += obj.putIfAbsent('absent', JSONObject.NULL)
        returned += obj.putIfAbsent('present', JSONObject.NULL)

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
        returned += obj.putOptIfAbsent('absent1', 'b')
        returned += obj.putOptIfAbsent('present', 'b')
        returned += obj.putOptIfAbsent('absent2', null)
        returned += obj.putOptIfAbsent(null, 'b')

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

        def val1 = obj.getOrPut('absent', 2.0g)
        def val2 = obj.getOrPut('present', 2.0g)
        def val3 = obj.getOrPut('different', 2.0g)

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

        def val1 = obj.getOrPut('absent', 2g)
        def val2 = obj.getOrPut('present', 2g)
        def val3 = obj.getOrPut('different', 2g)

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

        def val1 = obj.getOrPut('absent', true)
        def val2 = obj.getOrPut('present', true)
        def val3 = obj.getOrPut('different', true)

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

        def val1 = obj.getOrPut('absent', 2.0d)
        def val2 = obj.getOrPut('present', 2.0d)
        def val3 = obj.getOrPut('different', 2.0d)

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

        def val1 = obj.getOrPut('absent', GroupLayout.Alignment.LEADING)
        def val2 = obj.getOrPut('present', GroupLayout.Alignment.LEADING)
        def val3 = obj.getOrPut('different', GroupLayout.Alignment.LEADING)

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

        def val1 = obj.getOrPut('absent', 2.0f)
        def val2 = obj.getOrPut('present', 2.0f)
        def val3 = obj.getOrPut('different', 2.0f)

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

        def val1 = obj.getOrPut('absent', 2)
        def val2 = obj.getOrPut('present', 2)
        def val3 = obj.getOrPut('different', 2)

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

        def val1 = obj.getOrPut('absent', new JSONArray(['b']))
        def val2 = obj.getOrPut('present', new JSONArray(['b']))
        def val3 = obj.getOrPut('different', new JSONArray(['b']))

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

        def val1 = obj.getOrPut('absent', new JSONObject([b: true]))
        def val2 = obj.getOrPut('present', new JSONObject([b: true]))
        def val3 = obj.getOrPut('different', new JSONObject([b: true]))

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

        def val1 = obj.getOrPut('absent', 2l)
        def val2 = obj.getOrPut('present', 2l)
        def val3 = obj.getOrPut('different', 2l)

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

        def val1 = obj.getOrPut('absent', BigInteger.valueOf(2) as Number)
        def val2 = obj.getOrPut('present', BigInteger.valueOf(2) as Number)
        def val3 = obj.getOrPut('different', BigInteger.valueOf(2) as Number)

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

        def val1 = obj.getOrPut('absent', 'bar')
        def val2 = obj.getOrPut('present', 'bar')
        def val3 = obj.getOrPut('different', 'bar')

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
        def copy = obj.deepCopy()

        then:
        copy != obj
        copy.similar(obj)

        copy.getJSONObject("subObj") != obj.getJSONObject("subObj")

    }

    def "asReadOnly"() {
        given:
        def obj = new JSONObject()

        when:
        def ro1 = obj.asReadOnly()

        then:
        ro1 instanceof ReadOnlyJsonObject
        ro1.empty

        when:
        obj.put("foo", "bar")

        then:
        ro1.get("foo") == "bar"
    }
}
