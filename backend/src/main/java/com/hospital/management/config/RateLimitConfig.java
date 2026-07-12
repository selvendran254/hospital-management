package com.hospital.management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class RateLimitConfig {

    @Value("${app.rate-limit.enabled:false}")
    private boolean enabled;

    @Value("${app.rate-limit.requests-per-minute:100}")
    private int requestsPerMinute;

    private final ConcurrentHashMap<String, RateLimitBucket> buckets = new ConcurrentHashMap<>();

    public boolean isEnabled() {
        return enabled;
    }

    public boolean tryConsume(String key) {
        if (!enabled) {
            return true;
        }
        RateLimitBucket bucket = buckets.computeIfAbsent(key, k -> new RateLimitBucket(requestsPerMinute));
        return bucket.tryConsume();
    }

    private static class RateLimitBucket {
        private final int maxRequests;
        private final AtomicInteger count = new AtomicInteger(0);
        private volatile long windowStart = System.currentTimeMillis();

        RateLimitBucket(int maxRequests) {
            this.maxRequests = maxRequests;
        }

        synchronized boolean tryConsume() {
            long now = System.currentTimeMillis();
            if (now - windowStart >= 60_000) {
                windowStart = now;
                count.set(0);
            }
            if (count.get() >= maxRequests) {
                return false;
            }
            count.incrementAndGet();
            return true;
        }
    }
}
