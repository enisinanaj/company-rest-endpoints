package com.nlc.company.controllers;

import com.nlc.company.CompanyApplication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by sinanaj on 18/06/2018.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CompanyApplication.class })
@WebAppConfiguration
public class CompanyControllerIntegrationTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void verifyTestConfiguration() {
        ServletContext servletContext = wac.getServletContext();

        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(wac.getBean("companyController"));
    }

    @Test
    public void givenCompaniesURI_whenMockMVC_thenVerifyResponseEmptyArray() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/companies"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn();

        Assert.assertEquals("application/json;charset=UTF-8",
                mvcResult.getResponse().getContentType());
    }

    @Test
    public void givenGetCompanyByIdURI_whenMockMVC_thenVerifyResponseNotFoundError() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/companies/20"))
                .andDo(print()).andExpect(status().isNotFound())
                .andReturn();

        Assert.assertEquals(404,
                mvcResult.getResponse().getStatus());
    }

    @Test
    public void givenPostCompanyURI_whenMockMVC_thenVerifyResponseLocationInHeader() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/companies")
                .content("{\n" +
                        "  \"email\": \"info@apple.com\",\n" +
                        "  \"phone\": \"+1 333 555 8294\",\n" +
                        "  \"country\": \"USA\",\n" +
                        "  \"name\": \"Apple, INC\",\n" +
                        "  \"city\": \"Coupertino\",\n" +
                        "  \"address\": \"One Infinite Loop\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated())
                .andReturn();

        assertThat("location contains companies/1", mvcResult.getResponse().getHeader("Location"), containsString("/companies/1"));
    }

    @Test
    public void givenGetCompanyURI_whenMockMVC_thenVerifyResponseContainsTheCompany() throws Exception {
        String companyContent = "{\"email\": \"info@apple.com\",\n" +
                "  \"phone\": \"+1 333 555 8294\",\n" +
                "  \"country\": \"USA\",\n" +
                "  \"name\": \"Apple, INC\",\n" +
                "  \"city\": \"Coupertino\",\n" +
                "  \"address\": \"One Infinite Loop\"\n" +
                "}";

        MvcResult mvcResult = this.mockMvc.perform(post("/companies")
                .content(companyContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated())
                .andReturn();

        MvcResult mvcResultFromGet = this.mockMvc.perform(
                get(mvcResult.getResponse().getHeader("Location").replace("http://localhost", "")))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        assertThat("getter result", mvcResultFromGet.getResponse().getContentAsString()
                , containsString("\"name\":\"Apple, INC\",\"address\":\"One Infinite Loop\",\"city\":\"Coupertino\",\"country\":\"USA\",\"email\":\"info@apple.com\",\"phone\":\"+1 333 555 8294\",\"beneficialOwners\":[]}"));
    }

    @Test
    public void givenPatchCompanyURI_whenMockMVC_thenVerifyResponseContainsTheUpdatedCompany() throws Exception {
        String companyContent = "{\"email\": \"info@apple.com\",\n" +
                "  \"phone\": \"+1 333 555 8294\",\n" +
                "  \"country\": \"USA\",\n" +
                "  \"name\": \"Apple, INC\",\n" +
                "  \"city\": \"Coupertino\",\n" +
                "  \"address\": \"One Infinite Loop\"\n" +
                "}";

        MvcResult mvcResult = this.mockMvc.perform(post("/companies")
                .content(companyContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated())
                .andReturn();

        String fieldsToUpdate = "{\"email\": \"company@company.com\",\n" +
                "  \"phone\": \"+39 333 555 8294\",\n" +
                "  \"country\": \"Germany\",\n" +
                "  \"name\": \"New Name, INC\",\n" +
                "  \"city\": \"San Francisco\",\n" +
                "  \"address\": \"Two Infinite Loop\"\n" +
                "}";

        MvcResult mvcResultFromPatch = this.mockMvc.perform(
                patch(mvcResult.getResponse().getHeader("Location").replace("http://localhost", ""))
                .content(fieldsToUpdate)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        assertThat("result from patch", mvcResultFromPatch.getResponse().getContentAsString()
                , containsString("\"name\":\"New Name, INC\",\"address\":\"Two Infinite Loop\",\"city\":\"San Francisco\",\"country\":\"Germany\",\"email\":\"company@company.com\",\"phone\":\"+39 333 555 8294\",\"beneficialOwners\":[]}"));
    }
}
