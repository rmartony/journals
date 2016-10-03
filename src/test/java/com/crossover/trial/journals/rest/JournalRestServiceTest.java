package com.crossover.trial.journals.rest;

import com.crossover.trial.journals.Application;
import com.crossover.trial.journals.service.CurrentUser;
import com.crossover.trial.journals.service.CurrentUserDetailsService;
import com.crossover.trial.journals.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class JournalRestServiceTest {

    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private CurrentUserDetailsService currentUserDetailsService;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private void loginAsPublisher() {
        CurrentUser currentUser = currentUserDetailsService.loadUserByUsername("publisher1");
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .defaultRequest(get("/").with(user(currentUser))).apply(springSecurity()).build();
    }

    private void loginAsUser() {
        CurrentUser currentUser = currentUserDetailsService.loadUserByUsername("user1");
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .defaultRequest(get("/").with(user(currentUser))).apply(springSecurity()).build();
    }

    @Test
    public void getJournalsSuccess() throws Exception {
        loginAsPublisher();
        mockMvc.perform(get("/rest/journals"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }

    @Test
    public void getJournalSubscriptions() throws Exception {
        loginAsUser();
        mockMvc.perform(get("/rest/journals/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$[2].name", is("endocrinology")))
                .andExpect(jsonPath("$[2].active", is(Boolean.TRUE)));

    }

}
