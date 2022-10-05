package com.rollingstone.spring.controller;

import com.rollingstone.dto.PasswordResponseDTO;
import com.rollingstone.events.UserEvent;
import com.rollingstone.exceptions.HTTP404Exception;
import com.rollingstone.spring.model.User;
import com.rollingstone.spring.service.UserService;
import com.rollingstone.spring.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController extends AbstractController {

    final static Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*---Add new User---*/
    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        User savedUser = userService.save(user);
        UserEvent userCreatedEvent = new UserEvent("One User is created", savedUser);
        eventPublisher.publishEvent(userCreatedEvent);
        return ResponseEntity.ok().body("New User has been saved with ID:" + savedUser.getId());
    }

    /*---Get a User by id---*/
    @GetMapping("/user/id/{id}")
    @ResponseBody
    public User getUser(@PathVariable("id") long id) {
        Optional<User> returnedUser = userService.get(id);
        User user  = returnedUser.get();

        UserEvent UserRetrievedEvent = new UserEvent("One User is retrieved", user);
        eventPublisher.publishEvent(UserRetrievedEvent);
        return user;
    }

    /*---Get a User by UserName---*/
    @GetMapping("/user/{userName}")
    @ResponseBody
    public User getUserByUserName(@PathVariable("userName") String userName) {
        User returnedUser = userService.getByEmail(userName);
        UserEvent UserRetrievedEvent = new UserEvent("One User is retrieved By Email", returnedUser);
        eventPublisher.publishEvent(UserRetrievedEvent);
        return returnedUser;
    }



    /*---get Paged User---*/
    @GetMapping("/user")
    public @ResponseBody Page<User> getUsersByPage(
            @RequestParam(value="pagenumber", required=true, defaultValue="0") Integer pageNumber,
            @RequestParam(value="pagesize", required=true, defaultValue="20") Integer pageSize) {
        Page<User> pagedUsers = userService.getUsersByPage(pageNumber, pageSize);
        return pagedUsers;
    }


    /*---Update a User by id---*/
    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        checkResourceFound(this.userService.get(id));
        userService.update(id, user);
        return ResponseEntity.ok().body("User has been updated successfully.");
    }



    @PostMapping("/{userName}/validate-password")
    public PasswordResponseDTO validateUserPassword(@PathVariable("userName") String userName,
                                                  @RequestBody String password) {
        User user = this.userService.getByEmail(userName);
        if (user == null) {
            throw new HTTP404Exception("User does Not Exist");
        }
        boolean isValid = this.userService.isValid(userName, password);
        logger.info("Password Valid ? "+ isValid);
        PasswordResponseDTO isValidPassword = new PasswordResponseDTO(isValid);
        return isValidPassword;
    }
}