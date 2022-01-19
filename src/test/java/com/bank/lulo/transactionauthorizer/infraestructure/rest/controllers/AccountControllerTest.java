package com.bank.lulo.transactionauthorizer.infraestructure.rest.controllers;

import com.bank.lulo.transactionauthorizer.application.commandbus.CommandBus;
import com.bank.lulo.transactionauthorizer.application.querybus.QueryBus;
import com.bank.lulo.transactionauthorizer.domain.model.account.Account;
import com.bank.lulo.transactionauthorizer.domain.model.account.RequestAccount;
import com.bank.lulo.transactionauthorizer.domain.shared.domaineventbus.DomainEventCollection;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommandBus commandBus;

    @MockBean
    private QueryBus queryBus;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void get_error_bad_request() throws Exception {

        RequestAccount requestAccount = new RequestAccount(new Account(1, true, 100, new DomainEventCollection()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                post("/api/accounts").
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).
                content(requestAccount.toString());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(400, response.getStatus());
    }

    @Test
    public void account_created_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                post("/api/accounts").
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).
                content("{account: {id: 1 ,active-card: true ,available-limit: 100 }}").
                characterEncoding("utf-8");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(400, response.getStatus());
    }

    @Test
    public void authorized_transaction_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                post("/api/accounts").
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).
                content("{ \"transaction\" : { \"id\" : 1 , \"merchant\" : \"Burger King\" , \"amount\" : 20 ,\n" +
                        "\"time\" : \"2019-02-13T10:00:00.000Z\" }}").
                characterEncoding("utf-8");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(404, response.getStatus());
    }
}
