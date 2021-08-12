package az.jwtapp.service;

import az.jwtapp.model.User;

import java.util.List;
import java.util.Optional;



public interface UserService {

    User register(User user);

    List<User> getAll();

    User findByUsername(String username);

    Optional<User>  findById(Long id);

    void delete(Long id);

    User updateUser(User user);


    Optional<User> getUserByEmail(String email);

}
