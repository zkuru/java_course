package filter;

import javax.servlet.*;
import java.io.IOException;

public class FirstFilter implements Filter {
    public void init(FilterConfig filterConfig) {}

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("First filter");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {}
}
