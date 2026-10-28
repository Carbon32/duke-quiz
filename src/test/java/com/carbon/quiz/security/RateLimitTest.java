package com.carbon.quiz.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RateLimitTest {
    @Test
    void rejectsRequestsAfterTheConfiguredBurstForOneIp() {
        RateLimit rateLimit = new RateLimit();
        String ip = "192.0.2.10";

        for (int request = 0; request < 30; request++) {
            assertTrue(rateLimit.tryConsume(ip).isConsumed(),
                    "Request " + (request + 1) + " should fit in the initial bucket");
        }

        assertFalse(rateLimit.tryConsume(ip).isConsumed(),
                "The next request should be rate limited");
    }

    @Test
    void tracksDifferentIpsInSeparateBuckets() {
        RateLimit rateLimit = new RateLimit();

        for (int request = 0; request < 30; request++) {
            assertTrue(rateLimit.tryConsume("192.0.2.11").isConsumed());
        }

        assertFalse(rateLimit.tryConsume("192.0.2.11").isConsumed());
        assertTrue(rateLimit.tryConsume("192.0.2.12").isConsumed());
    }
}