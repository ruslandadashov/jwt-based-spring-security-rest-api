package az.jwtapp;

import az.jwtapp.config.SecurityConfig;
import az.jwtapp.model.Role;
import az.jwtapp.model.Status;
import az.jwtapp.model.User;
import az.jwtapp.repository.RoleRepository;
import az.jwtapp.repository.UserRepository;
import az.jwtapp.rest.AdminRestController;
import az.jwtapp.service.UserService;
import az.jwtapp.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@ContextConfiguration
//@SpringBootTest
//@AutoConfigureMockMvc
//@Import({SecurityConfig.class})
//@WebMvcTest(controllers = AdminRestController.class)
public class TestUserService {

    private UserService userService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private User user;
    private Role role;
    private List<User> users;


    @Before
    public void setUp() {
        users = new ArrayList<>();


        user = new User();
        user.setId(1L);
        user.setUserName("ruslan");
        user.setFirstName("ruslan");
        user.setLastName("dadashov");
        user.setEmail("ruslan@gmail.com");
        user.setPassword("1111");

        users.add(user);

        role = new Role();
        role.setId(1L);
        role.setName("USER");
        role.setUsers(users);
        role.setStatus(Status.ACTIVE);

        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, roleRepository, bCryptPasswordEncoder);


    }

    @Test
    public void testFindByUserId_whenUserIdExists_shouldReturnUser() {


        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User userResult = userService.findById(1L).get();

        Assert.assertEquals(userResult, user);
        Mockito.verify(userRepository, times(1)).findById(1L);

    }

    @Test
    public void testFindByUserName_whenUserNameExists_shouldReturnUser() {


        Mockito.when(userRepository.findByUserName("ruslan")).thenReturn(user);

        User userResult = userService.findByUsername("ruslan");
        Assert.assertNotNull(userResult);
        Assert.assertEquals(userResult, user);
        Mockito.verify(userRepository, times(1)).findByUserName("ruslan");

    }

    @Test
    public void testUpdateUser_whenUserExists_shouldReturnUpdatedUser() {


        Mockito.when(userRepository.save(user)).thenReturn(user);

        User updatedUser = userService.updateUser(user);

        Assert.assertEquals(updatedUser, user);
        Mockito.verify(userRepository, times(1)).save(user);

    }

    @Test
    public void testGetUserByEmail_whenEmailExists_shouldReturnUser() {


        Mockito.when(userRepository.getUserByEmail("ruslan@gmail.com")).thenReturn(Optional.of(user));

        User resultUser = userService.getUserByEmail("ruslan@gmail.com").get();
        Assert.assertNotNull(resultUser);

        Assert.assertEquals(resultUser, user);
        Mockito.verify(userRepository, times(1)).getUserByEmail("ruslan@gmail.com");

    }

    @Test
    public void testGetAllUsers_shouldReturnListUsers() {


        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<User> userList = userService.getAll();

        Assert.assertEquals(userList, users);
        Mockito.verify(userRepository, times(1)).findAll();

    }

    @Test
    public void testAddUser_whenUserExists_shouldReturnUser() {

        Mockito.when(roleRepository.findByName("USER")).thenReturn(role);
        Mockito.when(bCryptPasswordEncoder.encode("1111")).thenReturn(user.getPassword());
        Mockito.when(userRepository.save(user)).thenReturn(user);
        User userResult = userService.register(user);


        Assert.assertEquals(userResult, user);
        Mockito.verify(roleRepository, times(1)).findByName("USER");
        Mockito.verify(bCryptPasswordEncoder, times(1)).encode("1111");
        Mockito.verify(userRepository, times(1)).save(user);


    }


//    @Test
//    public void testGetExample() throws Exception {
//        List<User> users = new ArrayList<>();
//        User user = new User();
//        user.setId(1L);
//        user.setUserName("ruslan");
//        user.setFirstName("ruslan");
//        user.setLastName("dadashov");
//        user.setEmail("ruslan@gmail.com");
//        user.setPassword("1111");
//        users.add(user);
//        Mockito.when(userService.getAll()).thenReturn(users);
//        mockMvc.perform(get("/users"))
//                .andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(1)))
//                .andExpect(jsonPath("$[0].firstname", Matchers.equalTo("ruslan")));
//    }

//    @Test
//    public void testPostExample() throws Exception {
//        Student student = new Student();
//        student.setId(1);
//        student.setName("Arun");
//        Mockito.when(studentService.saveStudent(ArgumentMatchers.any())).thenReturn(student);
//        String json = mapper.writeValueAsString(student);
//        mockMvc.perform(post("/postMapping").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
//                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
//                .andExpect(jsonPath("$.name", Matchers.equalTo("Arun")));
//    }
//
//    @Test
//    public void testPutExample() throws Exception {
//        Student student = new Student();
//        student.setId(2);
//        student.setName("John");
//        Mockito.when(studentService.updateStudent(ArgumentMatchers.any())).thenReturn(student);
//        String json = mapper.writeValueAsString(student);
//        mockMvc.perform(put("/putMapping").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
//                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", Matchers.equalTo(2)))
//                .andExpect(jsonPath("$.name", Matchers.equalTo("John")));
//    }
//
//    @Test
//    public void testDeleteExample() throws Exception {
//        Mockito.when(studentService.deleteStudent(ArgumentMatchers.anyString())).thenReturn("Student is deleted");
//        MvcResult requestResult = mockMvc.perform(delete("/deleteMapping").param("student-id", "1"))
//                .andExpect(status().isOk()).andExpect(status().isOk()).andReturn();
//        String result = requestResult.getResponse().getContentAsString();
//        assertEquals(result, "Student is deleted");
//    }
}
