package com.carbon.quiz.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class FilterTest {
    @Test
    void respondsWith429AndRetryAfterWhenIpHasExceededItsLimit() throws Exception {
        String ip = "198.51.100.20";
        RateLimit rateLimit = new RateLimit();
        for (int i = 0; i < 30; i++) { 
            // Too lazy to make a test-oriented constructor and all that
            // Don't change the 30 or the test fails, keep it above 30, it's the default limit
            rateLimit.tryConsume(ip);
}        TokenVerifier verifier = mock(TokenVerifier.class);
        Filter filter = new Filter(verifier, rateLimit);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/quiz/history");
        request.setServletPath("/api/quiz/history");
        request.setRemoteAddr(ip);
        request.addHeader("Authorization", "Bearer valid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertEquals(429, response.getStatus());
        assertNotNull(response.getHeader("Retry-After"));
        verifyNoInteractions(verifier);
    }
}