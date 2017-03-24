# Changelog

## 0.4.0 (2017-03-24)
Redesigned the `EventStore` interface. The event store is basically an intersection between:

* A data store (existing events fetching).
* A message broker (events notification to arbitrary listeners).

The API has been simplified to explicitly reflect those two modes, by explosing basically just three operations:

* (Store) Fetch event by ID.
* (Store) Fetch events according some criteria. Available query criteria has been modeled with the `EventStoreFilters` 
  value object (which exposes a fluent-style interface for construction).
* (Message broker) Get the `Observable` stream to subscribe to.

The features implemented by removed methods can easily be obtained combining those operations.

Now `Event`s explicitly include data about the *[aggregate](https://martinfowler.com/bliki/DDD_Aggregate.html)* 
they refer to:

- `AggregateType`
- `AggregateID`

This way jevsto API is now more explicitly geared toward DDD. 

## 0.3.0 (2017-03-22)
### Changes
Added `Stream` concept: `EventStore` is now an abstraction on a dynamic set of event streams, whereas before there was 
conceptually only a single, global stream.

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
