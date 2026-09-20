// src/main/java/com/adharsh/adharshmart/filter/LoggingFilter.java
package com.adharsh.adharshmart.filter;

import org.slf4j.MDC;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.UUID;

@WebFilter("/*")
public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        String reqId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put("reqId", reqId);
        try {
            chain.doFilter(req, resp);
        } finally {
            MDC.remove("reqId");
        }
    }
}