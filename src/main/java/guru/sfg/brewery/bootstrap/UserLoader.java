package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if(authorityRepository.count()==0){
            loadUsers();
        }

    }

    private void loadUsers() {

        Authority admin = Authority.builder().role("ADMIN").build();
        Authority userRole = Authority.builder().role("USER").build();
        Authority customer = Authority.builder().role("CUSTOMER").build();

        authorityRepository.save(admin);
        authorityRepository.save(userRole);
        authorityRepository.save(customer);

        User gab = User.builder()
                .username("gab")
                .password(passwordEncoder.encode("hugo"))
                .authority(admin)
                .build();


        User hugo = User.builder()
                .username("hugo")
                .password(passwordEncoder.encode("boss"))
                .authority(customer)
                .build();

        User user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .authority(userRole)
                .build();

        userRepository.save(gab);
        userRepository.save(hugo);
        userRepository.save(user);


        log.debug("Users loaded : "+userRepository.count());

    }
}
