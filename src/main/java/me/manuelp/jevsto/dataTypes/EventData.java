package me.manuelp.jevsto.dataTypes;

import me.manuelp.jevsto.NotNull;

import java.util.Arrays;

public class EventData {
  private final byte[] data;

  private EventData(byte[] data) {
    NotNull.check(data);
    this.data = data;
  }

  public static EventData eventData(byte[] data) {
    return new EventData(data);
  }

  public byte[] getData() {
    return data;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    EventData eventData = (EventData) o;

    return Arrays.equals(data, eventData.data);

  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(data);
  }
}
