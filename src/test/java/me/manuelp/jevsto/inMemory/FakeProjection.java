package me.manuelp.jevsto.inMemory;

import fj.F;
import fj.data.List;
import me.manuelp.jevsto.Projection;
import me.manuelp.jevsto.dataTypes.Event;

public class FakeProjection implements Projection {
  private List<Event> received;

  public FakeProjection() {
    received = List.list();
  }

  @Override
  public void f(Event event) {
    received = received.cons(event);
  }

  public List<Event> getReceived() {
    return received;
  }

  public boolean hasReceived(final Event e) {
    return received.find(new F<Event, Boolean>() {
      @Override
      public Boolean f(Event event) {
        return event.equals(e);
      }
    }).isSome();
  }
}
