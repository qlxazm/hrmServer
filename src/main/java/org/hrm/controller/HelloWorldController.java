package org.hrm.controller;


import org.hrm.domain.Book;
import org.hrm.domain.Dept;
import org.hrm.domain.User;
import org.hrm.enums.ResponseStatus;
import org.hrm.security.MyUserDetailsService;
import org.hrm.service.HrmService;
import org.hrm.utils.Response;
import org.hrm.utils.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloWorldController {
    @Autowired
    private HrmService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private TokenProvider tokenProvider;

    @RequestMapping(value = "/authenticate")
    public String authorize(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        //1、创建UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        //2、校验认证用户的合法性
        Authentication authentication = this.authenticationManager.authenticate(token);
        //3、保存校验之后的结果
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //4、加载userDetails
        UserDetails details = this.userDetailsService.loadUserByUsername(username);
        //5、生成自定义的token
        String tokenStr = this.tokenProvider.createToken(details);
        return tokenStr;
    }

    @RequestMapping("home")
    public String home() {
        String currentUser = "";
        Object principl = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principl instanceof UserDetails) {
            currentUser = ((UserDetails)principl).getUsername();
        }else {
            currentUser = principl.toString();
        }
        return "It's home page! Current user is " + currentUser;
    }

    @RequestMapping("info")
    public String info() {
        return "It's info page";
    }

    @RequestMapping("findBook")
    public Book findBook() {
        Book book = new Book();
        book.setName("hahah1");
        book.setPrice(100);
        book.setPrice(book.getPrice() + 10);
        return book;
    }

    @RequestMapping(value = "findDept")
    public Response findDept(@RequestBody Dept dept) {
        Dept dept1 = service.findDeptById(dept);
        return new Response(ResponseStatus.SUCCESS, dept1);
    }
}
