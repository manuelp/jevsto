package me.manuelp.jevsto.dataTypes;

public class EventType {
  private final String type;

  private EventType(String type) {
    this.type = type;
  }

  public static EventType eventType(String type) {
    return new EventType(type);
  }

  public String getType() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    EventType eventType = (EventType) o;

    return type.equals(eventType.type);

  }

  @Override
  public int hashCode() {
    return type.hashCode();
  }

  @Override
  public String toString() {
    return "EventType{" + "type='" + type + '\'' + '}';
  }
}
