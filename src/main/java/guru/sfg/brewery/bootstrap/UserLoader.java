package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (authorityRepository.count() == 0) {
            loadUsers();
        }

    }

    private void loadUsers() {
//beer auths
        Authority createBeer = authorityRepository.save(Authority.builder().permission("create.beer").build());
        Authority readBeer = authorityRepository.save(Authority.builder().permission("read.beer").build());
        Authority updateBeer = authorityRepository.save(Authority.builder().permission("update.beer").build());
        Authority deleteBeer = authorityRepository.save(Authority.builder().permission("delete.beer").build());
//customer auths
        Authority createCustomer = authorityRepository.save(Authority.builder().permission("create.customer").build());
        Authority readCustomer = authorityRepository.save(Authority.builder().permission("read.customer").build());
        Authority updateCustomer = authorityRepository.save(Authority.builder().permission("update.customer").build());
        Authority deleteCustomer = authorityRepository.save(Authority.builder().permission("delete.customer").build());

        //brewery auths
        Authority createBrewery = authorityRepository.save(Authority.builder().permission("create.brewery").build());
        Authority readBrewery = authorityRepository.save(Authority.builder().permission("read.brewery").build());
        Authority updateBrewery = authorityRepository.save(Authority.builder().permission("update.brewery").build());
        Authority deleteBrewery = authorityRepository.save(Authority.builder().permission("delete.brewery").build());


        //beer order auths
        Authority createOrder = authorityRepository.save(Authority.builder().permission("create.order").build());
        Authority readOrder = authorityRepository.save(Authority.builder().permission("read.order").build());
        Authority updateOrder = authorityRepository.save(Authority.builder().permission("update.order").build());
        Authority deleteOrder = authorityRepository.save(Authority.builder().permission("delete.order").build());
        Authority createOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.create.order").build());
        Authority readOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.read.order").build());
        Authority updateOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.update.order").build());
        Authority deleteOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.delete.order").build());


        Role adminRole = roleRepository.save(Role.builder().roleName("ADMIN").build());
        Role userRole = roleRepository.save(Role.builder().roleName("USER").build());
        Role customerRole = roleRepository.save(Role.builder().roleName("CUSTOMER").build());

        adminRole.setAuthorities(new HashSet<>(Set.of(createBeer, readBeer, updateBeer, deleteBeer,
                                        createCustomer, readCustomer, updateCustomer, deleteCustomer,
                                        createBrewery, readBrewery, updateBrewery, deleteBrewery,
                                        createOrder, readOrder,updateOrder,deleteOrder)));

        customerRole.setAuthorities(new HashSet<>(Set.of(readBeer, readCustomer, readBrewery,
                                                            createOrder,readOrder,updateOrder, deleteOrder)));

        userRole.setAuthorities(new HashSet<>(Set.of(readBeer)));

        roleRepository.saveAll(Arrays.asList(adminRole, customerRole, userRole));

        userRepository.save(User.builder()
                .username("gab")
                .password(passwordEncoder.encode("hugo"))
                .role(adminRole)
                .build());

        userRepository.save(User.builder()
                .username("hugo")
                .password(passwordEncoder.encode("boss"))
                .role(customerRole)
                .build());

        userRepository.save(User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .role(userRole)
                .build());


        log.debug("Users loaded : " + userRepository.count());

    }
}
