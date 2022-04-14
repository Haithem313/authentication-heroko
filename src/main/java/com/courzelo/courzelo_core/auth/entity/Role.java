package com.courzelo.courzelo_core.auth.entity;

import java.io.Serializable;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the role database table.
 * 
 */
@Document
@Getter
@Setter
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String USER = "USER";
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	
	@Transient
    public static final String SEQUENCE_NAME = "roles_sequence";

	
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@Column(name = "ROLE_ID")
	@Id
	private Long roleId;

	private String name;

	// bi-directional many-to-many association to User
	//@ManyToMany(mappedBy = "roles")
	private Set<User> users;
	
	

	public Role(Long roleId, String name) {
		this.roleId = roleId;
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Role role = (Role) obj;
		if (!role.equals(role.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Role [name=").append(name).append("]").append("[id=").append(roleId).append("]");
		return builder.toString();
	}

	public Role(Long roleId, String name, Set<User> users) {
		super();
		this.roleId = roleId;
		this.name = name;
		this.users = users;
	}

	public Role() {
		super();
	}
}