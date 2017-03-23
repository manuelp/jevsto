package me.manuelp.jevsto.dataTypes;

import fj.Equal;
import fj.F;
import java.util.Objects;
import me.manuelp.jevsto.NotNull;

/**
 * Represents the identifier of a stream of {@link Event}s that refers to the same type of aggregate.
 *
 * @see me.manuelp.jevsto.EventStore
 */
public class AggregateType {
  private final String value;

  private AggregateType(String value) {
    NotNull.check(value);
    this.value = value;
  }

  public static AggregateType aggregateType(String name) {
    return new AggregateType(name);
  }

  public static Equal<AggregateType> equal() {
    return Equal.stringEqual.contramap(new F<AggregateType, String>() {
      @Override
      public String f(AggregateType s) {
        return s.getValue();
      }
    });
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AggregateType that = (AggregateType) o;
    return Objects.equals(getValue(), that.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getValue());
  }

  @Override
  public String toString() {
    return "AggregateType{" +
        "value='" + value + '\'' +
        '}';
  }
}
