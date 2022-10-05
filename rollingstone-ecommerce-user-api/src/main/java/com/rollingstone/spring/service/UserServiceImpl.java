package com.rollingstone.spring.service;

import com.rollingstone.exceptions.HTTP400Exception;
import com.rollingstone.exceptions.HTTP404Exception;
import com.rollingstone.spring.dao.UserDaoRepository;
import com.rollingstone.spring.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDaoRepository userDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User save(User category) {
        try{
            return userDao.save(category);
        }
        catch (Exception e)
        {
            throw new HTTP400Exception(e.getMessage());
        }
    }

    @Override
    public Optional<User> get(long id) {
        try{
            logger.info("inside user get!");
            Optional<User> userOptinoal = userDao.findById(id);

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
    public User getByEmail(String email) {
        try{
            logger.info("inside user getByEmail!");
            User user = userDao.findByEmail(email);

            if (user != null){
                return user;
            }else {
                throw new HTTP404Exception("User Not Found");
            }
        }  catch (Exception e)
        {
            logger.info("inside user getByEmail ex!");

            throw new HTTP404Exception(e.getMessage());
        }
    }

    @Override
    public Page<User> getUsersByPage(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("userName").descending());
        return userDao.findAll(pageable);
    }


    @Override
    public void update(long id, User user) {
        userDao.save(user);
    }


    @Override
    public void delete(long id) {
        userDao.deleteById(id);
    }

    public boolean isValid(String email, String plainTextPassword) {
        User user  = userDao.findByEmail(email);
        logger.info("plainTextPassword :" + plainTextPassword);
        if (user != null) {
            logger.info("User Not Null :" + plainTextPassword);
            String extractedPassword = plainTextPassword.substring(plainTextPassword.indexOf("=")+1);
            logger.info("User Not getEncryptedPassword :" + user.getEncryptedPassword());
            logger.info("User Not extractedPassword :" + extractedPassword);

            logger.info(" passwordEncoder.matches(plainTextPassword, user.getEncryptedPassword()) :" +
                    passwordEncoder.matches(extractedPassword, user.getEncryptedPassword()));
            return passwordEncoder.matches(extractedPassword, user.getEncryptedPassword());
        }else {
            logger.info("User Null :" + plainTextPassword);
        }
        return false;
    }


}