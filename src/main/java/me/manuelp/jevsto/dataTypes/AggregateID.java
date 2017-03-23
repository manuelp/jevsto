package me.manuelp.jevsto.dataTypes;

import java.util.Objects;
import me.manuelp.jevsto.NotNull;

/**
 * Represents the ID of an aggregate of a specific {@link AggregateType type}.
 * <p>
 * The aggregate with this ID can be reconstructed by replaying all events of the relevant {@link AggregateType} and that refers to the same {@link AggregateID}.
 * </p>
 *
 * @see me.manuelp.jevsto.EventStore
 */
public class AggregateID {
  private final String value;

  private AggregateID(String value) {
    NotNull.check(value);
    this.value = value;
  }

  public static AggregateID aggregateID(String value) {
    return new AggregateID(value);
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AggregateID that = (AggregateID) o;
    return Objects.equals(getValue(), that.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getValue());
  }

  @Override
  public String toString() {
    return "AggregateID{" +
        "value='" + value + '\'' +
        '}';
  }
}
