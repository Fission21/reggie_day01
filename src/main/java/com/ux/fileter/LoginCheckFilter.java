package com.ux.fileter;

import com.alibaba.fastjson.JSON;
import com.ux.common.BaseContext;
import com.ux.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录检查过滤器
 * 检查用户是否完成登录
 * @author john
 * @version 1.1
 */

// urlPatterns = *  所有的请求都拦截
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    // 路径匹配器，支持通配符
    public static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1、获取本次请求的URI
        String requestURI = request.getRequestURI();
        // log.info("拦截到请求,{}",requestURI);

        // 不需要处理的请求路径，其中backend目录下都是静态资源，我们的核心是拦截页面通过ajax请求的动态数据。
        // 如果只是配置/backend/**这种通配符是无法拦截到 /backend/index.html 这种路径的，所以要使用路径匹配器
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };

        // 2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        // 3、如果不需要处理，则直接放行
        if (check){
            // log.info("本次请求不需要处理,{}",requestURI);
            chain.doFilter(request,response);
            return; // 如果匹配上直接放行了，那么后边的代码也没有必要再执行了，直接结束整个方法
        }

        // 4、判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("employee") != null){
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            chain.doFilter(request,response);
            return;
        }

        log.info("用户未登录");
        // 5、如果未登录则返回未登录结果,通过输出流的方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        // 遍历我们放行的路径和前端传过来的路径做比较，如果匹配上了是放行路径，就返回一个true直接放行
        // 如果没有找到放心路径就返回false
        for (String url : urls) {
            boolean match = ANT_PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
