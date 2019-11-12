package lab4;

public class Logger {
  public static void log(String msg) {
    System.out.println(String.format("[%d] %s", System.currentTimeMillis(), msg));
  }
}
