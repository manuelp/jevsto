package me.manuelp.jevsto.inMemory;

import me.manuelp.jevsto.dataTypes.Event;
import me.manuelp.jevsto.dataTypes.EventConstructor;
import me.manuelp.jevsto.dataTypes.EventData;
import org.junit.Test;

import static me.manuelp.jevsto.dataTypes.EventData.eventData;
import static me.manuelp.jevsto.inMemory.TestEvent.testEvent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EventConstructorTest {
  @Test
  public void can_build_a_constructor_for_arbitrary_types() {
    EventConstructor<SomeValue, TestEvent> c = new EventConstructor<>();
    MockWriter writer = new MockWriter();

    Event e1 = c.f(writer).f(testEvent()).f(new SomeValue("_"));

    assertTrue(writer.isCalled());
    assertEquals(testEvent(), e1.getType());
  }

  private class SomeValue {
    private final String v;

    private SomeValue(String v) {
      this.v = v;
    }

    public String getV() {
      return v;
    }
  }

  private class MockWriter implements me.manuelp.jevsto.EventDataWriter<SomeValue> {
    private boolean called = false;

    @Override
    public EventData f(SomeValue someValue) {
      called = true;
      return eventData(someValue.getV().getBytes());
    }

    public boolean isCalled() {
      return called;
    }
  }
}
