package com.rollingstone.spring.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rollingstone_users")
public class User implements Serializable {
    private static final long serialVersionUID = 5313493413859894403L;
 
    public User() {}
    
    public User(long id, String userId, String firstName, String lastName, String userName, String email, String encryptedPassword,
				 Boolean emailVerificationStatus, String preferredUserName, String tenantId) {
		this.id = id;
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.email = email;
		this.encryptedPassword = encryptedPassword;
		this.emailVerificationStatus = emailVerificationStatus;
		this.preferredUserName = preferredUserName;
		this.tenantId = tenantId;
	}

	@Id
    @GeneratedValue
    private long id;

    @Column(name="user_id", nullable = false)
    private String userId;

    @Column(name="first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name="last_name", nullable = false, length = 50)
    private String lastName;

	@Column(name="user_name", nullable = false, length = 50)
	private String userName;

    @Column(name="email_id",nullable = false, length = 120)
    private String email;

    @Column(name="encrypted_password", nullable = false)
    private String encryptedPassword;

	@Column(name="preferred_username", nullable = false)
	private String preferredUserName;

	@Column(name="tenant_id", nullable = false)
	private String tenantId;


    @Column(name="email_verification_status", nullable = false)
    private Boolean emailVerificationStatus = false;



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}


	public Boolean getEmailVerificationStatus() {
		return emailVerificationStatus;
	}

	public void setEmailVerificationStatus(Boolean emailVerificationStatus) {
		this.emailVerificationStatus = emailVerificationStatus;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
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

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", userId='" + userId + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", userName='" + userName + '\'' +
				", email='" + email + '\'' +
				", encryptedPassword='" + encryptedPassword + '\'' +
				", preferredUserName='" + preferredUserName + '\'' +
				", tenantId='" + tenantId + '\'' +
				", emailVerificationStatus=" + emailVerificationStatus +
				'}';
	}
}
