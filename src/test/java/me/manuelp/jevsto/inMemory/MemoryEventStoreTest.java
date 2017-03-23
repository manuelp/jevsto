package me.manuelp.jevsto.inMemory;

import static me.manuelp.jevsto.dataTypes.AggregateID.aggregateID;
import static me.manuelp.jevsto.dataTypes.AggregateType.aggregateType;
import static me.manuelp.jevsto.dataTypes.Event.event;
import static me.manuelp.jevsto.dataTypes.EventData.eventData;
import static me.manuelp.jevsto.dataTypes.EventType.eventType;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import fj.data.List;
import fj.data.Option;
import me.manuelp.jevsto.EventStore;
import me.manuelp.jevsto.dataTypes.Event;
import org.junit.Before;
import org.junit.Test;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;
import rx.Scheduler;
import rx.functions.Func1;
import rx.observers.TestSubscriber;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

public class MemoryEventStoreTest {
  private EventStore es;

  @Before
  public void setUp() {
    RxJavaHooks.setOnComputationScheduler(new Func1<Scheduler, Scheduler>() {
      @Override
      public Scheduler call(Scheduler s) {
        return Schedulers.immediate();
      }
    });
    es = new MemoryEventStore();
  }

  @Test
  public void notifies_all_projections_of_new_events() {
    TestSubscriber<Event> projection1 = new TestSubscriber<>();
    TestSubscriber<Event> projection2 = new TestSubscriber<>();
    TestSubscriber<Event> projection3 = new TestSubscriber<>();
    es.stream().subscribe(projection1);
    es.stream().subscribe(projection2);
    es.stream().subscribe(projection3);
    Event e = event(aggregateType("_"), aggregateID("x"), Instant.now(), eventType("test"), eventData(new byte[] {}));

    es.append(e);

    projection1.assertValues(e);
    projection2.assertValues(e);
    projection3.assertValues(e);
  }

  @Test
  public void can_find_a_specific_event_by_ID() {
    Event e1 = event(aggregateType("_"), aggregateID("x"), Instant.now(), eventType("test"), eventData(new byte[] {}));
    es.append(e1);

    Option<Event> found = es.getById(e1.getId());

    assertThat(found.some(), is(e1));
  }

  @Test
  public void can_provide_all_events_from_a_certain_point_in_time() {
    Event e1 = event(aggregateType("_"), aggregateID("x"),
        LocalDateTime.parse("2017-03-20T15:00:00").toInstant(ZoneOffset.UTC), eventType("test"),
        eventData(new byte[] {}));
    Event e2 = event(aggregateType("_"), aggregateID("x"),
        LocalDateTime.parse("2017-03-21T15:00:00").toInstant(ZoneOffset.UTC), eventType("test"),
        eventData(new byte[] {}));
    Event e3 = event(aggregateType("_"), aggregateID("x"),
        LocalDateTime.parse("2017-03-21T16:30:00").toInstant(ZoneOffset.UTC), eventType("test"),
        eventData(new byte[] {}));
    es.append(e1);
    es.append(e2);
    es.append(e3);

    List<Event> events = es.getFrom(Option.some(LocalDateTime.parse("2017-03-21T15:00:00").toInstant(ZoneOffset.UTC)));

    assertThat(events, is(List.list(e2, e3)));
  }
}