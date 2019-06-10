package filter;

import javax.servlet.*;
import java.io.IOException;

public class SecondFilter implements Filter {
    public void init(FilterConfig filterConfig) {}

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("filter.SecondFilter");
    }

    public void destroy() {}
}
