package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
public class BeerRestControllerIT extends BaseIT {


    @Test
    void deleteBeerWithUrlParamAuth() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/"+ UUID.randomUUID())
                .param("Api-User","gab")
                .param("Api-Password", "hugo"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBeerBadCredentials() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/"+ UUID.randomUUID())
                .header("Api-User","gab")
                .header("Api-Password", "xxxx"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/"+ UUID.randomUUID())
                .header("Api-User","gab")
                .header("Api-Password", "hugo"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBeerWithHttpBasic() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/"+ UUID.randomUUID())
                .with(httpBasic("gab","hugo")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/"+ UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }




    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer"))
                .andExpect(status().isOk());
    }


    @Test
    void findBeerById() throws Exception {
        mockMvc.perform(get("/api/v1/beer/"+ UUID.randomUUID()))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/*"))
                .andExpect(status().isOk());
    }




}