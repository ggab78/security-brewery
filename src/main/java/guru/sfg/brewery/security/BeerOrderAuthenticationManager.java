package guru.sfg.brewery.security;

import guru.sfg.brewery.domain.security.User;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public class BeerOrderAuthenticationManager {

    public boolean isCustomerMatch(Authentication authentication, UUID customerId){

        User authenticatedUser = (User) authentication.getPrincipal();

        return authenticatedUser.getCustomer().getId().equals(customerId);

    }


}
