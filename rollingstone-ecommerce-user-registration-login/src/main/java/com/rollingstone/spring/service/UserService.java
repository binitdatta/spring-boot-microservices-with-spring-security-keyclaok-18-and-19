package com.rollingstone.spring.service;

import com.rollingstone.response.UserDTO;
import com.rollingstone.spring.model.UserEntity;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {

    UserEntity save(UserDTO user);
    Optional<UserEntity> get(long id);
    Page<UserEntity> getUsersByPage(Integer pageNumber, Integer pageSize);
    void update(long id, UserEntity category);
    void delete(long id);
}