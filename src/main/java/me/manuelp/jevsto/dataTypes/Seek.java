package me.manuelp.jevsto.dataTypes;

import fj.F;
import fj.data.Either;
import fj.data.Option;
import java.util.UUID;
import me.manuelp.jevsto.NotNull;
import org.threeten.bp.Instant;

/**
 * Seek indication to fetch {@link Event}s from an {@link me.manuelp.jevsto.EventStore}.
 */
public class Seek {
  private final Either<Instant, UUID> from;

  private Seek(Either<Instant, UUID> from) {
    NotNull.check(from);
    this.from = from;
  }

  public static Seek seek(Instant from) {
    return new Seek(Either.<Instant, UUID> left(from));
  }

  public static Seek seek(UUID id) {
    return new Seek(Either.<Instant, UUID> right(id));
  }

  public Either<Instant, UUID> getFrom() {
    return from;
  }

  public static F<Seek, Option<UUID>> getEventIDF() {
    return new F<Seek, Option<UUID>>() {
      @Override
      public Option<UUID> f(Seek seek) {
        return seek.getFrom().right().toOption();
      }
    };
  }

  public static F<Seek, Option<Instant>> getTimestampF() {
    return new F<Seek, Option<Instant>>() {
      @Override
      public Option<Instant> f(Seek seek) {
        return seek.getFrom().left().toOption();
      }
    };
  }
}
