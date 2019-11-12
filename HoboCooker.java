package lab4;
import java.util.concurrent.*;

public class HoboCooker implements Runnable {
  private String name;
  private Warehouse mayo;
  private Warehouse bread;
  private Warehouse sausages;
  private CountDownLatch robbers;

  public HoboCooker(String newName, Warehouse newMayo, Warehouse newBread, Warehouse newSausages, CountDownLatch newRobbers) {
    this.name = newName;
    this.mayo = newMayo;
    this.bread = newBread;
    this.sausages = newSausages;
    this.robbers = newRobbers;
  }

  public void run() {
    Logger.log(String.format("hobo %s: awaiting robbers", name));
    try {
      robbers.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
      return;
    }
    synchronized (mayo) {
      mayo.add(-8);
    }
    synchronized (bread) {
      bread.add(-8);
    }
    synchronized (sausages) {
      sausages.add(-8);
    }
    Logger.log(String.format("hobo %s: cooked ALS", name));
  }
}
