package org.hrm.security;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
//import org.springframework.security.web.access.expression.WebExpressionConfigAttribute;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.util.AntPathMatcher;

import java.util.*;

public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final Map<String, String> urlRoleMap = new HashMap<>();

    {
        urlRoleMap.put("/info", "ROLE_ROOT");
        urlRoleMap.put("/home", "ROLE_USER");
    }


    public MyFilterInvocationSecurityMetadataSource(FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource) {
        this.filterInvocationSecurityMetadataSource = filterInvocationSecurityMetadataSource;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //获取请求的资源路径
        FilterInvocation fi = (FilterInvocation)o;
        String url = fi.getRequestUrl();
        List<ConfigAttribute> attributeList = new ArrayList<>();

        for (Map.Entry<String, String> entry : urlRoleMap.entrySet()) {
            if (antPathMatcher.match(entry.getKey(), url)) {
//                attributeList.add(new WebExpressionConfigAttribute(entry.getValue()));
//                return SecurityConfig.createList(entry.getValue());
                attributeList.add(new SecurityConfig(entry.getValue()));
            }
        }
        return attributeList;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
