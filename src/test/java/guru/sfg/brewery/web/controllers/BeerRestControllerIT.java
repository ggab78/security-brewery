package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.domain.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
public class BeerRestControllerIT extends BaseIT {


    @Test
    void deleteBeerWithUrlParamAuth() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/" + UUID.randomUUID())
                .param("Api-User", "gab")
                .param("Api-Password", "hugo"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBeerBadCredentials() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/" + UUID.randomUUID())
                .header("Api-User", "gab")
                .header("Api-Password", "xxxx"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/" + UUID.randomUUID())
                .header("Api-User", "gab")
                .header("Api-Password", "hugo"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBeerWithHttpBasic() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/" + UUID.randomUUID())
                .with(httpBasic("gab", "hugo")))
                .andExpect(status().is2xxSuccessful());
    }


    @Test
    void deleteBeerWithHttpBasicUserRole() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/" + UUID.randomUUID())
                .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteBeerWithHttpBasicCustomerRole() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/" + UUID.randomUUID())
                .with(httpBasic("hugo", "boss")))
                .andExpect(status().isForbidden());
    }


    @Test
    void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/" + UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer")
                .with(httpBasic("user", "password")))
                .andExpect(status().isOk());
    }


    @Test
    void findBeerById() throws Exception {
        mockMvc.perform(get("/api/v1/beer/" + UUID.randomUUID())
                .with(httpBasic("gab", "hugo")))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/*")
                .with(httpBasic("hugo", "boss")))
                .andExpect(status().isOk());
    }


    @Test
    void findBeerFormAdmin() throws Exception {
        Beer beer = Beer.builder()
                .beerName("tyskie")
                .id(UUID.randomUUID())
                .build();
        List<Beer> beerList = new ArrayList<>();
        beerList.add(beer);
        Page<Beer> beerPage = new PageImpl(beerList);

        when(beerRepository.findAllByBeerNameIsLike(anyString(), any())).thenReturn(beerPage);

        mockMvc.perform(get("/beers").param("beerName", "")
                .with(httpBasic("gab", "hugo")))
                .andExpect(status().is3xxRedirection());
    }

}