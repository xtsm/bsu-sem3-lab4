package lab4;
import java.util.Random;

public class ShipGenerator implements Runnable {
  private ShipAcceptor shipOutput;

  public ShipGenerator(ShipAcceptor newShipOutput) {
    this.shipOutput = newShipOutput;
  }

  public void run() {
    Random rng = new Random();
    while (true) {
      int cargoCount = (rng.nextInt(3) + 1) * 10;
      CargoType cargoType;
      switch (rng.nextInt(3)) {
        case 0:
          cargoType = CargoType.BREAD;
          break;
        case 1:
          cargoType = CargoType.MAYO;
          break;
        case 2:
          cargoType = CargoType.SAUSAGES;
          break;
        default: // should never get to this branch
          cargoType = CargoType.CHILD_SLAVES;
      }
      Ship newShip = new Ship(cargoType, cargoCount);
      Logger.log(String.format("ship generator: created %s", newShip.toString()));
      try {
        Thread.sleep(rng.nextInt(5000) + 1000);
      } catch (InterruptedException e) {
        return;
      }
      if (!shipOutput.tryAccept(newShip)) {
        Logger.log(String.format("ship generator: sunk %s", newShip.toString()));
      } else {
        Logger.log(String.format("ship generator: dispatched %s", newShip.toString()));
      }
    }
  }
}
