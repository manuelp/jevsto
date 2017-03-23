package me.manuelp.jevsto;

import fj.data.List;
import fj.data.Option;
import java.util.UUID;
import me.manuelp.jevsto.dataTypes.AggregateType;
import me.manuelp.jevsto.dataTypes.Event;
import org.threeten.bp.Instant;
import rx.Observable;

/**
 * Conceptually, this abstraction represents a dynamic set of {@link Event} streams. Every stream refers to a specific
 * {@link AggregateType}, and every events refers to a specific {@link me.manuelp.jevsto.dataTypes.AggregateID}. It can
 * be queried at a specific point in time or subscribed to (thanks to RxJava).
 */
public interface EventStore {
  /*--------------------------------------------------------------------------
   * DB methods
   *--------------------------------------------------------------------------*/
  void append(Event e);

  Option<Event> getById(UUID id);

  List<Event> getFrom(Option<Instant> from);

  /*--------------------------------------------------------------------------
   * Message broker methods
   *--------------------------------------------------------------------------*/

  Observable<Event> stream();
}
