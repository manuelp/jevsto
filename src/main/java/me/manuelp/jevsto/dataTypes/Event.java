package me.manuelp.jevsto.dataTypes;

import fj.F;
import fj.Ord;
import java.util.Objects;
import java.util.UUID;
import me.manuelp.jevsto.NotNull;
import org.threeten.bp.Instant;
import rx.functions.Func1;

public class Event {
  private final UUID id;
  private final Instant timestamp;
  private final EventType type;
  private final AggregateType aggregateType;
  private final AggregateID aggregateID;
  private final EventData data;

  private Event(UUID id, AggregateType aggregateType, AggregateID aggregateID, Instant timestamp, EventType type,
    EventData data) {
    NotNull.check(id, aggregateType, aggregateID, timestamp, type, data);
    this.id = id;
    this.aggregateType = aggregateType;
    this.aggregateID = aggregateID;
    this.timestamp = timestamp;
    this.type = type;
    this.data = data;
  }

  public static Event event(AggregateType aggregateType, AggregateID aggregateID, Instant timestamp, EventType type,
      EventData data) {
    return new Event(UUID.randomUUID(), aggregateType, aggregateID, timestamp, type, data);
  }

  public static Event event(UUID id, AggregateType aggregateType, AggregateID aggregateID, Instant timestamp,
      EventType type, EventData data) {
    return new Event(id, aggregateType, aggregateID, timestamp, type, data);
  }

  public static Ord<Event> byTimestamp() {
    return Ord.longOrd.contramap(new F<Event, Long>() {
      @Override
      public Long f(Event e) {
        return e.getTimestamp().getEpochSecond();
      }
    });
  }

  public UUID getId() {
    return id;
  }

  public AggregateType getAggregateType() {
    return aggregateType;
  }

  public AggregateID getAggregateID() {
    return aggregateID;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public EventType getType() {
    return type;
  }

  public EventData getData() {
    return data;
  }

  /**
   * Returns a predicate that checks if an {@link Event} is of a certain type.
   *
   * @param t {@link EventType} to check for
   * @return Predicate on {@link Event}s
   */
  public static Func1<Event, Boolean> isOfType(final EventType t) {
    return new Func1<Event, Boolean>() {
      @Override
      public Boolean call(Event e) {
        return e.getType().equals(t);
      }
    };
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Event event = (Event) o;
    return Objects.equals(getId(), event.getId()) && Objects.equals(getAggregateType(), event.getAggregateType())
        && Objects.equals(getAggregateID(), event.getAggregateID())
        && Objects.equals(getTimestamp(), event.getTimestamp()) && Objects.equals(getType(), event.getType())
        && Objects.equals(getData(), event.getData());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getAggregateType(), getAggregateID(), getTimestamp(), getType(), getData());
  }

  @Override
  public String toString() {
    return "Event{" + "id=" + id + ", timestamp=" + timestamp + ", type=" + type + ", aggregateType=" + aggregateType
        + ", aggregateID=" + aggregateID + ", data=" + data + '}';
  }
}
