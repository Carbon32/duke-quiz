package com.carbon.quiz.security;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;

@Service
public class RateLimit {
    private final Cache<String, Bucket> buckets = Caffeine.newBuilder()
            .maximumSize(100000)
            .expireAfterAccess(Duration.ofMinutes(10))
            .build();

    public ConsumptionProbe tryConsume(String clientIp) {
        Bucket bucket = buckets.get(clientIp, ignored ->
                Bucket.builder()
                        .addLimit(limit -> limit
                                .capacity(30)
                                .refillGreedy(120, Duration.ofMinutes(1)))
                        .build());

        return bucket.tryConsumeAndReturnRemaining(1);
    }
}