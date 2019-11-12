package lab4;
import java.util.concurrent.*;
import java.util.HashMap;

public class SortingBay implements Runnable, ShipAcceptor {
  private static final int MAX_SHIPS = 5;

  private BlockingQueue<Ship> shipQueue;

  private HashMap<CargoType, ShipAcceptor> unloadingDocks;

  public SortingBay(HashMap<CargoType, ShipAcceptor> newUnloadingDocks) {
    this.shipQueue = new ArrayBlockingQueue<Ship>(MAX_SHIPS);
    this.unloadingDocks = newUnloadingDocks;
  }

  public void accept(Ship newShip) {
    try {
      shipQueue.put(newShip);
    } catch (InterruptedException e) {
      e.printStackTrace();
      return;
    }
  }

  public boolean tryAccept(Ship newShip) {
    return shipQueue.offer(newShip);
  }

  public void run() {
    while (true) {
      Ship newShip;
      try {
        newShip = shipQueue.take();
      } catch (InterruptedException e) {
        e.printStackTrace();
        return;
      }
      Logger.log(String.format("sorting bay: processing %s", newShip.toString()));
      CargoType cargoType = newShip.getCargoType();
      if (!unloadingDocks.containsKey(cargoType)) {
        Logger.log(String.format("sorting bay: sunk %s", newShip.toString()));
        continue;
      }
      ShipAcceptor dock = unloadingDocks.get(cargoType);
      dock.accept(newShip);
      Logger.log(String.format("sorting bay: dispatched %s", newShip.toString()));
    }
  }
}
