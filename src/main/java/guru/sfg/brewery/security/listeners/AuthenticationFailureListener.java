package guru.sfg.brewery.security.listeners;

import guru.sfg.brewery.domain.security.LoginFailure;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.LoginFailureRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Slf4j
@Component
public class AuthenticationFailureListener {


    private final LoginFailureRepository loginFailureRepository;
    private final UserRepository userRepository;


    @EventListener
    public void listen(AuthenticationFailureBadCredentialsEvent event) {


        LoginFailure.LoginFailureBuilder builder = LoginFailure.builder();


        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();

            if (token.getPrincipal() instanceof String) {
                String userName = (String) token.getPrincipal();
                userRepository.findByUsername(userName).ifPresent(builder::user);
                log.debug("Bed credentials error for user: " + userName);
            }
            if (token.getDetails() instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails auth = (WebAuthenticationDetails) token.getDetails();
                log.debug("Remote address : " + auth.getRemoteAddress());
                builder.sourceIp(auth.getRemoteAddress());
            }
        }

        LoginFailure savedLoginFailure = loginFailureRepository.save(builder.build());
        log.debug("Login failure saved :" + savedLoginFailure.getId());

        if(savedLoginFailure.getUser()!=null){
            lockUser(savedLoginFailure.getUser());
        }

    }

    private void lockUser(User user){

        Timestamp time = Timestamp.valueOf(LocalDateTime.now().minusDays(1));
        List<LoginFailure> failures = loginFailureRepository.findAllByUserAndCreateDateAfter(user,time);
        if(failures.size()>3){
            log.debug("Locking user :"+user.getUsername());
            user.setAccountNonLocked(false);
            userRepository.save(user);
        }
    }


}
