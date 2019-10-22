package org.hrm.security;

import org.hrm.dao.RoleDao;
import org.hrm.dao.UserDao;
import org.hrm.domain.Role;
import org.hrm.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        //1、查询用户信息
        User user = new User();
        user.setUsername(s);
        user = userDao.findUserByUserName(user);

        if (user == null) {
            throw new UsernameNotFoundException(" User " + s + " was not find in db");
        }

        //2、设置角色
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        List<Role> roles = roleDao.findRolesByUserName(user);
        for (int i = 0, len = roles.size(); i < len; i++) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(roles.get(i).getRoleName());
            grantedAuthorities.add(grantedAuthority);
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
