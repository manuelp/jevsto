package me.manuelp.jevsto.dataTypes;

import java.util.Objects;
import me.manuelp.jevsto.NotNull;

/**
 * Represents a stream of {@link Event}s.
 * <p>
 * In DDD+CQRS+event sourcing, it's usually an aggregate identifier.
 * </p>
 *
 * @see me.manuelp.jevsto.EventStore
 */
public class Stream {
  private final String name;

  private Stream(String name) {
    NotNull.check(name);
    this.name = name;
  }

  public static Stream stream(String name) {
    return new Stream(name);
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Stream stream = (Stream) o;
    return Objects.equals(getName(), stream.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }

  @Override
  public String toString() {
    return "Stream{" +
        "name='" + name + '\'' +
        '}';
  }
}
