package org.hrm.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return "abcd";
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        System.out.println("charSequence: " + charSequence);
        System.out.println("s: " + s);
        return true;
    }
}
