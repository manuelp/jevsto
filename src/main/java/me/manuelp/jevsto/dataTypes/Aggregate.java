package me.manuelp.jevsto.dataTypes;

import fj.F;

public interface Aggregate<T> {
  AggregateID id();

  AggregateType type();

  F<T, F<Event, T>> apply();
}
