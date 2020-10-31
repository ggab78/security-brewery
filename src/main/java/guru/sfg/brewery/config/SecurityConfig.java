package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    PasswordEncoder passwordEncoder(){
        //return NoOpPasswordEncoder.getInstance();
        //return new LdapShaPasswordEncoder();
        //return new StandardPasswordEncoder();
        //return new BCryptPasswordEncoder();
        //return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return CustomPasswordFactory.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests(auth->{
                    auth
                            .antMatchers("/","/webjars/**", "/login", "/resources/**").permitAll()
                            .antMatchers("/beers/find","/beers*").permitAll()
                            .antMatchers(HttpMethod.GET,"/api/v1/beer","/api/v1/beer/*").permitAll()
                            .mvcMatchers(HttpMethod.GET,"/api/v1/beerUpc/{upc}").permitAll();
                })
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("gab")
                .password("{bcrypt15}$2a$15$o3GNOHuosLqN6mfVIIXtveHaydAuiIKGquv65dkTBPdEFaWufo2z2")//for bcrypt
//                .password("{bcrypt}$2a$10$Up23Vp4h6dkBD.bsqhrEx.yfOyMT62iWv/E7ymL4hyza07ZfGK8ri")//for bcrypt
//              .password("{sha256}52dd91af279225298fd1e25ed368185ae3cdc8d93cd67e2f1497c3b74f07d78abf38931c1a2cd5e8")//for sha256
//                .password("{ldap}{SSHA}1CIB+3H5UKy1I44hZLnySzoUNCJbcgDduzMbPw==")//for ldap
//                .password("{noop}hugo")//for noop configuration
                .roles("ADMIN")

                .and()
                .withUser("user")
                .password("{bcrypt15}$2a$15$VcyneN62j/1julqHbpCpIeM/Khnyq9IhuLYTNuwAENZyUfvXvAPq6")//for bcrypt
//                .password("{bcrypt}$2a$10$X0IyFcUlKSMfO0Lk7BzuVOkHUj99/X6LebypYcxYOZ0xrrIg52Ki6")//for bcrypt
//                .password("{sha256}e8d5544810407492e04c3fbbd8a2619645a8547a8ef1bd8a325042b8521784c71115364a4ef85e43")//for sha256
//                .password("{ldap}{SSHA}UT0nOqMAtH1ah3ZTrNNZ+wRsFtyRfc7pWFMfUw==")//for ldap
//                .password("{noop}password") //for noop configuration
                .roles("USER")

                .and()
                .withUser("hugo")
                .password("{bcrypt15}$2a$15$mGJ8i6nnfFwk.klILApskeF8NmLDp1rfB07rGc8gS7ajDovHJ6NLS")//for bcrypt
//                .password("{bcrypt}$2a$10$x.WxWcJvqkId5e6w5w0FS.Xj8W0FOpqlo7RU5oouxLrjpiJXycGjK")//for bcrypt
//                .password("{sha256}6ab5d90bf21e491b1369b97aa277dc3b2f6e86bfb17bb2350cba08e323c963572d63755802cf3b77")//for sha256
//                .password("{ldap}{SSHA}qyEVZxw2Vk5761xxtMhgrUd9kBcWvG4twQPI6A==")//for ldap
//                .password("{noop}boss")//for noop configuration
                .roles("CUSTOMER");

    }

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//
//        UserDetails admin= User.withDefaultPasswordEncoder()
//                .username("gab")
//                .password("hugo")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user=User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }
}