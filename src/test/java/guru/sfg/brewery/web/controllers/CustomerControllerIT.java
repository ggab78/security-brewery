package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.domain.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class CustomerControllerIT extends BaseIT {


    @DisplayName("find customer")
    @Nested
    class FindCustomer {
        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAdminCustomer")
        void testListCustomersAdminCustomer(String user, String pwd) throws Exception {

            Customer customer = Customer.builder()
                    .customerName("test")
                    .id(UUID.randomUUID())
                    .build();

            List<Customer> customers = new ArrayList<>();
            customers.add(customer);
            when(customerRepository.findAllByCustomerNameLike(anyString())).thenReturn(customers);

            mockMvc.perform(get("/customers").with(httpBasic(user, pwd)))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/customers/" + customer.getId()));
        }


        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamUser")
        void testListCustomersUser(String user, String pwd) throws Exception {

            mockMvc.perform(get("/customers").with(httpBasic(user, pwd)))
                    .andExpect(status().isForbidden());
        }


        @Test
        public void testListCustomersNoAuth() throws Exception {

            mockMvc.perform(get("/customers"))
                    .andExpect(status().isUnauthorized());

        }
    }


    @DisplayName("new customer")
    @Nested
    class NewCustomer {
        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAdmin")
        void testListCustomersAdminCustomer(String user, String pwd) throws Exception {
            Customer customer = Customer.builder()
                    .customerName("test")
                    .id(UUID.randomUUID())
                    .build();
            when(customerRepository.save(any())).thenReturn(customer);

            mockMvc.perform(post("/customers/new").with(httpBasic(user, pwd))
                    .param("customer", "foo"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/customers/" + customer.getId()));
        }


        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamNotAdmin")
        void testListCustomersUser(String user, String pwd) throws Exception {

            mockMvc.perform(post("/customers/new").with(httpBasic(user, pwd))
                    .param("customer", "foo"))
                    .andExpect(status().isForbidden());
        }


        @Test
        public void testListCustomersNoAuth() throws Exception {

            mockMvc.perform(post("/customers/new")
                    .param("customer", "foo"))
                    .andExpect(status().isUnauthorized());

        }
    }


}
