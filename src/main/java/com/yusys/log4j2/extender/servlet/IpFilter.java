package com.yusys.log4j2.extender.servlet;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(urlPatterns = "/*",filterName = "myFilter")
public class IpFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(IpFilter.class);
    private ThreadLocal<String> threadLocal = new ThreadLocal<String>();
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        threadLocal.set(request.getRemoteAddr());
        ThreadContext.put("ip", threadLocal.get());
        threadLocal.remove();
        chain.doFilter(request,response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {
        // 清除日志上下文
        ThreadContext.clearAll();
        threadLocal.remove();
        logger.info("容器销毁,清除日志上下文ThreadContext");
    }
}
