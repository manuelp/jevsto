package me.manuelp.jevsto.dataTypes;

import fj.data.Option;
import java.util.Objects;
import me.manuelp.jevsto.NotNull;
import org.threeten.bp.Instant;

public class EventStoreFilters {
  private final Option<Instant> from;
  private final Option<AggregateType> aggregateType;
  private final Option<AggregateID> aggregateID;
  private final Option<Integer> maxEvents;

  private EventStoreFilters(Option<Instant> from, Option<AggregateType> aggregateType, Option<AggregateID> aggregateID,
    Option<Integer> maxEvents) {
    NotNull.check(from, aggregateType, aggregateID, maxEvents);
    this.from = from;
    this.aggregateType = aggregateType;
    this.aggregateID = aggregateID;
    this.maxEvents = maxEvents;
  }

  public static EventStoreFilters eventStoreFilters() {
    return new EventStoreFilters(Option.<Instant> none(), Option.<AggregateType> none(), Option.<AggregateID> none(),
        Option.<Integer> none());
  }

  public EventStoreFilters from(Instant from) {
    return new EventStoreFilters(Option.some(from), getAggregateType(), getAggregateID(), getMaxEvents());
  }

  public EventStoreFilters ofAggregateType(AggregateType t) {
    return new EventStoreFilters(getFrom(), Option.some(t), getAggregateID(), getMaxEvents());
  }

  public EventStoreFilters ofAggregateID(AggregateID id) {
    return new EventStoreFilters(getFrom(), getAggregateType(), Option.some(id), getMaxEvents());
  }

  public EventStoreFilters maxEvents(Integer n) {
    return new EventStoreFilters(getFrom(), getAggregateType(), getAggregateID(), Option.some(n));
  }

  public Option<Instant> getFrom() {
    return from;
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
    return Objects.equals(getFrom(), that.getFrom()) && Objects.equals(getAggregateType(), that.getAggregateType())
        && Objects.equals(getAggregateID(), that.getAggregateID())
        && Objects.equals(getMaxEvents(), that.getMaxEvents());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getFrom(), getAggregateType(), getAggregateID(), getMaxEvents());
  }

  @Override
  public String toString() {
    return "EventStoreFilters{" + "from=" + from + ", aggregateType=" + aggregateType + ", aggregateID=" + aggregateID
        + ", maxEvents=" + maxEvents + '}';
  }
}
