package lab4;
import java.util.concurrent.*;

public class HoboRobber implements Runnable {
  private String name;
  private Warehouse from;
  private Warehouse to;
  private CountDownLatch robbers;

  public HoboRobber(String newName, Warehouse newFrom, Warehouse newTo, CountDownLatch newRobbers) {
    this.name = newName;
    this.from = newFrom;
    this.to = newTo;
    this.robbers = newRobbers;
  }

  public void run() {
    while (true) {
      synchronized (to) {
        if (to.getCount() >= 8) {
          Logger.log(String.format("hobo %s: has 8 %s, finishing", name, to.getItemType().toString()));
          break;
        }
      }
      synchronized (from) {
        while (from.getCount() < 1) {
          try {
            from.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
            return;
          }
        }
        try {
          Thread.sleep(3000);
        } catch (InterruptedException e) {
          e.printStackTrace();
          return;
        }
        from.add(-1);
      }
      Logger.log(String.format("hobo %s: stolen %s", name, from.getItemType().toString()));
      synchronized (to) {
        to.add(1);
      }
      Logger.log(String.format("hobo %s: put %s", name, to.getItemType().toString()));
    }
    robbers.countDown();
  }
}
