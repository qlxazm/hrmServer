package org.hrm.controller;

import org.hrm.domain.User;
import org.hrm.enums.ResponseStatus;
import org.hrm.security.MyUserDetailsService;
import org.hrm.utils.ApiRequest;
import org.hrm.utils.Response;
import org.hrm.utils.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MyUserDetailsService userDetailsService;
    @Autowired
    private TokenProvider provider;


    /**
     * 登录
     * @param user
     * @return
     */
    @RequestMapping("/login")
    public Response login(@RequestBody User user) {
        String userName = user.getUsername();
        String password = user.getPassword();
        //1、创建Authentication
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
        //2、验证用户
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        //3、保存验证之后的结果
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //4、加载UserDetail
        UserDetails details = this.userDetailsService.loadUserByUsername(userName);
        //5、生成Token
        String tokenStr = this.provider.createToken(details);
        HashMap<String, String> result = new HashMap<>();
        result.put("token", tokenStr);
        return new Response(ResponseStatus.SUCCESS, result);
    }
}
