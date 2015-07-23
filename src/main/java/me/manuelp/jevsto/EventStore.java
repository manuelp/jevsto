package me.manuelp.jevsto;

import fj.data.List;
import me.manuelp.jevsto.dataTypes.Event;

import java.time.LocalDateTime;

public interface EventStore {
  void append(Event e);

  List<Event> getAll();

  List<Event> getFrom(LocalDateTime t);

  void subscribe(Projection p);
}
