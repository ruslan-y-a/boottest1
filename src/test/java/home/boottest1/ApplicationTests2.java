package home.boottest1;

import com.fasterxml.jackson.databind.ObjectMapper;
import home.boottest1.controllers.LogicException;
import home.boottest1.controllers.RolesController;
import home.boottest1.controllers.UsersController;
import home.boottest1.entities.Role;
import home.boottest1.entities.Roles;
import home.boottest1.entities.Users;
import home.boottest1.repos.RolesRepository;
import home.boottest1.repos.UsersRepository;
import home.boottest1.service.RolesService;
import home.boottest1.service.UsersService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import javax.validation.constraints.AssertTrue;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
//@WebMvcTest({UsersController.class, RolesController.class})
@AutoConfigureMockMvc
//@DataJpaTest
public class ApplicationTests2 {
    @Autowired private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Autowired private UsersRepository usersRepository;
    @Autowired private RolesRepository rolesRepository;
    @Autowired RolesService rolesService;
    @Autowired UsersService usersService;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void integrTest1() throws Exception {
        Roles roles = rolesRepository.findRolesByRole(Role.ADMIN);

/*
        mockMvc.perform(post("/roles/jsave", Role.ADMIN).with(authentication())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login", is(roles.getRole().toString())));
 */
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/roles/jsave").with(authentication());
        request.param("role", Role.ADMIN.toString());
        if (roles!=null) { request.param("id", roles.getId().toString());}
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.status().isOk()); //isOk

    }

    @Test
    public void integrTest1a() throws Exception {
        Roles roles = rolesRepository.findRolesByRole(Role.ADMIN);
        try { assertTrue(roles.getId()>0);}// True(admin.getId()!=null);}
            catch (Exception e) {fail();}
    }

    @Test
    public void integrTest2() throws Exception {
        Roles roles = rolesRepository.findRolesByRole(Role.ADMIN);
        Users user = usersRepository.findUserByLogin("admin");
        System.out.println("==============================admin1");
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/users/jmsave/").with(authentication()); //.with(authentication());
        if (user!=null) {request.requestAttr("id", user.getId());}
        request.requestAttr("password", "admin");
        request.requestAttr("login", "admin");// .param("role", "USER");
        request.requestAttr("email", "admin@yandex.ru");
        if (roles!=null) {request.requestAttr("roleid", roles.getId());}
        ResultActions result = mockMvc.perform(request);
        System.out.println("==============================admin request: " + request);
    //    result.andExpect(MockMvcResultMatchers.status().isOk()); // jsonPath("$.login", is("admin"))

        user = usersRepository.findUserByLogin("admin");
        Assert.assertNotNull(user);
      //  result.andExpect(MockMvcResultMatchers.redirectedUrlPattern("/users/role?id=*")); //isOk()
    }

    @Test
    public void unitTest3() throws Exception {
       /*  MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/users/jlist/").with(authentication()); //.with(authentication());
        ResultActions result = mockMvc.perform(request);
        result.andExpect(status().isOk());
        result.andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        result.andExpect(jsonPath("$.size()", greaterThan (0)));
       */
        mockMvc.perform(get("/users/jlist").with(authentication())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", greaterThan (0)));
              //  */

    }
    /*
    @Test
    public void unitTest4() throws Exception {

        Users testuser = getTestUser("testuser");
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/users/jsave/").with(authentication());
        request.requestAttr("users", asJsonString(testuser));// .param("role", "USER");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.status().isOk()); //isOk
        result.andExpect(MockMvcResultMatchers.redirectedUrlPattern("/users/role?id=*")); //isOk()

    }
    @Test
    public void unitTest5() throws Exception {

        Users testuser  = usersRepository.findUserByLogin("testuser");
        assertNotNull(testuser);// True(admin.getId()!=null);
    }  */
    @Test
    public void unitTest6() throws Exception {

        Users testuser  = usersRepository.findUserByLogin("testuser");

        mockMvc.perform(post("/users/delete", testuser)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
   /////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////

    private Users getUser(String login){
        Users user = usersRepository.findUserByLogin(login);
        System.out.println("==============================user read:" +user);
        if (user==null) {
            user = new Users(); user.setLogin(login); user.setPassword(login);
            user.setEmail(login + "@yandex.ru");  Role role = (login.equals("admin") ? Role.ADMIN : Role.USER);
            Roles roles = rolesRepository.findRolesByRole(role);
            if (roles == null) {
                roles = new Roles(); roles.setRole(role);
                roles = rolesRepository.save(roles);
            }
            user.addRoles(roles);
        }
      return user;
    }
    private Users getUserNoRole(String login){
        Users user = usersRepository.findUserByLogin(login);
        if (user==null) {
            user = new Users(); user.setLogin(login); user.setPassword(login);
            user.setEmail(login + "@yandex.ru");  Role role = (login.equals("admin") ? Role.ADMIN : Role.USER);
            Roles roles = new Roles(); roles.setRole(role);
            user.addRoles(roles);
        }
        return user;
    }
    private Users getTestUser(String login){
        Users user = new Users(); user.setLogin(login); user.setPassword(login);
        user.setEmail(login + "@yandex.ru"); Roles rolw=new Roles(); rolw.setRole(Role.USER);   user.addRoles(rolw);
        return user;
    }

//////////////////////////////////////////////////////////
    private static HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }
    private static String createAuthHeader(String username, String password){

            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")) );
            String authHeader = "Basic " + new String( encodedAuth );
           return authHeader;
    }
    public static RequestPostProcessor authentication() {
        return request -> {
            request.addHeader("Authorization", createAuthHeader("admin", "admin"));
            return request;
        };
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
