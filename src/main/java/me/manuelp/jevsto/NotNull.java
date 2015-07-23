package me.manuelp.jevsto;

public class NotNull {
  public static void check(Object... xs) {
    for (Object x : xs) {
      if(x == null)
        throw new IllegalArgumentException("A mandatory value is null!");
    }
  }
}
