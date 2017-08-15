package cn.edu.swpu.cins.service;

public interface PasswordService {
    String encode(String password);

    boolean match(String password, String encodedPassword);
}
