package me.manuelp.jevsto.inMemory;

import static fj.data.List.iterableList;

import fj.F;
import fj.data.List;
import fj.data.Option;
import java.util.ArrayList;
import java.util.UUID;
import me.manuelp.jevsto.EventStore;
import me.manuelp.jevsto.dataTypes.Event;
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
  public synchronized List<Event> getFrom(final Option<Instant> from) {
    return iterableList(store).filter(new F<Event, Boolean>() {
      @Override
      public Boolean f(Event e) {
        return from.isNone() || e.getTimestamp().equals(from.some()) || e.getTimestamp().isAfter(from.some());
      }
    });
  }

  /*--------------------------------------------------------------------------
   * Message broker methods
   *--------------------------------------------------------------------------*/

  @Override
  public Observable<Event> stream() {
    return stream;
  }
}
