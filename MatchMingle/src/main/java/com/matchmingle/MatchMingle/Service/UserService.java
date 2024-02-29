package com.matchmingle.MatchMingle.Service;

import com.matchmingle.MatchMingle.Model.User;
import com.matchmingle.MatchMingle.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

//    public Optional<User> findByPhoneNumber(String phoneNumber) {
//        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
//        if (user.isPresent()) {
//            System.out.println("User found: " + user.get().getFullname());
//        }
//        else {
//            System.out.println("User not found");
//        }
//        return user;
//    }
}