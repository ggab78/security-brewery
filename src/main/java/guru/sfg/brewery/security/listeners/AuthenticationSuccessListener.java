package guru.sfg.brewery.security.listeners;

import guru.sfg.brewery.domain.security.LoginSuccsess;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.LoginSuccessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Slf4j
@Component
public class AuthenticationSuccessListener {


    private final LoginSuccessRepository loginSuccessRepository;


    @EventListener
    public void listen(AuthenticationSuccessEvent event) {

        LoginSuccsess.LoginSuccsessBuilder builder = LoginSuccsess.builder();

        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();

            if (token.getPrincipal() instanceof User) {
            User user = (User)token.getPrincipal();

                builder.user(user);

            log.debug("User logged : "+user.getUsername());
            }
            if (token.getDetails() instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails auth = (WebAuthenticationDetails) token.getDetails();
                builder.sourceIp(auth.getRemoteAddress());
                log.debug("Remote address : "+auth.getRemoteAddress());
            }
        }

        LoginSuccsess savedLoginSuccess=loginSuccessRepository.save(builder.build());

        log.debug("Success Login saved :" + savedLoginSuccess.getId());
    }



}
