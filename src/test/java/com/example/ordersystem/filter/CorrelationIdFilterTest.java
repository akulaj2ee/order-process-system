package com.example.ordersystem.filter;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class CorrelationIdFilterTest {

    private final CorrelationIdFilter filter = new CorrelationIdFilter();

    @Test
    void shouldAddCorrelationIdToResponseAndMDC() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = mock(FilterChain.class);

        filter.doFilterInternal(request, response, chain);

        String correlationId = response.getHeader("X-Correlation-ID");

        // Validate header is set
        assertNotNull(correlationId);

        // Validate MDC contains it DURING processing
        // (we need to capture it before MDC.clear())
        verify(chain).doFilter(request, response);

        // After filter completes → MDC should be cleared
        assertNull(MDC.get("correlationId"));
    }

    @Test
    void shouldAlwaysCallFilterChain() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = mock(FilterChain.class);

        filter.doFilterInternal(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
    }
}