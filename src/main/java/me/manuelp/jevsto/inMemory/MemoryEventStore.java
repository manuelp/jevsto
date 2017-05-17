package me.manuelp.jevsto.inMemory;

import fj.F;
import fj.data.Either;
import fj.data.List;
import fj.data.Option;
import java.util.ArrayList;
import java.util.UUID;
import me.manuelp.jevsto.EventStore;
import me.manuelp.jevsto.dataTypes.Event;
import me.manuelp.jevsto.dataTypes.EventType;
import me.manuelp.jevsto.dataTypes.Seek;
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
  public List<Event> fetch(Seek s, final Option<EventType> type) {

    return s.getOffset().map(Either.either_(new F<LocalDateTime, List<Event>>() {
      @Override
      public List<Event> f(LocalDateTime t) {
        return getFrom(t);
      }
    }, new F<UUID, List<Event>>() {
      @Override
      public List<Event> f(final UUID id) {
        return getAll().dropWhile(new F<Event, Boolean>() {
          @Override
          public Boolean f(Event e) {
            return !e.getId().equals(id);
          }
        });
      }
    })).orSome(getAll()).filter(new F<Event, Boolean>() {
      @Override
      public Boolean f(Event event) {
        return type.isNone() || event.getType().equals(type.some());
      }
    }).take(s.getMax());
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
