package az.jwtapp.rest;

import az.jwtapp.dto.AdminUserDto;
import az.jwtapp.dto.UserDto;
import az.jwtapp.model.User;
import az.jwtapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "/api/admin/")
public class AdminRestController {

    private final UserService userService;

    @Autowired
    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "user/{userId}")
    public ResponseEntity<AdminUserDto> getUserById(@PathVariable(name = "userId") Long id) {
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();


            AdminUserDto result = AdminUserDto.fromUser(user);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }



    @PutMapping(value = "user/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable(name = "userId") long userId,
            @RequestBody User user) {
        Optional<User> optionalUser = userService.findById(userId);
        if (optionalUser.isPresent()) {
            user.setId(userId);
            userService.updateUser(user);
            return new ResponseEntity<>(UserDto.fromUser(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }


    @DeleteMapping("/user/{userId}")
    public ResponseEntity deleteUser(@PathVariable(name = "userId") long id) {

        if (userService.findById(id).isPresent()) {
            userService.delete(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}
