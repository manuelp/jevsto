package me.manuelp.jevsto.dataTypes;

import fj.F;
import fj.F2;
import me.manuelp.jevsto.EventDataReader;
import me.manuelp.jevsto.EventDataWriter;
import org.threeten.bp.Instant;

public class EventDescriptor<T> {
  private final EventType type;
  private final EventDataReader<T> reader;
  private final F2<AggregateType, AggregateID, F<T, Event>> constructor;

  private EventDescriptor(final EventType type, EventDataReader<T> reader, final EventDataWriter<T> writer) {
    this.type = type;
    this.reader = reader;
    this.constructor = new F2<AggregateType, AggregateID, F<T, Event>>() {
      @Override
      public F<T, Event> f(final AggregateType aggregateType, final AggregateID aggregateID) {
        return new F<T, Event>() {
          @Override
          public Event f(T t) {
            return Event.event(aggregateType, aggregateID, Instant.now(), type, writer.f(t));
          }
        };
      }
    };
  }

  public static <V> EventDescriptor<V> eventDescriptor(EventType type, EventDataReader<V> reader,
      EventDataWriter<V> writer) {
    return new EventDescriptor<>(type, reader, writer);
  }

  public EventType getType() {
    return type;
  }

  public F2<AggregateType, AggregateID, F<T, Event>> writer() {
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
