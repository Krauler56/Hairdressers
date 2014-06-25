package org.korek.spring.controllers.models;

import javax.validation.constraints.Size;

public class ChangePassword
{
	@Size(min=1, max=30)
	private String oldPass;
	
	@Size(min=1, max=30)
	private String newPass;
	
	@Size(min=1, max=30)
	private String newPassRepeat;

	public ChangePassword()
	{
		super();
	}

	public String getOldPass()
	{
		return oldPass;
	}

	public void setOldPass(String oldPass)
	{
		this.oldPass = oldPass;
	}

	public String getNewPass()
	{
		return newPass;
	}

	public void setNewPass(String newPass)
	{
		this.newPass = newPass;
	}

	public String getNewPassRepeat()
	{
		return newPassRepeat;
	}

	public void setNewPassRepeat(String newPassRepeat)
	{
		this.newPassRepeat = newPassRepeat;
	}

}
