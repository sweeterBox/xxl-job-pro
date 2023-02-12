package com.xxl.job.admin;

import org.springframework.util.DigestUtils;

/**
 * @author sweeter
 * @date 2023/2/11
 */
public class PasswordTest {

    public static void main(String[] args) {
        String slat = DigestUtils.md5DigestAsHex("xxljob".getBytes());
        String passwordMd5 = DigestUtils.md5DigestAsHex((DigestUtils.md5DigestAsHex("xxljob".getBytes()) + slat).getBytes());
        System.out.println(passwordMd5);
    }
}
