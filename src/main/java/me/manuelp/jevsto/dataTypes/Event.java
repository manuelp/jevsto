package me.manuelp.jevsto.dataTypes;

import fj.F;
import me.manuelp.jevsto.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public class Event {
  private final UUID id;
  private final LocalDateTime timestamp;
  private final EventType type;
  private final EventData data;

  private Event(UUID id, LocalDateTime timestamp, EventType type, EventData data) {
    NotNull.check(id, timestamp, type, data);
    this.id = id;
    this.timestamp = timestamp;
    this.type = type;
    this.data = data;
  }

  public static Event event(LocalDateTime timestamp, EventType type, EventData data) {
    return new Event(UUID.randomUUID(), timestamp, type, data);
  }

  public static F<Event, Boolean> hasBeenCreatedAtOrAfter(final LocalDateTime t) {
    return new F<Event, Boolean>() {
      @Override
      public Boolean f(Event e) {
        return e.getTimestamp().isEqual(t) || e.getTimestamp().isAfter(t);
      }
    };
  }

  public UUID getId() {
    return id;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public EventType getType() {
    return type;
  }

  public EventData getData() {
    return data;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Event event = (Event) o;

    if (!id.equals(event.id)) return false;
    if (!timestamp.equals(event.timestamp)) return false;
    if (!type.equals(event.type)) return false;
    return data.equals(event.data);

  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + timestamp.hashCode();
    result = 31 * result + type.hashCode();
    result = 31 * result + data.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Event{" +
           "id=" + id +
           ", timestamp=" + timestamp +
           ", type=" + type +
           ", data=" + data +
           '}';
  }
}
