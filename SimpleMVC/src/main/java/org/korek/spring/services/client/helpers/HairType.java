package org.korek.spring.services.client.helpers;

public enum HairType
{
	LONG(90),
	MEDIUM(60),
	SHORT(45);
	
	/*Minutes*/
	private final int duration;
	
	private HairType(int duration)
	{
		this.duration = duration;
	}

	public int getDuration()
	{
		return duration;
	}
	
}
