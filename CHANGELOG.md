# Changelog

## 0.2.1 (?)
This version is a functional backport of some features already implemented in newer jevsto versions, but that have 
a redesigned interface.

* Added new query methods to `EventStore`, to enable seeked fetching
    * `getFrom(t :: LocalDateTime, max :: int) :: List<Event>`
    * `getFrom(id :: UUID, max :: int) :: List<Event>`

## 0.2.0 (2017-01-03)
### Changed
* Upgraded dependencies:
    * threetenbp 1.3.3
    * functionaljava 4.6
    * rxjava 1.2.4

### Added
This release is all about [Apache Avro](https://avro.apache.org/) support for event data serialization/deserialization.

* `AvroEventDataWriter` and `AvroEventDataReader` to use Apache Avro for `EventData` serialization/deserialization.
* `AvroEvent` for easy configuration of Avro-based `EventDescriptor`s.
* `AvroRead` with some utility methods for reading Avro schemas and data structures.

## 0.1.1
* Aligned `MemoryEventStore` to the API change introduced in version 0.1.0.

## 0.1.0
* Changed `Event` predicates from `F<A, B>` to `Func1<A, B>` since they are generally used to filter and event stream.

## 0.0.3
* Added predicate `Event#isOfType(EventType)`.
* Added `EventConstructor` static factory method.
* Added `EventDescriptor` type to encapsulate `EventType` and reading and writing of `Event`s.

## 0.0.2
* Changed group name and LICENSE (to MPL 2.0)

## 0.0.1
First public stable release. Basic functionality and data types.
