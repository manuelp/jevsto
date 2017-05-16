package me.manuelp.jevsto.inMemory;

import static fj.data.List.iterableList;

import fj.F;
import fj.data.List;
import fj.data.Option;
import java.util.ArrayList;
import java.util.UUID;
import me.manuelp.jevsto.EventStore;
import me.manuelp.jevsto.dataTypes.AggregateID;
import me.manuelp.jevsto.dataTypes.AggregateType;
import me.manuelp.jevsto.dataTypes.Event;
import me.manuelp.jevsto.dataTypes.EventStoreFilters;
import me.manuelp.jevsto.dataTypes.Seek;
import org.threeten.bp.Instant;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

public class MemoryEventStore implements EventStore {
  private final ArrayList<Event> store;
  private final SerializedSubject<Event, Event> stream;

  public MemoryEventStore() {
    store = new ArrayList<>();
    stream = new SerializedSubject<>(PublishSubject.<Event> create());
  }

  /*--------------------------------------------------------------------------
   * DB methods
   *--------------------------------------------------------------------------*/

  @Override
  public synchronized void append(final Event e) {
    store.add(e);
    stream.onNext(e);
  }

  @Override
  public synchronized Option<Event> getById(final UUID id) {
    return iterableList(store).find(new F<Event, Boolean>() {
      @Override
      public Boolean f(Event e) {
        return e.getId().equals(id);
      }
    });
  }

  @Override
  public synchronized List<Event> fetch(EventStoreFilters filters) {
    List<Event> filtered = iterableList(store).filter(createdAtOrAfter(filters.getSeek().bind(Seek.getTimestampF())))
        .dropWhile(isNotLastSeenEvent(filters.getSeek().bind(Seek.getEventIDF())))
        .filter(ofAggregateType(filters.getAggregateType())).filter(ofAggregateID(filters.getAggregateID()));
    List<Event> batch = filters.getMaxEvents().isSome() ? filtered.take(filters.getMaxEvents().some()) : filtered;
    return batch.sort(Event.byTimestamp());
  }

  private F<Event, Boolean> createdAtOrAfter(final Option<Instant> from) {
    return new F<Event, Boolean>() {
      @Override
      public Boolean f(Event e) {
        return from.isNone() || e.getTimestamp().equals(from.some()) || e.getTimestamp().isAfter(from.some());
      }
    };
  }

  private F<Event, Boolean> isNotLastSeenEvent(final Option<UUID> e) {
    return new F<Event, Boolean>() {
      @Override
      public Boolean f(Event event) {
        return !e.isNone() && !e.some().equals(event.getId());
      }
    };
  }

  private F<Event, Boolean> ofAggregateType(final Option<AggregateType> type) {
    return new F<Event, Boolean>() {
      @Override
      public Boolean f(Event e) {
        return type.isNone() || e.getAggregateType().equals(type.some());
      }
    };
  }

  private F<Event, Boolean> ofAggregateID(final Option<AggregateID> id) {
    return new F<Event, Boolean>() {
      @Override
      public Boolean f(Event e) {
        return id.isNone() || e.getAggregateID().equals(id.some());
      }
    };
  }

  /*--------------------------------------------------------------------------
   * Message broker methods
   *--------------------------------------------------------------------------*/

  @Override
  public Observable<Event> stream() {
    return stream;
  }
}
