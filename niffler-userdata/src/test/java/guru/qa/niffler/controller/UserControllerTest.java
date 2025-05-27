package guru.qa.niffler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Sql(scripts = "/currentUserShouldBeReturned.sql")
  @Test
  void currentUserShouldBeReturned() throws Exception {
    mockMvc.perform(get("/internal/users/current")
            .contentType(MediaType.APPLICATION_JSON)
            .param("username", "dima")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("dima"))
        .andExpect(jsonPath("$.fullname").value("Dmitrii Tuchs"))
        .andExpect(jsonPath("$.currency").value("RUB"))
        .andExpect(jsonPath("$.photo").isNotEmpty())
        .andExpect(jsonPath("$.photoSmall").isNotEmpty());
  }

  @Sql(scripts = "/allUsersShouldBeReturned.sql")
  @Test
  void allUsersShouldReturnedWithoutCurrent() throws Exception {
    mockMvc.perform(get("/internal/users/all")
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
            .andExpect(jsonPath("$[0].status").doesNotExist())
            .andExpect(jsonPath("$[1].username").value("viktor"))
            .andExpect(jsonPath("$[1].fullname").value("Viktor Gansales"))
            .andExpect(jsonPath("$[1].currency").value("RUB"))
            .andExpect(jsonPath("$[1].photoSmall").isNotEmpty())
            .andExpect(jsonPath("$[1].status").doesNotExist());
  }

  @Sql(scripts = "/allUsersShouldBeReturned.sql")
  @Test
  void allUsersWithQueryShouldReturnedFilteredList() throws Exception {
    mockMvc.perform(get("/internal/users/all")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("username", "dima")
                    .param("searchQuery", "petr")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].username").value("petr"))
            .andExpect(jsonPath("$[0].fullname").value("Petr Janga"))
            .andExpect(jsonPath("$[0].currency").value("RUB"))
            .andExpect(jsonPath("$[0].photoSmall").isNotEmpty())
            .andExpect(jsonPath("$[0].status").doesNotExist());
  }

  @Sql(scripts = "/currentUserShouldBeReturned.sql")
  @Test
  void updatedUserShouldReturned() throws Exception {
      UserJson currentUser = new UserJson(UUID.fromString("a9165b45-a4aa-47d6-ac50-43611d624421"),
              "dima", null, null, "Ivan Kupala",
              CurrencyValues.RUB, null, null, null);
      mockMvc.perform(post("/internal/users/update")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(new ObjectMapper().writeValueAsBytes(currentUser))
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value("a9165b45-a4aa-47d6-ac50-43611d624421"))
          .andExpect(jsonPath("$.username").value("dima"))
          .andExpect(jsonPath("$.fullname").value("Ivan Kupala"))
          .andExpect(jsonPath("$.currency").value("RUB"))
          .andExpect(jsonPath("$.photoSmall").isNotEmpty())
          .andExpect(jsonPath("$.status").doesNotExist());
  }

}
