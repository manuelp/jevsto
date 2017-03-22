package me.manuelp.jevsto.inMemory;

import static fj.data.List.list;
import static me.manuelp.jevsto.dataTypes.Event.event;
import static me.manuelp.jevsto.dataTypes.EventData.eventData;
import static me.manuelp.jevsto.dataTypes.EventType.eventType;
import static me.manuelp.jevsto.dataTypes.Stream.stream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import fj.data.List;
import me.manuelp.jevsto.EventStore;
import me.manuelp.jevsto.dataTypes.Event;
import org.junit.Test;
import org.threeten.bp.LocalDateTime;
import rx.observers.TestSubscriber;

public class MemoryEventStoreTest {

  @Test
  public void notifies_all_projections_of_new_events() {
    EventStore es = new MemoryEventStore();
    TestSubscriber<Event> projection1 = new TestSubscriber<>();
    TestSubscriber<Event> projection2 = new TestSubscriber<>();
    TestSubscriber<Event> projection3 = new TestSubscriber<>();
    es.getEvents(stream("_")).subscribe(projection1);
    es.getEvents(stream("_")).subscribe(projection2);
    es.getEvents(stream("_")).subscribe(projection3);
    Event e = event(LocalDateTime.now(), eventType("test"), eventData(new byte[]{}));

    es.append(stream("_"), e);

    projection1.assertReceivedOnNext(list(e).toJavaList());
    projection2.assertReceivedOnNext(list(e).toJavaList());
    projection3.assertReceivedOnNext(list(e).toJavaList());
  }

  @Test
  public void events_are_segregated_by_stream() {
    EventStore es = new MemoryEventStore();
    TestSubscriber<Event> projection1 = new TestSubscriber<>();
    TestSubscriber<Event> projection2 = new TestSubscriber<>();
    es.getEvents(stream("A")).subscribe(projection1);
    es.getEvents(stream("B")).subscribe(projection2);
    Event e = event(LocalDateTime.now(), eventType("test"), eventData(new byte[]{}));

    es.append(stream("A"), e);

    projection1.assertValues(e);
    projection2.assertNoValues();
  }

  @Test
  public void can_provide_all_events_received_so_far() {
    EventStore es = new MemoryEventStore();
    Event e1 = event(LocalDateTime.now(), eventType("test"), eventData(new byte[]{}));
    Event e2 = event(LocalDateTime.now(), eventType("test"), eventData(new byte[]{}));

    es.append(stream("_"), e1);
    es.append(stream("_"), e2);

    List<Event> events = es.getAll(stream("_"));
    assertEquals(2, events.length());
    assertTrue(events.toJavaList().contains(e1));
    assertTrue(events.toJavaList().contains(e2));
  }

  @Test
  public void can_provide_all_events_from_a_certain_instant() {
    EventStore es = new MemoryEventStore();
    Event e1 = event(LocalDateTime.of(2015, 7, 21, 22, 52), eventType("test"), eventData(new byte[]{}));
    Event e2 = event(LocalDateTime.of(2015, 7, 23, 22, 52), eventType("test"), eventData(new byte[]{}));

    es.append(stream("_"), e1);
    es.append(stream("_"), e2);

    List<Event> events = es.getFrom(stream("_"), LocalDateTime.of(2015, 7, 23, 22, 52));
    assertEquals(1, events.length());
    assertTrue(events.toJavaList().contains(e2));
  }

  @Test
  public void can_subscribe_to_a_stream_of_all_events() {
    EventStore es = new MemoryEventStore();
    Event e1 = event(LocalDateTime.now(), eventType("test"), eventData(new byte[]{}));
    Event e2 = event(LocalDateTime.now(), eventType("test"), eventData(new byte[]{}));
    Event e3 = event(LocalDateTime.now(), eventType("test"), eventData(new byte[]{}));
    es.append(stream("_"), e1);
    es.append(stream("_"), e2);
    TestSubscriber<Event> projection1 = new TestSubscriber<>();

    es.getAllEvents(stream("_")).subscribe(projection1);
    es.append(stream("_"), e3);

    projection1.assertReceivedOnNext(list(e1, e2, e3).toJavaList());
  }

  @Test
  public void can_subscribe_to_a_stream_of_all_events_from_a_certain_date() {
    EventStore es = new MemoryEventStore();
    Event e1 = event(LocalDateTime.of(2015, 7, 21, 22, 52), eventType("test"), eventData(new byte[]{}));
    Event e2 = event(LocalDateTime.of(2015, 7, 27, 22, 52), eventType("test"), eventData(new byte[]{}));
    Event e3 = event(LocalDateTime.of(2015, 7, 27, 22, 52), eventType("test"), eventData(new byte[]{}));
    es.append(stream("_"), e1);
    es.append(stream("_"), e2);
    TestSubscriber<Event> projection1 = new TestSubscriber<>();

    es.getAllEventsFrom(stream("_"), LocalDateTime.of(2015, 7, 27, 22, 52)).subscribe(projection1);
    es.append(stream("_"), e3);

    projection1.assertReceivedOnNext(list(e2, e3).toJavaList());
  }
}