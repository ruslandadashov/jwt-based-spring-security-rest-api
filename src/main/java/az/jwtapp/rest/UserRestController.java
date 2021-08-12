package az.jwtapp.rest;

import az.jwtapp.dto.UserDto;
import az.jwtapp.model.User;
import az.jwtapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;



@RestController
@RequestMapping(value = "/api/user/")
public class UserRestController {
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "userId") Long id, HttpServletRequest request) {
        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();


            UserDto result = UserDto.fromUser(user);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
