package az.jwtapp;

import az.jwtapp.model.User;
import az.jwtapp.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserDao {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void userSave(){
        User user = new User();
        user.setUserName("Test");
        user.setFirstName("Test");
        user.setLastName("Testov");
        user.setEmail("test@gmail.com");
        user.setPassword("1234");

        User result = userRepository.save(user);
        assertEquals("Test", result.getFirstName());
    }
}
