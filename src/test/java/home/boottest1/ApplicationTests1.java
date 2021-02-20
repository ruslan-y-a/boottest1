package home.boottest1;

import com.fasterxml.jackson.databind.ObjectMapper;
import home.boottest1.controllers.RolesController;
import home.boottest1.controllers.UsersController;
import home.boottest1.entities.Role;
import home.boottest1.entities.Roles;
import home.boottest1.entities.Users;
import home.boottest1.repos.RolesRepository;
import home.boottest1.repos.UsersRepository;
import home.boottest1.service.RolesService;
import home.boottest1.service.UsersService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class ApplicationTests1 {
    @Autowired private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Autowired static ObjectMapper objectMapper;
    //@MockBean
    @MockBean private UsersRepository usersRepoMock;
    @MockBean private RolesRepository rolesRepoMock;

    @InjectMocks RolesController rolesController;
    @InjectMocks UsersController usersController;
    @Mock UsersService usersService;
    @Mock RolesService rolesService;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        usersService.setRepo(usersRepoMock); rolesService.setRepo(rolesRepoMock);
    }

    @Test
    public void getHome() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getMessage() throws Exception {
        this.mockMvc.perform(get("/about"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void unitTest1() throws Exception {

        Users testuser = getTestUser("testuser");
        List<Users> allUsers = Arrays.asList(testuser);
        given(usersService.findAll()).willReturn(allUsers);
        given(usersRepoMock.findAll()).willReturn(allUsers);

        mockMvc.perform(get("/users/jlist")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].login", is(testuser.getLogin())));
    }


    @Test
    public void unitTest2() throws Exception {
        Users testuser = getTestUser("testuser");
        given(usersService.save(testuser)).willReturn(testuser);
        given(usersRepoMock.save(testuser)).willReturn(testuser);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/users/jmsave");
        request.param("users", asJsonString(testuser));
          ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.status().isOk()); //isOk
/*
        mockMvc.perform(get("/users/jlist")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].login", is(testuser.getLogin())));
        */
    }


   /////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////

    private Users getTestUser(String login){
        Users user = new Users(); user.setLogin(login); user.setPassword(login);
        user.setEmail(login + "@yandex.ru"); Roles rolw=new Roles(); rolw.setRole(Role.USER);   user.addRole(rolw);
        return user;
    }


    public static ResultMatcher rolesMatcher(String prefix, Roles roles) {
        return ResultMatcher.matchAll(
                jsonPath(prefix + ".role").value(roles.getRole())
        );
    }
    public static ResultMatcher userMatcher(String prefix, Users users) {
        return ResultMatcher.matchAll(
                jsonPath(prefix + ".login").value(users.getLogin()),
                jsonPath(prefix + ".email").value(users.getEmail())
        );
    }
    public ResultMatcher noCacheHeader() {
        return header().string("Cache-Control", "no-cache");
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
