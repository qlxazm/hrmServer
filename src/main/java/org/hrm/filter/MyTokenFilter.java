package org.hrm.filter;

import org.hrm.security.MyUserDetailsService;
import org.hrm.utils.TokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MyTokenFilter extends GenericFilterBean {
    private TokenProvider tokenProvider;
    private MyUserDetailsService userDetailsService;
    private final static String AUTH_TOKEN_HEADER_NAME = "AuthToken";

    public MyTokenFilter(TokenProvider tokenProvider, MyUserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        //1、尝试从请求头中获取token
        String tokenStr = httpServletRequest.getHeader(AUTH_TOKEN_HEADER_NAME);

        if (tokenStr != null) {
            //2、尝试从token中解析出用户名
            String username = tokenProvider.getUsernameFromToken(tokenStr);
            if (username != null) {
                //3、尝试从数据库中查询出用户名对应的信息
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //4、验证token的合法性
                if (tokenProvider.validateToken(tokenStr, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
