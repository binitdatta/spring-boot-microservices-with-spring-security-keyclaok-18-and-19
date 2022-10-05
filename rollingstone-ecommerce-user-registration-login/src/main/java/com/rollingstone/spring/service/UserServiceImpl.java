package com.rollingstone.spring.service;

import com.rollingstone.exceptions.HTTP400Exception;
import com.rollingstone.exceptions.HTTP404Exception;
import com.rollingstone.response.UserDTO;
import com.rollingstone.spring.dao.UserDaoRepository;
import com.rollingstone.spring.model.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDaoRepository userDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserEntity save(UserDTO userDto) {
        try{
            UserEntity user = populateUserFromDto(userDto);
            return userDao.save(user);
        }
        catch (Exception e)
        {
            throw new HTTP400Exception(e.getMessage());
        }
    }

    @Override
    public Optional<UserEntity> get(long id) {
        try{
            logger.info("inside user get!");
            Optional<UserEntity> userOptinoal = userDao.findById(id);

            if (userOptinoal.isPresent()){
                return userOptinoal;
            }else {
                throw new HTTP404Exception("User Not Found");
            }
        }  catch (Exception e)
        {
            logger.info("inside user get ex!");

            throw new HTTP404Exception(e.getMessage());
        }
    }

    @Override
    public Page<UserEntity> getUsersByPage(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("userName").descending());
        return userDao.findAll(pageable);
    }


    @Override
    public void update(long id, UserEntity user) {
        userDao.save(user);
    }


    @Override
    public void delete(long id) {
        userDao.deleteById(id);
    }

    private UserEntity populateUserFromDto(final UserDTO userDto){
        UserEntity user = new UserEntity();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUserId(userDto.getUserId());
        user.setUserName(userDto.getUserName());
        user.setPreferredUserName(userDto.getPrefferedUsername());
        user.setTenantId(userDto.getTenantId());
        user.setEmail(userDto.getEmail());

        user.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));

        user.setPreferredUserName(user.getUserName());
        logger.info(" PreferredUserName :"+ user.getPreferredUserName());
        logger.info(" UserName :"+ user.getUserName());

        user.setEmailVerificationStatus(false);
        return user;
    }
}