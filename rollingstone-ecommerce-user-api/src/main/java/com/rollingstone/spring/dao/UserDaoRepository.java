package com.rollingstone.spring.dao;

import com.rollingstone.spring.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDaoRepository extends PagingAndSortingRepository<User, Long> {

    User findByEmail(String email);
    Page<User> findAll(Pageable pageable);
}
