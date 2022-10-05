package com.rollingstone.events;

import com.rollingstone.spring.model.UserEntity;
import org.springframework.context.ApplicationEvent;

public class UserEvent extends ApplicationEvent {

    private String eventType;
    private UserEntity user;
    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    public UserEntity getUser() {
        return user;
    }
    public void setUser(UserEntity user) {
        this.user = user;
    }
    public UserEvent(String eventType, UserEntity user) {
        super(user);
        this.eventType = eventType;
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserEvent [eventType=" + eventType + ", user=" + user + "]";
    }


}