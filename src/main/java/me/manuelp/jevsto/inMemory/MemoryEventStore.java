package me.manuelp.jevsto.inMemory;

import static fj.data.List.list;

import fj.F;
import fj.data.HashMap;
import fj.data.List;
import fj.data.Option;
import java.util.UUID;
import me.manuelp.jevsto.EventStore;
import me.manuelp.jevsto.dataTypes.Event;
import me.manuelp.jevsto.dataTypes.Stream;
import org.threeten.bp.LocalDateTime;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

public class MemoryEventStore implements EventStore {
  private final HashMap<Stream, List<Event>> container;
  private final HashMap<Stream, SerializedSubject<Event, Event>> streams;

  public MemoryEventStore() {
    container = HashMap.arrayHashMap();
    streams = HashMap.arrayHashMap();
  }

  private SerializedSubject<Event, Event> createStream() {
    return new SerializedSubject<>(PublishSubject.<Event>create());
  }

  @Override
  public List<Stream> getStreams() {
    return container.keys();
  }

  private List<Event> getList(Stream stream) {
    return container.get(stream).orSome(List.<Event>list());
  }

  private SerializedSubject<Event, Event> getStream(Stream stream) {
    SerializedSubject<Event, Event> s = streams.get(stream).orSome(createStream());
    streams.set(stream, s);
    return s;
  }

  @Override
  public void append(Stream stream, Event e) {
    List<Event> events = getList(stream);
    container.set(stream, events.append(list(e)));

    getStream(stream).onNext(e);
  }

  @Override
  public List<Event> getAll(Stream stream) {
    return getList(stream);
  }

  @Override
  public List<Event> getFrom(Stream stream, final LocalDateTime t) {
    return getAll(stream).filter(new F<Event, Boolean>() {
      @Override
      public Boolean f(Event event) {
        return Event.hasBeenCreatedAtOrAfter(t).call(event);
      }
    });
  }

  @Override
  public synchronized Option<Event> getById(final UUID id) {
    List<Event> all = List.join(getStreams().map(new F<Stream, List<Event>>() {
      @Override
      public List<Event> f(Stream s) {
        return getList(s);
      }
    }));
    return all.find(new F<Event, Boolean>() {
      @Override
      public Boolean f(Event event) {
        return Event.hasId(id).call(event);
      }
    });
  }

  @Override
  public Observable<Event> getEvents(Stream stream) {
    return getStream(stream);
  }

  @Override
  public Observable<Event> getAllEvents(Stream stream) {
    return Observable.from(getList(stream)).concatWith(getStream(stream));
  }

  @Override
  public Observable<Event> getAllEventsFrom(Stream stream, LocalDateTime from) {
    return getAllEvents(stream).filter(Event.hasBeenCreatedFrom(from));
  }

}
