package de.ait.platform.user.service;

import de.ait.platform.user.entity.User;
import de.ait.platform.user.reposittory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp {
      private UserRepository userRepository;
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
   }
