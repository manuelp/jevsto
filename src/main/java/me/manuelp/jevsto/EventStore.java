package me.manuelp.jevsto;

import fj.data.List;
import fj.data.Option;
import java.util.UUID;
import me.manuelp.jevsto.dataTypes.Event;
import org.threeten.bp.LocalDateTime;
import rx.Observable;

public interface EventStore {
  void append(Event e);

  Observable<Event> getEvents();

  Observable<Event> getAllEvents();

  Observable<Event> getAllEventsFrom(LocalDateTime from);

  List<Event> getAll();

  List<Event> getFrom(LocalDateTime t);

  List<Event> getFrom(LocalDateTime t, int max);

  List<Event> getFrom(UUID id, int max);

  Option<Event> getById(UUID id);
}
