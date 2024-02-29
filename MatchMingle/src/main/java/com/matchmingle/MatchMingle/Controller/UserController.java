package com.matchmingle.MatchMingle.Controller;

import com.matchmingle.MatchMingle.Model.User;
import com.matchmingle.MatchMingle.Service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping("/createUser")
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("/{phoneNumber}")
    public User getUserByPhoneNumber(@PathVariable String phoneNumber) {
        return userService.getUserByPhoneNumber(phoneNumber);
    }

//    @GetMapping
//    public List<User> getUserByPhoneNumber(String phoneNumber) {
//        return userService.findByPhoneNumber(phoneNumber);
//    }
}
