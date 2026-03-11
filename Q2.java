import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Q2 {
    private Map<String, AtomicInteger> stockMap = new ConcurrentHashMap<>();
    private Map<String, Queue<Integer>> waitingList = new ConcurrentHashMap<>();
    public void addProduct(String productId, int initialStock) {
        stockMap.put(productId, new AtomicInteger(initialStock));
        waitingList.put(productId, new LinkedList<>());
    }
    public String checkStock(String productId) {
        AtomicInteger stock = stockMap.get(productId);
        if (stock == null) return "Product not found";
        return productId + " → " + stock.get() + " units available";
    }
    public synchronized String purchaseItem(String productId, int userId) {
        AtomicInteger stock = stockMap.get(productId);
        if (stock == null) return "Product not found";

        if (stock.get() > 0) {
            int remaining = stock.decrementAndGet();
            return "Success, " + remaining + " units remaining";
        } else {
            Queue<Integer> queue = waitingList.get(productId);
            queue.add(userId);
            return "Added to waiting list, position #" + queue.size();
        }
    }
    public List<Integer> getWaitingList(String productId) {
        Queue<Integer> queue = waitingList.get(productId);
        if (queue == null) return Collections.emptyList();
        return new ArrayList<>(queue);
    }
    public static void main(String[] args) {
        Q2 manager = new Q2();
        manager.addProduct("IPHONE15_256GB", 100);
        System.out.println(manager.checkStock("IPHONE15_256GB"));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));
        for (int i = 0; i < 98; i++) {
            manager.purchaseItem("IPHONE15_256GB", i);
        }
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 100000));
        System.out.println("Waiting list: " + manager.getWaitingList("IPHONE15_256GB"));
    }
}
