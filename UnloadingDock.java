package lab4;
import java.util.concurrent.*;

public class UnloadingDock implements Runnable, ShipAcceptor {
  private static final int UNLOAD_SPEED = 5;

  private BlockingQueue<Ship> shipQueue;

  public Warehouse warehouse;

  public UnloadingDock(CargoType newCargoType) {
    this.shipQueue = new SynchronousQueue<Ship>();
    this.warehouse = new Warehouse(newCargoType);
  }

  public void accept(Ship newShip) {
    try {
      shipQueue.put(newShip);
    } catch (InterruptedException e) {
      return;
    }
  }

  public boolean tryAccept(Ship newShip) {
    return shipQueue.offer(newShip);
  }

  public Warehouse getWarehouse() {
    return warehouse;
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
      if (newShip.getCargoType() != warehouse.getItemType()) {
        Logger.log(String.format("unloading dock %s: declined %s", warehouse.getItemType().toString(), newShip.toString()));
        continue;
      }
      Logger.log(String.format("unloading dock %s: accepted %s", warehouse.getItemType().toString(), newShip.toString()));
      synchronized (warehouse) {
        int cargoCount = newShip.getCargoCount();
        while (cargoCount > 0) {
          int delta = Math.min(cargoCount, UNLOAD_SPEED);
          cargoCount -= delta;
          warehouse.add(delta);
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
            return;
          }
        }
      }
      Logger.log(String.format("unloading dock %s: unloaded %s", warehouse.getItemType().toString(), newShip.toString()));
    }
  }
}
