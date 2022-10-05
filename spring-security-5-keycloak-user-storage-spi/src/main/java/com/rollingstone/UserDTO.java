package com.rollingstone;

public class UserDTO {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String userId;

    private String preferredUserName;
    private String tenantId;

    private String encryptedPassword;

    private String emailVerificationStatus;

    public UserDTO() {
    }

    public UserDTO(String id, String firstName, String lastName, String email, String userName, String userId,
                   String preferredUserName,
                   String tenantId,
                   String encryptedPassword,
                   String emailVerificationStatus
                   )
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.userId = userId;
        this.preferredUserName = preferredUserName;
        this.tenantId = tenantId;
        this.encryptedPassword = encryptedPassword;
        this.emailVerificationStatus = emailVerificationStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getEmailVerificationStatus() {
        return emailVerificationStatus;
    }

    public void setEmailVerificationStatus(String emailVerificationStatus) {
        this.emailVerificationStatus = emailVerificationStatus;
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

    public String getPreferredUserName()
    {
        return preferredUserName;
    }

    public void setPreferredUserName(String preferredUserName)
    {
        this.preferredUserName = preferredUserName;
    }


    public String getTenantId()
    {
        return tenantId;
    }

    public void setTenantId(String tenantId)
    {
        this.tenantId = tenantId;
    }

    @Override public String toString()
    {
        return "UserRest{" + "firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", email='" + email
                + '\'' + ", userName='" + userName + '\'' + ", userId='" + userId + '\'' + ", preferredUserName='"
                + preferredUserName + '\'' + ", tenantId='" + tenantId + '\'' + '}';
    }


}
