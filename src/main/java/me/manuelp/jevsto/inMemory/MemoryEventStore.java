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
    List<Event> es = iterableList(store).filter(createdAtOrAfter(filters.getFrom()))
        .filter(ofAggregateType(filters.getAggregateType())).filter(ofAggregateID(filters.getAggregateID()));
    return filters.getMaxEvents().isSome() ? es.take(filters.getMaxEvents().some()) : es;
  }

  private F<Event, Boolean> createdAtOrAfter(final Option<Instant> from) {
    return new F<Event, Boolean>() {
      @Override
      public Boolean f(Event e) {
        return from.isNone() || e.getTimestamp().equals(from.some()) || e.getTimestamp().isAfter(from.some());
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
