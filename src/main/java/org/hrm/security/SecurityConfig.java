package org.hrm.security;

import org.hrm.filter.MyTokenFilter;
import org.hrm.utils.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 注入自定义的UserDetailService
     */
    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    /**
     * 用于用户认证
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * 用于配置鉴权
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/info").hasRole("ROOT")
                .antMatchers("/home").hasRole("USER")
                .anyRequest().permitAll()
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(entryPointUnauthorizedHandler)
                .accessDeniedHandler(myAccessDeniedHandler);

        /**
         * 本次 json web token 权限控制的核心配置部分
         * 在 Spring Security 开始判断本次会话是否有权限时的前一瞬间
         * 通过添加过滤器将 token 解析，将用户所有的权限写入本次 Spring Security 的会话
         */
        http.addFilterBefore(myTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public MyTokenFilter myTokenFilter() {
        return new MyTokenFilter(tokenProvider, userDetailsService);
    }
}
