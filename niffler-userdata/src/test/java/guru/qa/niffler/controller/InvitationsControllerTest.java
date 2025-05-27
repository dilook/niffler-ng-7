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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class InvitationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Sql(scripts = "/invitationsShouldBeProcessed.sql")
    @Test
    void invitationShouldBeSent() throws Exception {
        mockMvc.perform(post("/internal/invitations/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "dima")
                        .param("targetUsername", "viktor")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("viktor"))
                .andExpect(jsonPath("$.friendshipStatus").value("INVITE_SENT"));
    }

    @Sql(scripts = "/invitationsShouldBeProcessed.sql")
    @Test
    void invitationShouldBeAccepted() throws Exception {
        mockMvc.perform(post("/internal/invitations/accept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "dima")
                        .param("targetUsername", "petr")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("petr"))
                .andExpect(jsonPath("$.friendshipStatus").value("FRIEND"));
    }

    @Sql(scripts = "/invitationsShouldBeProcessed.sql")
    @Test
    void invitationShouldBeDeclined() throws Exception {
        mockMvc.perform(post("/internal/invitations/decline")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "dima")
                        .param("targetUsername", "alex")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alex"))
                .andExpect(jsonPath("$.friendshipStatus").doesNotExist());
    }
}
