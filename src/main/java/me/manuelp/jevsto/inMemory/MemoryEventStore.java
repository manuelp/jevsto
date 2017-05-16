package me.manuelp.jevsto.inMemory;

import fj.F;
import fj.data.List;
import fj.data.Option;
import java.util.ArrayList;
import java.util.UUID;
import me.manuelp.jevsto.EventStore;
import me.manuelp.jevsto.dataTypes.Event;
import org.threeten.bp.LocalDateTime;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

public class MemoryEventStore implements EventStore {
  private final ArrayList<Event> queue;
  private final SerializedSubject<Event, Event> stream;

  public MemoryEventStore() {
    queue = new ArrayList<>();
    stream = new SerializedSubject<>(PublishSubject.<Event> create());
  }

  @Override
  public synchronized void append(final Event e) {
    queue.add(e);
    stream.onNext(e);
  }

  @Override
  public Observable<Event> getEvents() {
    return stream;
  }

  @Override
  public Observable<Event> getAllEvents() {
    return Observable.from(queue).concatWith(stream);
  }

  @Override
  public Observable<Event> getAllEventsFrom(final LocalDateTime from) {
    return getAllEvents().filter(Event.hasBeenCreatedFrom(from));
  }

  @Override
  public synchronized List<Event> getAll() {
    return List.iterableList(queue);
  }

  @Override
  public synchronized List<Event> getFrom(final LocalDateTime t) {
    return getAll().filter(new F<Event, Boolean>() {
      @Override
      public Boolean f(Event event) {
        return Event.hasBeenCreatedAtOrAfter(t).call(event);
      }
    });
  }

  @Override
  public List<Event> getFrom(LocalDateTime t, int max) {
    return getFrom(t).take(max);
  }

  @Override
  public List<Event> getFrom(final UUID id, int max) {
    return getAll().dropWhile(new F<Event, Boolean>() {
      @Override
      public Boolean f(Event e) {
        return !e.getId().equals(id);
      }
    }).take(max);
  }

  @Override
  public synchronized Option<Event> getById(final UUID id) {
    return getAll().find(new F<Event, Boolean>() {
      @Override
      public Boolean f(Event event) {
        return Event.hasId(id).call(event);
      }
    });
  }

}
