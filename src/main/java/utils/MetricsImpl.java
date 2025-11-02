package utils;

import java.util.HashMap;
import java.util.Map;

public class MetricsImpl implements Metrics {
    private long startTime;
    private long totalTime;
    private final Map<String, Long> counters = new HashMap<>();

    @Override
    public void start() {
        startTime = System.nanoTime();
    }

    @Override
    public void stop() {
        totalTime = System.nanoTime() - startTime;
    }

    @Override
    public void increment(String counter) {
        counters.put(counter, counters.getOrDefault(counter, 0L) + 1);
    }

    @Override
    public long getTimeNs() {
        return totalTime;
    }

    @Override
    public long getCounter(String name) {
        return counters.getOrDefault(name, 0L);
    }

    @Override
    public void reset() {
        counters.clear();
        totalTime = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Time (ns): ").append(totalTime).append("\n");
        for (Map.Entry<String, Long> entry : counters.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
