package com.mayihi.store.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.interfaces.RSAKey;

@WebFilter(filterName = "AdminFilter",urlPatterns = "/admin/*",initParams = {@WebInitParam(name = "EXCLUDED_PAGES",value = "/admin/index.jsp")})
public class AdminFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String admin = (String) request.getSession().getAttribute("admin");
        if (null != admin) {
            chain.doFilter(req, resp);
        } else {
            request.setAttribute("msg","请先登录");
            request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
