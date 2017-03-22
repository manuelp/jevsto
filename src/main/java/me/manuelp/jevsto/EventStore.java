package me.manuelp.jevsto;

import fj.data.List;
import fj.data.Option;
import java.util.UUID;
import me.manuelp.jevsto.dataTypes.Event;
import me.manuelp.jevsto.dataTypes.Stream;
import org.threeten.bp.LocalDateTime;
import rx.Observable;

/**
 * Conceptually, this abstraction represents a dynamic set of {@link Event} streams.
 * Every stream has a unique {@link Stream identifier} used as key, and can be queried at a specific point in time or
 * subscribed to (thanks to RxJava).
 */
public interface EventStore {
  void append(Stream stream, Event e);

  List<Stream> getStreams();

  Option<Event> getById(UUID id);

  Observable<Event> getEvents(Stream stream);

  Observable<Event> getAllEvents(Stream stream);

  Observable<Event> getAllEventsFrom(Stream stream, LocalDateTime from);

  List<Event> getAll(Stream stream);

  List<Event> getFrom(Stream stream, LocalDateTime t);
}
