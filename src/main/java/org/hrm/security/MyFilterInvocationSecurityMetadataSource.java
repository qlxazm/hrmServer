package org.hrm.security;

import org.hrm.domain.Permission;
import org.hrm.domain.Role;
import org.hrm.service.HrmService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import java.util.*;

/**
 * @author qian
 */
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final Map<String, Collection<ConfigAttribute>> urlRoleMap = new HashMap<>();


    public MyFilterInvocationSecurityMetadataSource(RedisTemplate<String, String> template, HrmService service) {
        List<Permission> permissions = service.getAllPermissions();
        for (Permission permission : permissions) {
            List<ConfigAttribute> roleList = new ArrayList<>();
            for (Role role : permission.getRoles()){
                roleList.add(new SecurityConfig(role.getRoleName()));
            }
            urlRoleMap.put(permission.getPermission(), roleList);
        }

    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //获取请求的资源路径
        FilterInvocation fi = (FilterInvocation)o;
        String url = fi.getRequestUrl();

        for (Map.Entry<String, Collection<ConfigAttribute>> entry : urlRoleMap.entrySet()) {
            if (antPathMatcher.match(entry.getKey(), url)) {
                return entry.getValue();
            }
        }
        return new ArrayList<>();
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
