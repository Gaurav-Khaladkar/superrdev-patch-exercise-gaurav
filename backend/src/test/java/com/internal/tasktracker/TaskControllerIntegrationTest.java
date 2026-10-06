package com.internal.tasktracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void filtersBothTitleAndDescriptionMatchesByStatus() throws Exception {
        mockMvc.perform(get("/api/tasks")
                        .param("status", "OPEN")
                        .param("pageSize", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[*].status", everyItem(is("OPEN"))));
    }

    @Test
    void excludesArchivedTasksWhenTheirDescriptionMatchesTheSearch() throws Exception {
        mockMvc.perform(get("/api/tasks")
                        .param("q", "legacy")
                        .param("status", "DONE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test
    void rejectsInvalidFilterAndPaginationParameters() throws Exception {
        mockMvc.perform(get("/api/tasks").param("status", "INVALID"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/tasks").param("page", "0"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/tasks").param("pageSize", "101"))
                .andExpect(status().isBadRequest());
    }
}
