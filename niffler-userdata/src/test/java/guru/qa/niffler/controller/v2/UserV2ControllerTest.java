package guru.qa.niffler.controller.v2;

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
class UserV2ControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Sql(scripts = "/allUsersShouldReturnedPageable.sql")
  @Test
  void allUsersShouldReturnedPageable() throws Exception {
    mockMvc.perform(get("/internal/v2/users/all")
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

    mockMvc.perform(get("/internal/v2/users/all")
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

  @Sql(scripts = "/allUsersShouldReturnedPageableFiltered.sql")
  @Test
  void allUsersWithQueryShouldReturnedPageableFilteredList() throws Exception {
    mockMvc.perform(get("/internal/v2/users/all")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("username", "dima")
                    .param("searchQuery", "ale")
                    .param("page", "0")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements").value(12))
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

    mockMvc.perform(get("/internal/v2/users/all")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("username", "dima")
                    .param("searchQuery", "ale")
                    .param("page", "1")
            )
            .andExpect(jsonPath("$.totalElements").value(12))
            .andExpect(jsonPath("$.totalPages").value(2))
            .andExpect(jsonPath("$.size").value(10))
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content.length()").value(2))
            .andExpect(jsonPath("$.number").value(1))
            .andExpect(jsonPath("$.sort.empty").value(true))
            .andExpect(jsonPath("$.sort.sorted").value(false))
            .andExpect(jsonPath("$.sort.unsorted").value(true))
            .andExpect(jsonPath("$.first").value(false))
            .andExpect(jsonPath("$.last").value(true))
            .andExpect(jsonPath("$.numberOfElements").value(2))
            .andExpect(jsonPath("$.empty").value(false));

    mockMvc.perform(get("/internal/v2/users/all")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("username", "dima")
                    .param("searchQuery", "alex")
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
  }
}