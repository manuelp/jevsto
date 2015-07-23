package me.manuelp.jevsto;

import me.manuelp.jevsto.dataTypes.Event;

public interface EventStore {
  void append(Event e);
}
