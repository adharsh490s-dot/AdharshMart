// src/main/java/com/adharsh/adharshmart/filter/AuthFilter.java
package com.adharsh.adharshmart.filter;

import com.adharsh.adharshmart.dto.ApiResponse;
import com.adharsh.adharshmart.util.JsonUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter({"/api/v1/cart/*", "/api/v1/orders/*", "/api/v1/admin/*"})
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.fail("UNAUTHORIZED", "Active session required.")));
            return;
        }
        chain.doFilter(req, resp);
    }
}