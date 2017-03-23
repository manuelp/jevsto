package me.manuelp.jevsto.dataTypes;

import fj.F;
import fj.F2;
import me.manuelp.jevsto.EventDataReader;
import me.manuelp.jevsto.EventDataWriter;
import org.threeten.bp.Instant;

public class EventDescriptor<T> {
  private final EventType type;
  private final EventDataReader<T> reader;
  private final F2<AggregateID, T, Event> constructor;

  private EventDescriptor(final AggregateType aggregateType, final EventType type, EventDataReader<T> reader,
    final EventDataWriter<T> writer) {
    this.type = type;
    this.reader = reader;
    this.constructor = new F2<AggregateID, T, Event>() {
      @Override
      public Event f(AggregateID aggregateID, T data) {
        return Event.event(aggregateType, aggregateID, Instant.now(), type, writer.f(data));
      }
    };
  }

  public static <V> EventDescriptor<V> eventDescriptor(AggregateType aggregateType, EventType type,
      EventDataReader<V> reader, EventDataWriter<V> writer) {
    return new EventDescriptor<>(aggregateType, type, reader, writer);
  }

  public EventType getType() {
    return type;
  }

  public F2<AggregateID, T, Event> writer() {
    return constructor;
  }

  public F<Event, T> reader() {
    return new F<Event, T>() {
      @Override
      public T f(Event event) {
        return reader.f(event.getData());
      }
    };
  }
}
