package lab4;

public class Warehouse {
  private CargoType itemType;

  private int itemCount;

  public Warehouse(CargoType newItemType) {
    this.itemType = newItemType;
    this.itemCount = 0;
  }

  public CargoType getItemType() {
    return itemType;
  }
  
  public int getCount() {
    return itemCount;
  }

  public synchronized void add(int count) {
    itemCount += count;
    notifyAll();
  }
}
