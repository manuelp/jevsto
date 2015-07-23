package me.manuelp.jevsto.inMemory;

import fj.data.List;
import fj.function.Effect1;
import me.manuelp.jevsto.EventStore;
import me.manuelp.jevsto.Projection;
import me.manuelp.jevsto.dataTypes.Event;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MemoryEventStore implements EventStore {
  private final ConcurrentLinkedQueue<Event> queue;
  private List<Projection> projectors;

  public MemoryEventStore() {
    queue = new ConcurrentLinkedQueue<>();
    projectors = List.list();
  }

  @Override
  public void append(final Event e) {
    queue.add(e);
    projectors.foreachDoEffect(applyProjector(e));
  }

  private Effect1<Projection> applyProjector(final Event e) {
    return new Effect1<Projection>() {
      @Override
      public void f(Projection projection) {
        projection.f(e);
      }
    };
  }

  @Override
  public List<Event> getAll() {
    return List.iterableList(queue);
  }

  @Override
  public List<Event> getFrom(final LocalDateTime t) {
    return getAll().filter(Event.hasBeenCreatedAtOrAfter(t));
  }

  @Override
  public void subscribe(Projection p) {
    projectors = projectors.cons(p);
  }
}
