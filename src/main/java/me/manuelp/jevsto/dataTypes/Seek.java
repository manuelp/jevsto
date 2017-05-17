package me.manuelp.jevsto.dataTypes;

import fj.data.Either;
import fj.data.Option;
import java.util.UUID;
import me.manuelp.jevsto.NotNull;
import org.threeten.bp.LocalDateTime;

public class Seek {
  private final Option<Either<LocalDateTime, UUID>> offset;
  private final int max;

  private Seek(Option<Either<LocalDateTime, UUID>> offset, int max) {
    NotNull.check(offset);
    this.offset = offset;
    this.max = max;
  }

  public static Seek seek(int max) {
    return new Seek(Option.<Either<LocalDateTime, UUID>> none(), max);
  }

  public static Seek seek(Either<LocalDateTime, UUID> offset, int max) {
    return new Seek(Option.some(offset), max);
  }

  public static Seek seek(LocalDateTime ts, int max) {
    return new Seek(Option.some(Either.<LocalDateTime, UUID> left(ts)), max);
  }

  public static Seek seek(UUID eventID, int max) {
    return new Seek(Option.some(Either.<LocalDateTime, UUID> right(eventID)), max);
  }

  public Option<Either<LocalDateTime, UUID>> getOffset() {
    return offset;
  }

  public int getMax() {
    return max;
  }
}
