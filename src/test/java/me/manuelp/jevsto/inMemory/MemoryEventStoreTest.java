package me.manuelp.jevsto.inMemory;

import fj.data.List;
import me.manuelp.jevsto.EventStore;
import me.manuelp.jevsto.dataTypes.Event;
import org.junit.Test;
import org.threeten.bp.LocalDateTime;
import rx.observers.TestObserver;

import static me.manuelp.jevsto.dataTypes.Event.event;
import static me.manuelp.jevsto.dataTypes.EventData.eventData;
import static me.manuelp.jevsto.inMemory.TestEvent.testEvent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MemoryEventStoreTest {

  @Test
  public void notifies_all_projections_of_new_events() {
    EventStore es = new MemoryEventStore();
    TestObserver<Event> projection1 = new TestObserver<>();
    TestObserver<Event> projection2 = new TestObserver<>();
    TestObserver<Event> projection3 = new TestObserver<>();
    es.getEvents().subscribe(projection1);
    es.getEvents().subscribe(projection2);
    es.getEvents().subscribe(projection3);
    Event e = event(LocalDateTime.now(), testEvent(), eventData(new byte[]{}));

    es.append(e);

    projection1.assertReceivedOnNext(List.list(e).toJavaList());
    projection2.assertReceivedOnNext(List.list(e).toJavaList());
    projection3.assertReceivedOnNext(List.list(e).toJavaList());
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

  @Test
  public void can_subscribe_to_a_stream_of_all_events() {
    EventStore es = new MemoryEventStore();
    Event e1 = event(LocalDateTime.now(), testEvent(), eventData(new byte[]{}));
    Event e2 = event(LocalDateTime.now(), testEvent(), eventData(new byte[]{}));
    Event e3 = event(LocalDateTime.now(), testEvent(), eventData(new byte[]{}));
    es.append(e1);
    es.append(e2);
    TestObserver<Event> projection1 = new TestObserver<>();

    es.getAllEvents().subscribe(projection1);
    es.append(e3);

    projection1.assertReceivedOnNext(List.list(e1, e2, e3).toJavaList());
  }

  @Test
  public void can_subscribe_to_a_stream_of_all_events_from_a_certain_date() {
    EventStore es = new MemoryEventStore();
    Event e1 = event(LocalDateTime.of(2015, 7, 21, 22, 52), testEvent(), eventData(new byte[]{}));
    Event e2 = event(LocalDateTime.of(2015, 7, 27, 22, 52), testEvent(), eventData(new byte[]{}));
    Event e3 = event(LocalDateTime.of(2015, 7, 27, 22, 52), testEvent(), eventData(new byte[]{}));
    es.append(e1);
    es.append(e2);
    TestObserver<Event> projection1 = new TestObserver<>();

    es.getAllEventsFrom(LocalDateTime.of(2015, 7, 27, 22, 52)).subscribe(projection1);
    es.append(e3);

    projection1.assertReceivedOnNext(List.list(e2, e3).toJavaList());
  }
}