# json-extensions

Various extensions for [JSON-java](https://github.com/stleary/JSON-java). This primarily consists of unmodifiable
wrappers around the standard JSON structures, among various other utilities.

## Read-only JSON containers

This library provides read-only alternatives to JSON-java's `JSONObject` and `JSONArray` classes. These also feature a
more expansive (and hopefully more intuitive) API for retrieving values. They are still backed by ordinary `JSONObject`s
and `JSONArray`s, so they can be integrated with code that uses these relatively easily.

The examples on this page mostly use `ReadOnlyJSONObject`s, but the API is nearly identical for `ReadOnlyJSONArray`s
(just with keys replaced by indices).

### Creating read-only containers

There are several ways of creating read-only containers. They can be generally divided into two groups: some are backed
by an ordinary containers, and others are independent of one.

`create` is the standard factory method for creating a container backed by another one. There is also an `asReadOnly`
extension method on ordinary containers.

```kotlin
val orig = JSONObject()

val ro1 = ReadOnlyJSONObject.create(orig)
val ro2 = orig.asReadOnly()

orig.put("foo", "bar")
ro1.getStringOrNull("foo") == "bar"
ro2.getStringOrNull("foo") == "bar"
```

`snapshot` is a factory method for creating a container not backed by anything else. It is similar to creating a deep
copy of a container and using that. There is also an `readOnlySnapshot`
extension method on ordinary containers. Creating snapshots may deep-copy the original object, so it may be
computationally expensive.

```kotlin
val orig = JSONObject()

val ro1 = ReadOnlyJSONObject.snapshot(orig)
val ro2 = orig.readOnlySnapshot()

orig.put("foo", "bar")
ro1.getStringOrNull("foo") == null
ro2.getStringOrNull("foo") == null
```

There are also `snapshot` factory methods for creating read-only objects directly from key-value pairs or maps, and
read-only arrays directly from lists.

```kotlin
val obj1 = ReadOnlyJSONObject.snapshot(JSONObject("""{"foo": "bar"}"""))
val obj2 = ReadOnlyJSONObject.snapshot(mapOf("foo" to "bar"))
val obj3 = ReadOnlyJSONObject.snapshot("foo" to "bar")

val arr1 = ReadOnlyJSONArray.snapshot(JSONArray("""[1, 2, 3]"""))
val arr2 = ReadOnlyJSONArray.snapshot(listOf(1, 2, 3))
val arr3 = ReadOnlyJSONArray.snapshot(1, 2, 3)
```

Finally, there are `ReadOnlyJSONObject.EMPTY` and `ReadOnlyJSONArray.EMPTY` constants to represent permanently empty
containers.

### Retrieving values

The new containers feature many methods to retrieve data out of them. They usually use the
pattern `get{Type}or{Default}`. This way, the fallback for any operation is immediately apparent in the code. All types
that are supported by JSON-java can also be used here, using mostly equivalent conversion algorithms to the original. So
like JSON-java, the read-only classes will attempt to parse Strings into booleans and numbers.

Which fallback values are available depends on the type retrieved. All types allow `null` and a closure as a fallback (
using the `orElse` methods). Throwing exceptions is not supported; instead an exception should be thrown from within
an `orElse` closure. Different types support additional fallback values, such as empty Strings, empty collections, `0`
or `-1`.

There are also `getOrNull` and `getOrElse` methods which return an `Any?` or `Any` respectively, and a `get` method
which returns a `ReadOnlyJSONVal` as a value container. The latter features various methods to cast it to different
types. They use the same pattern as the getter. So the following calls have identical results:

```kotlin
val obj = ReadOnlyJSONObject.snapshot("foo" to 42)

obj.getIntOrNull("foo") == 42
obj.get("foo").asIntOrNull() == 42
obj["foo"].asIntOrNull() == 42
```

#### Strings

JSON-java featured different conversion mechanisms in its `getString` and `optString` methods.
`getString` would be successful only if the value was actually a String, while `optString` would call `toString` on any
non-null values to convert them to Strings. The `getString...` methods in json-extensions feature an optional `coerce`
parameter to simulate the `optString` behavior. See the example below:

```kotlin
val obj = ReadOnlyJSONObject.snapshot("i" to 1)

obj.getStringOrNull("i", false) == null
obj.getStringOrNull("i", true) == "1"
obj.getOrNull("i")?.toString() == "1"
```

### Comparing containers

Like the ordinary containers, read-only containers do not implement value equality. Instead, they are compared
using `similar`. In order to preserve symmetry between comparisons, comparing a read-only container with an ordinary one
using similar will always return `false`.

To compare read-only containers with ordinary ones, there are `similarToPlainObject` and `similarToPlainArray` methods
on the read-only containers.

Another option is using the `JSONSimilar` utility object, which will freely compare read-only and ordinary containers.
If passed anything else, `JSONSimilar` will use `equals` for comparison.

```kotlin
val plain = JSONObject("""{}""")
val ro = ReadOnlyJSONObject.EMPTY

plain.similar(ro) == false // can't be helped since that method is in a JSON-java class
ro.similar(plain) == false // also false to be symmetrical
ro.similarToPlainObject(plain) == true
JSONSimilar.similar(ro, plain) == true
JSONSimilar.similar(plain, ro) == true
```

### Converting read-only containers to ordinary ones

There is no way of getting the backing object or array out of a read-only container. Only a copy of it can be accessed
by calling `container.copyToPlain()`. This is to prevent any unauthorized write access to the container.

## Unmodifiable JSON subclasses

The `JSONObjectUnmodifiable` and `JSONArrayUnmodifiable` are unmodifiable subclasses of `JSONObject` and `JSONArray`
respectively. They are similar in function to Java's `Collections.unmodifiableList` etc. methods in that the
modification methods may still be called, but will throw an exception.

As such, apart from modifying methods, their API is identical to the normal `JSONObject` and `JSONArray`. They will,
however, also wrap any object or array return values in unmodifiable objects.

The benefit of these classes is that they can be introduced into existing code without having to break any API. Keep in
mind that receivers of these objects may not expect them to be unmodifiable, and while their existing code will compile
fine, it may throw exceptions at runtime.


