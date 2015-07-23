package me.manuelp.jevsto.inMemory;

import fj.data.List;
import me.manuelp.jevsto.EventDataReader;
import me.manuelp.jevsto.EventDataWriter;
import me.manuelp.jevsto.EventStore;
import me.manuelp.jevsto.Projection;
import me.manuelp.jevsto.dataTypes.Event;
import me.manuelp.jevsto.dataTypes.EventData;
import org.junit.Test;

import java.time.LocalDateTime;

import static me.manuelp.jevsto.dataTypes.Event.event;
import static me.manuelp.jevsto.dataTypes.EventData.eventData;
import static me.manuelp.jevsto.inMemory.TestEvent.testEvent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MemoryEventStoreTest {

  @Test
  public void notifies_all_projections_of_new_events() {
    EventStore es = new MemoryEventStore();
    Projection p1 = new FakeProjection();
    Projection p2 = new FakeProjection();
    Projection p3 = new FakeProjection();
    es.subscribe(p1);
    es.subscribe(p2);
    es.subscribe(p3);
    Event e = event(LocalDateTime.now(), testEvent(), eventData(new byte[]{}));

    es.append(e);

    assertTrue(((FakeProjection) p1).hasReceived(e));
    assertTrue(((FakeProjection) p2).hasReceived(e));
    assertTrue(((FakeProjection) p3).hasReceived(e));
  }

  @Test
  public void can_provide_all_events_received_so_far() {
    EventStore es = new MemoryEventStore();
    Event e1 = event(LocalDateTime.now(), testEvent(), eventData(new byte[]{}));
    Event e2 = event(LocalDateTime.now(), testEvent(), eventData(new byte[]{}));

    es.append(e1);
    es.append(e2);

    List<Event> events = es.getAll();
    assertEquals(2, events.length());
    assertTrue(events.toJavaList().contains(e1));
    assertTrue(events.toJavaList().contains(e2));
  }

  @Test
  public void can_provide_all_events_from_a_certain_instant() {
    EventStore es = new MemoryEventStore();
    Event e1 = event(LocalDateTime.of(2015, 7, 21, 22, 52), testEvent(), eventData(new byte[]{}));
    Event e2 = event(LocalDateTime.of(2015, 7, 23, 22, 52), testEvent(), eventData(new byte[]{}));

    es.append(e1);
    es.append(e2);

    List<Event> events = es.getFrom(LocalDateTime.of(2015, 7, 23, 22, 52));
    assertEquals(1, events.length());
    assertTrue(events.toJavaList().contains(e2));
  }
}