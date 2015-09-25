package me.manuelp.jevsto.dataTypes;

import fj.F;
import me.manuelp.jevsto.EventDataWriter;
import org.threeten.bp.LocalDateTime;

public class EventConstructor<V>
    implements F<EventDataWriter<V>, F<EventType, F<V, Event>>> {
  @Override
  public F<EventType, F<V, Event>> f(final EventDataWriter<V> writer) {
    return new F<EventType, F<V, Event>>() {
      @Override
      public F<V, Event> f(final EventType t) {
        return new F<V, Event>() {
          @Override
          public Event f(V v) {
            return Event.event(LocalDateTime.now(), t, writer.f(v));
          }
        };
      }
    };
  }

  public static <V> EventConstructor<V> create() {
    return new EventConstructor<>();
  }
}
