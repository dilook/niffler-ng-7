package guru.qa.niffler.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FriendsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Sql(scripts = "/friendsShouldBeReturned.sql")
    @Test
    void friendsShouldBeReturned() throws Exception {
        mockMvc.perform(get("/internal/friends/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "dima")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("petr"))
                .andExpect(jsonPath("$[0].fullname").value("Petr Janga"))
                .andExpect(jsonPath("$[0].currency").value("RUB"))
                .andExpect(jsonPath("$[0].photoSmall").isNotEmpty())
                .andExpect(jsonPath("$[1].username").value("viktor"))
                .andExpect(jsonPath("$[1].fullname").value("Viktor Gansales"))
                .andExpect(jsonPath("$[1].currency").value("RUB"))
                .andExpect(jsonPath("$[1].photoSmall").isNotEmpty());
    }

    @Sql(scripts = "/friendsShouldBeReturned.sql")
    @Test
    void friendsWithQueryShouldReturnFilteredList() throws Exception {
        mockMvc.perform(get("/internal/friends/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "dima")
                        .param("searchQuery", "viktor")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].username").value("viktor"))
                .andExpect(jsonPath("$[0].fullname").value("Viktor Gansales"))
                .andExpect(jsonPath("$[0].currency").value("RUB"))
                .andExpect(jsonPath("$[0].photoSmall").isNotEmpty());
    }

    @Sql(scripts = "/friendsShouldBeReturned.sql")
    @Test
    void friendShouldBeRemoved() throws Exception {
        mockMvc.perform(delete("/internal/friends/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "dima")
                        .param("targetUsername", "viktor")
                )
                .andExpect(status().isOk());

        // Verify the friend was removed by checking the list of friends
        mockMvc.perform(get("/internal/friends/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "dima")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].username").value("petr"));
    }
}
