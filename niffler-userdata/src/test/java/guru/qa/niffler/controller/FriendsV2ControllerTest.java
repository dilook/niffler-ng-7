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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FriendsV2ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Sql(scripts = "/friendsShouldBeReturnedPageable.sql")
    @Test
    void friendsShouldBeReturnedPageable() throws Exception {
        // Test first page (should contain 10 items)
        mockMvc.perform(get("/internal/v2/friends/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "dima")
                        .param("page", "0")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(11))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(10))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.sort.empty").value(true))
                .andExpect(jsonPath("$.sort.sorted").value(false))
                .andExpect(jsonPath("$.sort.unsorted").value(true))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(false))
                .andExpect(jsonPath("$.numberOfElements").value(10))
                .andExpect(jsonPath("$.empty").value(false));

        // Test second page (should contain 1 item)
        mockMvc.perform(get("/internal/v2/friends/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "dima")
                        .param("page", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(11))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.sort.empty").value(true))
                .andExpect(jsonPath("$.sort.sorted").value(false))
                .andExpect(jsonPath("$.sort.unsorted").value(true))
                .andExpect(jsonPath("$.first").value(false))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.empty").value(false));
    }

    @Sql(scripts = "/friendsShouldBeReturnedPageable.sql")
    @Test
    void friendsWithQueryShouldReturnFilteredPageableList() throws Exception {
        // Test with search query that matches multiple users
        mockMvc.perform(get("/internal/v2/friends/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "dima")
                        .param("searchQuery", "son") // Should match "Bob Johnson", "Charlie Wilson", "Frank Wilson"
                        .param("page", "0")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.sort.empty").value(true))
                .andExpect(jsonPath("$.sort.sorted").value(false))
                .andExpect(jsonPath("$.sort.unsorted").value(true))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.numberOfElements").value(3))
                .andExpect(jsonPath("$.empty").value(false));

        // Test with search query that matches a specific user
        mockMvc.perform(get("/internal/v2/friends/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "dima")
                        .param("searchQuery", "alex") // Should match only "Alex Smith"
                        .param("page", "0")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.sort.empty").value(true))
                .andExpect(jsonPath("$.sort.sorted").value(false))
                .andExpect(jsonPath("$.sort.unsorted").value(true))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.empty").value(false));
    }
}