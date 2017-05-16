package me.manuelp.jevsto.dataTypes;

import fj.F;
import me.manuelp.jevsto.EventDataReader;
import me.manuelp.jevsto.EventDataWriter;

public class EventDescriptor<T> {
  private final EventType type;
  private final EventDataReader<T> reader;
  private final F<T, Event> constructor;

  private EventDescriptor(EventType type, EventDataReader<T> reader, EventDataWriter<T> writer) {
    this.type = type;
    this.reader = reader;
    this.constructor = EventConstructor.<T> create().f(writer).f(type);
  }

  public static <V> EventDescriptor<V> eventDescriptor(EventType type, EventDataReader<V> reader,
      EventDataWriter<V> writer) {
    return new EventDescriptor<>(type, reader, writer);
  }

  public EventType getType() {
    return type;
  }

  public F<T, Event> writer() {
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
