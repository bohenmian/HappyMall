package cn.edu.swpu.cins.service.impl;

import cn.edu.swpu.cins.service.PasswordService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean match(String password, String encodedPassword) {
        return encoder.matches(password, encodedPassword);
    }
}
