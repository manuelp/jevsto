package me.manuelp.jevsto;

import fj.data.List;
import fj.data.Option;
import me.manuelp.jevsto.dataTypes.Event;
import rx.Observable;

import java.time.LocalDateTime;
import java.util.UUID;

public interface EventStore {
  void append(Event e);

  Observable<Event> getEvents();

  List<Event> getAll();

  List<Event> getFrom(LocalDateTime t);

  Option<Event> getById(UUID id);
}
