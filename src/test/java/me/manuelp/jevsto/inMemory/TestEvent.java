package me.manuelp.jevsto.inMemory;

import me.manuelp.jevsto.dataTypes.EventType;

public class TestEvent extends EventType {
  private TestEvent() {
    super("test-event");
  }

  public static TestEvent testEvent() {
    return new TestEvent();
  }
}
