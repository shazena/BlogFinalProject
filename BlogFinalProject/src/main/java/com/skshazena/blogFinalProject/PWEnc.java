package com.skshazena.blogFinalProject;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 21, 2020
 */
public class PWEnc {

    public static void main(String[] args) {
        String clearTxtPw = "creator";
        // BCrypt
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPw = encoder.encode(clearTxtPw);
        System.out.println(hashedPw);
    }
}
