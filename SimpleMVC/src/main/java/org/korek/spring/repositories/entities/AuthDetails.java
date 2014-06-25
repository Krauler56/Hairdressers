package org.korek.spring.repositories.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.korek.spring.repositories.ContrainsBD;
import org.korek.spring.services.common.helpers.RolesEnum;

@NamedNativeQueries({
	@NamedNativeQuery(
			name = AuthDetails.GET_PASS_AND_ROLES_BY_LOGIN,
			query = "select * from AuthDetails s where s.login = :login",
			resultClass = AuthDetails.class
	)
})
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name=ContrainsBD.UNIQE_LOGIN, columnNames = "login"))
public class AuthDetails
{

	public static final String GET_PASS_AND_ROLES_BY_LOGIN = "GET_PASS_AND_ROLES_BY_LOGIN";

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, length = 30)
	private String login;

	@Column(nullable = false, length = 60)
	private String password;

	@Enumerated(EnumType.ORDINAL)
	private RolesEnum role;

	public AuthDetails()
	{
		super();
	}

	public AuthDetails(String login, String password, RolesEnum role)
	{
		super();
		this.login = login;
		this.password = password;
		this.role = role;

	}

	public String getLogin()
	{
		return login;
	}

	public void setLogin(String login)
	{
		this.login = login;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public RolesEnum getRole()
	{
		return role;
	}

	public void setRole(RolesEnum role)
	{
		this.role = role;
	}

	
}
