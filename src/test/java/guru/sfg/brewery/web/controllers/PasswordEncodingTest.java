package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import static org.junit.Assert.assertTrue;

public class PasswordEncodingTest {


    static final String PASSWORD="password";


    @Test
    void bcryptEncoding(){

        PasswordEncoder bcrypt = new BCryptPasswordEncoder();
        String encoded = bcrypt.encode(PASSWORD);
        System.out.println(encoded);
        assertTrue(bcrypt.matches(PASSWORD,encoded));

        System.out.println(bcrypt.encode("hugo"));
        System.out.println(bcrypt.encode("boss"));

    }

    @Test
    void sha256Encoding(){

        PasswordEncoder sha = new StandardPasswordEncoder();
        String encoded = sha.encode(PASSWORD);
        System.out.println(encoded);
        assertTrue(sha.matches(PASSWORD,encoded));

        System.out.println(sha.encode("hugo"));
        System.out.println(sha.encode("boss"));

    }


    @Test
    void ldapEncoding(){

        PasswordEncoder ldap = new LdapShaPasswordEncoder();
        String encoded = ldap.encode(PASSWORD);
        System.out.println(encoded);
        assertTrue(ldap.matches(PASSWORD,encoded));

        System.out.println(ldap.encode("hugo"));
        System.out.println(ldap.encode("boss"));

    }


    @Test
    void noOpEncoding(){

        PasswordEncoder noop=NoOpPasswordEncoder.getInstance();
        String encoded = noop.encode(PASSWORD);
        System.out.println(encoded);
        assertTrue(noop.matches(PASSWORD,encoded));
    }

    @Test
    void hashEncoding(){
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
    }

}
