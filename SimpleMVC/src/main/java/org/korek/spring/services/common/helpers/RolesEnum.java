package org.korek.spring.services.common.helpers;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.collect.Lists;

public enum RolesEnum
{
	ADMIN("ROLE_ADMIN"),
	EMPLOYEE("ROLE_EMPLOYEE"),
	USER("ROLE_USER");

	private final String roleName;
	private final GrantedAuthority grantedAuthority;
	private final Collection<? extends GrantedAuthority> grantedAuthorities;

	private RolesEnum(String roleName)
	{
		this.roleName = roleName;
		this.grantedAuthority = new SimpleGrantedAuthority(roleName);
		this.grantedAuthorities = Lists.newArrayList(grantedAuthority);
	}

	public String getRoleName()
	{
		return roleName;
	}

	public GrantedAuthority getGrantedAuthority()
	{
		return grantedAuthority;
	}

	public Collection<? extends GrantedAuthority> getGrantedAuthorities()
	{
		return grantedAuthorities;
	}

}
