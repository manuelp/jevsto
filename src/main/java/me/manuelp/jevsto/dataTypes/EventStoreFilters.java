package me.manuelp.jevsto.dataTypes;

import fj.data.Option;
import java.util.Objects;
import me.manuelp.jevsto.NotNull;

public class EventStoreFilters {
  private final Option<Seek> seek;
  private final Option<AggregateType> aggregateType;
  private final Option<AggregateID> aggregateID;
  private final Option<Integer> maxEvents;

  private EventStoreFilters(Option<Seek> seek, Option<AggregateType> aggregateType, Option<AggregateID> aggregateID,
    Option<Integer> maxEvents) {
    NotNull.check(seek, aggregateType, aggregateID, maxEvents);
    this.seek = seek;
    this.aggregateType = aggregateType;
    this.aggregateID = aggregateID;
    this.maxEvents = maxEvents;
  }

  public static EventStoreFilters eventStoreFilters() {
    return new EventStoreFilters(Option.<Seek> none(), Option.<AggregateType> none(), Option.<AggregateID> none(),
        Option.<Integer> none());
  }

  public EventStoreFilters from(Seek seek) {
    return new EventStoreFilters(Option.some(seek), getAggregateType(), getAggregateID(), getMaxEvents());
  }

  public EventStoreFilters ofAggregateType(AggregateType t) {
    return new EventStoreFilters(getSeek(), Option.some(t), getAggregateID(), getMaxEvents());
  }

  public EventStoreFilters ofAggregateID(AggregateID id) {
    return new EventStoreFilters(getSeek(), getAggregateType(), Option.some(id), getMaxEvents());
  }

  public EventStoreFilters maxEvents(Integer n) {
    return new EventStoreFilters(getSeek(), getAggregateType(), getAggregateID(), Option.some(n));
  }

  public Option<Seek> getSeek() {
    return seek;
  }

  public Option<AggregateType> getAggregateType() {
    return aggregateType;
  }

  public Option<AggregateID> getAggregateID() {
    return aggregateID;
  }

  public Option<Integer> getMaxEvents() {
    return maxEvents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    EventStoreFilters that = (EventStoreFilters) o;
    return Objects.equals(getSeek(), that.getSeek()) && Objects.equals(getAggregateType(), that.getAggregateType())
        && Objects.equals(getAggregateID(), that.getAggregateID())
        && Objects.equals(getMaxEvents(), that.getMaxEvents());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getSeek(), getAggregateType(), getAggregateID(), getMaxEvents());
  }

  @Override
  public String toString() {
    return "EventStoreFilters{" + "seek=" + seek + ", aggregateType=" + aggregateType + ", aggregateID=" + aggregateID
        + ", maxEvents=" + maxEvents + '}';
  }
}
