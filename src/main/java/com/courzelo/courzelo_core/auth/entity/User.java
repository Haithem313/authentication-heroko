package com.courzelo.courzelo_core.auth.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The persistent class for the user database table.
 * 
 */
@Document
@Getter
@Setter
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 65981149772133526L;
	
	@Transient
    public static final String SEQUENCE_NAME = "users_sequence";

	
	//@Column(name = "USER_ID")
	@Id
	private Long id;

	//@Column(name = "PROVIDER_USER_ID")
	private String providerUserId;

	private String email;

	//@Column(name = "enabled", columnDefinition = "BIT", length = 1)
	private boolean enabled;

	//@Column(name = "DISPLAY_NAME")
	private String displayName;

	//@Column(name = "created_date", nullable = false, updatable = false)
	//@Temporal(TemporalType.TIMESTAMP)
	protected Date createdDate;

	//@Temporal(TemporalType.TIMESTAMP)
	protected Date modifiedDate;

	private String password;

	private String provider;
	
	private String resetPasswordToken;
	
	private String registerToken;

	// bi-directional many-to-many association to Role
	//@JsonIgnore
	private Set<Role> roles;

	public User(Long id, String providerUserId, String email, boolean enabled, String displayName, Date createdDate,
			Date modifiedDate, String password, String provider, Set<Role> roles) {
		super();
		this.id = id;
		this.providerUserId = providerUserId;
		this.email = email;
		this.enabled = enabled;
		this.displayName = displayName;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.password = password;
		this.provider = provider;
		this.roles = roles;
	}

	public User() {
		super();
	}
	
	
}