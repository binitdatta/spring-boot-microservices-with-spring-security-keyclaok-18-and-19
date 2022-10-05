package com.rollingstone.spring.dao;

import com.rollingstone.spring.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDaoRepository extends PagingAndSortingRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    Page<UserEntity> findAll(Pageable pageable);
}
