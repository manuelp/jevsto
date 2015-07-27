package me.manuelp.jevsto;

import fj.data.List;
import fj.data.Option;
import me.manuelp.jevsto.dataTypes.Event;
import org.threeten.bp.LocalDateTime;
import rx.Observable;

import java.util.UUID;

public interface EventStore {
  void append(Event e);

  Observable<Event> getEvents();

  Observable<Event> getAllEvents();

  Observable<Event> getAllEventsFrom(LocalDateTime from);

  List<Event> getAll();

  List<Event> getFrom(LocalDateTime t);

  Option<Event> getById(UUID id);
}
