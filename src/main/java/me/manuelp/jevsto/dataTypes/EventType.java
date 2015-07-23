package me.manuelp.jevsto.dataTypes;

public abstract class EventType {
  private final String type;

  protected EventType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    EventType eventType = (EventType) o;

    return type.equals(eventType.type);

  }

  @Override
  public int hashCode() {
    return type.hashCode();
  }

  @Override
  public String toString() {
    return "EventType{" +
           "type='" + type + '\'' +
           '}';
  }
}
