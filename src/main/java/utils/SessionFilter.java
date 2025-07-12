package utils;

import javax.servlet.annotation.WebFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebFilter(filterName = "sessionFilter",urlPatterns = {"/*"})
public class SessionFilter implements Filter {
 
    //标示符：表示当前用户未登录(可改为json样式：如下方我使用的是map)
    String NO_LOGIN = "403";
 
    Map<String, Object> rtnMap = new HashMap<String, Object>();
 
    //免登录就可访问的路径(比如:注册,登录,注册页面上的一些获取数据等)
    String[] includeUrls = new String[]{"getTaskForWX","getSelected","blacklist","systemSetting","bannerSetting","managementSetting"};
 
 
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        //当前请求的url
        String uri = request.getRequestURI();
        //判断url是否需要过滤
        boolean needFilter = isNeedFilter(uri);
        if (!needFilter) { //不需要过滤直接传给下一个过滤器
            filterChain.doFilter(servletRequest, servletResponse);
        } else { //需要过滤器
            // session中包含user对象,则是登录状态
            if(null != session && null != SessionUtils.getUser(request)){
                filterChain.doFilter(request, response);
//                System.out.println(SessionUtils.getUser(request).getCampus());
            }else{
                rtnMap.put("code", 500);
                rtnMap.put("errMsg", "您还未登录,请先登录！！！");
                response.setContentType("text/html; charset=utf-8");
                response.getWriter().write(JSON.toJSONString(rtnMap));
               
            }
        }
    }
 
    /**
     * @Author: wdd
     * @Description: 是否需要过滤
     * @Date: 2019-02-21 13:20:54
     * @param uri
     */
    public boolean isNeedFilter(String uri) {
 
        for (String includeUrl : includeUrls) {
            if(uri.contains(includeUrl)) {
                return true;
            }
        }
 
        return false;
    }
 
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
 
    }
 
    @Override
    public void destroy() {
 
    }
}

