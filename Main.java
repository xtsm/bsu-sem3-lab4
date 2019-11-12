package lab4;
import java.util.*;
import java.util.concurrent.*;

public class Main {
  public static void main(String[] args) {
    UnloadingDock breadDock = new UnloadingDock(CargoType.BREAD);
    UnloadingDock mayoDock = new UnloadingDock(CargoType.MAYO);
    UnloadingDock sausagesDock = new UnloadingDock(CargoType.SAUSAGES);

    HashMap<CargoType, ShipAcceptor> docksMap = new HashMap<CargoType, ShipAcceptor>();
    docksMap.put(CargoType.BREAD, breadDock);
    docksMap.put(CargoType.MAYO, mayoDock);
    docksMap.put(CargoType.SAUSAGES, sausagesDock);
    SortingBay sortingBay = new SortingBay(docksMap);

    ShipGenerator shipGenerator = new ShipGenerator(sortingBay);

    new Thread(breadDock).start();
    new Thread(sausagesDock).start();
    new Thread(mayoDock).start();
    new Thread(sortingBay).start();
    new Thread(shipGenerator).start();

    List<String> names = Arrays.asList(new String[] {"Rob", "Jack", "Dave", "Michael", "John", "Daniel", "Eric", "Adolf"});
    Warehouse breadPile = new Warehouse(CargoType.BREAD);
    Warehouse mayoPile = new Warehouse(CargoType.MAYO);
    Warehouse sausagesPile = new Warehouse(CargoType.SAUSAGES);

    while (true) {
      Collections.shuffle(names);
      CountDownLatch robbers = new CountDownLatch(6);
      List<Thread> hoboThreads = new ArrayList<Thread>(8);
      hoboThreads.add(new Thread(new HoboRobber(names.get(0), breadDock.getWarehouse(), breadPile, robbers)));
      hoboThreads.add(new Thread(new HoboRobber(names.get(1), breadDock.getWarehouse(), breadPile, robbers)));
      hoboThreads.add(new Thread(new HoboRobber(names.get(2), mayoDock.getWarehouse(), mayoPile, robbers)));
      hoboThreads.add(new Thread(new HoboRobber(names.get(3), mayoDock.getWarehouse(), mayoPile, robbers)));
      hoboThreads.add(new Thread(new HoboRobber(names.get(4), sausagesDock.getWarehouse(), sausagesPile, robbers)));
      hoboThreads.add(new Thread(new HoboRobber(names.get(5), sausagesDock.getWarehouse(), sausagesPile, robbers)));
      hoboThreads.add(new Thread(new HoboCooker(names.get(6), mayoPile, breadPile, sausagesPile, robbers)));
      hoboThreads.add(new Thread(new HoboCooker(names.get(7), mayoPile, breadPile, sausagesPile, robbers)));

      for (Thread thread : hoboThreads) {
        thread.start();
      }

      for (Thread thread : hoboThreads) {
        try {
          thread.join();
        } catch (InterruptedException e) {
          e.printStackTrace();
          return;
        }
      }
    }
  }
}
