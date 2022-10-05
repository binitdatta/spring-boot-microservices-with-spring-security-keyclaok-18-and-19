package com.rollingstone.spring.service;

import com.rollingstone.spring.model.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);
    Optional<User> get(long id);

    User getByEmail(String email);
    Page<User> getUsersByPage(Integer pageNumber, Integer pageSize);
    void update(long id, User category);

    boolean isValid(String emailId, String plainTextPassword);
    void delete(long id);
}