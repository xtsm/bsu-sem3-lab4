package lab4;

public class Ship {
  private CargoType cargoType;
  
  private int cargoCount;

  public Ship(CargoType type, int count) {
    this.cargoType = type;
    this.cargoCount = count;
  }

  public CargoType getCargoType() {
    return cargoType;
  }

  public int getCargoCount() {
    return cargoCount;
  }

  public String toString() {
    return String.format("ship, %d %s", cargoCount, cargoType.toString());
  }
}
