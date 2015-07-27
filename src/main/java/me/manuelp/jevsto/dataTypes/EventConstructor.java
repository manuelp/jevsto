package me.manuelp.jevsto.dataTypes;

import fj.F;
import me.manuelp.jevsto.EventDataWriter;
import org.threeten.bp.LocalDateTime;

public class EventConstructor<V, T extends EventType> implements F<EventDataWriter<V>, F<T, F<V, Event>>> {
  @Override
  public F<T, F<V, Event>> f(final EventDataWriter<V> writer) {
    return new F<T, F<V, Event>>() {
      @Override
      public F<V, Event> f(final T t) {
        return new F<V, Event>() {
          @Override
          public Event f(V v) {
            return Event.event(LocalDateTime.now(), t, writer.f(v));
          }
        };
      }
    };
  }
}
