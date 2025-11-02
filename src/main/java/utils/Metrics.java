package utils;

public interface Metrics {
    void start();
    void stop();
    void increment(String counter);
    long getTimeNs();
    long getCounter(String name);
    void reset();
}
