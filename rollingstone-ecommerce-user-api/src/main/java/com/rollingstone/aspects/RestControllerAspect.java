package com.rollingstone.aspects;

import io.micrometer.core.instrument.Counter;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RestControllerAspect {

    private final Logger logger = LoggerFactory.getLogger("RestControllerAspect");

    @Autowired
    Counter createdUserCreationCounter;

    @Before("execution(public * com.rollingstone.spring.controller.*Controller.*(..))")
    public void generalAllMethodASpect() {
        logger.info("All Method Calls invoke this general aspect method");
    }

    @AfterReturning("execution(public * com.rollingstone.spring.controller.*Controller.createUser(..))")
    public void getsCalledOnCategorySave() {
        logger.info("This aspect is fired when the createUser method of the controller is called");
        createdUserCreationCounter.increment();
    }
}