package org.hrm.security;

import org.hrm.service.HrmService;
import org.hrm.utils.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

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

    @Autowired
    private RedisTemplate<String, String> template;

    @Autowired
    private HrmService service;

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
                .accessDecisionManager(accessDecisionManager())
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(myFilterInvocationSecurityMetadataSource(o.getSecurityMetadataSource()));
                        return o;
                    }
                })
               /* .antMatchers("/info").hasAnyAuthority("ROLE_ROOT")
                .antMatchers("/home").hasAnyAuthority("ROLE_USER")*/
                .antMatchers("/user/login").permitAll()
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
    public MyTokenFilter myTokenFilter() throws Exception {
        return new MyTokenFilter(tokenProvider, userDetailsService, authenticationManagerBean());
    }

    /**
     * 自定义的AccessDecisionManager。需要在configure(HttpSecurity http)里面配置 .accessDecisionManager(accessDecisionManager())
     * 但是这里的RoleBasedVoter类并不完整，还差从DB中动态获取权限并判断的限制
     * @return
     */
    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> decisionVoters = Arrays.asList(
            new WebExpressionVoter(),
            new RoleBasedVoter(),
            new AuthenticatedVoter()
        );
        return new AffirmativeBased(decisionVoters);
    }
    /**
     * 自定义的FilterInvocationSecurityMetadataSource
     * @param filterInvocationSecurityMetadataSource
     * @return
     */
    @Bean
    public MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource(FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource) {
        return new MyFilterInvocationSecurityMetadataSource(template, service);
    }
}
