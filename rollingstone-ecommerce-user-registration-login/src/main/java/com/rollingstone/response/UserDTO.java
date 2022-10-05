package com.rollingstone.response;

public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String userName;

    private String password;
    private String userId;
    private String prefferedUsername;
    private String tenantId;

    public UserDTO() {
    }

    public UserDTO(String firstName, String lastName, String email, String userName, String password, String userId, String prefferedUsername, String tenantId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.userId = userId;
        this.prefferedUsername = userName;
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", userId='" + userId + '\'' +
                ", prefferedUsername='" + prefferedUsername + '\'' +
                ", tenantId='" + tenantId + '\'' +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrefferedUsername()
    {
        return prefferedUsername;
    }

    public void setPrefferedUsername(String prefferedUsername)
    {
        this.prefferedUsername = prefferedUsername;
    }


    public String getTenantId()
    {
        return tenantId;
    }

    public void setTenantId(String tenantId)
    {
        this.tenantId = tenantId;
    }

}
