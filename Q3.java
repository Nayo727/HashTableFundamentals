import java.util.*;
import java.util.concurrent.*;
class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;
    DNSEntry(String domain, String ipAddress, int ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
    }
    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}
public class Q3 {
    private final int MAX_CACHE_SIZE = 100;
    private final Map<String, DNSEntry> cache;
    private int hits = 0;
    private int misses = 0;
    public Q3() {
        cache = new LinkedHashMap<>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        };
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            synchronized (cache) {
                Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();
                while (it.hasNext()) {
                    if (it.next().getValue().isExpired()) {
                        it.remove();
                    }
                }
            }
        }, 5, 5, TimeUnit.SECONDS);
    }
    public synchronized String resolve(String domain) {
        DNSEntry entry = cache.get(domain);
        if (entry != null && !entry.isExpired()) {
            hits++;
            return "Cache HIT → " + entry.ipAddress;
        } else {
            misses++;
            String ip = queryUpstreamDNS(domain);
            cache.put(domain, new DNSEntry(domain, ip, 300));
            return "Cache MISS → Queried upstream → " + ip;
        }
    }
    private String queryUpstreamDNS(String domain) {
        return "172.217." + new Random().nextInt(255) + "." + new Random().nextInt(255);
    }
    public String getCacheStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);
        return String.format("Hit Rate: %.2f%%, Hits=%d, Misses=%d", hitRate, hits, misses);
    }
    public static void main(String[] args) throws InterruptedException {
        Q3 dnsCache = new Q3();
        System.out.println(dnsCache.resolve("google.com"));
        System.out.println(dnsCache.resolve("google.com"));
        Thread.sleep(310_000);
        System.out.println(dnsCache.resolve("google.com"));
        System.out.println(dnsCache.getCacheStats());
    }
}
