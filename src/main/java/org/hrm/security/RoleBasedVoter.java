package org.hrm.security;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class RoleBasedVoter implements AccessDecisionVoter<Object> {
    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    /**
     *
     * @param authentication 这里面存放的是认证的用户的认证信息
     * @param o              这里存放的是要获取的资源的信息
     * @param collection     这里应该存放要获得资源o需要哪些 权限信息
     * @return
     */
    @Override
    public int vote(Authentication authentication, Object o, Collection<ConfigAttribute> collection) {
        if (authentication == null){return ACCESS_DENIED;}
        int result = ACCESS_ABSTAIN;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        //遍历authorities和collection，查看当前用户是否有权限访问当前资源
        //如果当前用户有访问资源的权限就放行
        for (ConfigAttribute attribute : collection) {
            if (attribute == null){continue;}
            if (this.supports(attribute)){
                result = ACCESS_DENIED;
                for (GrantedAuthority authority : authorities){
                    if (attribute.getAttribute().equals(authority.getAuthority())){
                        return ACCESS_GRANTED;
                    }
                }
            }
        }
        return result;
    }
}
