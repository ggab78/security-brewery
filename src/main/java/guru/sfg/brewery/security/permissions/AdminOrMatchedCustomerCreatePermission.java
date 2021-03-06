package guru.sfg.brewery.security.permissions;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('order.create') OR " +
        "hasAuthority('customer.order.create') AND @beerOrderAuthenticationManager.isCustomerMatch(authentication, #customerId)")
public @interface AdminOrMatchedCustomerCreatePermission {
}
