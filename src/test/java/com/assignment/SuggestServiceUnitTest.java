package com.assignment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SuggestServiceUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test_suggestion_returns_case_insensitive_results() throws Exception {
        mockMvc.perform(get("/suggest_cities")
                .param("start", "abd")
                .param("atmost", "5"))
                .andDo(print())
                .andExpect(
                        status().isOk()
                ).andExpect(
                content().string("Abdasa")
        );
    }

    @Test
    public void test_suggestion_returns_atmost_specified_values() throws Exception {
        MvcResult result = mockMvc.perform(get("/suggest_cities")
                .param("start", "a")
                .param("atmost", "5"))
                .andDo(print())
                .andExpect(
                        status().isOk()
                ).andReturn();

        String[] suggestions = result.getResponse().getContentAsString().split("\n");
        assertEquals(5, suggestions.length);
    }

    @Test
    public void test_suggestion_returns_empty_response() throws Exception {
        mockMvc.perform(get("/suggest_cities")
                .param("start", "aaaxxxx")
                .param("atmost", "5"))
                .andDo(print())
                .andExpect(
                        status().isOk()
                ).andExpect(
                content().string("")
        );
    }

    @Test
    public void test_invalid_query_param_returns_bad_request_response() throws Exception {
        mockMvc.perform(get("/suggest_cities")
                .param("start", "")
                .param("atmost", "0"))
                .andDo(print())
                .andExpect(
                        status().isBadRequest()
                );
    }
}